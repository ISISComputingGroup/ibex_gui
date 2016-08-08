/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.logplotter;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.wb.swt.SWTResourceManager;

@SuppressWarnings("checkstyle:magicnumber")
/**
 *	The view to be displayed to the user when no plots have yet been made.
 */
public class EmptyLogPlotterView extends ViewPart {

	/**
	 * The ID for the view, used to import it externally
	 */
	static public String ID = "uk.ac.stfc.isis.ibex.ui.logplotter.EmptyLogPlotterView";
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Label lblTitle = new Label(parent, SWT.NONE);
		lblTitle.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
		lblTitle.setText("Log Plotter View");
		
		Label lblDescription = new Label(parent, SWT.NONE);
		lblDescription.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		lblDescription.setText("This can be populated with graphs displaying block history.\r\nTo display a graph right click on a block in the blocks view above and click Display Block History.\r\nAlternatively a graph can be displyed by right clicking on a PV in an OPI and selecting Process Variable -> Log Plotter.");

		
	}

	@Override
	public void setFocus() {
		
	}

}
