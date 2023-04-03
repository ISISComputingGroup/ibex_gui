package uk.ac.stfc.isis.ibex.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Closeable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * This class implements batched, deferred property changes.
 * 
 * This class wraps a provided PropertyChangeListener class, and periodically checks whether it needs to
 * execute property changes. Property changes which occur on the same source object and the same property
 * name are aggregated together.
 * 
 * i.e. for two updates:
 *     oldValue = 1, newValue = 2
 *     oldValue = 2, newValue = 3
 * Which occur close together in time, these will be batched into a single update:
 *     oldValue = 1, newValue = 3
 *     
 * The batching process may delay property changes by up to the defertime parameter.
 * 
 * Note that instances of this class MUST be closed via the .close() method to avoid leaking resources.
 */
public class DeferredPropertyChangeListener implements PropertyChangeListener, Closeable, AutoCloseable {
	
	private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder()
					    .setNameFormat("DeferredPropertyChangeListener worker %d")
					    .build());
	
	private static record PropertyChangeEventProperties(Object source, String propertyName) { };
	private static record BatchedPropertyChangeEvent(Object oldValue, Object newValue) { };

	private final PropertyChangeListener listener;
	private final ScheduledFuture<?> scheduledFuture;
	
	private final ConcurrentHashMap<PropertyChangeEventProperties, BatchedPropertyChangeEvent> batchedEvents = new ConcurrentHashMap<>();
	
	/**
	 * Constructor.
     * @param listener the listener to wrap.
     * @param deferTime the maximum time over which to batch updates.
     * @param deferTimeUnits the units of the timescale over which to batch updates
	 */
    public DeferredPropertyChangeListener(PropertyChangeListener listener, long deferTime, TimeUnit deferTimeUnits) {
    	this(listener, deferTime, deferTimeUnits, EXECUTOR);
    }
    
    /**
     * Constructor allowing an explicit worker thread to be passed in, for testing.
     * @param listener the listener to wrap.
     * @param deferTime the maximum time over which to batch updates.
     * @param deferTimeUnits the units of the timescale over which to batch updates
     * @param executor the executor to use when checking for updates.
     */
    public DeferredPropertyChangeListener(PropertyChangeListener listener, long deferTime, TimeUnit deferTimeUnits, ScheduledExecutorService executor) {
    	this.listener = listener;
    	scheduledFuture = executor.scheduleWithFixedDelay(this::propertyChangeIfNeeded, 0, deferTime, deferTimeUnits);
    }
    
    /**
     * Fires batched property changes for events as needed.
     */
    private void propertyChangeIfNeeded() {
    	batchedEvents.forEachEntry(Long.MAX_VALUE, e -> {
    		batchedEvents.remove(e.getKey(), e.getValue());
    		try {
    		    listener.propertyChange(new PropertyChangeEvent(e.getKey().source(), e.getKey().propertyName(), e.getValue().oldValue(), e.getValue().newValue()));
    		} catch (Exception err) {
    			LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), err.getMessage(), err);
    		}
    	});
    }

    /**
     * Overridden implementation of propertyChange.
     * 
     * Note: subclasses should *not* override this method, they should override propertyChange in the wrapped
     * PropertyChangeListener instead. Hence this method is declared as final.
     */
	@Override
	public final void propertyChange(PropertyChangeEvent evt) {
		final var key = new PropertyChangeEventProperties(evt.getSource(), evt.getPropertyName());
		batchedEvents.compute(key, (k, v) -> {
			if (v == null) {
				return new BatchedPropertyChangeEvent(evt.getOldValue(), evt.getNewValue());
			} else {
				return new BatchedPropertyChangeEvent(v.oldValue(), evt.getNewValue());
			}
		});
	}

	/**
	 * Closes this deferred property change listener.
	 * 
	 * Note that this class MUST be explicitly closed to avoid leaking property change listeners.
	 */
	@Override
	public void close() {
		batchedEvents.clear();
		scheduledFuture.cancel(false);
	}
}
