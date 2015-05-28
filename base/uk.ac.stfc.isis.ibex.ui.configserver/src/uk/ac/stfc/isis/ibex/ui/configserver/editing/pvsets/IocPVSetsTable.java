package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvsets;


import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.editing.EditablePVSet;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IocCheckboxLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

public class IocPVSetsTable extends DataboundTable<EditablePVSet> {
	private boolean isEditable = true;
	
	public IocPVSetsTable(Composite parent, int style, int tableStyle) {
		super(parent, style, EditablePVSet.class, tableStyle | SWT.BORDER);

		initialise();
	}

	@Override
	protected void addColumns() {
		name();
		enable();
		description();
	}
	
	private void name() {
		TableViewerColumn desc = createColumn("Name", 8);
		desc.setLabelProvider(new DataboundCellLabelProvider<EditablePVSet>(observeProperty("name")) {
			@Override
			protected String valueFromRow(EditablePVSet row) {
				return row.getName();
			}
		});	
	}
	
	private void description() {
		TableViewerColumn desc = createColumn("Description", 6);
		desc.setLabelProvider(new DataboundCellLabelProvider<EditablePVSet>(observeProperty("description")) {
			@Override
			protected String valueFromRow(EditablePVSet row) {
				return row.getDescription();
			}
		});	
	}
	
	private void enable() {
		TableViewerColumn enabled = createColumn("Enabled?", 2);
		IObservableMap[] stateProperties = { observeProperty("enabled") };
		enabled.setLabelProvider(new IocCheckboxLabelProvider<EditablePVSet>(stateProperties) {	
			@Override
			protected boolean checked(EditablePVSet pvset) {
				return pvset.getEnabled();
			}
			
			@Override
			protected void setChecked(EditablePVSet pvset, boolean checked) {
				pvset.setEnabled(checked);
			}
			
			@Override
			protected boolean isEditable(EditablePVSet pvset) {
				return isEditable;
			}
		});	
	}
	
	public boolean getIsEditable() {
		return isEditable;
	}
	
	public void setIsEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
}
