package uk.ac.stfc.isis.ibex.experimentdetails.tests.database;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataField;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataFieldsCreator;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataFieldsEnum;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataTablesEnum;

public class ExperimentDataFieldsTest {
	@Test
	public void user_table_contains_name() {
		// Act
		ExpDataField result = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.NAME);
				
		//Assert
		assertEquals(result.toString(), "user.name");
	}
	
	@Test
	public void role_table_contains_priority() {
		//Act
		ExpDataField  result = ExpDataFieldsCreator.getField(ExpDataTablesEnum.ROLE_TABLE, ExpDataFieldsEnum.PRIORITY);
		
		//Assert
		assertEquals(result.toString(), "role.priority");		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void experiment_table_does_not_contain_name() {
		//Act
		ExpDataField  result = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.NAME);	
	}
}
