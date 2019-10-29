package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.List;

public interface Config {
	public List<String> getParameters();
	public String doAction(Object... arguments);
	public String parametersValid(Object... arguments);
	public String getInstrument();
}
