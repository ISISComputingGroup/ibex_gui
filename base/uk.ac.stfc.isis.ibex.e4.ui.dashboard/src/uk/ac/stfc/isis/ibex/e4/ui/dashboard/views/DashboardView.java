 
package uk.ac.stfc.isis.ibex.e4.ui.dashboard.views;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;

public class DashboardView {
    
	@Inject
	public DashboardView() {
	}
	
	@PostConstruct
	public void createPartControl(Composite parent) {
		System.out.println("Creating part...");
        GridLayout glParent = new GridLayout(3, false);
		glParent.marginHeight = 0;
		glParent.marginWidth = 0;
		glParent.horizontalSpacing = 1;
		glParent.verticalSpacing = 0;
		parent.setLayout(glParent);
		
		Label prototype = new Label(parent, SWT.NONE);
		GridData gd_prototype = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		Image img = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.e4.ui", "icons/Dashboard.png");
		prototype.setImage(img);
		prototype.setLayoutData(gd_prototype);
	}
	
}