package uk.ac.stfc.isis.ibex.ui.motor.views;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/** Configurable settings for the table of motors. */
public class SettingsView {
	static Text enableAdvanceMotorViewLabel;

	/**
     * Initialises the view.
     *
     * @param parent The parent composite
     */
	@PostConstruct
	public void createPartControl(Composite parent) {
	
	GridLayout gridLayout = new GridLayout(2, false);
	parent.setLayout(gridLayout);
	
	/** The advance motor view provides more status and read back information for a minimal motor view **/
	new Button(parent, SWT.CHECK).setText("Enable advance table of motors");
	
	}
}
