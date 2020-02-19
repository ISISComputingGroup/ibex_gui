package uk.ac.stfc.isis.ibex.opis.tests.desc;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private List<String> opiCategories1 = Collections.<String>emptyList();

    private String opiName2 = "Test Opi 2";
    private String opiPath2 = "TestOpi2\\TestOpi2.opi";
    private String opiDescription2 = "This is test OPI 2";
    private List<String> opiCategories2 = Arrays.asList("Temperature controllers");
	
	DescriptionsProvider descProvider;
	
	@Before
	public void setUp() throws IOException, SAXException {
		List<MacroInfo> macros = new ArrayList<MacroInfo>();
		
        OpiDescription opi1 = new OpiDescription(opiType1, opiPath1, opiDescription1, macros, opiCategories1);
		OpiDescription opi2 = new OpiDescription(opiType1, opiPath2, opiDescription2, macros, opiCategories2);

		Map<String, OpiDescription> opis = new HashMap<String, OpiDescription>();
		opis.put(opiName1, opi1);
		opis.put(opiName2, opi2);
		
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
	
	@Test
	public void GIVEN_opi_is_in_multiple_categories_WHEN_get_categories_THEN_the_opi_is_in_all_of_its_categories_and_not_in_the_unknown_category() {
	    Map<String, List<String>> map = descProvider.getOpiCategories();
	    
	    for (String key : opiCategories2) {
	        assertTrue(map.containsKey(key));
	        assertTrue(map.get(key).contains(opiName2));
	    }
	    assertFalse(map.get(DescriptionsProvider.CATEGORY_UNKNOWN).contains(opiName2));
	}
	
   @Test
   public void GIVEN_two_uncategorised_opis_WHEN_get_categories_THEN_both_opis_are_in_the_unknown_category() {
        Map<String, List<String>> map = descProvider.getOpiCategories();
        
        assertTrue(map.containsKey(DescriptionsProvider.CATEGORY_UNKNOWN));
        assertTrue(map.get(DescriptionsProvider.CATEGORY_UNKNOWN).contains(opiName1));
        assertFalse(map.get(DescriptionsProvider.CATEGORY_UNKNOWN).contains(opiName2));
   }
   
   @Test
   public void WHEN_get_opi_categories_THEN_all_devices_category_contains_both_categorised_and_uncategorised_devices() {
       Map<String, List<String>> map = descProvider.getOpiCategories();
       
       assertTrue(map.containsKey(DescriptionsProvider.CATEGORY_ALL));
       assertTrue(map.get(DescriptionsProvider.CATEGORY_ALL).contains(opiName1));
       assertTrue(map.get(DescriptionsProvider.CATEGORY_ALL).contains(opiName2));
  }
}
