/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2016 Science &
 * Technology Facilities Council. All rights reserved.
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

package uk.ac.stfc.isis.ibex.opis.tests.desc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;

@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename", "checkstyle:methodname" })
public class OpiDescriptionTest {

	@Before
    public void setUp() {
	}

	@Test
    public void GIVEN_no_macros_WHEN_get_desciption_by_key_THEN_default_answer_returned() {
        OpiDescription opiDescription = new OpiDescription("", "", "", new ArrayList<MacroInfo>());

        String result = opiDescription.getMacroDescription("name");

        assertEquals("", result);
	}

    @Test
    public void
            GIVEN_macros_with_description_WHEN_get_desciption_by_key_THEN_description_is_returned() {
        ArrayList<MacroInfo> macros = new ArrayList<MacroInfo>();
        String expectedDescription = "desc";
        String expectedName = "name";
        macros.add(new MacroInfo(expectedName, expectedDescription));
        OpiDescription opiDescription = new OpiDescription("", "", "", macros);

        String result = opiDescription.getMacroDescription(expectedName);

        assertEquals(expectedDescription, result);
    }

}
