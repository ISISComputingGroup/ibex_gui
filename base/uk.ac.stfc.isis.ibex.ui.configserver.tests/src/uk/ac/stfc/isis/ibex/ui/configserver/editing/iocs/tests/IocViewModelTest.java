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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IocViewModel;

/**
 *
 */
@SuppressWarnings("checkstyle:methodname")
public class IocViewModelTest {

//    private EditableIoc mockIoc;
//    private static final String INITIAL_NAME = "initial_name";
//    private static final boolean INITIAL_AUTOSTART = false;
//    private static final boolean INITIAL_AUTORESTART = false;
//    private static final Collection<Macro> INITIAL_MACROS = mock(Collection.class);
//    private static final Collection<PVDefaultValue> INITIAL_PVVALS = mock(Collection.class);
//    private static final Collection<PVSet> INITIAL_PVSETS = mock(Collection.class);

    private IocViewModel viewModel;
    private EditableConfiguration config;

    public void setUpMockIoc(String name, boolean autostart, boolean autorestart, Collection<Macro> macros) {
    }

    @Before
    public void setUp() {
        config = mock(EditableConfiguration.class);

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
    public void GIVEN_ioc_set_on_viewmodel_THEN_contains_iocs_values() {

    }

    // ioc set then vars set but not updated: ioc != vars
    // ioc set then vars set then updated: ioc = vars
    // vars set then ioc set: vars = ioc
}
