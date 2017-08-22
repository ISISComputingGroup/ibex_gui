package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PerspectiveSelectionAdapter extends SelectionAdapter {
	
	private PerspectiveInfo perspective;

	public PerspectiveSelectionAdapter(PerspectiveInfo perspective) {
		super();
		this.perspective = perspective;
	}
	
	@Override
	public void widgetSelected(SelectionEvent event) {
		// How do we open a perspective from an event?
		// MPerspective element = (MPerspective) modelService.find(perspective.getPartID(), app);
	    // if (element != null) {
	    //  	partService.switchPerspective(element);
	    // } 
	    // else {
	    //  	System.out.println("Unable to find perspective part: " + perspective.getPartID());
	    // }
	}
}
