
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

import java.util.Calendar;
import java.util.Optional;
import java.util.OptionalInt;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.journal.EmptySearch;
import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.journal.JournalSearch;
import uk.ac.stfc.isis.ibex.journal.SearchNumber;
import uk.ac.stfc.isis.ibex.journal.SearchString;
import uk.ac.stfc.isis.ibex.journal.SearchTime;

/**
 * Test the Journal SQL Statement.
 */
@SuppressWarnings("checkstyle:methodname")
public class JournalSearchTest {
    
    private JournalSearch search;
    private static final OptionalInt TEST_NUM = OptionalInt.of(0);
    private static final Optional<Calendar> TEST_TIME = Optional.of(Calendar.getInstance());
    
    
    @Test
    public void test_GIVEN_string_search_WHEN_create_where_template_THEN_correct_statement_returned() {
        search = new SearchString(JournalField.TITLE, "Tuturu!");
        
        String whereTemplate = search.createWhereTemplate();
        
        assertEquals(" WHERE title LIKE ?", whereTemplate);
    }
    
    @Test
    public void test_GIVEN_number_search_with_no_numbers_WHEN_create_where_template_THEN_correct_statement_returned() {
        search = new SearchNumber(JournalField.RUN_NUMBER, OptionalInt.empty(), OptionalInt.empty());
        
        String whereTemplate = search.createWhereTemplate();
        
        assertEquals("", whereTemplate);
    }
    
    @Test
    public void test_GIVEN_number_search_with_one_number_WHEN_create_where_template_THEN_correct_statement_returned() {
        search = new SearchNumber(JournalField.RUN_NUMBER, TEST_NUM, OptionalInt.empty());
        
        String whereTemplate = search.createWhereTemplate();
        
        assertEquals(" WHERE run_number >= ?", whereTemplate);
    }
    
    @Test
    public void test_GIVEN_number_search_with_two_numbers_WHEN_create_where_template_THEN_correct_statement_returned() {
        search = new SearchNumber(JournalField.RUN_NUMBER, TEST_NUM, TEST_NUM);
        
        String whereTemplate = search.createWhereTemplate();
        
        assertEquals(" WHERE run_number >= ? AND run_number <= ?", whereTemplate);
    }
    
    @Test
    public void test_GIVEN_time_search_with_no_numbers_WHEN_create_where_template_THEN_correct_statement_returned() {
        search = new SearchTime(JournalField.START_TIME, Optional.empty(), Optional.empty());
        
        String whereTemplate = search.createWhereTemplate();
        
        assertEquals("", whereTemplate);
    }
    
    @Test
    public void test_GIVEN_time_search_with_one_number_WHEN_create_where_template_THEN_correct_statement_returned() {
        search = new SearchTime(JournalField.START_TIME, TEST_TIME, Optional.empty());
        
        String whereTemplate = search.createWhereTemplate();
        
        assertEquals(" WHERE start_time >= ?", whereTemplate);
    }
    
    @Test
    public void test_GIVEN_time_search_with_two_numbers_WHEN_create_where_template_THEN_correct_statement_returned() {
        search = new SearchTime(JournalField.START_TIME, TEST_TIME, TEST_TIME);
        
        String whereTemplate = search.createWhereTemplate();
        
        assertEquals(" WHERE start_time >= ? AND start_time <= ?", whereTemplate);
    }
    
    @Test(expected = IllegalStateException.class)
    public void test_GIVEN_no_sorts_WHEN_create_sort_limit_template_THEN_exception_thrown() {
        search = new EmptySearch();
        search.clearSorts();
        
        search.createSortLimitTemplate();
    }
    
    @Test
    public void test_GIVEN_one_sort_WHEN_create_sort_limit_template_THEN_correct_statement_returned() {
        search = new EmptySearch();
        
        String sortLimitTemplate = search.createSortLimitTemplate();
        
        assertEquals(sortLimitTemplate, " ORDER BY run_number DESC LIMIT ?, ?");
    }
    
    @Test
    public void test_GIVEN_multiple_sorts_WHEN_create_sort_limit_template_THEN_correct_statement_returned() {
        search = new EmptySearch();
        search.addSort(JournalField.USERS);
        search.addSort(JournalField.RUN_NUMBER);
        
        String sortLimitTemplate = search.createSortLimitTemplate();
        
        assertEquals(sortLimitTemplate, " ORDER BY run_number DESC, users ASC, run_number DESC LIMIT ?, ?");
    }
    
    @Test
    public void test_GIVEN_rb_number_sort_WHEN_create_sort_limit_template_THEN_correct_statement_returned() {
        search = new EmptySearch();
        search.clearSorts();
        search.addSort(JournalField.RB_NUMBER);
        
        String sortLimitTemplate = search.createSortLimitTemplate();
        
        assertEquals(sortLimitTemplate, " ORDER BY cast(rb_number as unsigned) DESC LIMIT ?, ?");
    }
}
