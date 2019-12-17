package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.stfc.isis.ibex.scriptgenerator.generation.PythonGeneratorInterface;

public interface ConfigWrapper {
	public List<Config> getActionDefinitions();
	
	public PythonGeneratorInterface getGenerator();

	public String generate(List<Map<String, String>> scriptGenContent, Config config);

	public HashMap<Integer, String> checkParamValidity(List<Map<String, String>> scriptGenContent, Config config);
}
