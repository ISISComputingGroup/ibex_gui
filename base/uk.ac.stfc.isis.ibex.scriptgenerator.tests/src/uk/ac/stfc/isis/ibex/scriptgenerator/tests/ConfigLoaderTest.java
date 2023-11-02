package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionLoader;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonNotReadyException;

public class ConfigLoaderTest {

	private ScriptDefinitionLoader configLoader;
	private List<ScriptDefinitionWrapper> availableConfigs;
	
	private PythonInterface mockPythonInterface;
	
	private ScriptDefinitionWrapper mockedConfig1;
	private ScriptDefinitionWrapper mockedConfig2;
	
	@Before
	public void setUp() {
		
		mockedConfig1 = createMockConfig("Config1", "Param1", "Val1");
		mockedConfig2 = createMockConfig("Config2", "Param2", "Val2");
		
		availableConfigs = new ArrayList<ScriptDefinitionWrapper>();
		availableConfigs.add(mockedConfig1);
		availableConfigs.add(mockedConfig2);
		
		mockPythonInterface = mock(PythonInterface.class);
		
		try {
			when(mockPythonInterface.getScriptDefinitions()).thenReturn(availableConfigs);
		} catch(PythonNotReadyException e) {
			fail("We are mocking this out so should not throw exception");
		}
		
		configLoader = new ScriptDefinitionLoader(mockPythonInterface);
	}

	private ScriptDefinitionWrapper createMockConfig(String configName, String action_parameter_name,
			String action_parameter_value) {
		ScriptDefinitionWrapper mockedConfig = mock(ScriptDefinitionWrapper.class);
		
		when(mockedConfig.getName()).thenReturn(configName);
		
		List<ActionParameter> configParams = new ArrayList<ActionParameter>();
		configParams.add(new JavaActionParameter("param1", "val1", false, false, null));
		when(mockedConfig.getParameters()).thenReturn(configParams);
		
		return mockedConfig;
	}
	
	@Test
	public void test_GIVEN_new_configLoader_THEN_loader_initialised_to_first_config() {
		// Arrange (in setUp)
		
		// Assert
		assertEquals(configLoader.getScriptDefinition(), mockedConfig1);
	}
	
	@Test
	public void test_GIVEN_config_loader_WHEN_config_changed_THEN_action_parameters_and_config_update() {		
		// Arrange (in setUp)
		
		// Act
		configLoader.setScriptDefinition(mockedConfig2);
		
		// Assert
		assertEquals(mockedConfig2, configLoader.getScriptDefinition());
		
		// Compare the parameters in the mocked Config to the ActionParameter in the configLoader.
		assertEquals(mockedConfig2.getParameters().get(0).getName(), configLoader.getParameters().get(0).getName());
	}
	
	@Test
	public void test_GIVEN_config_loader_WHEN_config_set_THEN_ActionParameters_generated_from_action_definition() {
		// Arrange
		ScriptDefinitionWrapper mockedConfigManyParameters = mock(ScriptDefinitionWrapper.class);
		
		when(mockedConfigManyParameters.getName()).thenReturn("ConfigManyParams");
		
		List<ActionParameter> configParams = new ArrayList<ActionParameter>();
		
		String param1name = "parameter one";
		String param1val = "one";
		String param2name = "parameter two";
		String param2val = "two";
		String param3name = "parameter three";
		String param3val = "three";
		
		ActionParameter param1 = new JavaActionParameter(param1name, param1val, false, false, null);
		ActionParameter param2 = new JavaActionParameter(param2name, param2val, false, false, null);
		ActionParameter param3 = new JavaActionParameter(param3name, param3val, false, false, null);
		
		configParams.add(param1);
		configParams.add(param2);
		configParams.add(param3);
		
		when(mockedConfigManyParameters.getParameters()).thenReturn(configParams);
		
		List<ActionParameter> actionParamList = new ArrayList<ActionParameter>();
		
		actionParamList.add(param1);
		actionParamList.add(param2);
		actionParamList.add(param3);
		
		// Act
		configLoader.setScriptDefinition(mockedConfigManyParameters);
		
		// Assert
		assertEquals(actionParamList, configLoader.getParameters());
		
	}
	
	@Test
	public void test_GIVEN_instantiated_config_loader_WHEN_all_available_configs_requested_THEN_all_configs_returned() {
		// Arrange (in setUp)
		
		// Assert
		assertEquals(configLoader.getAvailableScriptDefinitions(), availableConfigs);
	}
	
}
