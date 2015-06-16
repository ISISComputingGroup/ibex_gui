package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;

public class PVAddressSearch extends ViewerFilter {
	private String searchString = ".*";
	
	public void setSearchText(String s) {
	    // ensure that the value can be used for matching 
	    this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		PV p = (PV) element;
		if (p.getAddress().matches(searchString)) {
			return true;
		}
		if (p.getDescription().matches(searchString)) {
			return true;
		}
		// Use uppercase checks to eliminate case sensitivity on the search - Story 600
		String upSearch = searchString.toUpperCase();
		String upadd = p.getAddress().toUpperCase();
		String updesc = p.getDescription().toUpperCase();
		if (upadd.matches(upSearch) || updesc.matches(upSearch)) {
			return true;
		}
		return false;
	}
}
