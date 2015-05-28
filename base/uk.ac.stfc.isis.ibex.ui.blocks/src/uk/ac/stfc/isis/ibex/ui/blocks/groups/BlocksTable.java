package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

public class BlocksTable extends DataboundTable<DisplayBlock> {

	private BlockVisibilityFilter visibilityFilter = new BlockVisibilityFilter();
		
	public BlocksTable(Composite parent, int style, int tableStyle) {
		super(parent, style, DisplayBlock.class, tableStyle | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.NO_SCROLL);
		
		initialise();
		table().setHeaderVisible(false);
		table().setLinesVisible(false);
		table().setMenu(new BlocksMenu(this).createContextMenu());
		viewer().addFilter(visibilityFilter);
		
		ColumnViewerToolTipSupport.enableFor(viewer(), ToolTip.NO_RECREATE); 
	}
	
	public void showHiddenBlocks(boolean showHidden) {
		if (showHidden) {
			viewer().removeFilter(visibilityFilter);
		} else {
			viewer().addFilter(visibilityFilter);
		}		
	}
	
	@Override
	public void setBackground(Color color) {
		super.setBackground(color);
		table().setBackground(color);
	}
	
	@Override
	public void addFocusListener(FocusListener listener) {
		super.addFocusListener(listener);
		table().addFocusListener(listener);
	}
	
	@Override
	protected void addColumns() {
		name();
		value();
	}

	private void name() {
		TableViewerColumn name = createColumn("Name", 4);
		name.setLabelProvider(new DataboundCellLabelProvider<DisplayBlock>(observeProperty("name")) {
			@Override
			protected String valueFromRow(DisplayBlock row) {
				return row.getName();
			}
			
			@Override
			public String getToolTipText(Object element) {
				return toolTipText(element);
			}
		});	
	}
	
	private void value() {
		TableViewerColumn value = createColumn("Value", 4);
		value.getColumn().setAlignment(SWT.RIGHT);
		value.setLabelProvider(new DataboundCellLabelProvider<DisplayBlock>(observeProperty("value")) {
			@Override
			protected String valueFromRow(DisplayBlock row) {
				String value = row.getValue();
				return value ==  null ? "not connected" :  value;
			}
			
			@Override
			public String getToolTipText(Object element) {
				return toolTipText(element);
			}
		});	
	}
	
	private String toolTipText(Object element) {
		if (!(element instanceof DisplayBlock)) {
			return "";
		}
		
		DisplayBlock block = (DisplayBlock) element;
		String desc = block.getDescription();
		return desc == null ? "" : desc;
	}
}
