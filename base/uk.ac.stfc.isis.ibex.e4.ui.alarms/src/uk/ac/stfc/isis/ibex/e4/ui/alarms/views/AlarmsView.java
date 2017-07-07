 
package uk.ac.stfc.isis.ibex.e4.ui.alarms.views;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.prototyping.PerspectiveView;

public class AlarmsView extends PerspectiveView {
    
	@Inject
	public AlarmsView() {
		name = "Alarms";
	}

	@Override @PostConstruct
	public void draw(Composite parent) {
		this.createPartControl(parent, "uk.ac.stfc.isis.ibex.e4.ui", "icons/Alarms.png");
	}	
}