/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2023 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.configserver.tests.internal;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro.HasDefault;
import uk.ac.stfc.isis.ibex.configserver.configuration.PVDefaultValue;
import uk.ac.stfc.isis.ibex.configserver.configuration.PVSet;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.ImportConverter;

public class ImportConverterTest {
	
	private final static String SOURCE_PREFIX = "SOURCE";
	private final static String DESTINATION_PREFIX = "DESTINATION";
	private final static String COMPONENT_NAME = "COMP";
	private final static String COMPONENT_DESC = "DESC";
	private final static String IOC_NAME = "IOC_01";
	private final static boolean IOC_AUTOSTART = true;
	private final static boolean IOC_RESTART = true;
	
	private final static Macro MACRO = new Macro("MACRO", "10", "DESC", "pattern", "1", HasDefault.YES);
	private final static PVDefaultValue PV = new PVDefaultValue(SOURCE_PREFIX.concat(":PV"), "10");
	private final static PVSet PV_SET = new PVSet(SOURCE_PREFIX.concat(":PV_SET"), true);
	private final static Block BLOCK = new Block("BLOCK", SOURCE_PREFIX.concat(":PV"), true, true);
	private final static Group GROUP = new Group("GROUP", Arrays.asList("BLOCK"), null);
	
	private static Collection<EditableIoc> editableIocs;
	private static Configuration source;
	private static Configuration empty;
	private static EditableConfiguration destination;
	
	
	@BeforeClass
	public static void setUp() {
		editableIocs = new ArrayList<EditableIoc>();
		EditableIoc editableIoc = new EditableIoc(IOC_NAME);
		editableIoc.setAutostart(IOC_AUTOSTART);
		editableIoc.setRestart(IOC_RESTART);
		editableIoc.setMacros(Arrays.asList(MACRO));
		editableIoc.setPvs(Arrays.asList(PV));
		editableIoc.setPvSets(Arrays.asList(PV_SET));
		editableIocs.add(editableIoc);
		
		source = new Configuration(COMPONENT_NAME, COMPONENT_DESC, null, Arrays.asList(new Ioc(editableIoc)), Arrays.asList(BLOCK),
				 Arrays.asList(GROUP), Collections.emptyList(), Collections.emptyList(), false, false, false);
		empty = new Configuration("EMPTY", "EMTPY DESC", null, Collections.emptyList(), Collections.emptyList(),
				Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), false, false, false);
		destination = new EditableConfiguration(empty, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

		ImportConverter.convert(source, destination, SOURCE_PREFIX, DESTINATION_PREFIX, editableIocs);
	}
	
	@Test
	public void GIVEN_ioc_WHEN_component_converted_THEN_ioc_values_converted_correctly() {
		var ioc = destination.getAddedIocs().stream().findFirst().get();
		assertTrue(ioc.getName().equals(IOC_NAME));
		assertTrue(ioc.getAutostart() == IOC_AUTOSTART);
		assertTrue(ioc.getRestart() == IOC_RESTART);
		assertTrue(ioc.getMacros().contains(MACRO));
		
		var pv = ioc.getPvs().stream().findFirst().get();
		assertTrue(pv.getName().equals(DESTINATION_PREFIX.concat(":PV")));
		assertTrue(pv.getValue().equals(PV.getValue()));
		
		var pvSet = ioc.getPvSets().stream().findFirst().get();
		assertTrue(pvSet.getName().equals(DESTINATION_PREFIX.concat(":PV_SET")));
		assertTrue(pvSet.getEnabled() == PV_SET.getEnabled());
	}
	
	@Test
	public void GIVEN_block_WHEN_component_converted_THEN_block_values_converted_correctly() {
		var block = destination.getAllBlocks().stream().findFirst().get();
		assertTrue(block.getName().equals(BLOCK.getName()));
		assertTrue(block.getPV().equals(DESTINATION_PREFIX.concat(":PV")));
		assertTrue(block.getIsLocal());
		assertTrue(block.getIsVisible());
	}
	
	@Test
	public void GIVEN_group_with_block_WHEN_component_converted_THEN_group_values_converted_correctly() {
		var group = destination.getEditableGroups().stream().findFirst().get();
		assertTrue(group.getName().equals(GROUP.getName()));
		assertTrue(group.getBlocks().stream().findFirst().get().equals(BLOCK.getName()));

		var block = group.getSelectedBlocks().stream().findFirst().get();
		assertTrue(block.getName().equals(block.getName()));
		assertTrue(block.getPV().equals(DESTINATION_PREFIX.concat(":PV")));
	}
}
