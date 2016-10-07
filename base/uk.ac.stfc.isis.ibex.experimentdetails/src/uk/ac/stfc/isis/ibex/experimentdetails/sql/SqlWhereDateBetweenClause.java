
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

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataField;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataFieldsCreator;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataFieldsEnum;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataTablesEnum;

/**
 * Provide a start and end date for the SQL statement.
 */
public class SqlWhereDateBetweenClause extends SqlWhereClause {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
	private static final ExpDataField EXP_START = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.STARTDATE);
	private static final ExpDataField EXP_DURATION = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.DURATION);
	private static final String BETWEEN_CASE = EXP_START + " AND DATE_ADD(" + EXP_START + ", INTERVAL " + EXP_DURATION + " DAY)";
	
	
	public SqlWhereDateBetweenClause(GregorianCalendar lhs) {
		super("'" + FORMATTER.format(lhs.getTime()) + "'", BETWEEN_CASE);
	}

	@Override
	public String toString() {
		return lhs + " BETWEEN " + rhs;
	}

}
