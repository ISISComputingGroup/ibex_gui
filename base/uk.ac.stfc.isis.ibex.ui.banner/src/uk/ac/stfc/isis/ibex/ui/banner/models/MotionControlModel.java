package uk.ac.stfc.isis.ibex.ui.banner.models;

import uk.ac.stfc.isis.ibex.banner.InMotionState;
import uk.ac.stfc.isis.ibex.banner.Observables;
import uk.ac.stfc.isis.ibex.banner.motion.ObservableMotionControl;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.controls.ControlModel;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorStateObserver;

public class MotionControlModel extends ModelObject implements ControlModel{
	
	private final ObservableMotionControl motionControl;
	private static final String TEXT = "Stop All";
	
	private IndicatorStateObserver<InMotionState> inMotion;
	
	public MotionControlModel(Observables observables) {
		motionControl = new ObservableMotionControl(observables.stop);
		inMotion = new IndicatorStateObserver<InMotionState>(observables.inMotion, new InMotionViewState());
	}
	
	public UpdatedValue<Boolean> enabled() {	
		return inMotion.bool();
	}
	
	public void click() {
		motionControl.stop();
	}
	
	public String text() {
		return TEXT;
	}
}
