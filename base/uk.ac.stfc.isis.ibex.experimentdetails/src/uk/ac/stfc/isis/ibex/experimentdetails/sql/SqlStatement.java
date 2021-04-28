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

package uk.ac.stfc.isis.ibex.experimentdetails.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.experimentdetails.ExperimentDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataField;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataTablesEnum;
import uk.ac.stfc.isis.ibex.experimentdetails.preferences.PreferenceConstants;

/**
 * Prepares the SQL 'insert into' statement for adding a message to the
 * database.
 * 
 */
public class SqlStatement {
    private String schemaName;
	private List<ExpDataField> selectFields;
    private List<ExpDataTablesEnum> fromTables;
    private List<SqlWhereClause> whereFields;
    
    private List<ExpDataField> groupBy;
    private ExpDataField orderBy;

    /**
     * Create a new SQL statement.
     */
    public SqlStatement() {
    	try {
    		IPreferenceStore preferenceStore = ExperimentDetails.getInstance()
    				.getPreferenceStore();
    			schemaName = preferenceStore
    				.getString(PreferenceConstants.P_EXP_DATA_SQL_SCHEMA);    		
    	} catch (Exception e) {
    		schemaName = PreferenceConstants.DEFAULT_EXP_DATA_SQL_SCHEMA;
    	}

	
		this.selectFields = new ArrayList<ExpDataField>();
		this.whereFields = new ArrayList<SqlWhereClause>();
		this.fromTables = new ArrayList<ExpDataTablesEnum>();
    }

    /**
     * @param selectFields The (ordered) list of fields that will be retrieved by the select
     * statement.
     */
    public void setSelectFields(ExpDataField[] selectFields) {
		this.selectFields = new ArrayList<ExpDataField>(
			Arrays.asList(selectFields));
    }

    /**
     * @param whereLikeFields The ordered list of fields that will be featured in the WHERE clauses.
     */
    public void setWhereClause(List<SqlWhereClause> whereLikeFields) {
      this.whereFields = whereLikeFields;
    }
    
    /**
     * @param fromTables The ordered list of tables that will be queried.
     */
    public void setFromTables(List<ExpDataTablesEnum> fromTables) {   	
      this.fromTables = fromTables;
    }
    
    /**
     * @param groupBy The fields to group the returned results by.
     */
    public void setGroupBy(List<ExpDataField> groupBy) {
      this.groupBy = groupBy;
    }
    
    /**
     * @param orderBy The field to Order By
     */
    public void setOrderBy(ExpDataField orderBy) {
      this.orderBy = orderBy;
    }

    /**
     * @return A string representation of the SQL select statement with SELECT
     * fields, WHERE clauses.
     */
    public String getSelectStatement() {
    	String sqlStatement = "SELECT ";
    	
    	sqlStatement += selectList() + " FROM " + selectTableList()
			+ " WHERE " + whereList();
    	
//    	if (groupBy != null) {
//			sqlStatement += " GROUP BY " + groupByList();
//    	}
    	
    	if (orderBy != null) {
    		sqlStatement += " ORDER BY " + orderBy;
    	}
    	
    	System.out.println(sqlStatement);
    	
		return sqlStatement;
    }

    private <T> String joinString(List<T> list, String seperator) {
    	return joinString("", list, seperator);
    }
    
    private <T> String joinString(String prefix, List<T> list, String seperator) {
    	StringBuilder result = new StringBuilder();
    	int listSize = list.size();
    	
    	for (int i = 0; i < listSize; i++) {
    		result.append(prefix);
    		result.append(list.get(i));
			if (i != listSize - 1) {
				result.append(seperator);
			}
		}
    	
    	return result.toString();
    }
    
    /**
     * @return A string representation of the SELECT list.
     */
    private String selectList() {
    	//add aliases
    	ArrayList<String> aliasedSelects = new ArrayList<>();
    	for (ExpDataField field : this.selectFields) {
    		aliasedSelects.add(field.toString() + " AS \"" + field.toString() + "\"");
    	}
		return joinString(aliasedSelects, ", ");
    }

    /**
     *  @return A string representation of the Tables list.
     */
    private String selectTableList() {

		return joinString(schemaName + ".", this.fromTables, ", ");
    }

    /**
     * Get string representation of the list of WHERE clauses.
     */
    private String whereList() {
		
		return joinString(this.whereFields, " AND ");
    }
    
    private String groupByList() {
    	return joinString(this.groupBy, ", ");
    }
}
