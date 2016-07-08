
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

package uk.ac.stfc.isis.ibex.synoptic.tests.navigation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.devicescreens.desc.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.synoptic.navigation.InstrumentNavigationGraph;

@SuppressWarnings("checkstyle:methodname")
public class InstrumentWithNoComponentsTest {
	
	private InstrumentNavigationGraph graph;
	
	@Before
	public void setup() {
        Synoptic instrument = new NoComponentsInstrument();
		graph = new InstrumentNavigationGraph(instrument);
	}
	
	@Test
	public void graph_is_an_unconnected_node() {
		assertThat(graph.head().previous(), is(nullValue()));
		assertThat(graph.head().up(), is(nullValue()));
		assertThat(graph.head().next(), is(nullValue()));
		assertThat(graph.head().down(), is(nullValue()));
	}
	
    private class NoComponentsInstrument implements Synoptic {

		@Override
		public String name() {
			return "Empty";
		}

		@Override
		public List<? extends Component> components() {
			return new ArrayList<>();
		}

		@Override
        public SynopticDescription getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

        @Override
        public Boolean showBeam() {
            return null;
        }
	}
}
