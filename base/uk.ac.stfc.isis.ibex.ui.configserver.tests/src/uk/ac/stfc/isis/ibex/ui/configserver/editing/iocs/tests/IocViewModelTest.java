/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IocViewModel;

/**
 *
 */
@SuppressWarnings("checkstyle:methodname")
public class IocViewModelTest {

    private IocViewModel viewModel;
    private EditableConfiguration config;
    private EditableIoc autostartIoc;
    private String autostartName = "autostart_ioc";

    public void setUpMockIoc(String name, boolean autostart, boolean autorestart, Collection<Macro> macros) {
    }

    @Before
    public void setUp() {
        config = mock(EditableConfiguration.class);

        autostartIoc = new EditableIoc(new Ioc(autostartName));
        autostartIoc.setAutostart(true);

        viewModel = new IocViewModel(config);
    }

    @Test
    public void GIVEN_blank_viewmodel_THEN_contains_default_name() {
        // Arrange
        String expected = "";
        String actual = viewModel.getName();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_viewmodel_THEN_contains_default_auto_start() {
        // Arrange
        boolean expected = false;
        boolean actual = viewModel.isAutoStart();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_viewmodel_THEN_contains_default_auto_restart() {
        // Arrange
        boolean expected = false;
        boolean actual = viewModel.isAutoRestart();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_viewmodel_THEN_contains_default_sim_level() {
        // Arrange
        int expected = SimLevel.NONE.ordinal();
        int actual = viewModel.getSimLevel();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_viewmodel_THEN_contains_empty_macros() {
        // Arrange
        int expected = 0;
        int actual = viewModel.getMacros().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_viewmodel_THEN_contains_empty_pv_values() {
        // Arrange
        int expected = 0;
        int actual = viewModel.getPvVals().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_viewmodel_THEN_contains_empty_pv_sets() {
        // Arrange
        int expected = 0;
        int actual = viewModel.getPvSets().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void WHEN_ioc_set_on_viewmodel_THEN_viewmodel_initialised_with_values_from_ioc() {
        // Arrange
        viewModel.setIoc(autostartIoc);

        boolean expected = true;
        boolean actual = viewModel.isAutoStart();
        
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_ioc_set_WHEN_changing_viewmodel_values_without_saving_ioc_THEN_ioc_has_old_value() {
        // Arrange
        boolean expected = false;
        viewModel.setIoc(autostartIoc);

        // Act
        viewModel.setAutoStart(expected);
        boolean actual = viewModel.getIoc().getAutostart();

        // Assert
        assertNotEquals(expected, actual);
    }

    @Test
    public void GIVEN_ioc_set_WHEN_changing_viewmodel_values_and_saving_ioc_THEN_ioc_has_new_value() {
        // Arrange
        boolean expected = false;
        viewModel.setIoc(autostartIoc);

        // Act
        viewModel.setAutoStart(expected);
        viewModel.saveIoc();
        boolean actual = viewModel.getIoc().getAutostart();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_ioc_name_set_WHEN_updating_ioc_THEN_correct_ioc_is_set_in_viewmodel() {
        // Arrange
        Collection<EditableIoc> iocs = new ArrayList<EditableIoc>();
        iocs.add(autostartIoc);
        when(config.getSelectedIocs()).thenReturn(iocs);

        viewModel.setName(autostartName);
        
        // Act
        viewModel.updateIoc();
        
        // Assert
        assertEquals(autostartIoc, viewModel.getIoc());
    }
}
