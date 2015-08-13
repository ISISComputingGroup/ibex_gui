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

package uk.ac.stfc.isis.ibex.log.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Calendar;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.log.message.sql.LogMessageFieldsSql;
import uk.ac.stfc.isis.ibex.log.message.sql.LogMessageFieldsWhereSql;
import uk.ac.stfc.isis.ibex.log.rdb.SqlStatement;

/**
 * This class is responsible for testing the Sql Statement Class
 * 
 */
public class SqlStatementTest {

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.log.rdb.SqlStatement#setSelectFields(uk.ac.stfc.isis.ibex.log.message.sql.LogMessageFieldsSql[])}
     * .
     */
    @Test
    public final void testSetSelectFields() {
	// Mock
	SqlStatement mockedSqlStatement = mock(SqlStatement.class);
	// Arrange
	LogMessageFieldsSql[] selectFields = { LogMessageFieldsSql.SEVERITY };
	// Act
	mockedSqlStatement.setSelectFields(selectFields);
	// Assert
	verify(mockedSqlStatement).setSelectFields(selectFields);
    }

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.log.rdb.SqlStatement#setWhereLikeClause(uk.ac.stfc.isis.ibex.log.message.sql.LogMessageFieldsWhereSql[])}
     * .
     */
    @Test
    public final void testSetWhereLikeClause() {
	// Mock
	SqlStatement mockedSqlStatement = mock(SqlStatement.class);
	// Arrange
	LogMessageFieldsWhereSql[] whereLikeFields = { LogMessageFieldsWhereSql.CONTENTS };
	// Act
	mockedSqlStatement.setWhereLikeClause(whereLikeFields);
	// Assert
	verify(mockedSqlStatement).setWhereLikeClause(whereLikeFields);
    }

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.log.rdb.SqlStatement#setTimeRange(java.util.Calendar, java.util.Calendar)}
     * .
     */
    @Test
    public final void testSetTimeRange() {
	// Mock
	SqlStatement mockedSqlStatement = mock(SqlStatement.class);
	// Arrange
	Calendar startTime = null;
	Calendar endTime = null;
	// Act
	mockedSqlStatement.setTimeRange(startTime, endTime);
	// Assert
	verify(mockedSqlStatement).setTimeRange(startTime, endTime);
    }
}
