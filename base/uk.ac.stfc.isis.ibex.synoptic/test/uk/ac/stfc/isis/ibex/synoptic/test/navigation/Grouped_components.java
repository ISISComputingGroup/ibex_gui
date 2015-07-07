
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

package uk.ac.stfc.isis.ibex.synoptic.test.navigation;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Instrument;
import uk.ac.stfc.isis.ibex.synoptic.navigation.InstrumentNavigationGraph;
import uk.ac.stfc.isis.ibex.synoptic.navigation.TargetNode;
import org.junit.Before;
import org.junit.Test;

public class Grouped_components {
	
	private InstrumentNavigationGraph graph;

	private Component secondSubComponent = new ChildlessComponent("secondSub");	
	private Component firstSubComponent = new ChildlessComponent("firstSub");	
	private Component firstComponent = new ParentComponent("first", firstSubComponent, secondSubComponent);
	
	@Before
	public void setup() {
		Instrument instrument = new InstrumentWithComponents(firstComponent);
		graph = new InstrumentNavigationGraph(instrument);
	}	
	
	@Test
	public void group_subcomponents_can_drill_down() {
		TargetNode sub = graph.head().next().down();
		assertThat(sub.item(), is(firstSubComponent.target()));
	}
	
	@Test
	public void subcomponents_are_connected() {
		TargetNode first = graph.head().next().down();
		assertThat(first.next().item(), is(secondSubComponent.target()));
	}
}
