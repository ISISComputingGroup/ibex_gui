package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class AvailableMacroSearcher extends ModelObject {
	private String name;
	private String pattern;
	
	private final Collection<Macro> availabe;
	
	public AvailableMacroSearcher (Collection<Macro> availabeMacros)
	{
		availabe = availabeMacros;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		
		for (Macro a : availabe) {
			if (name.equals(a.getName())) {
				setPattern(a.getPattern());
				return;
			}
		}
		
		setPattern(null);
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public void setPattern(String pattern) {
		firePropertyChange("pattern", this.pattern, this.pattern = pattern);
	}
}
