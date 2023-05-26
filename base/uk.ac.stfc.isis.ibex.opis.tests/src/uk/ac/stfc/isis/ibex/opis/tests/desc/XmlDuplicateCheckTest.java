package uk.ac.stfc.isis.ibex.opis.tests.desc;

import static org.junit.Assert.assertEquals;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.runtime.Path;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.XMLUtil;

/**
 * Here we don't use the default Xml parser class in Descriptions.java as it uses
 * a map and we need to check all keys in the OPI list. 
 *
 */
public class XmlDuplicateCheck {
	
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class OpiEntry {
		public String key;
	}
	
	@XmlRootElement(name = "descriptions")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class KeyList {
		@XmlElementWrapper(name = "opis")
		@XmlElement(name = "entry", type = OpiEntry.class)
		public List<OpiEntry> keys;
		
		public Stream<String> getDuplicateKeys() {
			Set<String> uniques = new HashSet<>();
			return keys.stream()
					.map(entry -> entry.key)
					.filter(key -> !uniques.add(key));
		}	
	}
	
	@Test
	public void test_that_opi_info_file_does_not_contain_any_duplicate_keys() throws IOException, JAXBException {
		Path path = new Path("../uk.ac.stfc.isis.ibex.opis/resources/opi_info.xml");
	    KeyList keyList = XMLUtil.fromXml(new FileReader(path.toOSString()), KeyList.class);
	    
	    Set<String> duplicateKeys = keyList.getDuplicateKeys().collect(Collectors.toSet());
	    
		String error = String.format("The following keys are duplicated in opi_info: %s", 
				String.join(", ", duplicateKeys));
	    
	    assertEquals(error, 0, duplicateKeys.size());
	}

}
