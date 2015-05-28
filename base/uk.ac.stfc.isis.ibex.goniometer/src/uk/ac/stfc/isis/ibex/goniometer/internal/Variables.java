package uk.ac.stfc.isis.ibex.goniometer.internal;

import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;
import uk.ac.stfc.isis.ibex.instrument.Channels;
import uk.ac.stfc.isis.ibex.instrument.InstrumentVariables;
import uk.ac.stfc.isis.ibex.motor.observable.MotorSetPointVariables;

public class Variables extends InstrumentVariables {

	private static final PVAddress GONIO = PVAddress.startWith("GONIO");

	public final MotorSetPointVariables rUpper;
	public final MotorSetPointVariables rLower;
	public final MotorSetPointVariables cx;
	public final MotorSetPointVariables cy;
	public final MotorSetPointVariables z;
	public final MotorSetPointVariables theta;
	
	public Variables(Channels channels) {
		super(channels);
		rUpper = new MotorSetPointVariables(GONIO.append("RUPPER"), channels);
		rLower = new MotorSetPointVariables(GONIO.append("RLOWER"), channels);
		cx = new MotorSetPointVariables(GONIO.append("CX"), channels);
		cy = new MotorSetPointVariables(GONIO.append("CY"), channels);
		z = new MotorSetPointVariables(GONIO.append("Z"), channels);
		theta = new MotorSetPointVariables(GONIO.append("THETA"), channels);	
	}
}
