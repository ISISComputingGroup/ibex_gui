package uk.ac.stfc.isis.ibex.experimentdetails.tests.database;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataField;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataFieldsCreator;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataFieldsTags;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExperimentDataTablesTags;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlStatement;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereClause;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereEqualClause;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereLikeClause;

public class SqlStatementTest {
	
	@Test
	public void select_statement_singular_field() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExperimentDataField userName = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_USER_TABLE, ExperimentDataFieldsTags.TAG_NAME);
		sql.setSelectFields(new ExperimentDataField[]{userName});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT user.name FROM  WHERE ", result);
	}
	
	@Test
	public void select_statement_multiple_fields() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExperimentDataField userName = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_USER_TABLE, ExperimentDataFieldsTags.TAG_NAME);
		ExperimentDataField roleName = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_ROLE_TABLE, ExperimentDataFieldsTags.TAG_NAME);
		sql.setSelectFields(new ExperimentDataField[]{userName, roleName});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT user.name, role.name FROM  WHERE ", result);
	}
	
	@Test
	public void singular_where_like_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExperimentDataField experimentId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TABLE, ExperimentDataFieldsTags.TAG_EXPERIMENT_ID);
		SqlWhereLikeClause whereClause= new SqlWhereLikeClause(experimentId, "1234");
		sql.setWhereClause(new SqlWhereClause[]{whereClause});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE experiment.experimentID LIKE 1234", result);
	}
	
	@Test
	public void multiple_where_like_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		
		ExperimentDataField experimentId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TABLE, ExperimentDataFieldsTags.TAG_EXPERIMENT_ID);
		SqlWhereLikeClause whereClause= new SqlWhereLikeClause(experimentId, "1234");
		
		ExperimentDataField userName = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_USER_TABLE, ExperimentDataFieldsTags.TAG_NAME);
		SqlWhereLikeClause whereClause2= new SqlWhereLikeClause(userName, "Dave");
		
		sql.setWhereClause(new SqlWhereClause[]{whereClause, whereClause2});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE experiment.experimentID LIKE 1234 AND user.name LIKE Dave", result);
	}
	
	@Test
	public void singular_where_equals_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExperimentDataField experimentId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TABLE, ExperimentDataFieldsTags.TAG_EXPERIMENT_ID);
		ExperimentDataField experimentTeamsExperimentId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TEAMS_TABLE, ExperimentDataFieldsTags.TAG_EXPERIMENT_ID);
		SqlWhereEqualClause whereClause= new SqlWhereEqualClause(experimentId, experimentTeamsExperimentId);
		sql.setWhereClause(new SqlWhereClause[]{whereClause});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE experiment.experimentID = experimentteams.experimentID", result);
	}
	
	@Test
	public void multiple_where_equals_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExperimentDataField experimentId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TABLE, ExperimentDataFieldsTags.TAG_EXPERIMENT_ID);
		ExperimentDataField experimentTeamsExperimentId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TEAMS_TABLE, ExperimentDataFieldsTags.TAG_EXPERIMENT_ID);
		SqlWhereEqualClause whereClause= new SqlWhereEqualClause(experimentId, experimentTeamsExperimentId);
		
		ExperimentDataField userId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_USER_TABLE, ExperimentDataFieldsTags.TAG_USER_ID);
		ExperimentDataField experimentTeamsUserId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TEAMS_TABLE, ExperimentDataFieldsTags.TAG_USER_ID);
		SqlWhereEqualClause whereClause2= new SqlWhereEqualClause(userId, experimentTeamsUserId);
		
		sql.setWhereClause(new SqlWhereClause[]{whereClause, whereClause2});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE experiment.experimentID = experimentteams.experimentID AND user.userID = experimentteams.userID", result);
	}
	
	@Test
	public void multiple_mixed_where_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		
		ExperimentDataField userId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_USER_TABLE, ExperimentDataFieldsTags.TAG_USER_ID);
		ExperimentDataField experimentTeamsUserId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TEAMS_TABLE, ExperimentDataFieldsTags.TAG_USER_ID);
		SqlWhereEqualClause whereClause= new SqlWhereEqualClause(userId, experimentTeamsUserId);
		
		ExperimentDataField experimentId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TABLE, ExperimentDataFieldsTags.TAG_EXPERIMENT_ID);
		SqlWhereLikeClause whereClause2= new SqlWhereLikeClause(experimentId, "1234");
		
		sql.setWhereClause(new SqlWhereClause[]{whereClause, whereClause2});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE user.userID = experimentteams.userID AND experiment.experimentID LIKE 1234", result);
	}
	
	@Test
	public void singular_from_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		sql.setFromTables(new ExperimentDataTablesTags[]{ExperimentDataTablesTags.TAG_EXPERIMENT_TABLE});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM exp_data.experiment WHERE ", result);
	}
	
	@Test
	public void multiple_from_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		sql.setFromTables(new ExperimentDataTablesTags[]{ExperimentDataTablesTags.TAG_EXPERIMENT_TABLE, ExperimentDataTablesTags.TAG_ROLE_TABLE});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM exp_data.experiment, exp_data.role WHERE ", result);
	}
	
	@Test
	public void group_by_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExperimentDataField userId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_USER_TABLE, ExperimentDataFieldsTags.TAG_USER_ID);
		sql.setGroupBy(userId);
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE  GROUP BY user.userID", result);
	}
	
	@Test
	public void full_sql_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		
		ExperimentDataField userId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_USER_TABLE, ExperimentDataFieldsTags.TAG_USER_ID);
		ExperimentDataField roleName = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_ROLE_TABLE, ExperimentDataFieldsTags.TAG_NAME);
		sql.setSelectFields(new ExperimentDataField[]{userId, roleName});
		
		sql.setFromTables(new ExperimentDataTablesTags[]{ExperimentDataTablesTags.TAG_EXPERIMENT_TABLE, ExperimentDataTablesTags.TAG_ROLE_TABLE});
		
		ExperimentDataField experimentTeamsUserId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TEAMS_TABLE, ExperimentDataFieldsTags.TAG_USER_ID);
		SqlWhereEqualClause whereClause = new SqlWhereEqualClause(userId, experimentTeamsUserId);
		
		ExperimentDataField experimentId = ExperimentDataFieldsCreator.getField(ExperimentDataTablesTags.TAG_EXPERIMENT_TABLE, ExperimentDataFieldsTags.TAG_EXPERIMENT_ID);
		SqlWhereLikeClause whereClause2 = new SqlWhereLikeClause(experimentId, "1234");
		
		sql.setWhereClause(new SqlWhereClause[]{whereClause, whereClause2});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT user.userID, role.name FROM exp_data.experiment, exp_data.role WHERE user.userID = experimentteams.userID AND experiment.experimentID LIKE 1234", result);
	}
	
	
}
