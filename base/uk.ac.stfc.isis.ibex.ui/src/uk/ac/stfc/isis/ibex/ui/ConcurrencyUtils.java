package uk.ac.stfc.isis.ibex.ui;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.swt.widgets.Display;

/**
 * This class provides a set of adapters between eclipse's threading interface and java 8 futures.
 */
public class ConcurrencyUtils {

	private ConcurrencyUtils () {}

    /**
     * Adapter between eclipse's threading interface and java 8-style Futures.
     *
     * The advantage of this approach is that you can set a task running on the UI thread,
     * then do some other work, and then wait for the result from the UI thread (in effect,
     * this approach can be used to achieve better concurrency).
     *
     * @param <T> - The type of result to return.
     * @param callable - The function to call.
     * @return - A future representing the task.
     */
    public static <T> Future<T> callOnUiThread(final Callable<T> callable) {
    	final CompletableFuture<T> future = new CompletableFuture<T>();
    	final Runnable task = () -> {
    		T result;
    		try {
    		    result = callable.call();
    		} catch (Exception ex) {
    			future.completeExceptionally(ex);
    			return;
    		}
    		future.complete(result);
    	};

    	if (Display.getDefault().getThread() == Thread.currentThread()) {
    		// If we're already on the UI thread just run the code - otherwise could deadlock.
    		task.run();
    	} else {
    		Display.getDefault().asyncExec(task);
    	}

    	return future;
    }

    /**
     * Adapter between eclipse's threading interface and java 8-style Futures.
     *
     * The advantage of this approach is that you can set a task running on the UI thread,
     * then do some other work, and then wait for the result from the UI thread (in effect,
     * this approach can be used to achieve better concurrency).
     *
     * @param <T> - The type of result to return.
     * @param callable - The runnable to run.
     * @return - A future representing the task.
     */
    public static Future<Void> runOnUiThread(final Runnable runnable) {
    	return callOnUiThread(() -> {
    		runnable.run();
    		return null;
    	});
    }

    /**
     * Adapter between eclipse's threading interface and java 8-style Futures.
     *
     * The advantage of this approach is that you can set a task running on the UI thread,
     * then do some other work, and then wait for the result from the UI thread (in effect,
     * this approach can be used to achieve better concurrency).
     *
     * @param <T> - The type of result to return.
     * @param function - The function to call
     * @param argument - The argument to call the function with.
     * @return - A future representing the task.
     */
    public static <T> Future<Void> consumeOnUiThread(final Consumer<T> consumer, final T argument) {
    	return callOnUiThread(() -> {
    		consumer.accept(argument);
    		return null;
    	});
    }

    /**
     * Adapter between eclipse's threading interface and java 8-style Futures.
     *
     * The advantage of this approach is that you can set a task running on the UI thread,
     * then do some other work, and then wait for the result from the UI thread (in effect,
     * this approach can be used to achieve better concurrency).
     *
     * @param <T> - The type of result to return.
     * @param function - The function to call
     * @param argument - The argument to call the function with.
     * @return - A future representing the task.
     */
    public static <TIN, TOUT> Future<TOUT> transformOnUiThread(final Function<TIN, TOUT> function, final TIN argument) {
    	return callOnUiThread(() -> function.apply(argument));
    }
}
