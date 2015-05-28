package uk.ac.stfc.isis.ibex.configserver.configuration;

import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ConfigInfo {

	private final String name;
	private final String description;
	private final String pv;
	
	public ConfigInfo(String name, String description, String pv) {
		this.name = name;
		this.description = description;
		this.pv = pv;
	}
	
	public String name() {
		return name;
	}
	
	public String description() {
		return description;
	}
	
	public String pv() {
		return pv;
	}
	
	public static Collection<String> names(Collection<ConfigInfo> infos) {
		if (infos == null) {
			return Collections.emptyList();
		}
		
		return Lists.newArrayList(Iterables.transform(infos, new Function<ConfigInfo, String>(){
			@Override
			public String apply(ConfigInfo info) {
				return info.name();
			}	
		}));		
	}
}
