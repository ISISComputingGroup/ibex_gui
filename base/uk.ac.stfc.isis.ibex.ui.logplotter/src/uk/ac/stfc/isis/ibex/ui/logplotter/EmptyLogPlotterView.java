/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.logplotter;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.SWT;

/**
 *	The view to be displayed to the user when no plots have yet been made.
 */
public class EmptyLogPlotterView extends ViewPart {
	public EmptyLogPlotterView() {
	}

	static public String ID = "uk.ac.stfc.isis.ibex.ui.logplotter.EmptyLogPlotterView";
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("New Label");

		
	}

	@Override
	public void setFocus() {
		
	}

}
