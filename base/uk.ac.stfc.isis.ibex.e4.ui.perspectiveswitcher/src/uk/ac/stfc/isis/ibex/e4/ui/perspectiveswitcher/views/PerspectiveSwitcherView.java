package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.prototyping.GridLayout;
import uk.ac.stfc.isis.ibex.e4.ui.prototyping.PrototypeView;

public class PerspectiveSwitcherView {
    
	@Inject
	public PerspectiveSwitcherView() {
	}

	@PostConstruct
	public void draw(Composite parent) {
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