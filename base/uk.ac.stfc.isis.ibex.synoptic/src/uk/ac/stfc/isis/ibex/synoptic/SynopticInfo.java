package uk.ac.stfc.isis.ibex.synoptic;

import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class SynopticInfo {

	private final String name;
	private final String pv;
	
	public SynopticInfo(String name, String pv) {
		this.name = name;
		this.pv = pv;
	}
	
	public String name() {
		return name;
	}
	
	public String pv() {
		return pv;
	}
	
	public static Collection<String> names(Collection<SynopticInfo> infos) {
		if (infos == null) {
			return Collections.emptyList();
		}
		
		return Lists.newArrayList(Iterables.transform(infos, new Function<SynopticInfo, String>() {
			@Override
			public String apply(SynopticInfo info) {
				return info.name();
			}	
		}));		
	}
	
	public static SynopticInfo search(Collection<SynopticInfo> available, String name) {
		for (SynopticInfo synoptic : available) {
			if (synoptic.name().equals(name)) {
				return synoptic;
			}
		}
		return null;
	}
}
