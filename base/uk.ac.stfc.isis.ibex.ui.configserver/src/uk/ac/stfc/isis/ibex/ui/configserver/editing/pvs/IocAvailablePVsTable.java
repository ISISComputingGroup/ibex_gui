package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs;

import java.util.Collection;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePV;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

public class IocAvailablePVsTable extends DataboundTable<AvailablePV> {
	private IocPVNameFilter filter;
	
	public IocAvailablePVsTable(Composite parent, int style, int tableStyle) {
		super(parent, style, AvailablePV.class, tableStyle | SWT.BORDER);

		initialise();
	
		filter = new IocPVNameFilter();
		this.viewer().addFilter(filter);
	}
	
	@Override
	public void setRows(Collection<AvailablePV> rows) {
		super.setRows(rows);
	}

	@Override
	protected void addColumns() {
		name();
		description();
	}
	
	private void name() {
		TableViewerColumn desc = createColumn("Name", 8);
		desc.setLabelProvider(new DataboundCellLabelProvider<AvailablePV>(observeProperty("name")) {
			@Override
			protected String valueFromRow(AvailablePV row) {
				return row.getName();
			}
		});	
	}
	
	private void description() {
		TableViewerColumn desc = createColumn("Description", 6);
		desc.setLabelProvider(new DataboundCellLabelProvider<AvailablePV>(observeProperty("description")) {
			@Override
			protected String valueFromRow(AvailablePV row) {
				return row.getDescription();
			}
		});	
	}
	
	public void setFilter(String search) {
		filter.setSearchText(search);
		this.viewer().refresh();
	}
}
