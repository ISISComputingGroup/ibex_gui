
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

package uk.ac.stfc.isis.ibex.synoptic.tests.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.devicescreens.desc.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;
import uk.ac.stfc.isis.ibex.synoptic.xml.XMLUtil;

@SuppressWarnings("checkstyle:methodname")
public class SynopticLoadedFromXmlTest extends FileReadingTest {

    private SynopticDescription instrument;
	
	@Before
	public void setUp() throws Exception {
		instrument = XMLUtil.fromXml(fileContent());
	}

	@Override
	protected URL fileLocation() throws MalformedURLException {
        return getClass().getResource("/uk/ac/stfc/isis/ibex/synoptic/tests/xml/example_synoptic.xml");
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
		assertFalse(firstComponent().target().getProperties().isEmpty());
	}
	
	@Test
	public void component_target_propety_is_a_key_value_pair() {
		Property property = firstComponent().target().getProperties().get(0);
		assertThat(property.key(), is("M"));
		assertThat(property.value(), is("1"));
	}
	
	private ComponentDescription firstComponent() {
		return instrument.components().get(0);
	}
	
}
