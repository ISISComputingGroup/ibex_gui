package uk.ac.stfc.isis.ibex.ui.statusbar;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class StatusLineConfigLabel extends ContributionItem{

	private Label lbl;
	private String lblText;
	private String lblTooltip;
	
	public StatusLineConfigLabel(String id) {
		super(id);
		
    	setConfig("Unknown");
    	setToolTip("Unknown");
	}

	public void fill(Composite parent) {
		lbl = new Label(parent, SWT.FLAT | SWT.CENTER);
		lbl.setText(lblText);
		lbl.setToolTipText(lblTooltip);
		lbl.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
	}
	
	public void setConfig(String text) {
		lblText = "Current configuration: " + text;
	}
	
	public void setToolTip(String text) {
		lblTooltip = text;
	}
	
}
