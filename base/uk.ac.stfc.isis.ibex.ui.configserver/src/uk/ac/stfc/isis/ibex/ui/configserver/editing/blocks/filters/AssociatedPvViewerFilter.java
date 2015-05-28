package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

import java.util.Collection;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;

public class AssociatedPvViewerFilter extends ViewerFilter {
	
	private Collection<String> AllowedIocNames;
	
	public void setAllowedIocs(Collection<String> IocNames) {
		this.AllowedIocNames = IocNames;
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (AllowedIocNames == null) {
			return true;
		}
		
		if (AllowedIocNames.isEmpty()) {
			return false;
		}
		
		PV p = (PV) element;
		String PVIoc = p.iocName();
		
		if (AllowedIocNames.contains(PVIoc)) {
			return true;
		}
		
		return false;
	}

}
