package uk.ac.stfc.isis.ibex.configserver.tests.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.configserver.ServerStatus;
import uk.ac.stfc.isis.ibex.configserver.configuration.Component;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.json.JsonConverters;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

public class JsonConvertersTest {
	
	private String configJson = "";
	private String configDescription = "A test description";
	private String configName = "TESTCONFIG1";
	private String configBlockName = "testblock1";
	private String iocName = "SIMPLE1";
	private String iocJson = "{\"simlevel\": \"None\", \"autostart\": true, \"restart\": false, \"pvsets\": [], \"pvs\": [], \"macros\": [], \"name\": \"" + iocName + "\", \"subconfig\": null}";
	
	private String serverStatusBusy = "{\"status\": \"INITIALISING\"}";
	private String serverStatusNotBusy = "{\"status\": \"\"}";
	
	private String configPVName = "TEST_CONFIG";
	private String configInfos = "[{\"name\": \"" + configName + "\", \"description\": \"" + configDescription + "\", \"pv\": \"" + configPVName + "\"}]";
	
	private String editableIocJson = "{\"" + iocName + "\": {\"macros\": [{\"pattern\": \"\", \"description\": \"IP address of PLC\", \"name\": \"PLCIP\"}], \"running\": false, \"pvs\": [], \"pvsets\": []}}";
	
	private String pvsJson = "[[\"" + configPVName + "\", \"ai\", \"" + configDescription + "\", \"" + iocName + "\"]]";
	
    private String blockJson = "{\"name\": \"" + configBlockName
            + "\", \"local\": true, \"pv\": \"NDWXXX:xxxx:SIMPLE:VALUE1\", \"subconfig\": null, \"visible\": true}";

	@Before
    public void setUp() {
		configJson = "{"; 
		configJson += "\"name\": \""+ configName + "\", \"description\": \"" + configDescription + "\", \"history\": [\"2015-02-16\"]," ;
		configJson += "\"blocks\": [" + blockJson + "],";
		configJson += "\"iocs\": [" + iocJson + "],";
		configJson += "\"components\": [{\"name\": \"sub1\"}], \"groups\": [{\"blocks\": [\"testblock1\"], \"name\": \"Group1\", \"subconfig\": null}]";
		configJson += "}"; 
	}
	
	@Test
	public void conversion_to_config() throws ConversionException {
		// Arrange
		Converter<String, Configuration> conv = new JsonConverters().toConfig();
	
		// Act
		Configuration config = conv.convert(configJson);

		// Assert
		assertEquals(configDescription, config.description());
		assertEquals(configName, config.name());

		assertTrue(config.getIocs().size() == 1);
		assertTrue(config.getBlocks().size() == 1);
		assertTrue(config.getGroups().size() == 1);
		assertTrue(config.getComponents().size() == 1);
		assertTrue(config.getHistory().size() == 1);
	}
	
	@Test
	public void conversion_to_server_status_busy() throws ConversionException {
		// Arrange
		Converter<String, ServerStatus> conv = new JsonConverters().toServerStatus();
	
		// Act
		ServerStatus server = conv.convert(serverStatusBusy);

		// Assert
		assertTrue(server.isBusy());
	}
	
	@Test
	public void conversion_to_server_status_not_busy() throws ConversionException {
		// Arrange
		Converter<String, ServerStatus> conv = new JsonConverters().toServerStatus();
	
		// Act
		ServerStatus server = conv.convert(serverStatusNotBusy);

		// Assert
		assertTrue(!server.isBusy());
	}
	
	@Test
	public void conversion_to_configs_info() throws ConversionException {
		// Arrange
		Converter<String, Collection<ConfigInfo>> conv = new JsonConverters().toConfigsInfo();
	
		// Act
		Collection<ConfigInfo> cInfos = conv.convert(configInfos);

		// Assert
		assertTrue(cInfos.size() == 1);
		
		ConfigInfo cInfo = cInfos.iterator().next();
		
		assertEquals(configDescription, cInfo.description());
		assertEquals(configName, cInfo.name());
		assertEquals(configPVName, cInfo.pv());
	}
	
	@Test
	public void conversion_to_components() throws ConversionException {
		// Arrange
		Converter<String, Collection<Component>> conv = new JsonConverters().toComponents();
	
		// Act
		Collection<Component> comps = conv.convert(configInfos);

		// Assert
		assertTrue(comps.size() == 1);
		
		Component comp = comps.iterator().next();
		
		assertEquals(configDescription, comp.description());
		assertEquals(configName, comp.getName());
		assertEquals(configPVName, comp.pv());
	}
	
	@Test
	public void conversion_to_iocs() throws ConversionException {
		// Arrange
		Converter<String, Collection<EditableIoc>> conv = new JsonConverters().toIocs();
	
		// Act
		Collection<EditableIoc> iocs = conv.convert(editableIocJson);

		// Assert
		assertTrue(iocs.size() == 1);
		
		EditableIoc ioc = iocs.iterator().next();
		
		assertEquals(iocName, ioc.getName());
	}
	
	@Test
	public void conversion_to_pvs() throws ConversionException {
		// Arrange
		Converter<String, Collection<PV>> conv = new JsonConverters().toPVs();
	
		// Act
		Collection<PV> pvs = conv.convert(pvsJson);

		// Assert
		assertTrue(pvs.size() == 1);
		
		PV pv = pvs.iterator().next();
		
		assertEquals(configPVName, pv.getAddress());
		assertEquals(iocName, pv.iocName());
		assertEquals(configDescription, pv.description());
	}
	
	@Test
	public void conversion_string_to_names() throws ConversionException {
		//Arrange
		String namesJson = "[\"TEST_CONFIG1\", \"TEST_CONFIG2\"]";
		Converter<String, Collection<String>> conv = new JsonConverters().toNames();
		
		//Act
		Collection<String> namesList = conv.convert(namesJson);
		
		//Assert
		assertEquals(namesList.size(), 2);
		assertTrue(namesList.contains("TEST_CONFIG1"));
		assertTrue(namesList.contains("TEST_CONFIG2"));
		
	}
	
	@Test
	public void conversion_names_to_string() throws ConversionException {
		//Arrange
		Converter<Collection<String>, String> conv = new JsonConverters().namesToString();
		Collection<String> namesList = new ArrayList<>(Arrays.asList("TEST_CONFIG1", "TEST_CONFIG2"));
		String namesJson = "[\"TEST_CONFIG1\",\"TEST_CONFIG2\"]";
		
		//Act
		String test = conv.convert(namesList);
		
		//Assert
		assertEquals(namesJson, test);
		
	}
	
	@Test
	public void conversion_name_to_string() throws ConversionException {
		//Arrange
		Converter<String, String> conv = new JsonConverters().nameToString();
		String nameJson = "TEST_CONFIG1";
		String expected = "\"" + nameJson + "\"";
		
		//Act
		String test = conv.convert(nameJson);
		
		//Assert
		assertEquals(expected, test);
		
	}
	
	@Test
	public void conversion_to_ioc_states() throws ConversionException {
		//Arrange
		Converter<String, Collection<IocState>> conv = new JsonConverters().toIocStates();
		
		//Act
		Collection<IocState> iocList = conv.convert(editableIocJson);
		
		//Assert
		assertEquals(1, iocList.size());
		
		IocState ioc = iocList.iterator().next();
		assertEquals(iocName, ioc.getName());
		assertTrue(!ioc.getIsRunning());
	}
	
	@Test
	public void conversion_config_to_string() throws ConversionException {
		//Arrange
		Converter<Configuration, String> conv = new JsonConverters().configToString();
		Configuration testConfig = new Configuration(configName, configDescription);
		String expected = "{\"name\":\""+configName+"\",\"description\":\""+configDescription+"\",\"iocs\":[],\"blocks\":[],\"groups\":[],\"components\":[],\"history\":[]}";
		
		//Act
		String test = conv.convert(testConfig);
		
		//Assert
		assertEquals(expected, test);
	}

    @Test(expected = NullPointerException.class)
    public void cannot_convert_invalid_config_empty_json() throws ConversionException {
        // Arrange
        Converter<String, Configuration> conv = new JsonConverters().toConfig();

        // Assert
        Configuration config = conv.convert("{}");

    }

    @Test(expected = ConversionException.class)
    public void cannot_convert_invalid_config_invalid_json() throws ConversionException {
        // Arrange
        Converter<String, Configuration> conv = new JsonConverters().toConfig();

        // Assert
        Configuration config = conv.convert("{");

    }

}
