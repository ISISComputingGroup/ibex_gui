package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptDefinitionNotMatched;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorJsonFileHandler;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

import static org.mockito.Mockito.mock;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

public class ScriptGeneratorJsonFileHandlerTest {
	
	String geniePythonVersion = "1";
	String dateAndTime = "20/03/2010 00:00";
	PreferenceSupplier prefSupplier = mock(PreferenceSupplier.class);
	public ScriptGeneratorJsonFileHandler fileHandler = new ScriptGeneratorJsonFileHandler();
	Path scriptDefName = Path.of("scriptDefTest.py");
	Path JsonFileName =  Path.of("jsonFileTest.json");
	final String scriptDefContent = "from genie_python.genie_script_generator import ScriptDefinition";
	
	final String actualJsonContent = String.format("{\r\n" + 
			"  \"version_JSON_format\": \"1\",\r\n" + 
			"  \"script_generator_version\": \"\",\r\n" + 
			"  \"date_and_time\": \"%s\",\r\n" + 
			"  \"script_definition_file_path\": \"C:\\\\ScriptDefinitions\\\\imat.py\",\r\n" + 
			"  \"script_definition_file_git_hash\": \"\",\r\n" + 
			"  \"genie_python_version\": \"%s\",\r\n" + 
			"  \"script_definition_content\": \"%s\",\r\n" + 
			"  \"actions\": [\r\n" + 
			"    {\r\n" + 
			"      \"action_name\": \"default\",\r\n" + 
			"      \"parameters\": [\r\n" + 
			"        {\r\n" + 
			"          \"field\": \"1\",\r\n" + 
			"          \"temperature\": \"2\"\r\n" + 
			"        }\r\n" + 
			"      ]\r\n" + 
			"    }\r\n" + 
			"  ]\r\n" + 
			"}", dateAndTime, geniePythonVersion, scriptDefContent);
	

	@Test 
	public void Test_GIVEN_user_parameters_THEN_read_correctly() {
		JavaActionParameter actionParamOne = new JavaActionParameter("temperature", "", false, false, null);
		JavaActionParameter actionParamTwo = new JavaActionParameter("field", "", false, false, null);
		ArrayList<JavaActionParameter> actionParams = new ArrayList<JavaActionParameter>(
				Arrays.asList(actionParamOne, actionParamTwo));
		
		Map<JavaActionParameter, String> actionOne = new HashMap<JavaActionParameter, String>();
		actionOne.put(actionParamOne, "2");
		actionOne.put(actionParamTwo, "1");
		try {
						
			ScriptGeneratorJsonFileHandler fileHandlerSpy = Mockito.spy(fileHandler);
			Mockito.doReturn(actualJsonContent).when(fileHandlerSpy).readFileContent(JsonFileName);
			Mockito.doReturn(scriptDefContent).when(fileHandlerSpy).readFileContent(scriptDefName);
			
			List<Map<JavaActionParameter, String>> actual = fileHandlerSpy.getParameterValues(JsonFileName, scriptDefName, actionParams);
			List<Map<JavaActionParameter, String>> expected = new ArrayList<Map<JavaActionParameter, String>>();
			expected.add(actionOne);
			assertEquals(expected, actual);
		} catch (UnsupportedOperationException | ScriptDefinitionNotMatched e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail("should not be reading from a file");
		} 
	}
	
	@Test
	public void test_GIVEN_different_script_defintion_THEN_do_not_load_the_data_file() {
	
		try {
			String scriptDeftwo = "this is script definition";
			Path jsonFile = Path.of("jsonfile");
			Path pythonFile = Path.of("pythonFile");
			ScriptGeneratorJsonFileHandler fileHandlerSpy = Mockito.spy(fileHandler);
			Mockito.doReturn(actualJsonContent).when(fileHandlerSpy).readFileContent(jsonFile);
			Mockito.doReturn(scriptDeftwo).when(fileHandlerSpy).readFileContent(pythonFile);
			fileHandlerSpy.getParameterValues(jsonFile, pythonFile, new ArrayList<JavaActionParameter>());
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptDefinitionNotMatched e) {
			assertEquals(e.getMessage(), "Current script definition does not match with the "
					+ "script definition that was used to generate the selected data file");
		} catch (IOException e) {
			fail("should not be reading from a file");
		}
	}
	
	@Test 
	public void test_GIVEN_old_json_file_version_THEN_give_error() {
		
		String randomVersion = "3234";
		String originalVersion = ScriptGeneratorJsonFileHandler.CURRENT_VERSION;

		try {
			
			String incorrectVersion = String.format("{\"versionJSONFormat\": \"%s\"}", randomVersion);
			
			ScriptGeneratorJsonFileHandler fileHandlerSpy = Mockito.spy(fileHandler);
			Mockito.doReturn(incorrectVersion).when(fileHandlerSpy).readFileContent(JsonFileName);
			
			fileHandlerSpy.getParameterValues(JsonFileName, scriptDefName, new ArrayList<JavaActionParameter>());
		} catch (UnsupportedOperationException e) {
			assertEquals(e.getMessage(), String.format("Data file version %s is not supported.\nSupported version %s",randomVersion, originalVersion));
		} catch (ScriptDefinitionNotMatched e) {
			fail("Something went wrong when creating script defintion");
		} catch (IOException e) {
			fail("should not be reading from a file");
		}
	}
	
	@Test
	public void test_GIVEN_script_def_and_actions_THEN_generate_valid_Json() {
		
		class ScriptGeneratorFileHandlerForTest extends ScriptGeneratorJsonFileHandler {
			protected String getGeniePythonVersion() {
				return geniePythonVersion;
			}
			protected String getCurrentDateAndTime() {
				return dateAndTime;
			}
		}
		
		ScriptGeneratorFileHandlerForTest fileHandlerTemp = new ScriptGeneratorFileHandlerForTest();
		
		JavaActionParameter actionParamOne = new JavaActionParameter("temperature", "10", false, false, null);
		JavaActionParameter actionParamTwo = new JavaActionParameter("field", "10", false, false, null);
		
		Map<JavaActionParameter, String> actionOne = new HashMap<JavaActionParameter, String>();
		
		actionOne.put(actionParamOne, "2");
		actionOne.put(actionParamTwo, "1");
		
		List<ScriptGeneratorAction> actions = new ArrayList<ScriptGeneratorAction>();
		actions.add(new ScriptGeneratorAction(actionOne));
	
		String actual = fileHandlerTemp.createJsonString(actions, scriptDefContent, Paths.get("C:/ScriptDefinitions/imat.py"));
		// remove carriage return and test
		assertEquals(actualJsonContent.replace("\r", ""), actual);
	}
	

}
