
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;

import com.google.common.collect.Iterables;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Component;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.IocDescriber;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class EditableConfigurationTest implements IocDescriber {

	private static final String NAME = "base";
	private static final String DESCRIPTION = "description";
	private static final String SYNOPTIC = "";
	
	protected static final EditableIoc GALIL01 = new EditableIoc("GALIL_01");
    protected static final Block GAPX = new Block("GAPX", "ADDRESS", true, true, null, null, null, false);
    protected static final Block GAPY = new Block("GAPY", "ADDRESS", true, true, null, null, null, false);
	protected static final Group JAWS = new Group("JAWS");
	protected static final Group TEMPERATURE = new Group("TEMPERATURE");
	protected static final Component MOTOR = new Component("MOTOR");

	protected List<Ioc> iocs;
	protected List<Block> blocks;
	protected List<Group> groups;
	protected List<Component> components;
	protected List<String> history;
	
	protected List<EditableIoc> allIocs;
	protected List<Component> allComponents;
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
		allComponents.add(MOTOR);
		
		allPvs = new ArrayList<>();
		activePvs = new ArrayList<>();		
	}
	
	public static void assertAreEqual(Configuration expected, Configuration actual) {
		assertEquals("Iocs", expected.getIocs(), actual.getIocs());
		assertBlocksAreEqual(expected.getBlocks(), actual.getBlocks());
		assertEquals("Groups", expected.getGroups(), actual.getGroups());
		assertEquals("Components", expected.getComponents(), actual.getComponents());
	}
	
	public static void assertBlocksAreEqual(Collection<Block> expected, Collection<Block> actual) {
		assertEquals(expected.size(), actual.size());
		
		Iterator<Block> itrExp = expected.iterator();
		Iterator<Block> itrAct = actual.iterator();
		
	    while(itrExp.hasNext()) {
	    	Block exp = itrExp.next();
	    	Block act = itrAct.next();
	    	
	    	assertEquals(exp.getName(), act.getName());
	    	assertEquals(exp.getPV(), act.getPV());
	    	assertEquals(exp.getIsLocal(), act.getIsLocal());
	    	assertEquals(exp.getIsVisible(), act.getIsVisible());
            assertEquals(exp.getRCLowLimit(), act.getRCLowLimit());
            assertEquals(exp.getRCHighLimit(), act.getRCHighLimit());
            assertEquals(exp.getRCEnabled(), act.getRCEnabled());
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
				Collections.<Component>emptyList(),
				Collections.<String>emptyList());
	}
	
	@Override
	public UpdatedValue<String> getDescription(String iocName) {
		return new SettableUpdatedValue<String>("description");
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
		return new EditableConfiguration(config, allIocs, allComponents, allPvs, this);
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
}
