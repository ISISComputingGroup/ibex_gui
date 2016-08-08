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
	private BannerItemState true_state;
	private BannerItemState false_state;
	private BannerItemState unknown_state;
	private BannerItemState currentState;

    private ObservableFactory obsFactory = null;
	private ForwardingObservable<Boolean> pvObservable;

    /**
     * Creates an observable for the PV holding the current state of this banner
     * item.
     */
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

    /**
     * The name of the property.
     * 
     * @return the name
     */
	public String name() {
		return name;
	}
	
    /**
     * The property type.
     * 
     * @return the type
     */
	public String type() {
		return type;
	}
	
    /**
     * The PV holding the property state.
     * 
     * @return the PV address
     */
	public String pv() {
		return pv;
	}
	
    /**
     * Returns the display specification for the banner item in true state.
     * 
     * @return the true state
     */
	public BannerItemState true_state() {
		return true_state;
	}

    /**
     * Returns the display specification for the banner item in false state.
     * 
     * @return the false state
     */
	public BannerItemState false_state() {
		return false_state;
	}

    /**
     * Returns the display specification for the banner item in unknown state.
     * 
     * @return the unknown state
     */
	public BannerItemState unknown_state() {
		return unknown_state;
	}
	
    /**
     * Returns the display specification for the banner item in its current
     * state.
     * 
     * @return the current state
     */
    public BannerItemState getCurrentState() {
		return currentState;
	}

    /**
     * Sets the current state of the property and fires a property change for
     * listeners.
     * 
     * @param currentState the current state of the property
     */
	public void setCurrentState(BannerItemState currentState) {
        firePropertyChange("currentState", this.currentState, this.currentState = currentState);
	}

}
