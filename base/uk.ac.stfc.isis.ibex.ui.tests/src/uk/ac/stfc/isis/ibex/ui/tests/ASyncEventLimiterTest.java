
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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
package uk.ac.stfc.isis.ibex.ui.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.ui.AsyncMessageModerator;
import uk.ac.stfc.isis.ibex.ui.AsyncMessageModeratorTask;

@SuppressWarnings("checkstyle:methodname")
public class ASyncEventLimiterTest {

    private AsyncMessageModerator underTest;
    @Mock
    private AsyncMessageModeratorTask rerunTask;

    @Before
    public void SetUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new AsyncMessageModerator(rerunTask);
    }

    @Test
    public void GIVEN_no_asycn_event_WHEN_run_new_event_THEN_event_allowed() {

        boolean result = underTest.requestTaskLock();

        assertTrue("Process property change", result);

    }

    @Test
    public void GIVEN_asycn_event_already_running_WHEN_run_new_event_THEN_event_disallowed() {

        underTest.requestTaskLock();

        boolean result = underTest.requestTaskLock();

        assertFalse("Process property change", result);

    }

    @Test
    public void GIVEN_asycn_event_processed_WHEN_run_new_event_THEN_event_allowed() {

        underTest.requestTaskLock();
        underTest.releaseTaskLock();

        boolean result = underTest.requestTaskLock();

        assertTrue("Process property change", result);
    }

    @Test
    public void GIVEN_asycn_event_has_not_been_processed_WHEN_initial_task_finish_THEN_task_refired() {

        underTest.requestTaskLock(); // first task starts
        underTest.requestTaskLock(); // second task fails to start

        underTest.releaseTaskLock(); // task finish

        verify(rerunTask).reQueueTask();
    }

    @Test
    public void GIVEN_asycn_event_has_been_processed_WHEN_initial_task_finish_THEN_task_not_refired() {

        underTest.requestTaskLock(); // first task starts
        underTest.releaseTaskLock(); // first task finish
        
        underTest.requestTaskLock(); // second task fails to start
        underTest.releaseTaskLock(); // second task finish

        verify(rerunTask, never()).reQueueTask();
    }

    @Test
    public void GIVEN_null_as_task_WHEN_initial_task_finish_THEN_task_not_refired() {

        AsyncMessageModerator localUnderTest = new AsyncMessageModerator(null);
        localUnderTest.requestTaskLock(); // first task starts
        localUnderTest.requestTaskLock(); // second task fails to start

        localUnderTest.releaseTaskLock(); // task finish

    }

    @Test
    public void GIVEN_asycn_event_has_not_been_processed_and_reQueued_WHEN_new_task_THEN_task_is_not_rerun() {

        underTest.requestTaskLock(); // first task starts
        underTest.requestTaskLock(); // second task fails to start
        underTest.releaseTaskLock(); // task finish and task should be requeued
        underTest.requestTaskLock(); // last task
        underTest.releaseTaskLock(); // task is requeued without interuption and
                                     // so task is not requeued

        verify(rerunTask, times(1)).reQueueTask();
    }

}
