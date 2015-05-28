package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class IocFilteredConfiguration extends Configuration {
	
	public IocFilteredConfiguration(Configuration other) {
		super(other.name(), other.description(), other.synoptic(), filterIocs(other.getIocs()), other.getBlocks(), other.getGroups(), other.getComponents(), other.getHistory());
	}
	
	public static Collection<Ioc> filterIocs(Collection<Ioc> unfiltered) {
		return Lists.newArrayList(Iterables.filter(unfiltered, new Predicate<Ioc>(){
			@Override
			public boolean apply(Ioc ioc) {
				return !isDefault(ioc);
		}}));
	}

	private static boolean isDefault(Ioc ioc) {		
		return ioc.getAutostart() == false &&
				ioc.getRestart() == false &&
				ioc.getSimLevel() == SimLevel.NONE &&
				ioc.getPvs().isEmpty() &&
				ioc.getMacros().isEmpty() &&
				ioc.getPvSets().isEmpty();		
	}
}
