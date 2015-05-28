package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

import java.util.Collection;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;

public class FilterViewerFromPVList extends ViewerFilter {
	
	private Collection<String> AllowedPVAddresses;
	
	public void setAllowedPVs(Collection<String> PVNames) {
		this.AllowedPVAddresses = PVNames;
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (AllowedPVAddresses == null) {
			return true;
		}
		
		if (AllowedPVAddresses.isEmpty()) {
			return false;
		}
		
		PV p = (PV) element;
		String PVname = p.getAddress();
		
		if (AllowedPVAddresses.contains(PVname)) {
			return true;
		}
		
		return false;
	}

}
