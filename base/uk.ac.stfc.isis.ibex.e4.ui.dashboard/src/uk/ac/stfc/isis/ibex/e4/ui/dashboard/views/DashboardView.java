 
package uk.ac.stfc.isis.ibex.e4.ui.dashboard.views;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;

public class DashboardView {
    
	@Inject
	public DashboardView() {
		System.out.println("Dashboard view initialised");
	}
	
	@PostConstruct
	public void createPartControl(Composite parent) {
		System.out.println("Building dashboard");
		GridLayout gridLayout = new GridLayout(1, false);
		parent.setLayout(gridLayout);
		
		Label prototype = new Label(parent, SWT.NONE);
		GridData gd_prototype = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		prototype.setLayoutData(gd_prototype);
		prototype.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ex.prototype", "resources/Dashboard.png"));
		System.out.println("Dashboard construction complete");
	}
	
}