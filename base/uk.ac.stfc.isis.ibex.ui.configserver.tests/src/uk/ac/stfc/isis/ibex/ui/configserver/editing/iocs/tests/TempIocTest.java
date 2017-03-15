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

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog.TempEditableIoc;

/**
 *
 */
@SuppressWarnings("checkstyle:methodname")
public class TempIocTest {

    private TempEditableIoc blankTempIoc;
    private TempEditableIoc populatedTempIoc;
    private EditableIoc autostartIoc;
    private String autostartName = "autostart_ioc";
    private String autostartDescription = "autostart_description";

    @Before
    public void setUp() {
        autostartIoc = new EditableIoc(new Ioc(autostartName), autostartDescription);
        autostartIoc.setAutostart(true);

        blankTempIoc = new TempEditableIoc(new EditableIoc(""));

        populatedTempIoc = new TempEditableIoc(autostartIoc);
    }

    @Test
    public void GIVEN_blank_temp_ioc_THEN_contains_default_name() {
        // Arrange
        String expected = "";
        String actual = blankTempIoc.getName();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_temp_ioc_THEN_contains_default_auto_start() {
        // Arrange
        boolean expected = false;
        boolean actual = blankTempIoc.getAutostart();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_temp_ioc_THEN_contains_default_auto_restart() {
        // Arrange
        boolean expected = false;
        boolean actual = blankTempIoc.getAutostart();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_temp_ioc_THEN_contains_default_sim_level() {
        // Arrange
        SimLevel expected = SimLevel.NONE;
        SimLevel actual = blankTempIoc.getSimLevel();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_temp_ioc_THEN_contains_empty_macros() {
        // Arrange
        int expected = 0;
        int actual = blankTempIoc.getMacros().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_temp_ioc_THEN_contains_empty_pv_values() {
        // Arrange
        int expected = 0;
        int actual = blankTempIoc.getPvs().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_blank_temp_ioc_THEN_contains_empty_pv_sets() {
        // Arrange
        int expected = 0;
        int actual = blankTempIoc.getPvSets().size();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_temp_ioc_based_on_populated_ioc_THEN_viewmodel_initialised_with_values_from_ioc() {
        // Arrange
        boolean expected = true;
        boolean actual = populatedTempIoc.getAutostart();
        
        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void WHEN_changing_temp_ioc_values_without_saving_ioc_THEN_ioc_has_old_value() {
        // Arrange
        boolean expected = false;
        // Act
        populatedTempIoc.setAutostart(expected);
        boolean actual = autostartIoc.getAutostart();

        // Assert
        assertNotEquals(expected, actual);
    }

    @Test
    public void GIVEN_ioc_set_WHEN_changing_viewmodel_values_and_saving_ioc_THEN_ioc_has_new_value() {
        // Arrange
        boolean expected = false;

        // Act
        populatedTempIoc.setAutostart(expected);
        populatedTempIoc.saveIoc();
        boolean actual = autostartIoc.getAutostart();

        // Assert
        assertEquals(expected, actual);
    }
}
