package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;

public class BlockRowCellDecorator extends CellDecorator<EditableBlock> {
	
	private static final Color READONLY_COLOR = ResourceManager.getColor(SWT.COLOR_DARK_GRAY);

	private static final Display display = Display.getCurrent();
	
	@Override
	public void applyDecoration(ViewerCell cell) {
    	if (!isEditable(cell)) {
    		cell.setForeground(READONLY_COLOR);
    		italicise(cell);
    	}
   	}

	public boolean isEditable(ViewerCell cell) {
		return getRow(cell).isEditable();
	}
	
	private static void italicise(ViewerCell cell) {
		modifyFont(cell, SWT.ITALIC);
	}
	
	private static void modifyFont(ViewerCell cell, int modifier) {
		FontData fontData = cell.getFont().getFontData()[0];
		Font font = new Font(display, new FontData(fontData.getName(), fontData.getHeight(), fontData.getStyle() | modifier));
		cell.setFont(font);
	}
	
}
