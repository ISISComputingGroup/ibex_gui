 
package uk.ac.stfc.isis.ibex.e4.ui.beamstatus.views;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.prototyping.PrototypeView;

public class BeamStatusView extends PrototypeView {
    
	@Inject
	public BeamStatusView() {
	}

	@Override @PostConstruct
	public void draw(Composite parent) {
		this.createPartControl(parent, "uk.ac.stfc.isis.ibex.e4.ui", "icons/BeamStatus.png");
	}	
}