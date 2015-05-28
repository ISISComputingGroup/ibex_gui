package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class UsedIocFilter {
	
	public static Collection<EditableIoc> filterIocs(Collection<EditableIoc> unfiltered) {
		return Lists.newArrayList(Iterables.filter(unfiltered, new Predicate<EditableIoc>(){
			@Override
			public boolean apply(EditableIoc ioc) {
				return !isDefault(ioc);
		}}));
	}

	private static boolean isDefault(EditableIoc ioc) {		
		return ioc.getAutostart() == false &&
				ioc.getRestart() == false &&
				ioc.getSimLevel() == SimLevel.NONE &&
				ioc.getPvs().isEmpty() &&
				ioc.getMacros().isEmpty() &&
				ioc.getPvSets().isEmpty();		
	}
}
