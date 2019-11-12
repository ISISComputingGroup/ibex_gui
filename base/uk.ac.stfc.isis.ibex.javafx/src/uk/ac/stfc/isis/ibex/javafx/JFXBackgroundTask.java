package uk.ac.stfc.isis.ibex.javafx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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
    	DummyJFXApplication.launch();
    	System.out.println("Done launch");
    }
    
    public static CountDownLatch start() {
    	JFX_THREAD.submit(JFXBackgroundTask::startJFX);
    	return JFX_INITIALIZATION_LATCH;
    }
    
    public static void waitForJavaFxToBeReady() throws InterruptedException {
    	JFX_INITIALIZATION_LATCH.await();
    }
}
