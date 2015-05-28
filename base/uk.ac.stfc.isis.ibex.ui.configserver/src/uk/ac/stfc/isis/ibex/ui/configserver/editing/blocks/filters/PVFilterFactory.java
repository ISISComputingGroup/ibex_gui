package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

public class PVFilterFactory {
	private final Collection<EditableIoc> availableIOCs;
	
	public PVFilterFactory(Collection<EditableIoc> availableIOCs) {
		this.availableIOCs = availableIOCs;
	}
	
	public PVFilter getFilter(SourceFilters type){
		switch(type) {
			case ACTIVE:
				return new FilterFromPVList(Configurations.getInstance().variables().active_pvs);
			case ASSOCIATED:
				return new AssociatedPvFilter(availableIOCs);
			default:
				return new PVFilter();
		}
	}
	
	public PVFilter getFilter(InterestFilters type){
		switch(type) {
			case HIGH:
				return new FilterFromPVList(Configurations.getInstance().variables().highInterestPVs);
			case MEDIUM:
				return new FilterFromPVList(Configurations.getInstance().variables().mediumInterestPVs);
			default:
				return new PVFilter();
		}
	}
	
}
