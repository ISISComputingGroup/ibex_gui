package uk.ac.stfc.isis.ibex.ui.goniometer.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.motor.MotorSetpoint;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

public final class SetpointsTableView extends DataboundTable<MotorSetpoint> {

	public SetpointsTableView(Composite parent, int style) {
		super(parent, style, MotorSetpoint.class);
		
		initialise();
	}

	@Override
	protected void addColumns() {
		addAxisColumn();
		addPositionColumn();		
		addSetpointColumn();
		addHomingColumn();		
		addMotorsColumn();	
	}

	@Override
	protected void configureTable() {
		super.configureTable();
		
		// Hack to set the row height of the table
		table().addListener(SWT.MeasureItem, new Listener() {
			   public void handleEvent(Event event) {
			      event.height = 30;
			   }
		});
	}
	
	private void addHomingColumn() {
		TableViewerColumn homing = createColumn("", 15);
		homing.setLabelProvider(new HomingLabelProvider());		
	}
	
	private void addMotorsColumn() {
		TableViewerColumn motors = createColumn("", 15);
		motors.setLabelProvider(new MotorsLabelProvider());
	}
	
	private void addSetpointColumn() {
		TableViewerColumn setPoint = createColumn("Setpoint", 25);
		
		setPoint.getColumn().setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.goniometer", "icons/edit.png"));
		
		setPoint.setEditingSupport(new SetpointEditingSupport(viewer(), MotorSetpoint.class));
		setPoint.setLabelProvider(new DataboundCellLabelProvider<MotorSetpoint>(observeProperty("setpoint")) {
			@Override
			protected String valueFromRow(MotorSetpoint row) {
				return valueOrEmpty(row.getSetpoint());
			}
		});		
	}
	
	private void addPositionColumn() {
		TableViewerColumn position = createColumn("Position", 25);
		position.setLabelProvider(new DataboundCellLabelProvider<MotorSetpoint>(observeProperty("value")) {
			@Override
			protected String valueFromRow(MotorSetpoint row) {
				return valueOrEmpty(row.getValue());
			}
		});
	}
	
	private void addAxisColumn() {
		TableViewerColumn axis = createColumn("Axis", 15);
		axis.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				MotorSetpoint row = (MotorSetpoint) element;
				return row.getName();
			}
		});
	}
}
