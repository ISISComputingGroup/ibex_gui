package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;

public class BlockVisibilityFilter extends ViewerFilter {

	private boolean isEnabled = true;
	
	public void enable(boolean enable) {
		isEnabled = enable;
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (!isEnabled) {
			return true;
		}
		
		DisplayBlock model = (DisplayBlock) element;
		return model.getIsVisible();
	}

}
