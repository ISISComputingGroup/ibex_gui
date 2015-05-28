package uk.ac.stfc.isis.ibex.synoptic.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

public final class XMLUtil {
	
	private static final SchemaFactory SF = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private static final URL SCHEMA_LOCATION = XMLUtil.class.getResource("/uk/ac/stfc/isis/ibex/synoptic/xml/instrument.xsd");
    private static Schema schema; 

    private static JAXBContext context;
    private static Unmarshaller unmarshaller;
    private static Marshaller marshaller;
    
    private XMLUtil() { }
    
	public static InstrumentDescription fromXml(String xml) throws JAXBException, SAXException {
		if (context == null) {
			initialise();
		}
	         
		return (InstrumentDescription) unmarshaller.unmarshal(new StringReader(xml));
	}
	
	public static String toXml(InstrumentDescription instrument) throws JAXBException, SAXException {
		if (context == null) {
			initialise();
		}
	    
		StringWriter writer = new StringWriter();
		marshaller.marshal(instrument, writer);
		
		return writer.toString();		
	}
	
	private static void initialise() throws JAXBException, SAXException {
		try {
			context = JAXBContext.newInstance(InstrumentDescription.class);
			schema = SF.newSchema(SCHEMA_LOCATION);
			
			marshaller = context.createMarshaller();
			marshaller.setSchema(schema);
			
			unmarshaller = context.createUnmarshaller();	
			unmarshaller.setSchema(schema);
		} catch (Exception e) {
			context = null;
			throw e;
		}
	}
}
