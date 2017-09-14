 
package uk.ac.stfc.isis.ibex.e4.ui.bumpstrip.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.prototyping.PrototypeView;
import uk.ac.stfc.isis.ibex.ui.banner.views.BannerView;

public class BumpStripView extends PrototypeView {
    
	@Inject
	public BumpStripView() {
	}

	@Override @PostConstruct
	public void draw(Composite parent) {
        // this.createPartControl(parent, "uk.ac.stfc.isis.ibex.e4.ui",
        // "screenshots/BumpStrip.png");
        new BannerView().createPartControl(parent);
	}	
}