package uk.ac.stfc.isis.ibex.javafx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.eclipse.swt.widgets.Display;

import javafx.application.Application;
import javafx.application.Platform;

public class JFXBackgroundTask {
	
	public static final CountDownLatch JFX_INITIALIZATION_LATCH = new CountDownLatch(1);
	
    private static final ExecutorService JFX_THREAD = Executors.newSingleThreadExecutor(new ThreadFactory() {
		@Override
		public Thread newThread(Runnable task) {
			Thread thread = new Thread(task, "IBEX GUI JavaFX worker thread");
			thread.setDaemon(true);
			return thread;
		}
	});
    		
    private static void startJFX() {
    	System.out.println("Start task");
    	Platform.setImplicitExit(false);
    	System.out.println("Set exit");
    	Application.launch(DummyJFXApplication.class);
    	System.out.println("Done launch");
    }
    
    public static CountDownLatch start() {
    	Platform.setImplicitExit(false);
    	Platform.startup(() -> {
    		JFX_INITIALIZATION_LATCH.countDown();
    	});
    	return JFX_INITIALIZATION_LATCH;
    }
}
