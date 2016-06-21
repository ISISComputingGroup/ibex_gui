
/**
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.synoptic.tests.model.desc;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.RecordType;

@SuppressWarnings("checkstyle:methodname")
public class PVTest {
    
    private static final String DISPLAY_NAME = "display name";
    private static final String NEW_NAME = "new name";
    private static final String ADDRESS = "address";
    private static final String NEW_ADDRESS = "new address";
    private RecordType recordType;

    private PV source;

    @Before
    public void setUp() {
        // Arrange
        source = new PV();
        source.setDisplayName(DISPLAY_NAME);
        source.setAddress(ADDRESS);

        recordType = new RecordType();
        recordType.setIO(IO.READ);
        source.setRecordType(recordType);
    }

    @Test
    public void copied_object_has_same_display_name_as_source_object() {
        // Act
        PV copied = new PV(source);

        // Assert
        assertEquals(source.displayName(), copied.displayName());
    }

    @Test
    public void copied_object_has_display_name_that_is_not_linked_to_source_object() {
        // Act
        PV copied = new PV(source);
        copied.setDisplayName(NEW_NAME);

        // Assert
        assertEquals(DISPLAY_NAME, source.displayName());
        assertEquals(NEW_NAME, copied.displayName());
    }

    @Test
    public void copied_object_has_same_address_as_source_object() {
        // Act
        PV copied = new PV(source);

        // Assert
        assertEquals(source.address(), copied.address());
    }

    @Test
    public void copied_object_has_address_that_is_not_linked_to_source_object() {
        // Act
        PV copied = new PV(source);
        copied.setAddress(NEW_ADDRESS);

        // Assert
        assertEquals(ADDRESS, source.address());
        assertEquals(NEW_ADDRESS, copied.address());
    }

    @Test
    public void copied_object_has_same_record_type_as_source_object() {
        // Act
        PV copied = new PV(source);

        // Assert
        assertEquals(source.recordType(), copied.recordType());
    }

    @Test
    public void copied_object_has_record_type_that_is_not_linked_to_source_object() {
        // Act
        PV copied = new PV(source);
        RecordType newRecordType = new RecordType();
        newRecordType.setIO(IO.WRITE);
        copied.setRecordType(newRecordType);

        // Assert
        assertEquals(IO.READ, source.recordType().io());
        assertEquals(IO.WRITE, copied.recordType().io());
    }
}
