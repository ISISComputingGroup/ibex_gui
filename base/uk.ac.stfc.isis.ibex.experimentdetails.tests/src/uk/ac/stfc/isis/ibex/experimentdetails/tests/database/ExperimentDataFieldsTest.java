package uk.ac.stfc.isis.ibex.experimentdetails.tests.database;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataField;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataFieldsCreator;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataFieldsTags;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataTablesTags;

public class ExperimentDataFieldsTest {
	@Test
	public void user_table_contains_name() {
		// Act
		ExperimentDataField result = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_USER_TABLE, ExperimentDataFieldsTags.TAG_NAME);
				
		//Assert
		assertEquals(result.toString(), "user.name");
	}
	
	@Test
	public void role_table_contains_priority() {
		//Act
		ExperimentDataField  result = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_ROLE_TABLE, ExperimentDataFieldsTags.TAG_PRIORITY);
		
		//Assert
		assertEquals(result.toString(), "role.priority");		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void experiment_table_does_not_contain_name() {
		//Act
		ExperimentDataField  result = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TABLE, ExperimentDataFieldsTags.TAG_NAME);	
	}
}
