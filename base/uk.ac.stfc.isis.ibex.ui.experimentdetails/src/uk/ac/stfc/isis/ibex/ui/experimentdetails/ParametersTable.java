package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

public class ParametersTable extends DataboundTable<Parameter> {

	public ParametersTable(Composite parent, int style, int tableStyle) {
		super(parent, style, Parameter.class, tableStyle);
		initialise();
	}

	@Override
	protected void addColumns() {
		name();
		units();
		value();
	}

	private void name() {
		TableViewerColumn name = createColumn("Name", 4);
		name.setLabelProvider(new DataboundCellLabelProvider<Parameter>(observeProperty("name")) {
			@Override
			protected String valueFromRow(Parameter row) {
				return row.getName();
			}
		});		
	}
	
	private void units() {
		TableViewerColumn name = createColumn("Units", 1);
		name.setLabelProvider(new DataboundCellLabelProvider<Parameter>(observeProperty("units")) {
			@Override
			protected String valueFromRow(Parameter row) {
				return row.getUnits();
			}
		});		
	}
	
	private void value() {
		TableViewerColumn name = createColumn("Value", 2);
		name.setLabelProvider(new DataboundCellLabelProvider<Parameter>(observeProperty("value")) {
			@Override
			protected String valueFromRow(Parameter row) {
				return row.getValue();
			}
		});	
		name.setEditingSupport(new StringEditingSupport<Parameter>(viewer(), Parameter.class) {
			@Override
			protected String valueFromRow(Parameter row) {
				return row.getValue();
			}

			@Override
			protected void setValueForRow(Parameter row, String value) {
				row.setValue(value);
			}
		});
	}
}
