package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

public class AssociatedPvFilter extends PVFilter {
	
	private AssociatedPvViewerFilter filter = new AssociatedPvViewerFilter();;
	
	public AssociatedPvFilter(final Collection<EditableIoc> availableIocs) {	
		updateIocList(availableIocs);
		
		//Add listener to all available IOCs as they may become part of the configuration
		for (Ioc ioc : availableIocs) {
			ioc.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent arg0) {
					updateIocList(availableIocs);
					firePropertyChange("refresh", false, true);
				}
			});
		}
	}
	
	private void updateIocList(Collection<EditableIoc> availableIocs) {
		Collection<String> iocNames = new ArrayList<String>();
		Collection<EditableIoc> usedIOCs = UsedIocFilter.filterIocs(availableIocs);
		for (EditableIoc ioc : usedIOCs) {
			iocNames.add(ioc.getName());
		}
		filter.setAllowedIocs(iocNames);
	}
	
	@Override
	public ViewerFilter getFilter() {
		return filter;
	}
}
