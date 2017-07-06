 
package uk.ac.stfc.isis.ibex.e4.ui.dashboard.views;

import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;

import javax.annotation.PostConstruct;

public class DashboardView {
    
	@Inject
	public DashboardView() {
		System.out.println("Initialising dashboard view");
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
	}
	
}