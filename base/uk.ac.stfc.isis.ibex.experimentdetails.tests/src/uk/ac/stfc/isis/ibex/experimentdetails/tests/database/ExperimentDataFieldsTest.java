package uk.ac.stfc.isis.ibex.experimentdetails.tests.database;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataFields;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataTags;

public class ExperimentDataFieldsTest {
	@Test
	public void user_table_contains_name() {
		// Act
		String result = ExperimentDataFields.getAsString(ExperimentDataTags.TAG_USER_TABLE, ExperimentDataTags.TAG_NAME);
				
		//Assert
		assertEquals(result, "user.name");
	}
	
	@Test
	public void role_table_contains_priority() {
		//Act
		String  result = ExperimentDataFields.getAsString(ExperimentDataTags.TAG_ROLE_TABLE, ExperimentDataTags.TAG_PRIORITY);
		
		//Assert
		assertEquals(result, "role.priority");		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void experiment_table_does_not_contain_name() {
		//Act
		String  result = ExperimentDataFields.getAsString(ExperimentDataTags.TAG_EXPERIMENT_TABLE, ExperimentDataTags.TAG_NAME);	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void name_is_not_a_table() {
		//Act
		String  result = ExperimentDataFields.getAsString(ExperimentDataTags.TAG_NAME, ExperimentDataTags.TAG_NAME);	
	}
}
