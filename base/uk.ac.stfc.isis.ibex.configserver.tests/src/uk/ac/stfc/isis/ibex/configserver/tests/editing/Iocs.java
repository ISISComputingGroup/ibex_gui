package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

public class Iocs extends EditableConfigurationTest {
	@Test
	public void an_editable_config_has_all_available_iocs_to_edit() {
		allIocs.add(GALIL01);
		EditableConfiguration edited = edit(config());
		assertContains(edited.getEditableIocs(), new EditableIoc(GALIL01));
	}
	
	@Test
	public void redundant_iocs_are_removed_from_the_config() {
		GALIL01.setAutostart(false);
		GALIL01.setRestart(false);
		iocs.add(GALIL01);
		EditableConfiguration edited = edit(config());
		assertDoesNotContain(edited.asConfiguration().getIocs(), GALIL01);
	}
	
	@Test
	public void an_ioc_is_added_to_the_config_after_it_has_been_edited() {
		GALIL01.setAutostart(false);
		allIocs.add(GALIL01);
		
		EditableConfiguration edited = edit(config());
		assertDoesNotContain(edited.asConfiguration().getIocs(), GALIL01);
		
		EditableIoc galil = getFirst(edited.getEditableIocs());
		galil.setAutostart(true);
		
		assertContains(edited.asConfiguration().getIocs(), GALIL01);
	}
}
