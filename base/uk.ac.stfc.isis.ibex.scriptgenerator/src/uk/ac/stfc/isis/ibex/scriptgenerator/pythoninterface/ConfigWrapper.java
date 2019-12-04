package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.List;

import uk.ac.stfc.isis.ibex.scriptgenerator.generation.PythonGeneratorInterface;

public interface ConfigWrapper {
	public List<Config> getActionDefinitions();
	
	public PythonGeneratorInterface getGenerator();
}
