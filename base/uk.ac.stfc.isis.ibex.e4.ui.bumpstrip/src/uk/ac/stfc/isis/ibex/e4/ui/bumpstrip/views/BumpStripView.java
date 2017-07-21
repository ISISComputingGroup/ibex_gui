 
package uk.ac.stfc.isis.ibex.e4.ui.bumpstrip.views;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.prototyping.PrototypeView;

public class BumpStripView extends PrototypeView {
    
	@Inject
	public BumpStripView() {
	}

	@Override @PostConstruct
	public void draw(Composite parent) {
		this.createPartControl(parent, "uk.ac.stfc.isis.ibex.e4.ui", "screenshots/BumpStrip.png");
	}	
}