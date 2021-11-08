package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScript;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptNameFormatException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;

public class DynamicScriptTest {
	
	private DynamicScript script;
	private Integer id;
	
	@Before
	public void setUp() {
		id = 1;
		script = new DynamicScript(1);
	}
	
	@Test
	public void test_id_is_correct() {
		assertThat(script.getId(), is(id));
	}
	
	@Test
	public void test_name_is_correct() {
		assertThat(script.getName(), is("Script Generator: " + id));
	}
	
	@Test
	public void test_WHEN_no_code_set_THEN_empty() {
		assertThat(script.getCode(), is(Optional.empty()));
	}
	
	@Test
	public void test_WHEN_code_set_THEN_runscript_applied() {
		// Arrange
		String code = "test";
		script.setCode(code);
		// Assert
		String runscript = "\nrunscript()";
		assertThat(script.getCode(), is(Optional.of(code + runscript)));
	}
	
	@Test
	public void test_WHEN_get_id_from_correct_layout_1_THEN_id_correct() {
		// Arrange
		Integer testId = 1;
		String scriptName = "Script Generator: " + testId;
		// Act
		Integer idFromName;
		try {
			idFromName = DynamicScript.getIdFromName(scriptName);
		} catch (DynamicScriptNameFormatException e) {
			throw new AssertionError("Should be able to get id from name");
		}
		// Assert
		assertThat(idFromName, is(testId));
	}
	
	@Test
	public void test_WHEN_get_id_from_correct_layout_32_THEN_id_correct() {
		// Arrange
		Integer testId = 32;
		String scriptName = "Script Generator: " + testId;
		// Act
		Integer idFromName;
		try {
			idFromName = DynamicScript.getIdFromName(scriptName);
		} catch (DynamicScriptNameFormatException e) {
			throw new AssertionError("Should be able to get id from name");
		}
		// Assert
		assertThat(idFromName, is(testId));
	}
	
	@Test
	public void test_WHEN_get_id_from_correct_layout_with_whitespace_THEN_id_correct() {
		// Arrange
		Integer testId = 32;
		String scriptName = "Script Generator:  " + testId + "   \n";
		// Act
		Integer idFromName;
		try {
			idFromName = DynamicScript.getIdFromName(scriptName);
		} catch (DynamicScriptNameFormatException e) {
			throw new AssertionError("Should be able to get id from name");
		}
		// Assert
		assertThat(idFromName, is(testId));
	}
	
	@Test
	public void test_WHEN_get_id_from_layout_with_non_int_THEN_exception() {
		// Arrange
		Double testId = 3.2;
		String scriptName = "Script Generator: " + testId;
		// Act and Assert
		assertThrows(DynamicScriptNameFormatException.class, () -> {
			DynamicScript.getIdFromName(scriptName);
		});
	}
	
	@Test
	public void test_WHEN_get_id_from_layout_with_incorrect_spaces_THEN_exception() {
		// Arrange
		Integer testId = 40;
		String scriptName = "Script Generator: " + testId + " test";
		// Act and Assert
		assertThrows(DynamicScriptNameFormatException.class, () -> {
			DynamicScript.getIdFromName(scriptName);
		});
	}
	
	@Test
	public void test_WHEN_get_id_from_layout_with_incorrect_prefix_THEN_exception() {
		// Arrange
		Integer testId = 40;
		String scriptName = "Script rtor: " + testId + " test";
		// Act and Assert
		assertThrows(DynamicScriptNameFormatException.class, () -> {
			DynamicScript.getIdFromName(scriptName);
		});
	}

}
