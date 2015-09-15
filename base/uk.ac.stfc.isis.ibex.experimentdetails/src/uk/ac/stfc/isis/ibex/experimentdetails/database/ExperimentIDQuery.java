/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2015 Science & Technology Facilities Council.
 * All rights reserved.
 *
 * This program is distributed in the hope that it will be useful.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution.
 * EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
 * AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
 * OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
 *
 * You should have received a copy of the Eclipse Public License v1.0
 * along with this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or 
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.experimentdetails.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlStatement;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereClause;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereEqualClause;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereLikeClause;

public class ExperimentIDQuery {
    private static final ExpDataField[] SQL_SELECT_FIELDS = {
    	ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID)
    };

    private final Rdb rdb;

    private final SqlStatement sqlStatement;

    public ExperimentIDQuery(Rdb rdb) throws Exception {
    	this.rdb = rdb;
		this.sqlStatement = new SqlStatement();
    }

    private String addWildcards(String searchTerm) {
    	return "'%" + searchTerm + "%'";
    }
    
    /**
     * Performs a database search, returning all experiment IDs in the DB that
     * match the request parameters.
     * 
     * @param searchName
     *            The user name to search by.
     *            
     * @param searchRole
     * 			  The user role to search by.
     */
    public List<String> getExperimentID(String searchName, Role searchRole) throws Exception {
		List<SqlWhereClause> whereClauses = new ArrayList<>();
		List<ExpDataTablesEnum> fromTables = new ArrayList<>();
		
		ExpDataField userUserID = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.USER_ID);
		
		ExpDataField experimentteamsUserID = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.USER_ID);
		ExpDataField experimentteamsUserName = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.NAME);
		ExpDataField experimentteamsExperimentID = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID);
		ExpDataField experimentteamsRoleID = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.ROLE_ID);
		
		ExpDataField roleRoleID = ExpDataFieldsCreator.getField(ExpDataTablesEnum.ROLE_TABLE, ExpDataFieldsEnum.ROLE_ID);
		ExpDataField roleName = ExpDataFieldsCreator.getField(ExpDataTablesEnum.ROLE_TABLE, ExpDataFieldsEnum.NAME);
		
		whereClauses.add(new SqlWhereEqualClause(userUserID, experimentteamsUserID));
		whereClauses.add(new SqlWhereLikeClause(experimentteamsUserName, addWildcards(searchName)));

		fromTables.add(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE);
		fromTables.add(ExpDataTablesEnum.USER_TABLE);
		
		if (searchRole != Role.BLANK) {
			whereClauses.add(new SqlWhereEqualClause(roleRoleID, experimentteamsRoleID));
			whereClauses.add(new SqlWhereEqualClause(roleName, searchRole));
			
			fromTables.add(ExpDataTablesEnum.ROLE_TABLE);
		}
		
		sqlStatement.setSelectFields(SQL_SELECT_FIELDS);
		sqlStatement.setFromTables(fromTables);
		sqlStatement.setWhereClause(whereClauses);
		sqlStatement.setGroupBy(experimentteamsExperimentID);
		
		String selectStatement = sqlStatement.getSelectStatement();
	
		final PreparedStatement statement = rdb.getConnection()
			.prepareStatement(selectStatement);
	
		ArrayList<String> experimentIDs = new ArrayList<String>();
	
		try {
		    ResultSet result = statement.executeQuery();
	
		    while (result.next()) {
				experimentIDs.add(result.getString(ExpDataFieldsEnum.EXPERIMENT_ID.toString()));
		    }
		} finally {
		    statement.close();
		}
	
		return experimentIDs;
    }
}
