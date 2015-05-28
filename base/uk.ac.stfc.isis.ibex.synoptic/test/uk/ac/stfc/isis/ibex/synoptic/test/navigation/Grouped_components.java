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
