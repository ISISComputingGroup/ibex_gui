package uk.ac.stfc.isis.ibex.opis.tests.desc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.opis.DescriptionsProvider;
import uk.ac.stfc.isis.ibex.opis.desc.Descriptions;
import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;

@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename", "checkstyle:methodname" })
public class DescriptionsProviderTest {
	
    private String opiType1 = "Type1";
	private String opiName1 = "Test Opi 1";
	private String opiPath1 = "TestOpi1\\TestOpi1.opi";
	private String opiDescription1 = "This is test OPI 1";
	DescriptionsProvider descProvider;
	
	@Before
	public void setUp() throws JAXBException, SAXException {
		List<MacroInfo> macros = new ArrayList<MacroInfo>();
		
        OpiDescription opi1 = new OpiDescription(opiType1, opiPath1, opiDescription1, macros);
		//OpiDescription opi2 = new OpiDescription(opiPath2, opiDescription2, macros);

		Map<String, OpiDescription> opis = new HashMap<String, OpiDescription>();
		opis.put(opiName1, opi1);
		//opis.put(opi2.getPath(), opi2);
		
		Descriptions desc = new Descriptions();
		desc.setOpis(opis);
		
		descProvider = new DescriptionsProvider(desc);
	}
	
	@Test
	public void guess_name_from_path() {
		String guessedName = descProvider.guessOpiName(opiPath1);
		
		assertEquals(guessedName, opiName1);
	}
	
	@Test
	public void guess_name_from_path_backslash() {
		String guessedName = descProvider.guessOpiName(opiPath1.replace("\\", "/"));
		
		assertEquals(guessedName, opiName1);
	}
	
	@Test
	public void guess_name_from_path_case_insensitive() {
		String guessedName = descProvider.guessOpiName(opiPath1.toUpperCase());
		
		assertEquals(guessedName, opiName1);
	}
	
	@Test
	public void guess_name_valid_name() {
		String guessedName = descProvider.guessOpiName(opiName1);
		
		assertEquals(guessedName, opiName1);
	}
	
	@Test
	public void guess_name_invalid_name_return_empty() {
		String guessedName = descProvider.guessOpiName("Some junk");
		
		assertEquals(guessedName, "");
	}
	
	@Test
	public void get_opi_list() {
		assertTrue(descProvider.getOpiList().contains(opiName1));
	}
	
	@Test
	public void path_from_name_invalid_returns_null() {
		assertEquals(descProvider.pathFromName("junk"), null);
	}
}
