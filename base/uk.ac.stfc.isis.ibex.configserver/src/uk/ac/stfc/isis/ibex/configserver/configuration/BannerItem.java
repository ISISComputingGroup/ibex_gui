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
            setCurrentState(value);
        }

        @Override
        public void onError(Exception e) {
            setCurrentState(null);
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                setCurrentState(null);
            }
        }
    };

    public String name() {
        return this.name;
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
     * Sets the current state of the property based on the PV value and fires a
     * property change for listeners.
     * 
     * @param value the state value of the property.
     */
    public void setCurrentState(Boolean value) {
        BannerItemState newState;
        if (value == null) {
            newState = this.unknown_state;
        } else {
            if (value) {
                newState = this.true_state;
            } else {
                newState = this.false_state;
            }
        }
        firePropertyChange("currentState", this.currentState, this.currentState = newState);
	}

}
