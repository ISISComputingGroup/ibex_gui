package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import java.util.Collection;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

public class AddMacroTable extends DataboundTable<Macro> {
	private MacroAddressFilter filter;
	
	public AddMacroTable(Composite parent, int style, int tableStyle) {
		super(parent, style, Macro.class, tableStyle | SWT.BORDER);

		initialise();
	
		filter = new MacroAddressFilter();
		this.viewer().addFilter(filter);
	}
	
	@Override
	public void setRows(Collection<Macro> rows) {
		super.setRows(rows);
	}

	@Override
	protected void addColumns() {
		name();
		description();
		pattern();
	}
	
	private void name() {
		TableViewerColumn desc = createColumn("Macro name", 8);
		desc.setLabelProvider(new DataboundCellLabelProvider<Macro>(observeProperty("name")) {
			@Override
			protected String valueFromRow(Macro row) {
				return row.getName();
			}
		});	
	}
	
	private void description() {
		TableViewerColumn desc = createColumn("Description", 6);
		desc.setLabelProvider(new DataboundCellLabelProvider<Macro>(observeProperty("description")) {
			@Override
			protected String valueFromRow(Macro row) {
				return row.getDescription();
			}
		});	
	}
	
	private void pattern() {
		TableViewerColumn desc = createColumn("Pattern", 6);
		desc.setLabelProvider(new DataboundCellLabelProvider<Macro>(observeProperty("pattern")) {
			@Override
			protected String valueFromRow(Macro row) {
				return row.getPattern();
			}
		});	
	}
	
	public void setFilter(String search) {
		filter.setSearchText(search);
		this.viewer().refresh();
	}
}
