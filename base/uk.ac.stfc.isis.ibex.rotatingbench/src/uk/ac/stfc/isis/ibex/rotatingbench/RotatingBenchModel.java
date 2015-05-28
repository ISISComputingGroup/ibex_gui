package uk.ac.stfc.isis.ibex.rotatingbench;

import uk.ac.stfc.isis.ibex.epics.adapters.ModelAdapter;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.rotatingbench.internal.Variables;

public class RotatingBenchModel extends ModelAdapter implements IRotatingBench {
	
	private final UpdatedValue<Double> angle;
	private final UpdatedValue<Double> angleSP;
	private final SameTypeWriter<Double> writeAngleSP = new SameTypeWriter<>();
	private final UpdatedValue<Double> angleSPRBV;
	private final UpdatedValue<Status> status;
	private final UpdatedValue<LiftStatus> liftStatus;
	private final UpdatedValue<Command> command;
	
	private final SameTypeWriter<String> writeCommand = new SameTypeWriter<>();
	
	public RotatingBenchModel(Variables bench) {
		angle = adapt(bench.angle);
		angleSP = adapt(bench.angleSPValue);
		writeAngleSP.writeTo(bench.angleSP);
		
		angleSPRBV = adapt(bench.angleSPRBV);
		status = adapt(bench.status);
		liftStatus = adapt(bench.liftStatus);
		
		command = adapt(bench.checkHV);
		writeCommand.writeTo(bench.writeCheckHV);
	}

	@Override
	public UpdatedValue<Double> angle() {
		return angle;
	}
	
	@Override
	public UpdatedValue<Status> status() {
		return status;
	}
	
	@Override
	public UpdatedValue<LiftStatus> liftStatus() {
		return liftStatus;
	}

	@Override
	public UpdatedValue<Command> command() {
		return command;
	}

	@Override
	public UpdatedValue<Double> angleSP() {
		return angleSP;
	}
	
	@Override
	public void setAngleSP(double value) {
		writeAngleSP.write(value);
	}

	@Override
	public UpdatedValue<Double> angleSPRBV() {
		return angleSPRBV;
	}

	@Override
	public void setCommand(boolean value) {
		writeCommand.write(value ? "YES" : "NO");
	}
}
