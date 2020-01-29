package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigLoader;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonNotReadyException;

public class ConfigLoaderTest {

	private ConfigLoader configLoader;
	private List<Config> availableConfigs;
	
	private PythonInterface mockPythonInterface;
	
	private Config mockedConfig1;
	private Config mockedConfig2;
	
	@Before
	public void setUp() {
		
		mockedConfig1 = createMockConfig("Config1");
		mockedConfig2 = createMockConfig("Config2");
		
		availableConfigs = new ArrayList<Config>();
		availableConfigs.add(mockedConfig1);
		availableConfigs.add(mockedConfig2);
		
		mockPythonInterface = mock(PythonInterface.class);
		
		try {
			when(mockPythonInterface.getActionDefinitions()).thenReturn(availableConfigs);
		} catch(PythonNotReadyException e) {
			fail("We are mocking this out so should not throw exception");
		}
		
		configLoader = new ConfigLoader(mockPythonInterface);
	}

	private Config createMockConfig(String action_parameter_name) {
		Config mockedConfig = mock(Config.class);
		
		ArrayList<String> configParams = new ArrayList<String>();
		
		configParams.add(action_parameter_name);
				
		when(mockedConfig.getParameters()).thenReturn(configParams);
		
		return mockedConfig;
	}
	
	@Test
	public void test_GIVEN_new_configLoader_THEN_loader_initialised_to_first_config() {
		// Arrange (in setUp)
		
		// Assert
		assertEquals(configLoader.getConfig(), mockedConfig1);
	}
	
	@Test
	public void test_GIVEN_config_loader_WHEN_config_changed_THEN_action_parameters_and_config_update() {		
		// Arrange (in setUp)
		
		// Act
		configLoader.setConfig(mockedConfig2);
		
		// Assert
		assertEquals(mockedConfig2, configLoader.getConfig());
		
		// Compare the parameters in the mocked Config to the ActionParameter in the configLoader.
		assertEquals(mockedConfig2.getParameters().get(0), configLoader.getParameters().get(0).getName());
	}
	
	@Test
	public void test_GIVEN_config_loader_WHEN_config_set_THEN_ActionParameters_generated_from_action_definition() {
		// Arrange
		Config mockedConfigManyParameters = mock(Config.class);
		
		ArrayList<String> configParams = new ArrayList<String>();
		
		String param1 = "parameter one";
		String param2 = "parameter two";
		String param3 = "parameter three";
		
		configParams.add(param1);
		configParams.add(param2);
		configParams.add(param3);
		
		when(mockedConfigManyParameters.getParameters()).thenReturn(configParams);
		
		ArrayList<ActionParameter> actionParamList = new ArrayList<ActionParameter>();
		
		actionParamList.add(new ActionParameter(param1));
		actionParamList.add(new ActionParameter(param2));
		actionParamList.add(new ActionParameter(param3));
		
		// Act
		configLoader.setConfig(mockedConfigManyParameters);
		
		// Assert
		assertEquals(actionParamList, configLoader.getParameters());
		
	}
	
	@Test
	public void test_GIVEN_instantiated_config_loader_WHEN_all_available_configs_requested_THEN_all_configs_returned() {
		// Arrange (in setUp)
		
		// Assert
		assertEquals(configLoader.getAvailableConfigs(), availableConfigs);
	}
	
}
