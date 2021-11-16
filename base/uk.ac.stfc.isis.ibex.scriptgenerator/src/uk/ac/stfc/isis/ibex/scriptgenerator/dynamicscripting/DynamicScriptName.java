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
	
	public Optional<String> getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof DynamicScriptName) {
			DynamicScriptName otherScript = (DynamicScriptName) other;
			return this.name.equals(otherScript.getName());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public String toString() {
		return this.name.toString();
	}
}
