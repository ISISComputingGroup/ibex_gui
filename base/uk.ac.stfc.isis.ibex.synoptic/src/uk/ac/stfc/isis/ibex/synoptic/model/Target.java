package uk.ac.stfc.isis.ibex.synoptic.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Target {

	private final String name;
	private final Map<String, String> properties = new LinkedHashMap<>();
	
	public Target(String name) {
		this.name = name;
	}
	
	public String name() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public void addProperty(String key, String value) {
		properties.put(key, value);
	}

	public Map<String, String> properties() {
		return new LinkedHashMap<>(properties);
	}
}
