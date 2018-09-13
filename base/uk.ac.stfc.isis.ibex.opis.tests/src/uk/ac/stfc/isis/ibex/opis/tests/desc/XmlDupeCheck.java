package uk.ac.stfc.isis.ibex.opis.tests.desc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.XMLUtil;
import uk.ac.stfc.isis.ibex.opis.OpiProvider;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;

public class XmlDupeCheck {
	
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class OpiEntry {
		public String key;
		public OpiDescription value;
	}
	
	@XmlRootElement(name = "descriptions")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class KeyList {
		@XmlElementWrapper(name = "opis")
		@XmlElement(name = "entry", type = OpiEntry.class)
		public List<OpiEntry> keys;
		
		public Set<String> getDuplicateKeys() {
			Set<String> uniques = new HashSet<>();
			return keys.stream()
					.map(entry -> entry.key)
					.filter(key -> !uniques.add(key))
					.collect(Collectors.toSet());
		}
		
	}
	
	@Test
	public void test_that_fails() throws IOException, JAXBException {
	   	
		Path path = new Path("../uk.ac.stfc.isis.ibex.opis/resources/opi_info.xml");
		assertNotNull("Path not found", path);
	    KeyList keyList = XMLUtil.fromXml(new FileReader(path.toOSString()), KeyList.class);
	    
	    Set<String> duplicates = keyList.getDuplicateKeys();
	    
	    StringBuilder builder = new StringBuilder();
	    builder.append("The following keys are duplicated in opi_info: ");

	    for (String a : duplicates) {
	    	builder.append(String.format("%s, ", a));
	    }
	    
	    assertEquals(builder.toString(), 0, duplicates.size());
	}

}
