 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2018 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.journalviewer.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.journal.JournalModel;
import uk.ac.stfc.isis.ibex.ui.journalviewer.models.JournalViewModel;

/**
 * Tests for the JournalViewModel class.
 */
public class JournalViewModelTest {

    private JournalViewModel viewModel;
    private JournalModel model;

    private static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
    private static final String LAST_UPDATE_MESSAGE = "Last successful update: ";

    @Before
    public void setUp() {
        model = mock(JournalModel.class);
    }

    @Test
    public void GIVEN_message_in_model_THEN_viewmodel_returns_same_message() {
        // Arrange
        String error = "Some message";
        when(model.getMessage()).thenReturn(error);
        String expected = error;

        // Act
        viewModel = new JournalViewModel(model);
        String actual = viewModel.getMessage();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_last_updated_is_today_THEN_last_updated_string_contains_time_only() {
        // Arrange
        Date lastUpdate = new Date();
        when(model.getLastUpdate()).thenReturn(lastUpdate);
        String expected = LAST_UPDATE_MESSAGE + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(lastUpdate);

        // Act
        viewModel = new JournalViewModel(model);
        String actual = viewModel.getLastUpdate();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_no_successful_update_THEN_last_update_time_is_NA() {
        // Arrange
        when(model.getLastUpdate()).thenReturn(null);
        String expected = LAST_UPDATE_MESSAGE + "N/A";

        // Act
        viewModel = new JournalViewModel(model);
        String actual = viewModel.getLastUpdate();

        // Assert
        assertEquals(expected, actual);
    }


    @Test
    public void GIVEN_last_update_not_today_THEN_last_updated_string_contains_date_and_time() {
        // Arrange
        Date lastUpdate = new Date(System.currentTimeMillis() - DAY_IN_MILLIS);
        when(model.getLastUpdate()).thenReturn(lastUpdate);
        String expected = LAST_UPDATE_MESSAGE + DateFormat.getDateTimeInstance().format(lastUpdate);
        // Act
        viewModel = new JournalViewModel(model);
        String actual = viewModel.getLastUpdate();

        // Assert
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_model_says_title_is_selected_THEN_viewmodel_says_title_is_selected() {
    	when(model.getSelectedFields()).thenReturn(EnumSet.of(JournalField.TITLE));
    	
    	viewModel = new JournalViewModel(model);
    	
    	assertTrue(viewModel.getFieldSelected(JournalField.TITLE));
    }
    
    @Test
    public void GIVEN_model_says_title_is_not_selected_THEN_viewmodel_says_title_is_not_selected() {
    	when(model.getSelectedFields()).thenReturn(EnumSet.noneOf(JournalField.class));
    	
    	viewModel = new JournalViewModel(model);
    	
    	assertFalse(viewModel.getFieldSelected(JournalField.TITLE));
    }
    
    @Test
    public void GIVEN_incorrect_date_from_UI_WHEN_validate_date_time_THEN_validation_fail() {
        // Arrange
    	
    	String actual = "Date/Time in wrong order" ; 
    	viewModel = new JournalViewModel(model);
        Calendar calendar_1 = new GregorianCalendar(2013, 2, 12, 10, 2, 3);
        Calendar calendar_2 = new GregorianCalendar(2015, 2, 12, 15, 3, 4);
        
        viewModel.setInitialFromDateTime((Calendar)calendar_1.clone(), (Calendar)calendar_1.clone());
        viewModel.setInitialToDateTime((Calendar)calendar_2.clone(), (Calendar)calendar_2.clone());
        
        viewModel.setFromDate(calendar_2);
        viewModel.setFromTime(calendar_2);
        
        viewModel.setToDate(calendar_1);
        viewModel.setToTime(calendar_1);
        
        String expected = viewModel.getErrorMessage();
        
        assertEquals(expected, actual);
     
    }
    
    @Test
    public void GIVEN_correct_date_from_UI_WHEN_validate_date_time_THEN_validation_pass() {
        // Arrange
    	
    	String actual = "" ; 
        Calendar calendar_1 = new GregorianCalendar(2013, 2, 12, 10, 2, 3);
        Calendar calendar_2 = new GregorianCalendar(2015, 2, 12, 15, 3, 4);
        
        viewModel = new JournalViewModel(model);
        
        viewModel.setInitialFromDateTime((Calendar)calendar_1.clone(), (Calendar)calendar_1.clone());
        viewModel.setInitialToDateTime((Calendar)calendar_2.clone(), (Calendar)calendar_2.clone());
        
        viewModel.setFromDate(calendar_1);
        viewModel.setFromTime(calendar_1);
        
        viewModel.setToDate(calendar_2);
        viewModel.setToTime(calendar_2);
        
        String expected = viewModel.getErrorMessage();
        
        assertEquals(expected, actual);
     
    }
    
    @Test
    public void GIVEN_on_page_0_page_size_0_results_0_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(0);
    	when(model.getPageSize()).thenReturn(0);
    	when(model.getPage()).thenReturn(0);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 0-0 of 0", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_1_page_size_1_results_1_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(1);
    	when(model.getPageSize()).thenReturn(1);
    	when(model.getPage()).thenReturn(1);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 1-1 of 1", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_1_page_size_0_results_1_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(1);
    	when(model.getPageSize()).thenReturn(0);
    	when(model.getPage()).thenReturn(1);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 0-0 of 1", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_0_page_size_1_results_1_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(1);
    	when(model.getPageSize()).thenReturn(1);
    	when(model.getPage()).thenReturn(0);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 0-0 of 1", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_1_page_size_1_results_0_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(0);
    	when(model.getPageSize()).thenReturn(1);
    	when(model.getPage()).thenReturn(1);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 0-0 of 0", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_1_page_size_1_results_2_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(2);
    	when(model.getPageSize()).thenReturn(1);
    	when(model.getPage()).thenReturn(1);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 1-1 of 2", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_1_page_size_2_results_1_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(1);
    	when(model.getPageSize()).thenReturn(2);
    	when(model.getPage()).thenReturn(1);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 1-1 of 1", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_1_page_size_2_results_2_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(2);
    	when(model.getPageSize()).thenReturn(2);
    	when(model.getPage()).thenReturn(1);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 1-2 of 2", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_1_page_size_2_results_3_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(3);
    	when(model.getPageSize()).thenReturn(2);
    	when(model.getPage()).thenReturn(1);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 1-2 of 3", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_2_page_size_2_results_3_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(3);
    	when(model.getPageSize()).thenReturn(2);
    	when(model.getPage()).thenReturn(2);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 3-3 of 3", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_2_page_size_15_results_45_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(45);
    	when(model.getPageSize()).thenReturn(15);
    	when(model.getPage()).thenReturn(2);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 16-30 of 45", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_3_page_size_15_results_46_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(46);
    	when(model.getPageSize()).thenReturn(15);
    	when(model.getPage()).thenReturn(3);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 31-45 of 46", resultInfo);
    }
    
    @Test
    public void GIVEN_on_page_4_page_size_15_results_46_THEN_correct_string_returned() {
    	// Arrange
    	when(model.getResultsNumber()).thenReturn(46);
    	when(model.getPageSize()).thenReturn(15);
    	when(model.getPage()).thenReturn(4);
    	
    	// Act
    	viewModel = new JournalViewModel(model);
    	String resultInfo = viewModel.getResultsInfo();
    	
    	// Assert
    	assertEquals("Entries 46-46 of 46", resultInfo);
    }
    
}
