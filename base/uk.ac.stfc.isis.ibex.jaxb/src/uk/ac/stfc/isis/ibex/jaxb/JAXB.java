package uk.ac.stfc.isis.ibex.jaxb;

import java.io.IOException;
import java.util.Collections;

import org.glassfish.jaxb.runtime.v2.JAXBContextFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

/**
 * Utility methods to easily acquire JAXB marshallers and unmarshallers.
 */
public final class JAXB {
	
	private JAXB() {
		// prevents calls from subclass
	}
	
	static {
		System.setProperty("jakarta.xml.bind.context.factory", JAXBContextFactory.class.getName());
	}
	
    private static <T> JAXBContext getJaxbContext(Class<T> clazz) throws JAXBException {
		var factory = new JAXBContextFactory();
		return factory.createContext(new Class[] {clazz}, Collections.emptyMap());
    }
    
    /**
     * Get an XML unmarshaller.
     * @param <T> - the type to get an unmarshaller for
     * @param clazz - the class to get an unmarshaller for
     * @return the unmarshaller
     * @throws IOException if an error occured
     */
    public static <T> Unmarshaller getUnmarshaller(Class<T> clazz) throws IOException {
    	try {
    		return getJaxbContext(clazz).createUnmarshaller();
    	} catch (JAXBException e) {
    		throw new IOException(e.getMessage(), e);
    	}
    }
    
    /**
     * Get an XML marshaller.
     * @param <T> - the type to get a marshaller for
     * @param clazz - the class to get a marshaller for
     * @return the marshaller
     * @throws IOException if an error occured
     */
    public static <T> Marshaller getMarshaller(Class<T> clazz) throws IOException {
    	try {
    		return getJaxbContext(clazz).createMarshaller();
    	} catch (JAXBException e) {
    		throw new IOException(e.getMessage(), e);
    	}
    }
}
