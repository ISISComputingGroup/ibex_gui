package uk.ac.stfc.isis.ibex.synoptic.test.xml;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.XMLUtil;
import org.junit.Before;
import org.junit.Test;

public class Instrument_loaded_from_xml extends FileReadingTest {

	private InstrumentDescription instrument; 
	
	@Before
	public void setUp() throws Exception {
		instrument = XMLUtil.fromXml(fileContent());
	}

	@Override
	protected URL fileLocation() throws MalformedURLException {
		return getClass().getResource("/uk/ac/stfc/isis/ibex/synoptic/test/xml/example_instrument.xml");
	}
	
	@Test
	public void name_is_updated() {
		assertThat(instrument.name(), is("Larmor"));
	}

	@Test
	public void components_are_updated() {
		assertTrue(instrument.components().size() > 0);
	}

	@Test
	public void component_name_is_updated() {
		assertThat(firstComponent().name(), is("Slit 1"));
	}

	@Test
	public void component_type_is_updated() {
		assertThat(firstComponent().type(), is(ComponentType.JAWS));
	}
	
	@Test
	public void component_pvs_are_updated() {
		assertTrue(firstComponent().pvs().size() > 0);
	}	
	
	@Test
	public void pv_display_name_is_updated() {
		assertThat(firstComponent().pvs().get(0).displayName(), is("HGap"));
	}	

	@Test
	public void pv_address_is_updated() {
		assertThat(firstComponent().pvs().get(0).address(), is("$(P)$(JAWS)1:HGAP"));
	}	
	
	@Test
	public void pv_type_is_updated() {
		assertThat(firstComponent().pvs().get(0).recordType().io(), is(IO.READ));
	}	
	
	@Test
	public void multiple_components_can_be_read() {
		assertTrue(instrument.components().size() > 1);
	}
	
	@Test
	public void multiple_pvs_can_be_read() {
		assertTrue(instrument.components().get(1).pvs().size() > 1);
	}
	
	@Test
	public void component_can_have_sub_components() {
		assertFalse(firstComponent().components().isEmpty());
	}	

	@Test
	public void component_can_have_a_target() {
		assertThat(firstComponent().target().name(), is("Target 1"));
	}	

	@Test
	public void component_target_has_a_type() {
		assertThat(firstComponent().target().type(), is(TargetType.OPI));
	}	
	
	@Test
	public void component_target_can_have_properties() {
		assertFalse(firstComponent().target().properties().isEmpty());
	}
	
	@Test
	public void component_target_propety_is_a_key_value_pair() {
		Property property = firstComponent().target().properties().get(0);
		assertThat(property.key(), is("M"));
		assertThat(property.value(), is("1"));
	}
	
	private ComponentDescription firstComponent() {
		return instrument.components().get(0);
	}
	
}
