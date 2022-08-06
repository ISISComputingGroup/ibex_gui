package uk.ac.stfc.isis.ibex.model;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

public class UIThreadUtils {
	
	private static final Queue<Runnable> TASK_QUEUE = new ConcurrentLinkedQueue<>();
	private static final Logger LOG = IsisLog.getLogger(UIThreadUtils.class);
	private static final ScheduledExecutorService UI_THREAD_UPDATER = Executors.newSingleThreadScheduledExecutor(
			new ThreadFactoryBuilder().setNameFormat("UIThreadUpdater %d").build());
	
	static {
		UI_THREAD_UPDATER.scheduleWithFixedDelay(UIThreadUtils::processQueue, 25, 25, TimeUnit.MILLISECONDS);
	}
	
	private static class FutureTaskWithJobRef<T> extends FutureTask<T> {
		private final Class<? extends Runnable> classOfTask;
		
		public FutureTaskWithJobRef(Runnable runnable, T result) {
			super(runnable, result);
			this.classOfTask = runnable.getClass();
		}
		
		public Class<? extends Runnable> getClassOfTask() {
			return classOfTask;
		}
	}
	
	private static void processQueue() {
		CompletableFuture<Void> future = new CompletableFuture<>();
		Display.getDefault().asyncExec(() -> {
			Runnable job;
			while ((job = TASK_QUEUE.poll()) != null) {
				try {
					long startTime = System.nanoTime();
				    job.run();
				    long duration = System.nanoTime() - startTime;
				    
				    if (duration >= 5000000) {
				    	// Log if doing something on UI thread took more than 5ms.
				    	Class<? extends Runnable> clazz;
				    	if (job instanceof FutureTaskWithJobRef) {
				    		clazz = ((FutureTaskWithJobRef<?>) job).getClassOfTask();
				    	} else {
				    		clazz = job.getClass();
				    	}
				    	
				    	LOG.warn(String.format("Slow task (%s) on UI Thread took %d ns", clazz, duration));
				    }
				    
				} catch (Exception e) {
					LoggerUtils.logErrorWithStackTrace(LOG, "Error in UI thread task: " + e.getMessage(), e);
				}
			}
			future.complete(null);
		});
		
		try {
			// Wait for future to be completed, so that we don't create another processQueue task
			// overlapping with this one
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			LoggerUtils.logErrorWithStackTrace(LOG, "Error waiting for UI task queue: " + e, e);
		}
	}
	
	private static boolean isOnDisplayThread() {
		return Thread.currentThread() == Display.getDefault().getThread();
	}
	
	public static void asyncExec(final Runnable job) {
		TASK_QUEUE.add(job);
	}
	
	public static void syncExec(final Runnable job) {
		if (isOnDisplayThread()) {
			// If we're already on UI thread, just run task inline.
			job.run();
		} else {
			FutureTask<Void> futureTask = new FutureTaskWithJobRef<>(job, null);
			TASK_QUEUE.add(futureTask);
			try {
				// Wait for future to be completed.
				futureTask.get();
			} catch (InterruptedException | ExecutionException e) {
				LoggerUtils.logErrorWithStackTrace(LOG, "Error executing UI task: " + e.getMessage(), e);
			}
		}
	}
}
