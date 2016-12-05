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
package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.DateTimeProvider;
import uk.ac.stfc.isis.ibex.scriptgenerator.PythonBuilder;
import uk.ac.stfc.isis.ibex.scriptgenerator.row.Row;
import uk.ac.stfc.isis.ibex.scriptgenerator.row.WaitUnit;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.ApertureSans;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.ApertureTrans;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.CollectionMode;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.Order;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.SampleGeometry;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.SansSettings;

/**
 * Tests the PythonBuilder class for correct behaviour.
 */
public class PythonBuilderTest {
    PythonBuilder builder;
    LocalDate date;
    LocalTime time;
    SansSettings settings;
    Collection<Row> rows;

    SansSettings defaultSettings = new SansSettings(1, 1, 7, 7, Order.TRANS, false, ApertureSans.MEDIUM,
            ApertureTrans.MEDIUM, SampleGeometry.DISC, CollectionMode.HISTOGRAM);

    Row defaultRow = new Row("AA", 0.0, WaitUnit.UAMPS, 0.0, WaitUnit.UAMPS, 1.0, "", 1.0);


    String defaultSetup = "    set_sample_par('height', '7')\n" 
            + "    set_sample_par('width', '7')\n"
            + "    set_sample_par('geometry', 'Disc')\n";

    String defaultDoSans = "    for i in range(1):\n"
            + "        set_aperture('medium')\n"
            + "        lm.dosans_normal(position='AA', title='', uamps=0.0, thickness=1.0, rtype=0)\n";
    
    String defaultDoTrans = "    for i in range(1):\n"
            + "        set_aperture('medium')\n"
            + "        lm.dotrans_normal(position='AA', title='', uamps=0.0, thickness=1.0, rtype=0)\n";

    @Before
    public void setUp() {
        DateTimeProvider dateTime = mock(DateTimeProvider.class);
        rows = new ArrayList<Row>();
        settings = defaultSettings;

        builder = new PythonBuilder(dateTime);
        builder.setSettings(settings);

        date = LocalDate.now();
        time = LocalTime.now();
        when(dateTime.getDate()).thenReturn(date);
        when(dateTime.getTime()).thenReturn(time);
    }

//    @After
//    public void tearDown() {
//
//    }

    public String getHeader() {

        String header = "# Script created by ZOOM Script at " + date + " " + time + "\n" 
                + "import LSS.SANSroutines as lm\n"
                + "\n" 
                + "def my_script():\n" 
                + "    from genie_python.genie import *\n"
                + "    lm.setupzoom_normal()\n";

        return header;
    }

    @Test
    public void WHEN_run_with_blank_rows_and_default_settings_THEN_valid_base_script_is_generated() {
        // Arrange
        builder.setRows(rows);
        String expected = getHeader() + defaultSetup;
        
        // Act
        String actual = builder.createScript();
        
        // Assert
        assertEquals(expected.trim(), actual.trim());
    }

    @Test
    public void WHEN_row_is_empty_THEN_ignored_in_script() {
        // Arrange
        String expected = getHeader() + defaultSetup;

        // Act
        rows.add(new Row());
        builder.setRows(rows);
        String actual = builder.createScript();

        // Assert
        assertEquals(expected.trim(), actual.trim());
    }
    
    @Test
    public void WHEN_changing_sample_parameters_THEN_generated_script_contains_modified_parameters() {
        // Arrange
        builder.setRows(rows);
        int customWidth = 5;
        int customHeight = 10;
        String customGeometry = "Flat Plate";
        String expected =
                getHeader() + "    set_sample_par('height', '" + customHeight + "')\n" 
                        + "    set_sample_par('width', '" + customWidth + "')\n"
                        + "    set_sample_par('geometry', '" + customGeometry + "')\n";

        // Act
        settings.setSampleHeight(10);
        settings.setSampleWidth(5);
        settings.setGeometry(SampleGeometry.FLATPLATE);
        String actual = builder.createScript();
        
        // Assert
        assertEquals(expected.trim(), actual.trim());

    }

    @Test
    public void WHEN_sans_first_set_THEN_script_runs_sans_first() {
        // Arrange
        rows.add(defaultRow);
        builder.setRows(rows);
        settings.setOrder(Order.SANS);
        String expected = getHeader() + defaultSetup + "    \n" + defaultDoSans + "    \n" + defaultDoTrans;

        // Act
        String actual = builder.createScript();

        // Assert
        assertEquals(expected.trim(), actual.trim());
    }

    @Test
    public void WHEN_trans_first_set_THEN_script_runs_trans_first() {
        // Arrange
        rows.add(defaultRow);
        builder.setRows(rows);
        settings.setOrder(Order.TRANS);
        String expected = getHeader() + defaultSetup + "    \n" + defaultDoTrans + "    \n" + defaultDoSans;

        // Act
        String actual = builder.createScript();

        // Assert
        assertEquals(expected.trim(), actual.trim());
    }
}
