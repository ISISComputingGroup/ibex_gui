package uk.ac.stfc.isis.ibex.ui.goniometer;

import java.util.LinkedList;
import java.util.List;

import uk.ac.stfc.isis.ibex.goniometer.GoniometerModel;
import uk.ac.stfc.isis.ibex.motor.MotorSetpoint;

public class GoniometerViewModel {	
	
	private List<MotorSetpoint> settings = new LinkedList<>();
	
	public void setModel(GoniometerModel model) {
		settings.clear();
		settings.add(model.rUpper());
		settings.add(model.rLower());
		settings.add(model.cx());
		settings.add(model.cy());
		settings.add(model.z());
		settings.add(model.theta());
	}
	
	public List<MotorSetpoint> settings() {
		return settings;
	}
}
