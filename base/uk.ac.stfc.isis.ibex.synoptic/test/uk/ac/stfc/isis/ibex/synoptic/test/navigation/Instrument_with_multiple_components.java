package uk.ac.stfc.isis.ibex.synoptic.test.navigation;


import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Instrument;
import uk.ac.stfc.isis.ibex.synoptic.navigation.InstrumentNavigationGraph;
import uk.ac.stfc.isis.ibex.synoptic.navigation.TargetNode;
import org.junit.Before;
import org.junit.Test;

public class Instrument_with_multiple_components {
	
	private InstrumentNavigationGraph graph;
	private Component firstComponent = new ChildlessComponent("first");
	private Component secondComponent = new ChildlessComponent("second");	
	
	@Before
	public void setup() {
		Instrument instrument = new InstrumentWithComponents(firstComponent, secondComponent);
		graph = new InstrumentNavigationGraph(instrument);
	}	
	
	@Test
	public void first_is_connected_to_second() {
		TargetNode first = graph.head().next();
		assertThat(first.next().item(), is(secondComponent.target()));
	}

	@Test
	public void second_is_connected_to_first() {
		TargetNode second = graph.head().next().next();
		assertThat(second.previous().item(), is(firstComponent.target()));
	}
	
	
}
