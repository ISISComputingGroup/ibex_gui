package uk.ac.stfc.isis.ibex.jaxb;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

public class JAXB {
	
	private static final Logger LOG = IsisLog.getLogger(JAXB.class);
	
    public static <T> JAXBContext getJaxbContext(Class<T> clazz) throws JAXBException {
    	
//    	ClassLoader cl = JAXB.class.getClassLoader().loadClass("")
//    	
//    	return JAXBContextFactory.createContext(new Class[] { clazz }, Collections.emptyMap(), JAXB.class.getClassLoader());
//    	
    	// Nuclear approach - depending on where this gets called from
    	// we need different classloaders to be used. Try them all until
    	// one doesn't error. This is hacky but it's the best way I've found
    	// to get around JAXB's limitations so far.
    	List<ClassLoader> classloaders = List.of(
			clazz.getClassLoader(),
			JAXB.class.getClassLoader(),
			JAXBContextFactory.class.getClassLoader(),
			Thread.currentThread().getContextClassLoader(),
			ClassLoader.getPlatformClassLoader(),
			ClassLoader.getSystemClassLoader()
    	);
    	
    	Map<String, Throwable> errors = new HashMap<>();
    	
    	for (ClassLoader classloader : classloaders) {
    		LOG.info(String.format("Classloader %s loads JAXBContext from %s", classloader, classloader.getResource("javax/xml/bind/JAXBContext.class")));
    		try {
        		return JAXBContextFactory.createContext(new Class[] {clazz}, Collections.emptyMap(), classloader);
        	} catch (Exception | NoClassDefFoundError e) {
        		errors.put(String.format("used classloader %s", classloader), e);
        	}
    		
    		try {
        		return JAXBContextFactory.createContext(new Class[] {clazz}, null);
        	} catch (Exception | NoClassDefFoundError e) {
        		errors.put(String.format("used raw class moxy %s", clazz), e);
        	}
    		
    		try {
        		return JAXBContext.newInstance(clazz);
        	} catch (Exception | NoClassDefFoundError e) {
        		errors.put(String.format("used raw class %s", clazz), e);
        	}
    		
    		try {
        		return JAXBContext.newInstance(clazz.getPackageName(), classloader);
        	} catch (Exception | NoClassDefFoundError e) {
        		errors.put(String.format("used package name %s with classloader %s", clazz.getPackageName(), classloader), e);
        	}
    	}
    	
    	for (var entry : errors.entrySet()) {
    		LoggerUtils.logErrorWithStackTrace(LOG, 
    				String.format("%s, got exception: %s", entry.getKey(), entry.getValue().getMessage()), entry.getValue());
    		
    		if (entry.getValue().getCause() != null) {
    			LoggerUtils.logErrorWithStackTrace(LOG, "Caused by: " + entry.getValue().getCause().getMessage(), entry.getValue().getCause());
    		}
    	}
    	
    	throw new JAXBException("Failed to get a JAXBContext using any classloader");
    }
    
    public static <T> Unmarshaller getUnmarshaller(Class<T> clazz) throws JAXBException {
    	return getJaxbContext(clazz).createUnmarshaller();
    }
    
    public static <T> Marshaller getMarshaller(Class<T> clazz) throws JAXBException {
    	return getJaxbContext(clazz).createMarshaller();
    }
}
