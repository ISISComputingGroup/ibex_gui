package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.List;
import java.util.Map;

import uk.ac.stfc.isis.ibex.scriptgenerator.generation.PythonGeneratorInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public interface ConfigWrapper {
	
	public List<Config> getActionDefinitions();
	
	public PythonGeneratorInterface getGenerator();

	public String generate(List<ScriptGeneratorAction> scriptGenContent, Config config);

	public Map<Integer, String> areParamsValid(List<ScriptGeneratorAction> scriptGenContent, Config config);
	
}
