package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import java.util.Arrays;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

public class Components extends EditableConfigurationTest {

	@Test
	public void a_configuration_can_have_components() {
		components.add(MOTOR);
		EditableConfiguration edited = edit(config());
		assertContains(edited.asConfiguration().getComponents(), MOTOR);
	}
	
	@Test
	public void a_component_has_no_components_of_its_own() {
		components.add(MOTOR);
		EditableConfiguration edited = edit(config());
		assertEmpty(edited.asComponent().getComponents());
	}
	
	@Test
	public void an_empty_configuration_has_no_components_selected() {
		EditableConfiguration edited = edit(emptyConfig());
		assertEmpty(edited.getEditableComponents().getSelected());
	}
	
	@Test
	public void config_components_are_initially_selected() {
		components.add(MOTOR);
		EditableConfiguration edited = edit(config());
		assertContains(edited.getEditableComponents().getSelected(), MOTOR);
	}
	
	@Test
	public void a_component_can_be_added_or_removed() {
		EditableConfiguration edited = edit(emptyConfig());
		assertDoesNotContain(edited.getEditableComponents().getSelected(), MOTOR);
		
		edited.getEditableComponents().toggleSelection(Arrays.asList(MOTOR));
		assertContains(edited.getEditableComponents().getSelected(), MOTOR);
		
		edited.getEditableComponents().toggleSelection(Arrays.asList(MOTOR));
		assertDoesNotContain(edited.getEditableComponents().getSelected(), MOTOR);
	}
}
