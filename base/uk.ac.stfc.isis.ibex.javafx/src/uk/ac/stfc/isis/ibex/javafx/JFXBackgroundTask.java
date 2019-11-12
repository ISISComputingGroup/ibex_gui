package uk.ac.stfc.isis.ibex.javafx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javafx.embed.swing.JFXPanel;

public class JFXBackgroundTask {
	
	private static final ThreadFactory JFX_THREAD_FACTORY = new ThreadFactory() {
		@Override
		public Thread newThread(Runnable task) {
			Thread thread = new Thread(task, "IBEX GUI JavaFX worker thread");
			thread.setDaemon(true);
			return thread;
		}
	};
	
    private static final ExecutorService JFX_THREAD = Executors.newSingleThreadExecutor(JFX_THREAD_FACTORY);
    		
    private static void startJFX(CountDownLatch latch) {
    	JFXPanel backgroundPanel = new JFXPanel();
    	backgroundPanel.setVisible(false);
    	latch.countDown();
    }
    
    public static CountDownLatch start() {
    	final CountDownLatch latch = new CountDownLatch(1);
    	JFX_THREAD.submit(() -> startJFX(latch));
    	return latch;
    }
}
