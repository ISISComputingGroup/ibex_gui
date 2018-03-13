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
package uk.ac.stfc.isis.ibex.ui.nicos.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.nicos.messages.ExecutionInstruction;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptStatusViewModel;

/**
 * Class for testing the behaviour of the ScriptStatusViewModel.
 */
public class ScriptStatusViewModelTest {

    private static final Image PAUSE_ICON =
            ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/pause.png");
    private static final Image RESUME_ICON =
            ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/resume.png");
    private static final String PAUSE_TEXT = "Pause";
    private static final String RESUME_TEXT = "Resume";

    private NicosModel model;
    private ScriptStatusViewModel viewModel;

    @Captor
    ArgumentCaptor<ExecutionInstruction> captor = ArgumentCaptor.forClass(ExecutionInstruction.class);;

    private String prefixStatus(String status) {
        return "Status is: " + status;
    }

    @Before
    public void setUp() {
        model = mock(NicosModel.class);
        viewModel = new ScriptStatusViewModel(model);
    }

    @Test
    public void GIVEN_status_is_idle_THEN_buttons_are_disabled(){
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.IDLE);
        boolean expected = false;

        // Act
        boolean actual = viewModel.getEnableButtons();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_status_is_idleexc_THEN_buttons_are_disabled() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.IDLEEXC);
        boolean expected = false;

        // Act
        boolean actual = viewModel.getEnableButtons();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_status_is_stopping_THEN_buttons_are_disabled() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.STOPPING);
        boolean expected = false;

        // Act
        boolean actual = viewModel.getEnableButtons();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_status_is_running_THEN_buttons_are_enabled() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.RUNNING);
        boolean expected = true;

        // Act
        boolean actual = viewModel.getEnableButtons();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_status_is_paused_THEN_buttons_are_enabled() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.INBREAK);
        boolean expected = true;

        // Act
        boolean actual = viewModel.getEnableButtons();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_status_is_idle_THEN_status_readback_says_idle() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.IDLE);
        String expected = prefixStatus(ScriptStatus.IDLE.getDesc());

        // Act
        String actual = viewModel.getStatusReadback();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_status_is_idle_exc_THEN_status_readback_says_idle_last_failed() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.IDLEEXC);
        String expected = prefixStatus(ScriptStatus.IDLEEXC.getDesc());

        // Act
        String actual = viewModel.getStatusReadback();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_status_is_stopping_THEN_status_readback_says_stopping() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.STOPPING);
        String expected = prefixStatus(ScriptStatus.STOPPING.getDesc());

        // Act
        String actual = viewModel.getStatusReadback();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_status_is_running_THEN_status_readback_says_running() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.RUNNING);
        String expected = prefixStatus(ScriptStatus.RUNNING.getDesc());

        // Act
        String actual = viewModel.getStatusReadback();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_status_is_inbreak_THEN_status_readback_says_paused() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.INBREAK);
        String expected = prefixStatus(ScriptStatus.INBREAK.getDesc());

        // Act
        String actual = viewModel.getStatusReadback();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_status_is_running_THEN_toggle_button_labeled_as_pause() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.RUNNING);

        // Act
        Image expectedIcon = PAUSE_ICON;
        Image actualIcon = viewModel.getToggleButtonIcon();
        String expectedText = PAUSE_TEXT;
        String actualText = viewModel.getToggleButtonText();

        // Assert
        assertEquals(expectedIcon, actualIcon);
        assertEquals(expectedText, actualText);
    }

    @Test
    public void GIVEN_status_is_paused_THEN_toggle_button_labeled_as_resume() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.INBREAK);

        // Act
        Image expectedIcon = RESUME_ICON;
        Image actualIcon = viewModel.getToggleButtonIcon();
        String expectedText = RESUME_TEXT;
        String actualText = viewModel.getToggleButtonText();

        // Assert
        assertEquals(expectedIcon, actualIcon);
        assertEquals(expectedText, actualText);
    }

    @Test
    public void GIVEN_status_is_running_WHEN_toggling_pause_THEN_action_is_pause_script() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.RUNNING);

        // Act
        viewModel.toggleExecution();
        
        // Assert
        verify(model).sendExecutionInstruction(captor.capture());
        String expected = "break";
        String actual = captor.getValue().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_status_is_inbreak_WHEN_toggling_pause_THEN_action_is_unpause_script() {
        // Arrange
        viewModel.setScriptStatus(ScriptStatus.INBREAK);

        // Act
        viewModel.toggleExecution();

        // Assert
        verify(model).sendExecutionInstruction(captor.capture());
        String expected = "continue";
        String actual = captor.getValue().toString();
        assertEquals(expected, actual);

    }
}
