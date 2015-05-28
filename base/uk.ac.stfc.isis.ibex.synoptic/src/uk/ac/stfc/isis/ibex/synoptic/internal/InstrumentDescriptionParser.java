package uk.ac.stfc.isis.ibex.synoptic.internal;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.synoptic.xml.XMLUtil;

public class InstrumentDescriptionParser extends
		Converter<String, InstrumentDescription> {

	@Override
	public InstrumentDescription convert(String value)
			throws ConversionException {
		try {
			return XMLUtil.fromXml(value);
		} catch (JAXBException | SAXException e) {
			throw new ConversionException("Error parsing synoptic", e);
		}		
	}
}
