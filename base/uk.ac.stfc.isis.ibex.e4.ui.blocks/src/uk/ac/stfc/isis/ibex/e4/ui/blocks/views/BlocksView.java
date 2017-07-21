 
package uk.ac.stfc.isis.ibex.e4.ui.blocks.views;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.prototyping.PrototypeView;

public class BlocksView extends PrototypeView {
    
	@Inject
	public BlocksView() {
	}

	@Override @PostConstruct
	public void draw(Composite parent) {
		this.createPartControl(parent, "uk.ac.stfc.isis.ibex.e4.ui", "screenshots/Blocks.png");
	}	
}