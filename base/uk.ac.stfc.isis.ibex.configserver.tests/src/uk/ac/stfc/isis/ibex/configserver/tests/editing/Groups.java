package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableGroup;

public class Groups extends EditableConfigurationTest {
		
	@Test
	public void a_new_group_can_be_added() {
		EditableConfiguration edited = edit(emptyConfig());
		assertEmpty(edited.asConfiguration().getGroups());
		
		edited.addNewGroup();
		assertNotEmpty(edited.asConfiguration().getGroups());
	}
	
	@Test
	public void a_group_can_be_removed() {
		groups.add(JAWS);
		EditableConfiguration edited = edit(config());

		assertContains(edited.asConfiguration().getGroups(), JAWS);
		
		EditableGroup jaws = getFirst(edited.getEditableGroups());
		edited.removeGroup(jaws);		
		assertEmpty(edited.asConfiguration().getGroups());
	}
	
	@Test
	public void two_groups_can_be_swapped() {
		groups.add(JAWS);
		groups.add(TEMPERATURE);
		EditableConfiguration edited = edit(config());
		
		List<EditableGroup> grps = (List<EditableGroup>) edited.getEditableGroups();

		edited.swapGroups(grps.get(0), grps.get(1));
		
		grps = (List<EditableGroup>) edited.getEditableGroups();
			
		assertEquals(grps.get(0).getName(), "TEMPERATURE");
		assertEquals(grps.get(1).getName(), "JAWS");
		
	}
}
