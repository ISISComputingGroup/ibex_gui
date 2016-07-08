
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

package uk.ac.stfc.isis.ibex.targets.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.targets.OpiTarget;

/**
 * This class is responsible for testing the target class.
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname" })
public class OpiTargetTest {

    private static final String TARGET_NAME = "test_name";
    private static final String OPI_NAME = "opi_name";

    @Test
    public void
            GIVEN_opiName_and_title_WHEN_opi_target_is_contstucted_with_them_THEN_object_name_and_opiName_match_original_arguments() {

        // Act
        OpiTarget ovt = new OpiTarget(TARGET_NAME, OPI_NAME);

        // Assert
        assertEquals(TARGET_NAME, ovt.name());
        assertEquals(OPI_NAME, ovt.opiName());
    }
}
