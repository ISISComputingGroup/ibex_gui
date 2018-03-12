
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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

@SuppressWarnings("checkstyle:methodname")
public class EditableConfigurationTest {

	private static final String NAME = "base";
	private static final String DESCRIPTION = "description";
	private static final String SYNOPTIC = "";

    private static final double FLOAT_ASSERT_DELTA = 1.0e-6;
	
	protected static final EditableIoc GALIL01 = new EditableIoc("GALIL_01");
    protected static final Block GAPX = new Block("GAPX", "ADDRESS", true, true);
    protected static final Block GAPY = new Block("GAPY", "ADDRESS", true, true);
	protected static final Group JAWS = new Group("JAWS", Arrays.asList("GAPX"), null);
	protected static final Group EMPTY_GROUP_01 = new Group("EMPTY_GROUP_01");
	protected static final Group EMPTY_GROUP_02 = new Group("EMPTY_GROUP_02");
    protected static final Configuration MOTOR_DETAILS = new Configuration("MOTOR", "DESCRIPTION");
    protected static final ComponentInfo MOTOR = new ComponentInfo(MOTOR_DETAILS);

	protected List<Ioc> iocs;
	protected List<Block> blocks;
	protected List<Group> groups;
    protected List<ComponentInfo> components;
	protected List<String> history;
	
	protected List<EditableIoc> allIocs;
    protected List<Configuration> allComponents;
	protected List<PV> allPvs;
	protected List<PV> activePvs;
	
	@Before
	public void setup() {
		iocs = new ArrayList<>();
		blocks = new ArrayList<>();
		groups = new ArrayList<>();
		components = new ArrayList<>();
		history = new ArrayList<>();
		
		allIocs =  new ArrayList<>();
		GALIL01.setAutostart(false);
		allIocs.add(GALIL01);
		
		allComponents = new ArrayList<>();
		allComponents.add(MOTOR_DETAILS);
		
		allPvs = new ArrayList<>();
		activePvs = new ArrayList<>();		
	}
	
	public static void assertAreEqual(Configuration expected, Configuration actual) {
		assertEquals("Iocs", expected.getIocs(), actual.getIocs());
		assertBlocksAreEqual(expected.getBlocks(), actual.getBlocks());
		assertEquals("Groups", expected.getGroups(), actual.getGroups());
		assertEquals("Components", expected.getComponents(), actual.getComponents());
	}
	
	public static void assertBlocksAreEqual(Collection<Block> expected, Collection<? extends Block> actual) {
		assertEquals(expected.size(), actual.size());
		
		Iterator<Block> itrExp = expected.iterator();
		Iterator<? extends Block> itrAct = actual.iterator();
		
	    while (itrExp.hasNext()) {
	    	Block exp = itrExp.next();
	    	Block act = itrAct.next();
	    	
	    	assertEquals(exp.getName(), act.getName());
	    	assertEquals(exp.getPV(), act.getPV());
	    	assertEquals(exp.getIsLocal(), act.getIsLocal());
	    	assertEquals(exp.getIsVisible(), act.getIsVisible());
            assertEquals(exp.getRCLowLimit(), act.getRCLowLimit(), FLOAT_ASSERT_DELTA);
            assertEquals(exp.getRCHighLimit(), act.getRCHighLimit(), FLOAT_ASSERT_DELTA);
            assertEquals(exp.getRCEnabled(), act.getRCEnabled());
            assertEquals(exp.getLogPeriodic(), act.getLogPeriodic());
            assertEquals(exp.getLogDeadband(), act.getLogDeadband(), FLOAT_ASSERT_DELTA);
            assertEquals(exp.getLogRate(), act.getLogRate());
		}
	}
	
	public static Configuration emptyConfig() {
		return new Configuration(
				"Empty_config", 
				"No Description",
				"",
				Collections.<Ioc>emptyList(), 
				Collections.<Block>emptyList(),
				Collections.<Group>emptyList(),
				Collections.<ComponentInfo>emptyList(),
				Collections.<String>emptyList());
	}
	
	protected void populateConfig() {
		EditableIoc copyGalil = new EditableIoc(GALIL01);
		copyGalil.setAutostart(true);
		
		iocs.add(copyGalil);
		blocks.add(GAPX);
		groups.add(JAWS);
        components.add(MOTOR);
	}
	
	protected Configuration config() {
		return new Configuration(NAME, DESCRIPTION, SYNOPTIC, iocs, blocks, groups, components, history);
	}
	
	protected EditableConfiguration edit(Configuration config) {
        return new EditableConfiguration(config, allIocs, allComponents, allPvs);
	}
	
	protected static <T> T getFirst(Iterable<T> items) {
		return Iterables.get(items, 0);
	}
	
	protected static <T> void assertContains(Collection<T> items, T item) {
		assertTrue(items.contains(item));
	}
	
	protected static <T> void assertDoesNotContain(Collection<T> items, T item) {
		assertFalse(items.contains(item));
	}
	
	protected static <T> void assertEmpty(Collection<T> items) {
		assertTrue(items.isEmpty());
	}
	
	protected static <T> void assertNotEmpty(Collection<T> items) {
		assertFalse(items.isEmpty());
	}

    @Test
    public void GIVEN_editable_configuration_has_block_WHEN_block_requested_by_name_THEN_block_returned_has_name() {
        // Arrange
        populateConfig();
        EditableConfiguration config = edit(config());

        // Assert
        assertEquals(config.getBlockByName(GAPX.getName()).getName(), GAPX.getName());
    }

    @Test
    public void
            GIVEN_editable_configuration_does_not_have_block_WHEN_block_requested_by_name_THEN_block_returned_is_null() {
        // Arrange
        EditableConfiguration config = edit(config());

        // Assert
        assertNull(config.getBlockByName(GAPX.getName()));
    }
    
    @Test
    public void GIVEN_two_editable_configurations_created_from_same_list_of_iocs_with_macros_THEN_editable_configurations_do_not_share_state() {
    	EditableConfiguration config1 = edit(config());    	
    	allIocs.add(new EditableIoc(GALIL01));	
    	EditableConfiguration config2 = edit(config());
    	
    	assertFalse(config1.getAvailableIocs().equals(allIocs));
    	assertTrue(config2.getAvailableIocs().equals(allIocs));
    }
}
