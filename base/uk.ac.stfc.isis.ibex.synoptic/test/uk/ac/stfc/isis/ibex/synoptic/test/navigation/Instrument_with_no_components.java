package uk.ac.stfc.isis.ibex.synoptic.test.navigation;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Instrument;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.synoptic.navigation.InstrumentNavigationGraph;
import org.junit.Before;
import org.junit.Test;

public class Instrument_with_no_components {
	
	private InstrumentNavigationGraph graph;
	
	@Before
	public void setup() {
		Instrument instrument = new NoComponentsInstrument();
		graph = new InstrumentNavigationGraph(instrument);
	}
	
	@Test
	public void graph_is_an_unconnected_node() {
		assertThat(graph.head().previous(), is(nullValue()));
		assertThat(graph.head().up(), is(nullValue()));
		assertThat(graph.head().next(), is(nullValue()));
		assertThat(graph.head().down(), is(nullValue()));
	}
	
	private class NoComponentsInstrument implements Instrument {

		@Override
		public String name() {
			return "Empty";
		}

		@Override
		public List<? extends Component> components() {
			return new ArrayList<>();
		}

		@Override
		public void close() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public InstrumentDescription getDescription() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
