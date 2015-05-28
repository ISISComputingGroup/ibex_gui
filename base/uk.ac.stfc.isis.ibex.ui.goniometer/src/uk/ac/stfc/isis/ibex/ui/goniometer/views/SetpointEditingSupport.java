package uk.ac.stfc.isis.ibex.ui.goniometer.views;

import org.eclipse.jface.viewers.ColumnViewer;

import uk.ac.stfc.isis.ibex.motor.MotorSetpoint;
import uk.ac.stfc.isis.ibex.ui.widgets.DoubleEditingSupport;

public class SetpointEditingSupport extends DoubleEditingSupport<MotorSetpoint> {
	
	public SetpointEditingSupport(ColumnViewer viewer, Class<MotorSetpoint> rowType) {
		super(viewer, rowType);
	}

	@Override
	protected Double valueFromRow(MotorSetpoint row) {
		return row.getSetpoint();	
	}

	@Override
	protected void setValueForRow(MotorSetpoint row, Double value) {
		row.setSetpoint(value);		
	}
}
