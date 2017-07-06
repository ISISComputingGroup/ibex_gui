 
package uk.ac.stfc.isis.ibex.e4.ui.dashboard.views;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import org.eclipse.swt.widgets.Composite;

public class DashboardView {
	@Inject
	public DashboardView() {
		System.out.println("Initialising dashboard view");
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		System.out.println("Creating dashboard view");		
	}
	
	
	
	
}