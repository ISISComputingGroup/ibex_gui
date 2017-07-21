 
package uk.ac.stfc.isis.ibex.e4.ui.dae.views;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.prototyping.PerspectiveView;

public class DAEView extends PerspectiveView {
	    
	@Inject
	public DAEView() {
		name = "DAE";
	}

	@Override @PostConstruct
	public void draw(Composite parent) {
		System.out.println("Drawing DAE");
		this.createPartControl(parent, "uk.ac.stfc.isis.ibex.e4.ui", "screenshots/DAE.png");
	}	
}