package uk.ac.stfc.isis.ibex.configserver.configuration;


import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Instrument specific property displayed in spangle banner.
 * 
 * CREATED FROM JSON
 */
public class BannerItem extends ModelObject {

    private String name;
    private String pv;
    private String type;
	private BannerState true_state;
	private BannerState false_state;
	private BannerState unknown_state;
	private BannerState currentState;

    private ObservableFactory obsFactory = null;
	private ForwardingObservable<Boolean> pvObservable;

	
	public void createPVObservable() {
        obsFactory = new ObservableFactory(OnInstrumentSwitch.CLOSE);

        pvObservable = obsFactory.getSwitchableObservable(new BooleanChannel(),
                Instrument.getInstance().getPvPrefix() + this.pv);
        pvObservable.addObserver(stateAdapter);
	}

    private final BaseObserver<Boolean> stateAdapter = new BaseObserver<Boolean>() {

        @Override
        public void onValue(Boolean value) {
            if (value) {
        		setCurrentState(true_state);
        	} else {
        		setCurrentState(false_state);
        	}
        }

        @Override
        public void onError(Exception e) {
    		setCurrentState(unknown_state);
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
        		setCurrentState(unknown_state);
            }
        }
    };

	public String name() {
		return name;
	}
	
	public String type() {
		return type;
	}
	
	public String pv() {
		return pv;
	}
	
	public BannerState true_state() {
		return true_state;
	}
	
	public BannerState false_state() {
		return false_state;
	}
	
	public BannerState unknown_state() {
		return unknown_state;
	}
	
    public BannerState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(BannerState currentState) {
        firePropertyChange("currentState", this.currentState, this.currentState = currentState);
	}

}
