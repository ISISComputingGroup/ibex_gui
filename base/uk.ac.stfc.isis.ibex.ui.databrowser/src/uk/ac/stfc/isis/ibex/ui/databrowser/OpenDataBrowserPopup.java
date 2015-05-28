package uk.ac.stfc.isis.ibex.ui.databrowser;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import uk.ac.stfc.isis.ibex.ui.UI;

/*
 * This class is needed to disply OPI PVs in the databrowser
 */
public class OpenDataBrowserPopup extends org.csstudio.trends.databrowser2.OpenDataBrowserPopup {

	@Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {       
		UI.getDefault().switchPerspective(Perspective.ID);
		return super.execute(event);

    }
}
