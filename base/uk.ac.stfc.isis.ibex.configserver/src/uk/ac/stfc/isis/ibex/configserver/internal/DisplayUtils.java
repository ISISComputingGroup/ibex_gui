package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.Group;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class DisplayUtils {
	
	private static final String NONE = "none"; 
	private static final String OTHER = "Other";
	
	public static String renameGroup(String name) {
		return name.toLowerCase().equals(NONE) ? OTHER : name;
	}
	
	public static <T extends Group> Collection<T> removeOtherGroup(Collection<T> groups) {
		return Lists.newArrayList(Iterables.filter(groups, new Predicate<T>() {
			@Override
			public boolean apply(T group) {
				return !group.getName().equals(OTHER);
			}
		}));
	}
}
