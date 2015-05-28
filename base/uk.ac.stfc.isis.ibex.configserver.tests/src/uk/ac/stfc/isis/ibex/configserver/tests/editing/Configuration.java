package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

public class Configuration extends EditableConfigurationTest {
	
	@Test
	public void doing_nothing_to_an_empty_configuration_leaves_the_configuration_unchanged() {
		EditableConfiguration edited = edit(emptyConfig());		
		assertAreEqual(emptyConfig(), edited.asConfiguration());
	}
	
	@Test
	public void doing_nothing_to_a_configuration_leaves_the_configuration_unchanged() {
		populateConfig();
		EditableConfiguration edited = edit(config());		
		assertAreEqual(config(), edited.asConfiguration());
	}
}
