
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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

package uk.ac.stfc.isis.ibex.journal.tests;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.journal.JournalParameters;
import uk.ac.stfc.isis.ibex.journal.JournalSqlStatement;

/**
 * Test the Journal SQL Statement.
 */
@SuppressWarnings("checkstyle:methodname")
public class JournalSqlStatementTest {
    
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement st;
    private JournalParameters parameters;
    private static final int PAGE_NUMBER = 1;
    private static final int PAGE_SIZE = 25;

    
    @Before
    public void setup() {
        parameters = new JournalParameters();
    }
    
    @Test
    public void test_GIVEN_no_parameters_WHEN_create_where_template_THEN_nothing_returned() {
        JournalSqlStatement statement = new JournalSqlStatement(parameters, PAGE_NUMBER, PAGE_SIZE, connection);
        
        String t = statement.createWhereTemplate();
        
        assertEquals(t, "");
    }
    
    @Test
    public void test_GIVEN_string_parameter_WHEN_create_where_template_THEN_correct_statement_returned() {
        parameters.setField(JournalField.TITLE);
        parameters.setSearchString(Optional.of("Tuturu!"));
        JournalSqlStatement statement = new JournalSqlStatement(parameters, PAGE_NUMBER, PAGE_SIZE, connection);
        
        String t = statement.createWhereTemplate();
        
        assertEquals(t, " WHERE title LIKE ?");
    }
    
    @Test
    public void test_GIVEN_number_parameters_WHEN_create_where_template_THEN_correct_statement_returned() {
        parameters.setField(JournalField.RUN_NUMBER);
        parameters.setNumbers(Optional.of(0), Optional.of(1));
        JournalSqlStatement statement = new JournalSqlStatement(parameters, PAGE_NUMBER, PAGE_SIZE, connection);
        
        String t = statement.createWhereTemplate();
        
        assertEquals(t, " WHERE run_number >= ? AND run_number <= ?");
    }
    
    @Test(expected = IllegalStateException.class)
    public void test_GIVEN_no_sorts_WHEN_create_sort_limit_template_THEN_exception_thrown() {
        JournalSqlStatement statement = new JournalSqlStatement(parameters, PAGE_NUMBER, PAGE_SIZE, connection);
        
        statement.createSortLimitTemplate();
    }
    
    @Test
    public void test_GIVEN_one_sort_WHEN_create_sort_limit_template_THEN_correct_statement_returned() {
        JournalSqlStatement statement = new JournalSqlStatement(parameters, PAGE_NUMBER, PAGE_SIZE, connection);
        statement.addDescendingSort(JournalField.RUN_NUMBER);
        
        String t = statement.createSortLimitTemplate();
        
        assertEquals(t, " ORDER BY run_number DESC LIMIT ?, ?");
    }
    
    @Test
    public void test_GIVEN_multiple_sorts_WHEN_create_sort_limit_template_THEN_correct_statement_returned() {
        JournalSqlStatement statement = new JournalSqlStatement(parameters, PAGE_NUMBER, PAGE_SIZE, connection);
        statement.addDescendingSort(JournalField.RUN_NUMBER);
        statement.addAscendingSort(JournalField.USERS);
        statement.addDescendingSort(JournalField.RB_NUMBER);
        
        String t = statement.createSortLimitTemplate();
        
        assertEquals(t, " ORDER BY run_number DESC, users ASC, rb_number DESC LIMIT ?, ?");
    }
}
