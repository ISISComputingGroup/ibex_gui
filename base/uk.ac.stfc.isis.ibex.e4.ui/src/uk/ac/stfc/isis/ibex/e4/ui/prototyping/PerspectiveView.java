 
package uk.ac.stfc.isis.ibex.e4.ui.prototyping;

import javax.inject.Inject;

public abstract class PerspectiveView extends PrototypeView {
    
	protected String name;
	
	@Inject
	public PerspectiveView() {
	}
	
	public String getName() {
		return this.name;
	}	
}