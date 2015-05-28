package uk.ac.stfc.isis.ibex.synoptic.test.navigation;


import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

import uk.ac.stfc.isis.ibex.synoptic.model.Instrument;
import uk.ac.stfc.isis.ibex.synoptic.navigation.InstrumentNavigationGraph;
import org.junit.Before;
import org.junit.Test;

public class Instrument_with_single_component {
	
	private InstrumentNavigationGraph graph;
	
	@Before
	public void setup() {
		Instrument instrument = new InstrumentWithComponents(new ChildlessComponent("comonent"));
		graph = new InstrumentNavigationGraph(instrument);
	}
	
	@Test
	public void head_node_has_next() {
		assertThat(graph.head().next(), not(nullValue()));
	}
	
	@Test
	public void next_node_parent_is_head() {
		assertThat(graph.head().next().up(), is(graph.head()));
	}
	
}
