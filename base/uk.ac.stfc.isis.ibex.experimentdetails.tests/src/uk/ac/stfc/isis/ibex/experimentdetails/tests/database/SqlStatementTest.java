
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.experimentdetails.tests.database;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataField;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataFieldsCreator;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataFieldsEnum;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataTablesEnum;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlStatement;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereClause;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereEqualClause;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereLikeClause;

public class SqlStatementTest {
	
	@Test
	public void select_statement_singular_field() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExpDataField userName = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.NAME);
		sql.setSelectFields(new ExpDataField[]{userName});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
        assertEquals("SELECT user.name AS \"user.name\" FROM  WHERE ", result);
	}
	
	@Test
	public void select_statement_multiple_fields() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExpDataField userName = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.NAME);
		ExpDataField roleName = ExpDataFieldsCreator.getField(ExpDataTablesEnum.ROLE_TABLE, ExpDataFieldsEnum.NAME);
		sql.setSelectFields(new ExpDataField[]{userName, roleName});
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
        assertEquals("SELECT user.name AS \"user.name\", role.name AS \"role.name\" FROM  WHERE ", result);
	}
	
	@Test
	public void singular_where_like_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExpDataField experimentId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID);
		SqlWhereLikeClause whereClause= new SqlWhereLikeClause(experimentId, "1234");
		sql.setWhereClause(new ArrayList<SqlWhereClause>(Arrays.asList(whereClause)));
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE experiment.experimentID LIKE 1234", result);
	}
	
	@Test
	public void multiple_where_like_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		
		ExpDataField experimentId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID);
		SqlWhereLikeClause whereClause= new SqlWhereLikeClause(experimentId, "1234");
		
		ExpDataField userName = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.NAME);
		SqlWhereLikeClause whereClause2= new SqlWhereLikeClause(userName, "Dave");
		
		sql.setWhereClause(new ArrayList<SqlWhereClause>(Arrays.asList(whereClause, whereClause2)));
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE experiment.experimentID LIKE 1234 AND user.name LIKE Dave", result);
	}
	
	@Test
	public void singular_where_equals_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExpDataField experimentId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID);
		ExpDataField experimentTeamsExperimentId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID);
		SqlWhereEqualClause whereClause= new SqlWhereEqualClause(experimentId, experimentTeamsExperimentId);
		sql.setWhereClause(new ArrayList<SqlWhereClause>(Arrays.asList(whereClause)));
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE experiment.experimentID = experimentteams.experimentID", result);
	}
	
	@Test
	public void multiple_where_equals_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExpDataField experimentId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID);
		ExpDataField experimentTeamsExperimentId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID);
		SqlWhereEqualClause whereClause= new SqlWhereEqualClause(experimentId, experimentTeamsExperimentId);
		
		ExpDataField userId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.USER_ID);
		ExpDataField experimentTeamsUserId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.USER_ID);
		SqlWhereEqualClause whereClause2= new SqlWhereEqualClause(userId, experimentTeamsUserId);
		
		sql.setWhereClause(new ArrayList<SqlWhereClause>(Arrays.asList(whereClause, whereClause2)));
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE experiment.experimentID = experimentteams.experimentID AND user.userID = experimentteams.userID", result);
	}
	
	@Test
	public void multiple_mixed_where_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		
		ExpDataField userId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.USER_ID);
		ExpDataField experimentTeamsUserId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.USER_ID);
		SqlWhereEqualClause whereClause= new SqlWhereEqualClause(userId, experimentTeamsUserId);
		
		ExpDataField experimentId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID);
		SqlWhereLikeClause whereClause2= new SqlWhereLikeClause(experimentId, "1234");
		
		sql.setWhereClause(new ArrayList<SqlWhereClause>(Arrays.asList(whereClause, whereClause2)));
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE user.userID = experimentteams.userID AND experiment.experimentID LIKE 1234", result);
	}
	
	@Test
	public void singular_from_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		sql.setFromTables(new ArrayList<ExpDataTablesEnum>(Arrays.asList(ExpDataTablesEnum.EXPERIMENT_TABLE)));
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM exp_data.experiment WHERE ", result);
	}
	
	@Test
	public void multiple_from_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		sql.setFromTables(new ArrayList<ExpDataTablesEnum>(Arrays.asList(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataTablesEnum.ROLE_TABLE)));
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM exp_data.experiment, exp_data.role WHERE ", result);
	}
	
	@Test
	public void group_by_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		ExpDataField userId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.USER_ID);
		sql.setGroupBy(new ArrayList<ExpDataField>(Arrays.asList(userId)));
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
		assertEquals("SELECT  FROM  WHERE  GROUP BY user.userID", result);
	}
	
	@Test
	public void full_sql_statement() {
		//Arrange
		SqlStatement sql = new SqlStatement();
		
		ExpDataField userId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.USER_ID);
		ExpDataField roleName = ExpDataFieldsCreator.getField(ExpDataTablesEnum.ROLE_TABLE, ExpDataFieldsEnum.NAME);
		sql.setSelectFields(new ExpDataField[]{userId, roleName});
		
		sql.setFromTables(new ArrayList<ExpDataTablesEnum>(Arrays.asList(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataTablesEnum.ROLE_TABLE)));
		
		ExpDataField experimentTeamsUserId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.USER_ID);
		SqlWhereEqualClause whereClause = new SqlWhereEqualClause(userId, experimentTeamsUserId);
		
		ExpDataField experimentId = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID);
		SqlWhereLikeClause whereClause2 = new SqlWhereLikeClause(experimentId, "1234");
		
		sql.setWhereClause(new ArrayList<SqlWhereClause>(Arrays.asList(whereClause, whereClause2)));
		
		//Act
		String result = sql.getSelectStatement();
		
		//Assert
        assertEquals(
                "SELECT user.userID AS \"user.userID\", role.name AS \"role.name\" FROM exp_data.experiment, exp_data.role WHERE user.userID = experimentteams.userID AND experiment.experimentID LIKE 1234",
                result);
	}
	
	
}
