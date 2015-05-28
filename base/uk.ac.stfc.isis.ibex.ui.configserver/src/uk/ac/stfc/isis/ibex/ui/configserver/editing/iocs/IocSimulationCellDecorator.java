package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;

public class IocSimulationCellDecorator extends CellDecorator<EditableIoc> {

	private static final Display display = Display.getCurrent();

	@Override
	public void applyDecoration(ViewerCell cell) {
    	if (!isSimulating(cell)) {
    		unbold(cell);
    		return;
    	}

    	bold(cell);
		addSimulationLabel(cell);
	}

	private boolean isSimulating(ViewerCell cell) {
		return getRow(cell).getSimLevel() != SimLevel.NONE;
	}
	
	private static void bold(ViewerCell cell) {
		modifyFont(cell, SWT.BOLD, true);
	}
	
	private void unbold(ViewerCell cell) {
		modifyFont(cell, SWT.BOLD, false);
	}
	
	private static void addSimulationLabel(ViewerCell cell) {
		String text = cell.getText();
		cell.setText(text + " (SIM)");
	}
	
	private static void modifyFont(ViewerCell cell, int modifier, boolean enable) {
		FontData fontData = cell.getFont().getFontData()[0];
		int newStyle = enable ? fontData.getStyle() | modifier : fontData.getStyle() & ~modifier;
		Font font = new Font(display, new FontData(fontData.getName(), fontData.getHeight(), newStyle));
		cell.setFont(font);
	}
}
