package uk.ac.stfc.isis.ibex.configserver.tests.server;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;

public class Addresses {

	@Test
	public void are_colon_separated_by_default() {
		assertThat(PVAddress.startWith("IN").append("LARMOR").toString(), is("IN:LARMOR"));
	}
	
	@Test
	public void allow_fields_to_be_added(){
		assertThat(PVAddress.startWith("IN").append("LARMOR").field("VAL").toString(), is("IN:LARMOR.VAL"));
	}
	
	@Test
	public void are_immutable() {
		PVAddress address = PVAddress.startWith("IN");
		address.append("ALF");
		assertThat(address.append("LARMOR").toString(), is("IN:LARMOR"));
	}
}
