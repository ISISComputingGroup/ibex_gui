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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

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
    public void GIVEN_connection_success_THEN_last_update_time_is_correct() {
        // Arrange
        Date lastUpdate = new Date();
        when(model.getConnectionSuccess()).thenReturn(true);
        when(model.getLastUpdate()).thenReturn(lastUpdate);
        String expected = LAST_UPDATE_MESSAGE + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(lastUpdate);

        // Act
        viewModel = new JournalViewModel(model);
        String actual = viewModel.getLastUpdate();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_last_update_not_today_THEN_last_update_time_contains_day() {
        // Arrange
        Date lastUpdate = new Date(System.currentTimeMillis() - DAY_IN_MILLIS);
        when(model.getConnectionSuccess()).thenReturn(true);
        when(model.getLastUpdate()).thenReturn(lastUpdate);
        String expected = LAST_UPDATE_MESSAGE + DateFormat.getDateTimeInstance().format(lastUpdate);
        // Act
        viewModel = new JournalViewModel(model);
        String actual = viewModel.getLastUpdate();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_connection_failed_THEN_message_shows_error() {
        // Arrange
        String error = "Something went wrong";
        when(model.getConnectionSuccess()).thenReturn(false);
        when(model.getMessage()).thenReturn(error);
        String expected = error;

        // Act
        viewModel = new JournalViewModel(model);
        String actual = viewModel.getMessage();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_no_successful_update_THEN_last_update_time_is_NA() {
        // Arrange
        when(model.getConnectionSuccess()).thenReturn(false);
        when(model.getLastUpdate()).thenReturn(null);
        String expected = LAST_UPDATE_MESSAGE + "N/A";

        // Act
        viewModel = new JournalViewModel(model);
        String actual = viewModel.getLastUpdate();

        // Assert
        assertEquals(expected, actual);
    }

}
