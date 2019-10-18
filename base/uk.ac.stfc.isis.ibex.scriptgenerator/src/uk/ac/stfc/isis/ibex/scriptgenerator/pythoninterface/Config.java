package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.HashMap;

public interface Config {
	public HashMap<String, Class<?>> getParameters();
	public String doAction(Object... arguments);
	public String parametersValid(Object... arguments);
	public String getInstrument();
}
