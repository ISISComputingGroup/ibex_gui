package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DecoratedCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

public class BlocksTable extends DataboundTable<EditableBlock> {
	
	private TableViewerColumn enabled;
	private IObservableMap[] stateProperties = { observeProperty("isVisible") };
	private BlockVisibilityLabelProvider visibilityLabelProvider;
	
	private CellDecorator<EditableBlock> rowDecorator = new BlockRowCellDecorator();
	
	public BlocksTable(Composite parent, int style, int tableStyle) {
		super(parent, style, EditableBlock.class, tableStyle | SWT.BORDER);

		initialise();
	}

	@Override
	protected void addColumns() {
		name();
		pv();
		blockIsVisible();
	}
	
	@Override
	public void setRows(Collection<EditableBlock> rows) {
		clear();
		super.setRows(rows);
	}
	
	private void clear() {
		visibilityLabelProvider.dispose();
		visibilityLabelProvider = new BlockVisibilityLabelProvider(stateProperties);
		enabled.setLabelProvider(visibilityLabelProvider);	
	}
	
	private void name() {
		TableViewerColumn name = createColumn("Name", 3);
		name.setLabelProvider(new DecoratedCellLabelProvider<EditableBlock>(
				observeProperty("name"), 
				Arrays.asList(rowDecorator)) {
			@Override
			protected String valueFromRow(EditableBlock row) {
				return row.getName();
			}
		});	
	}
	
	private void pv() {
		TableViewerColumn desc = createColumn("PV address", 6);
		desc.setLabelProvider(new DecoratedCellLabelProvider<EditableBlock>(
				observeProperty("PV"), 
				Arrays.asList(rowDecorator)) {
			@Override
			protected String valueFromRow(EditableBlock row) {
				return row.getPV();
			}
		});	
	}
	
	private void blockIsVisible() {
		enabled = createColumn("Visible?", 2);
		visibilityLabelProvider = new BlockVisibilityLabelProvider(stateProperties);
		enabled.setLabelProvider(visibilityLabelProvider);		
	}
}
