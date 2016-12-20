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

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.PythonString;
import uk.ac.stfc.isis.ibex.scriptgenerator.Script;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.Order;

/**
 * Unit tests for the Script class checking that the script is assembled
 * correctly given ordering settings.
 */
public class ScriptTest {
    private Script script;
    private static final String INDENT = "    ";
    private static final String NEWLINE = "\n";
    private static final String headerValue = "HEADER:";
    private static final String setupValue = "SETUP";
    private static final String sansLoopValue = "SANS_LOOP:";
    private static final String transLoopValue = "TRANS_LOOP:";
    private static final String altHeaderValue = "ALT_HEADER";
    private static final String altLoopValue = "ALT_LOOP:";
    private static final String altSansConditionValue = "ALT_SANS_CONDITION:";
    private static final String altTransConditionValue = "ALT_TRANS_CONDITION:";
    private static final String altFooterValue = "ALT_FOOTER";
    private static final String doSansValue1 = "DO_SANS(1)";
    private static final String doSansValue2 = "DO_SANS(2)";
    private static final String doTransValue1 = "DO_TRANS(1)";
    private static final String doTransValue2 = "DO_TRANS(2)";

    private static PythonString header = new PythonString();
    private static PythonString setup = new PythonString();
    private static PythonString sansLoop = new PythonString();
    private static PythonString transLoop = new PythonString();
    private static PythonString altHeader = new PythonString();
    private static PythonString altLoop = new PythonString();
    private static PythonString altSansCondition = new PythonString();
    private static PythonString altTransCondition = new PythonString();
    private static PythonString altFooter = new PythonString();
    private static PythonString doSans1 = new PythonString();
    private static PythonString doSans2 = new PythonString();
    private static PythonString doTrans1 = new PythonString();
    private static PythonString doTrans2 = new PythonString();

    private static ArrayList<PythonString> doSansRows = new ArrayList<PythonString>();
    private static ArrayList<PythonString> doTransRows = new ArrayList<PythonString>();

    private String indent(String block) {
        return block.replaceAll("(?m)^", INDENT);
    }

    @Before
    public void setUp() {
        header.add(headerValue);
        setup.add(setupValue);
        sansLoop.add(sansLoopValue);
        transLoop.add(transLoopValue);
        altHeader.add(altHeaderValue);
        altLoop.add(altLoopValue);
        altSansCondition.add(altSansConditionValue);
        altTransCondition.add(altTransConditionValue);
        altFooter.add(altFooterValue);
        doSans1.add(doSansValue1);
        doSans2.add(doSansValue2);
        doTrans1.add(doTransValue1);
        doTrans2.add(doTransValue2);
        doSansRows = new ArrayList<PythonString>(Arrays.asList(doSans1, doSans2));
        doTransRows = new ArrayList<PythonString>(Arrays.asList(doTrans1, doTrans2));

        script = new Script();

        script.setHeader(header);
        script.setSetup(setup);
        script.setSansLoop(sansLoop);
        script.setTransLoop(transLoop);
        script.setAltHeader(altHeader);
        script.setAltLoop(altLoop);
        script.setAltSansCondition(altSansCondition);
        script.setAltTransCondition(altTransCondition);
        script.setAltFooter(altFooter);
        

        script.setDoSansRows(doSansRows);
        script.setDoTransRows(doTransRows);
    }

    @After
    public void tearDown() {
        header.clear();
        setup.clear();
        sansLoop.clear();
        transLoop.clear();
        altHeader.clear();
        altLoop.clear();
        altSansCondition.clear();
        altTransCondition.clear();
        altFooter.clear();
        doSans1.clear();
        doSans2.clear();
        doTrans1.clear();
        doTrans2.clear();
    }

    @Test
    public void GIVEN_no_rows_and_default_settings_WHEN_generating_script_THEN_valid_base_script_is_generated() {
        // Arrange
        doSansRows.clear();
        doTransRows.clear();
        String expected = headerValue + NEWLINE 
                + indent(setupValue);
        
        // Act
        script.createScript(Order.SANS, false);
        String actual = script.toString();
        
        // Assert
        assertEquals(expected.trim(), actual.trim());
    }

    @Test
    public void GIVEN_order_is_sans_first_WHEN_generating_script_THEN_script_runs_sans_first() {
        // Arrange
        String expected = headerValue + NEWLINE 
                + indent(setupValue) + NEWLINE 
                + indent(sansLoopValue) + NEWLINE 
                + indent(indent(doSansValue1)) + NEWLINE 
                + indent(indent(doSansValue2)) + NEWLINE
                + indent(transLoopValue) + NEWLINE 
                + indent(indent(doTransValue1)) + NEWLINE 
                + indent(indent(doTransValue2));
                
        // Act
        script.createScript(Order.SANS, false);
        String actual = script.toString();

        // Assert
        assertEquals(expected.trim(), actual.trim());
    }

    @Test
    public void GIVEN_order_is_trans_first_WHEN_generating_script_THEN_script_runs_trans_first() {
        // Arrange
        String expected = headerValue + NEWLINE 
                + indent(setupValue) + NEWLINE 
                + indent(transLoopValue) + NEWLINE 
                + indent(indent(doTransValue1)) + NEWLINE 
                + indent(indent(doTransValue2)) + NEWLINE
                + indent(sansLoopValue) + NEWLINE 
                + indent(indent(doSansValue1)) + NEWLINE 
                + indent(indent(doSansValue2));
                
        // Act
        script.createScript(Order.TRANS, false);
        String actual = script.toString();

        // Assert
        assertEquals(expected.trim(), actual.trim());
    }

    @Test
    public void GIVEN_order_is_altsans_WHEN_generating_script_THEN_script_alternates_with_sans_first() {
        // Arrange
        String expected = headerValue + NEWLINE 
                + indent(setupValue) + NEWLINE 
                + indent(altHeaderValue) + NEWLINE 
                + indent(altLoopValue) + NEWLINE 
                + indent(indent(altSansConditionValue)) + NEWLINE 
                + indent(indent(indent(doSansValue1))) + NEWLINE 
                + indent(indent(altTransConditionValue)) + NEWLINE 
                + indent(indent(indent(doTransValue1))) + NEWLINE 
                + indent(indent(altSansConditionValue)) + NEWLINE 
                + indent(indent(indent(doSansValue2))) + NEWLINE 
                + indent(indent(altTransConditionValue)) + NEWLINE 
                + indent(indent(indent(doTransValue2))) + NEWLINE 
                + indent(indent(altFooterValue));
                
        // Act
        script.createScript(Order.ALTSANS, false);
        String actual = script.toString();

        // Assert
        assertEquals(expected.trim(), actual.trim());
    }

    @Test
    public void GIVEN_order_is_alttrans_WHEN_generating_script_THEN_script_alternates_with_trans_first() {
        // Arrange
        String expected = headerValue + NEWLINE 
                + indent(setupValue) + NEWLINE 
                + indent(altHeaderValue) + NEWLINE 
                + indent(altLoopValue) + NEWLINE 
                + indent(indent(altTransConditionValue)) + NEWLINE 
                + indent(indent(indent(doTransValue1))) + NEWLINE 
                + indent(indent(altSansConditionValue)) + NEWLINE 
                + indent(indent(indent(doSansValue1))) + NEWLINE 
                + indent(indent(altTransConditionValue)) + NEWLINE 
                + indent(indent(indent(doTransValue2))) + NEWLINE 
                + indent(indent(altSansConditionValue)) + NEWLINE 
                + indent(indent(indent(doSansValue2))) + NEWLINE 
                + indent(indent(altFooterValue));
                
        // Act
        script.createScript(Order.ALTTRANS, false);
        String actual = script.toString();

        // Assert
        assertEquals(expected.trim(), actual.trim());
    }

    @Test
    public void GIVEN_loop_over_every_run_set_WHEN_creating_sequential_script_THEN_script_looping_over_every_run() {
        // Arrange
        String expected = headerValue + NEWLINE 
                + indent(setupValue) + NEWLINE 
                + indent(sansLoopValue) + NEWLINE 
                + indent(indent(doSansValue1)) + NEWLINE 
                + indent(sansLoopValue) + NEWLINE
                + indent(indent(doSansValue2)) + NEWLINE
                + indent(transLoopValue) + NEWLINE 
                + indent(indent(doTransValue1)) + NEWLINE 
                + indent(transLoopValue) + NEWLINE 
                + indent(indent(doTransValue2));
                
        // Act
        script.createScript(Order.SANS, true);
        String actual = script.toString();

        // Assert
        assertEquals(expected.trim(), actual.trim());
    }

    @Test
    public void GIVEN_loop_over_every_run_set_WHEN_creating_alternating_script_THEN_order_is_correct() {
        // Arrange
        String expected = headerValue + NEWLINE 
                + indent(setupValue) + NEWLINE 
                + indent(altHeaderValue) + NEWLINE 
                + indent(altLoopValue) + NEWLINE 
                + indent(indent(altSansConditionValue)) + NEWLINE 
                + indent(indent(indent(doSansValue1))) + NEWLINE 
                + indent(indent(altTransConditionValue)) + NEWLINE 
                + indent(indent(indent(doTransValue1))) + NEWLINE 
                + indent(indent(altFooterValue)) + NEWLINE 
                + indent(altLoopValue) + NEWLINE 
                + indent(indent(altSansConditionValue)) + NEWLINE 
                + indent(indent(indent(doSansValue2))) + NEWLINE 
                + indent(indent(altTransConditionValue)) + NEWLINE 
                + indent(indent(indent(doTransValue2))) + NEWLINE 
                + indent(indent(altFooterValue));
                
        // Act
        script.createScript(Order.ALTSANS, true);
        String actual = script.toString();

        // Assert
        assertEquals(expected.trim(), actual.trim());
    }
    

    @Test
    public void GIVEN_script_previously_generated_WHEN_generating_script_THEN_result_is_correct() {
        // Arrange
        String expected = headerValue + NEWLINE 
                + indent(setupValue) + NEWLINE 
                + indent(sansLoopValue) + NEWLINE 
                + indent(indent(doSansValue1)) + NEWLINE 
                + indent(indent(doSansValue2)) + NEWLINE
                + indent(transLoopValue) + NEWLINE 
                + indent(indent(doTransValue1)) + NEWLINE 
                + indent(indent(doTransValue2));
                
        // Act
        script.createScript(Order.TRANS, false);
        script.createScript(Order.SANS, false);
        String actual = script.toString();

        // Assert
        assertEquals(expected.trim(), actual.trim());
    }
}
