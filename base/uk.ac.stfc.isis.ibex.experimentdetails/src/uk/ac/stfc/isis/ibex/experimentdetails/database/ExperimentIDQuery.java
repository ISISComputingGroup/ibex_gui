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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import uk.ac.stfc.isis.ibex.databases.Rdb;
import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlStatement;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereClause;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereDateBetweenClause;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereEqualClause;
import uk.ac.stfc.isis.ibex.experimentdetails.sql.SqlWhereLikeClause;

/**
 * Class ExperimentIDQuery which builds and executes SQL queries for the
 * experiment database.
 */
public class ExperimentIDQuery {
	private static ExpDataField experimentteamsExpID = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID);
	private static ExpDataField userName = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.NAME);
	private static ExpDataField roleName = ExpDataFieldsCreator.getField(ExpDataTablesEnum.ROLE_TABLE, ExpDataFieldsEnum.NAME);
	private static ExpDataField userOrganisation = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.ORGANISATION);
	private static ExpDataField experimentStartDate = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.STARTDATE);	
	
    private static final ExpDataField[] SQL_SELECT_FIELDS = {
    	experimentteamsExpID, userName, experimentStartDate,
    	userOrganisation, roleName
    };

    private final Rdb rdb;

    /**
     * Instantiates a new experiment ID query.
     *
     * @param rdb
     *            the rdb to use
     * @throws Exception
     *             the exception
     */
    public ExperimentIDQuery(Rdb rdb) throws Exception {
    	this.rdb = rdb;
    }

    private String addWildcards(String searchTerm) {
    	return "'%" + searchTerm + "%'";
    }
    
    private SqlStatement buildSQL(String searchName, Role searchRole, GregorianCalendar date) {
        SqlStatement sqlStatement = new SqlStatement();
    	
		List<SqlWhereClause> whereClauses = new ArrayList<>();
		List<ExpDataTablesEnum> fromTables = new ArrayList<>();
		List<ExpDataField> groupBy = new ArrayList<>();
		
		ExpDataField userUserID = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.USER_ID);
		
		ExpDataField experimentteamsUserID = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.USER_ID);
		ExpDataField experimentteamsRoleID = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.ROLE_ID);
		
		ExpDataField roleRoleID = ExpDataFieldsCreator.getField(ExpDataTablesEnum.ROLE_TABLE, ExpDataFieldsEnum.ROLE_ID);
		
		ExpDataField experimentExpID = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.EXPERIMENT_ID);
		
        ExpDataField experimentStartDate =
                ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.STARTDATE);
        ExpDataField experimentteamsStartDate =
                ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TEAMS_TABLE, ExpDataFieldsEnum.STARTDATE);

		//Connect tables
		whereClauses.add(new SqlWhereEqualClause(roleRoleID, experimentteamsRoleID));
		whereClauses.add(new SqlWhereEqualClause(userUserID, experimentteamsUserID));
		whereClauses.add(new SqlWhereEqualClause(experimentExpID, experimentteamsExpID));
        whereClauses.add(new SqlWhereEqualClause(experimentStartDate, experimentteamsStartDate));
		
		whereClauses.add(new SqlWhereLikeClause(userName, addWildcards(searchName)));
		
		//Use all the tables
		fromTables.addAll(Arrays.asList(ExpDataTablesEnum.values()));
		
		groupBy.add(experimentteamsExpID);
        groupBy.add(experimentStartDate);
		groupBy.add(userName);
		
		if (searchRole != Role.BLANK) {
			whereClauses.add(new SqlWhereEqualClause(roleName, searchRole));
		}
		
		if (date != null) {
			whereClauses.add(new SqlWhereDateBetweenClause(date));
		}
		
		sqlStatement.setSelectFields(SQL_SELECT_FIELDS);
		sqlStatement.setFromTables(fromTables);
		sqlStatement.setWhereClause(whereClauses);
		sqlStatement.setGroupBy(groupBy);
		sqlStatement.setOrderBy(experimentteamsExpID);
		
		return sqlStatement;
    }
    
    /**
     * Performs a database search, returning all experiment IDs in the DB that
     * match the request parameters.
     *
     * @param searchName
     *            The user name to search by.
     * @param searchRole
     *            The user role to search by. If blank will search all roles.
     * @param date
     *            The date to search by. If null will search all dates.
     * @return A list of the search results
     * @throws Exception
     *             the exception
     */
    public List<UserDetails> getExperiments(String searchName, Role searchRole, GregorianCalendar date) throws Exception {
		String selectStatement = buildSQL(searchName, searchRole, date).getSelectStatement();
	
		final PreparedStatement statement = rdb.getConnection()
			.prepareStatement(selectStatement);
	
		ArrayList<UserDetails> userDetails = new ArrayList<UserDetails>();
	
		try {
		    ResultSet result = statement.executeQuery();
		    while (result.next()) {
		    	String name = result.getString(userName.toString());
		    	String institute = result.getString(userOrganisation.toString());
		    	String id = result.getString(experimentteamsExpID.toString());
		    	String role = result.getString(roleName.toString());
		    	
		    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	String startDateString = result.getString(experimentStartDate.toString());
		    	Date startDate = sdf.parse(startDateString);
                userDetails.add(new UserDetails(name, institute, Role.getByString(role), id, startDate));
		    }
		} finally {
		    statement.close();
		}
	
		return userDetails;
    }
}
