package uk.ac.stfc.isis.ibex.rotatingbench;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public interface IRotatingBench {
	UpdatedValue<Double> angle();
	
	UpdatedValue<Double> angleSP();
	void setAngleSP(double value);
		
	UpdatedValue<Double> angleSPRBV();
	
	UpdatedValue<Status> status();
	UpdatedValue<LiftStatus> liftStatus();

	UpdatedValue<Command> command();
	void setCommand(boolean value);
}
