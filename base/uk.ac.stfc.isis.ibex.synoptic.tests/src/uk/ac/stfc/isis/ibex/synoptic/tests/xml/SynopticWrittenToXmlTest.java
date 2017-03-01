
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.synoptic.xml.XMLUtil;

@SuppressWarnings("checkstyle:methodname")
public class SynopticWrittenToXmlTest extends FileReadingTest {

    private SynopticDescription instrument;

    private String badSchema =
            "<schema xmlns=\"http://www.w3.org/2001/XMLSchema\"> </schema>";
	
	@Before
	public void setUp() throws Exception {
        instrument = XMLUtil.fromXml(fileContent(), SynopticDescription.class);
	}

	@Override
	protected URL fileLocation() throws MalformedURLException {
        return getClass().getResource("/uk/ac/stfc/isis/ibex/synoptic/tests/xml/example_synoptic.xml");
	}
	
    @Test
    public void checks_against_schema_when_passed_one() throws JAXBException, SAXException {
        try {
            XMLUtil.toXml(instrument, SynopticDescription.class, badSchema);
        } catch (MarshalException e) {
            assertThat(e.getCause(), instanceOf(SAXParseException.class));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void has_the_same_xml_content_when_unchanged() throws JAXBException, SAXException {

        String input = fileContent();
        input = input.replace("\n", "").replace("\t", "");

        String output = XMLUtil.toXml(instrument, SynopticDescription.class, null);
        assertThat(output, is(input));
    }
}
