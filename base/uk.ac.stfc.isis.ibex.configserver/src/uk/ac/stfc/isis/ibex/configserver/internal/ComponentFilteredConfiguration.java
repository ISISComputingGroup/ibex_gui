package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/*
 * A configuration from which component-derived 
 * elements have been filtered.
 */
public class ComponentFilteredConfiguration extends Configuration {
	
	public ComponentFilteredConfiguration(Configuration other) {
		super(other);
	}
	
	@Override
	public Collection<Ioc> getIocs() {
		return Lists.newArrayList(Iterables.filter(super.getIocs(), new Predicate<Ioc>() {
			@Override
			public boolean apply(Ioc ioc) {
				return !ioc.hasSubConfig();
			}
		}));
	}

	@Override
	public Collection<Block> getBlocks() {
		return Lists.newArrayList(Iterables.filter(super.getBlocks(), new Predicate<Block>() {
			@Override
			public boolean apply(Block block) {
				return !block.hasSubConfig();
			}
		}));
	}
	
	@Override
	public Collection<Group> getGroups() {
		return Lists.newArrayList(Iterables.filter(super.getGroups(), new Predicate<Group>() {
			@Override
			public boolean apply(Group group) {
				return !group.hasSubConfig();
			}
		}));
	}
}
