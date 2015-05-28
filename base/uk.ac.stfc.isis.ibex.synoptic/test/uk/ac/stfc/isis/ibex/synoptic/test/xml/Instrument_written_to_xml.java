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
