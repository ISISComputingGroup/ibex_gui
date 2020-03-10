package uk.ac.stfc.isis.ibex.scriptgenerator.tests;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.mockito.Mockito;
import org.junit.After;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptDefinitionNotMatched;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorJsonFileHandler;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

public class ScriptGeneratorJsonFileHandlerTest {
	
	String filePathPrefix;
	PreferenceSupplier prefSupplier = mock(PreferenceSupplier.class);
	public ScriptGeneratorJsonFileHandler fileHandler = new ScriptGeneratorJsonFileHandler();
	String scriptDefName = "scriptDefTest";
	String JsonFileName =  "jsonFileTest.json";
	
	@Before 
	public void setUp() {
		filePathPrefix = (System.getProperty("user.dir") + "\\test_script_gen_handler\\");
		if (!(new File(filePathPrefix).mkdir())) {
			fail("We need to create this directory to write files to it");
		}
		when(prefSupplier.scriptGeneratorDataFileFolder()).thenReturn(filePathPrefix);
	}
	
	@After
	public void tearDown() {
		File test_scripts_folder = new File(filePathPrefix);
		if(test_scripts_folder.exists()) {
			for (String entry : test_scripts_folder.list()) {
				if(!new File(test_scripts_folder.getPath(), entry).delete()) {
					fail("Failed to delete file " + entry);
				}
			}
			if(!test_scripts_folder.delete()) {
				fail("Failed to delete folder " + test_scripts_folder.getPath());
			}
		}
	}
	
	@Test 
	public void Test_GIVEN_user_parameters_THEN_read_correctly() {
		ActionParameter actionParamOne = new ActionParameter("temp");
		ActionParameter actionParamTwo = new ActionParameter("uamps");
		
		Map<ActionParameter, String> actionOne = new HashMap<ActionParameter, String>();
		actionOne.put(actionParamOne, "1");
		actionOne.put(actionParamTwo, "2");
		
		ScriptGeneratorAction action = new ScriptGeneratorAction(actionOne);
		List<ScriptGeneratorAction> actions = new ArrayList<ScriptGeneratorAction>();
		actions.add(action);
		
		try {
			
			String filepath = filePathPrefix + scriptDefName + ".py";
			File file = new File(filepath);
			file.createNewFile();
			
			ScriptGeneratorJsonFileHandler fileHandlerSpy = Mockito.spy(fileHandler);
			Mockito.doReturn(ScriptGeneratorJsonFileHandler.CURRENT_VERSION).when(fileHandlerSpy).getCurrentGenieVersionNumber();
			
			fileHandlerSpy.saveParameters(actions, filepath, filePathPrefix + JsonFileName);
			List<Map<ActionParameter, String>> actual = fileHandlerSpy.getParameterValues(filePathPrefix + JsonFileName, filepath);
			List<Map<ActionParameter, String>> expected = new ArrayList<Map<ActionParameter, String>>();
			expected.add(actionOne);
			assertEquals(expected, actual);
		} catch (InterruptedException | ExecutionException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedOperationException | ScriptDefinitionNotMatched e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
		} 
	}
	
	@SuppressWarnings("static-access")
	@Test 
	public void test_GIVEN_old_json_file_version_THEN_give_error() {
		
		String randomVersion = "3234";
		String originalVersion = ScriptGeneratorJsonFileHandler.CURRENT_VERSION;
		List<ScriptGeneratorAction> actions = new ArrayList<ScriptGeneratorAction>();
		try {
			
			// create script definition
			String filepath = filePathPrefix + scriptDefName + ".py";
			File file = new File(filepath);
			file.createNewFile();
		

			ScriptGeneratorJsonFileHandler fileHandlerSpy = Mockito.spy(fileHandler);
			Mockito.doReturn(ScriptGeneratorJsonFileHandler.CURRENT_VERSION).when(fileHandlerSpy).getCurrentGenieVersionNumber();
			// use random version number 
			fileHandlerSpy.CURRENT_VERSION = randomVersion;
			fileHandlerSpy.saveParameters(actions, filepath, filePathPrefix + JsonFileName);
			// change it back to original
			fileHandlerSpy.CURRENT_VERSION = originalVersion;
			fileHandlerSpy.getParameterValues(filePathPrefix + JsonFileName, filepath);
		} catch (InterruptedException | ExecutionException | IOException e) {
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			assertEquals(e.getMessage(), String.format("Data file version %s is not supported.\nSupported version %s",randomVersion, originalVersion));
		} catch (ScriptDefinitionNotMatched e) {
			fail("Something went wrong when creating script defintion");
		}
	}
	
}
