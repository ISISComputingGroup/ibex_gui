package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;


public class EditableIocTest {

	@Test
	public void getting_available_macros_when_none_returns_empty_list() {
		// Arrange
		EditableIoc ioc = new EditableIoc("testioc");
		
		// Act
		Collection<Macro> avail = ioc.getAvailableMacros();
		
		// Assert
		assertEquals(avail.size(), 0);
		
	}
}
