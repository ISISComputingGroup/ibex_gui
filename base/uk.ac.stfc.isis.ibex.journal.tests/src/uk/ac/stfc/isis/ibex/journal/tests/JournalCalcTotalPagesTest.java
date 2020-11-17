
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2020
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

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.journal.JournalModel;

/**
 * Test the Journal SQL Statement.
 */
@SuppressWarnings("checkstyle:methodname")
public class JournalCalcTotalPagesTest {
    
	private JournalModel model;
	
    @Before
    public void setUp() {
        model = new JournalModel(null);
    }

    @Test
    public void GIVEN_no_results_THEN_return_correct_total_pages() {
    	// Arrange
    	int resultsNumber = 0;
    	
    	// Act
    	int totalPages = model.calcTotalPages(resultsNumber);
    	
    	// Assert
    	assertEquals(1, totalPages);
    	
    }
    
    @Test
    public void GIVEN_one_result_THEN_return_correct_total_pages() {
    	// Arrange
    	int resultsNumber = 1;
    	
    	// Act
    	int totalPages = model.calcTotalPages(resultsNumber);
    	
    	// Assert
    	assertEquals(1, totalPages);
    	
    }
    
    @Test
    public void GIVEN_25_results_THEN_return_correct_total_pages() {
    	// Arrange
    	int resultsNumber = 25;
    	
    	// Act
    	int totalPages = model.calcTotalPages(resultsNumber);
    	
    	// Assert
    	assertEquals(1, totalPages);
    	
    }
    
    @Test
    public void GIVEN_26_results_THEN_return_correct_total_pages() {
    	// Arrange
    	int resultsNumber = 26;
    	
    	// Act
    	int totalPages = model.calcTotalPages(resultsNumber);
    	
    	// Assert
    	assertEquals(2, totalPages);
    	
    }
    
}
