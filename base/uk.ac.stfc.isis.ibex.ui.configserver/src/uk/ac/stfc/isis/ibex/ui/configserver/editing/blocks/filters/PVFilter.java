package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class to extend when creating a new filter type for the available PVs
 */
public class PVFilter extends ModelObject {
	protected boolean refresh;
	
	public ViewerFilter getFilter() {
		return new ViewerFilter() {
			@Override
			public boolean select(Viewer arg0, Object arg1, Object arg2) {
				return true;
			}
		};
	};
}
