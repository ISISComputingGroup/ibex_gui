 
package uk.ac.stfc.isis.ibex.ui.banner.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;

public class Banner {
	@Inject
    public Banner(Composite parent) {
        (new BannerView()).createPartControl(parent);
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
	}
	
}