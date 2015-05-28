package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePV;

public class IocPVNameFilter extends ViewerFilter {
	private String searchString = ".*";
	
	public void setSearchText(String s) {
	    // ensure that the value can be used for matching 
	    this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer arg0, Object arg1, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		AvailablePV p = (AvailablePV) element;
		if (p.getName().matches(searchString)) {
			return true;
		}
		if (p.getDescription().matches(searchString)) {
			return true;
		}
		return false;
	}

}
