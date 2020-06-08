package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorFileHandler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ScriptGeneratorFileHandlerTest {
	
	String filepathPrefix;
	String fileExtension = ".py";
	
	String scriptLines;
	String filename;
	
	String script2Lines;
	String file2name;
	
	public ScriptGeneratorFileHandler fileHandler = new ScriptGeneratorFileHandler();
	

	@Before
	public void setUp() {
		filepathPrefix = (System.getProperty("user.dir") + "\\test_script_gen_handler_scripts\\");
		if(!(new File(filepathPrefix).mkdir())) {
			fail("We need to create this directory to write files to it");
		}
		
		scriptLines = "test\ntest2";
		filename = "test";
		
		script2Lines = "test2\ntest3";
		filename = "test2";
	}
	
	
	@After
	public void tearDown() {
		File test_scripts_folder = new File(filepathPrefix);
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
	
	public void assertFileCorrespondsToExpectedContents(String filepath, String expectedContents) {
		String[] expectedContentsLines = expectedContents.split("\n");
		try(BufferedReader reader = new BufferedReader(new FileReader(filepath))){
			int linenum = 0;
			String line;
			while((line = reader.readLine()) != null) {
				if(linenum > expectedContentsLines.length) {
					fail("Should be the same number of script lines as written lines");
				}
				assertThat("Lines should match the generated script",
						line, equalTo(expectedContentsLines[linenum]));
				linenum += 1;
			}
		} catch(IOException e) {
			fail("Should not fail to read from file");
		}
	}
	
	@Test
	public void test_WHEN_I_attempt_to_generate_script_THEN_no_exception_thrown_AND_written() {
		try {
			String filepath = filepathPrefix + filename + fileExtension;
			// WHEN I attempt to overwrite with check
			fileHandler.generate(filepath, script2Lines);
			// THEN file overwritten
			assertFileCorrespondsToExpectedContents(filepath, script2Lines);
		} catch (IOException e) {
			// THEN no exception thrown
			fail("Should manage to write file");
		}
	}	
}
