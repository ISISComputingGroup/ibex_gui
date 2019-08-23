package uk.ac.stfc.isis.ibex.ui.dae.spectraplots;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * A class to interact with "local" channels from CS-Studio. This class provides utility methods to read,
 * write and persist these channels.
 *
 * @param <T>
 */
public class PersistedLocalChannel<T> {
	
	private static final WritableFactory WRITE_FACTORY = new WritableFactory(OnInstrumentSwitch.NOTHING);
	private static final ObservableFactory OBS_FACTORY = new ObservableFactory(OnInstrumentSwitch.NOTHING);
	private static final Logger LOG = IsisLog.getLogger(PersistedLocalChannel.class);
	
	private final Observable<T> observable;
	private final Writable<T> writable;
	
	private final Consumer<T> persistenceSetter;
	private final Supplier<T> persistenceGetter;
	
	private final String address;
	
	/**
	 * Create a new local persisted channel.
	 * @param address - the address of the PV (for local PVs this usually starts with loc://).
	 * @param type - the channel type to use for reading and writing to this PV. Note that integer channels don't work with local PVs.
	 * @param persistenceSetter - the function to use when saving a value
	 * @param persistenceGetter - the function to use when getting a persisted value
	 */
    public PersistedLocalChannel(String address, ChannelType<T> type, Consumer<T> persistenceSetter, Supplier<T> persistenceGetter) {
    	observable = OBS_FACTORY.getSwitchableObservable(type, address);
    	writable = WRITE_FACTORY.getSwitchableWritable(type, address);
    	
    	this.persistenceGetter = persistenceGetter;
    	this.persistenceSetter = persistenceSetter;
    	this.address = address;
    }
    
    /**
     * Gets the persisted value from file, and writes this to the PV. Subsequently monitor the PV for changes and save new values to file.
     */
    public void setInitialValueAndSubscribeToChanges() {
    	T initialValue = persistenceGetter.get();
    	
    	try {
    		writable.write(initialValue);
    	} catch (IOException e) {
    		LoggerUtils.logErrorWithStackTrace(LOG, String.format("Couldn't set initial value to '%s' for local PV '%s'", initialValue, address), e);
    	}
    	
    	observable.subscribe(new BaseObserver<T>() {
    		@Override
    		public void onValue(T value) {
    			if (value != null) {
    				persistenceSetter.accept(value);
    			}
    		}
		});
    }
    
    /**
     * The PV address that this local channel uses.
     * @return the PV address that this local channel uses.
     */
    public String getPVAddress() {
    	return address;
    }
}
