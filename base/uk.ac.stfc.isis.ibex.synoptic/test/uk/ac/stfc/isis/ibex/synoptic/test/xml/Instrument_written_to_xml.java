
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

package uk.ac.stfc.isis.ibex.synoptic.test.xml;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.XMLUtil;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class Instrument_written_to_xml extends FileReadingTest {

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
	public void has_the_same_xml_content_when_unchanged() throws JAXBException, SAXException {
		
		String input = fileContent();
		input = input.replace("\n", "").replace("\t", "");
		
		String output = XMLUtil.toXml(instrument);
		assertThat(output, is(input));
	}
}
