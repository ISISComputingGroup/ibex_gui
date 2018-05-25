package uk.ac.stfc.isis.ibex.configserver.configuration;


import uk.ac.stfc.isis.ibex.configserver.AlarmState;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.EnumChannel;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Instrument specific property displayed in spangle banner.
 * 
 * CREATED FROM JSON therefore uses snake case
 */
@SuppressWarnings("checkstyle:MemberName")
public class BannerItem extends ModelObject {

    private String name;
    private String pv;
    private Boolean local = true;
    
    private String currentValue = null;
    private AlarmState currentAlarmState = AlarmState.UNDEFINED;

    private static final ObservableFactory OBSERVABLE_FACTORY = new ObservableFactory(OnInstrumentSwitch.CLOSE);
	private ForwardingObservable<String> pvObservable;
	private ForwardingObservable<AlarmState> alarmObservable;

    /**
     * Creates an observable for the PV holding the current state of this banner
     * item.
     */
	public void createPVObservable() {
		String pv = this.pv;
		if (local) {
			pv = InstrumentUtils.addPrefix(pv);
		}
		
        pvObservable = OBSERVABLE_FACTORY.getSwitchableObservable(new DefaultChannel(), pv);
        alarmObservable = OBSERVABLE_FACTORY.getSwitchableObservable(new EnumChannel<>(AlarmState.class), pv + ".SEVR");
        pvObservable.addObserver(valueAdapter);
        alarmObservable.addObserver(alarmAdapter);
	}

    private final BaseObserver<String> valueAdapter = new BaseObserver<String>() {

        @Override
        public void onValue(String value) {
            setCurrentValue(value);
        }

        @Override
        public void onError(Exception e) {
            setCurrentValue(null);
            IsisLog.getLogger(getClass()).error("Exception in banner item state adapter: " + e.getMessage());
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                setCurrentValue(null);
            }
        }
    };
    
    private final BaseObserver<AlarmState> alarmAdapter = new BaseObserver<AlarmState>() {

        @Override
        public void onValue(AlarmState alarm) {
            setCurrentAlarm(alarm);
        }

        @Override
        public void onError(Exception e) {
            setCurrentAlarm(AlarmState.INVALID);
            IsisLog.getLogger(getClass()).error("Exception in banner item state adapter: " + e.getMessage());
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                setCurrentAlarm(AlarmState.INVALID);
            }
        }
    };

    /**
     * Returns the display name of this banner item.
     * @return the display name of this banner item.
     */
    public String name() {
        return name;
    }
    
    /**
     * Returns the current value of this banner item.
     * @return the current value of this banner item.
     */
    public String value() {
    	return currentValue;
    }
    
    /**
     * Returns the alarm status of this banner item.
     * @return the alarm status of this banner item.
     */
    public AlarmState alarm() {
    	return currentAlarmState;
    }

    /**
     * Sets the current state of the property based on the PV value and fires a
     * property change for listeners.
     * 
     * @param value the state value of the property.
     */
    public synchronized void setCurrentValue(String value) {
        firePropertyChange("value", this.currentValue, this.currentValue = value);
	}
    
    /**
     * Sets the current state of the property based on the PV value and fires a
     * property change for listeners.
     * 
     * @param value the state value of the property.
     */
    public synchronized void setCurrentAlarm(AlarmState alarm) {
        firePropertyChange("alarm", this.currentAlarmState, this.currentAlarmState = alarm);
	}

}
