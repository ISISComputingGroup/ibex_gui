package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ToolItem;

public class PerspectiveSelectionAdapter extends SelectionAdapter {

	private final PerspectivesProvider provider;
	public static final String ID_KEY = "TARGET_ID";

	public PerspectiveSelectionAdapter(PerspectivesProvider provider) {
		super();
		this.provider = provider;
	}
	
	@Override
	public void widgetSelected(SelectionEvent event) {
		String targetElementId = (String) ((ToolItem) event.getSource()).getData(ID_KEY);
		MPerspective element = provider.getPerspective(targetElementId);
	    if (element != null) {
	      	provider.getPartService().switchPerspective(element);
	    }
	    else {
	      	System.out.println("Unable to find perspective part with ID: " + targetElementId);
	    }
	}
}
