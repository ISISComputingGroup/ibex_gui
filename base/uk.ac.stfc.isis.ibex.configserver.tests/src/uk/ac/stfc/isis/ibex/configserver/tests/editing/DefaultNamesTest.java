
/*
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

package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.DefaultName;

public class DefaultNamesTest {
	
    private DefaultName blockName;
    private DefaultName componentName;

	private static final String DEFAULT_NAME = "NAME";

	private List<String> existingBlock;
    private List<String> existingComponent;
	
	@Before
	public void setup() {
		existingBlock = new ArrayList<>();
        blockName = new DefaultName(DEFAULT_NAME);

        existingComponent = new ArrayList<>();
        componentName = new DefaultName(DEFAULT_NAME, " ", true);
	}
	
    private String numberedDefaultBlock(Integer number) {
        return DEFAULT_NAME + "_" + number.toString();
    }

    private String numberedDefaultComponent(Integer number) {
        return DEFAULT_NAME + " (" + number.toString() + ")";
    }

	@Test
    public void if_no_others_default_block_name() {
        assertThat(blockName.getUnique(existingBlock), is(DEFAULT_NAME));
	}
	
	@Test
    public void if_default_block_name_taken_number_is_appended() {
		existingBlock.add(DEFAULT_NAME);
        assertThat(blockName.getUnique(existingBlock), is(numberedDefaultBlock(1)));
	}

	@Test
    public void if_default_block_name_and_first_numbered_are_taken_2_is_appended() {
		existingBlock.add(DEFAULT_NAME);
        existingBlock.add(numberedDefaultBlock(1));
        assertThat(blockName.getUnique(existingBlock), is(numberedDefaultBlock(2)));
	}
	
	@Test
    public void if_nonsequential_block_names_next_available_is_used() {
		existingBlock.add(DEFAULT_NAME);
        existingBlock.add(numberedDefaultBlock(2));
        assertEquals(numberedDefaultBlock(1), blockName.getUnique(existingBlock));
	}
	
	@Test
    public void if_default_block_name_is_not_taken_default_is_used() {
        existingComponent.add(numberedDefaultComponent(1));
        assertThat(componentName.getUnique(existingComponent), is(DEFAULT_NAME));
	}
	
    @Test
    public void if_no_others_default_component_name() {
        assertThat(componentName.getUnique(existingComponent), is(DEFAULT_NAME));
    }

    @Test
    public void if_default_component_name_taken_number_is_appended() {
        existingComponent.add(DEFAULT_NAME);
        assertThat(componentName.getUnique(existingComponent), is(numberedDefaultComponent(1)));
    }

    @Test
    public void if_default_component_name_and_first_numbered_are_taken_2_is_appended() {
        existingComponent.add(DEFAULT_NAME);
        existingComponent.add(numberedDefaultComponent(1));
        assertThat(componentName.getUnique(existingComponent), is(numberedDefaultComponent(2)));
    }

    @Test
    public void if_nonsequential_component_names_next_available_is_used() {
        existingComponent.add(DEFAULT_NAME);
        existingComponent.add(numberedDefaultComponent(2));
        assertEquals(numberedDefaultComponent(1), componentName.getUnique(existingComponent));
    }

    @Test
    public void if_default_component_name_is_not_taken_default_is_used() {
        existingComponent.add(numberedDefaultComponent(1));
        assertThat(componentName.getUnique(existingBlock), is(DEFAULT_NAME));
    }
}
