 
package uk.ac.stfc.isis.ibex.e4.ui.prototyping;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;

public abstract class PrototypeView {
    
	@Inject
	public PrototypeView() {
	}
	
	protected abstract void draw(Composite parent);	
	
	protected void createPartControl(Composite parent, String pluginName, String imagePath) {
		System.out.println("Creating part...");
        GridLayout glParent = new GridLayout(3, false);
		glParent.marginHeight = 0;
		glParent.marginWidth = 0;
		glParent.horizontalSpacing = 1;
		glParent.verticalSpacing = 0;
		parent.setLayout(glParent);
		
		Label prototype = new Label(parent, SWT.NONE);
		GridData gd_prototype = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		Image img = ResourceManager.getPluginImage(pluginName, imagePath);
		prototype.setImage(img);
		prototype.setLayoutData(gd_prototype);
	}
	
}