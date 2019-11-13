package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigLoader;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class ConfigLoaderTest {

	private ConfigLoader configLoader;
	private List<Config> availableConfigs;
	
	private PythonInterface mockPythonInterface;
	
	private Config mockedConfig1;
	private Config mockedConfig2;
	
	@Before
	public void setUp() {
		
		mockedConfig1 = create_mock_config("Config1");
		mockedConfig2 = create_mock_config("Config2");
		
		availableConfigs = new ArrayList<Config>();
		availableConfigs.add(mockedConfig1);
		availableConfigs.add(mockedConfig2);
		
		PythonInterface mockPythonInterface = mock(PythonInterface.class);
		
		when(mockPythonInterface.getActionDefinitions()).thenReturn(availableConfigs);
		
		configLoader = new ConfigLoader(mockPythonInterface);
	}

	private Config create_mock_config(String action_parameter_name) {
		Config mockedConfig = mock(Config.class);
		
		ArrayList<String> configParams = new ArrayList<String>();
		
		configParams.add(action_parameter_name);
				
		when(mockedConfig.getParameters()).thenReturn(configParams);
		
		return mockedConfig;
	}
	
	@Test
	public void test_GIVEN_config_loader_WHEN_config_changed_THEN_action_parameters_and_config_update() {		
		assertEquals(configLoader.getConfig(), mockedConfig1);
		
		configLoader.setConfig(mockedConfig2);
		
		assertEquals(mockedConfig2, configLoader.getConfig());
		assertEquals(mockedConfig2.getParameters().get(0), configLoader.getParameters().get(0).getName());
	}
	
	
}
