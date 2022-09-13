package uk.ac.stfc.isis.ibex.jaxb;

import java.io.IOException;
import java.util.Collections;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.JAXBContextFactory;

/**
 * Utility methods to easily acquire JAXB marshallers and unmarshallers.
 */
public final class JAXB {
	
	private JAXB() {
		// prevents calls from subclass
	}
	
	static {
		System.setProperty("javax.xml.bind.context.factory", JAXBContextFactory.class.getName());
	}
	
    private static <T> JAXBContext getJaxbContext(Class<T> clazz) throws JAXBException {
    	try {
    		// For running the full GUI
    	    return JAXBContextFactory.createContext(new Class[] {clazz}, Collections.emptyMap(), JAXBContextFactory.class.getClassLoader());
    	} catch (Exception | LinkageError e) {
    		// For unit tests
    		return JAXBContext.newInstance(clazz.getPackageName(), clazz.getClassLoader());
    	}
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
