package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.HasStatus;

public class DynamicScriptName implements HasStatus<Optional<String>> {
	
	private Optional<String> name;
	
	public DynamicScriptName(Optional<String> name) {
		this.name = name;
	}

	@Override
	public Optional<String> getStatus() {
		return name;
	}
}
