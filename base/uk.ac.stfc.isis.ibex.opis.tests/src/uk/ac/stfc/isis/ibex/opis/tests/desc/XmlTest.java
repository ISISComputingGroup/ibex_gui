package uk.ac.stfc.isis.ibex.opis.tests.desc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.opis.desc.Descriptions;
import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.opis.desc.XmlUtil;

@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename", "checkstyle:methodname" })
public class XmlTest {

	private String macroName1 = "M1";
	private String macroDescription1 = "This is test macro 1";

	private String macroName2 = "M2";
	private String macroDescription2 = "This is test macro 2";

    private String opiType1 = "Type1";
	private String opiPath1 = "TestOpi1.opi";
	private String opiDescription1 = "This is test OPI 1";

    private String opiType2 = "Type2";
	private String opiPath2 = "TestOpi.opi";
	private String opiDescription2 = "This is test OPI 2";

	private String emptyDescriptionsXml;
	private String fullDescriptionsXml;

	@Before
	public void setUp() throws JAXBException, SAXException {
		// Generate example XML
		List<MacroInfo> macros = new ArrayList<MacroInfo>();
		macros.add(new MacroInfo(macroName1, macroDescription1));
		macros.add(new MacroInfo(macroName2, macroDescription2));
		
        OpiDescription opi1 = new OpiDescription(opiType1, opiPath1, opiDescription1, macros);
        OpiDescription opi2 = new OpiDescription(opiType2, opiPath2, opiDescription2, macros);

		Map<String, OpiDescription> opis = new HashMap<String, OpiDescription>();
		opis.put(opi1.getPath(), opi1);
		opis.put(opi2.getPath(), opi2);

		Descriptions desc = new Descriptions();
		emptyDescriptionsXml = XmlUtil.toXml(desc);
		desc.setOpis(opis);

		fullDescriptionsXml = XmlUtil.toXml(desc);
	}

	@Test
	public void generate_full_description_from_xml() throws JAXBException, SAXException {
		Descriptions desc = XmlUtil.fromXml(fullDescriptionsXml);

		Map<String, OpiDescription> opis = desc.getOpis();
        assertEquals(opis.get(opiPath1).getType(), opiType1);
		assertEquals(opis.get(opiPath1).getPath(), opiPath1);
		assertEquals(opis.get(opiPath1).getDescription(), opiDescription1);

		List<MacroInfo> macros = opis.get(opiPath1).getMacros();
		assertEquals(macros.get(0).getName(), macroName1);
		assertEquals(macros.get(0).getDescription(), macroDescription1);
		assertEquals(macros.get(1).getName(), macroName2);
		assertEquals(macros.get(1).getDescription(), macroDescription2);

        assertEquals(opis.get(opiPath2).getType(), opiType2);
		assertEquals(opis.get(opiPath2).getPath(), opiPath2);
		assertEquals(opis.get(opiPath2).getDescription(), opiDescription2);
		macros = opis.get(opiPath2).getMacros();
		assertEquals(macros.get(0).getName(), macroName1);
		assertEquals(macros.get(0).getDescription(), macroDescription1);
		assertEquals(macros.get(1).getName(), macroName2);
		assertEquals(macros.get(1).getDescription(), macroDescription2);
	}

	@Test
	public void generate_empty_description_from_xml() throws JAXBException, SAXException {
		Descriptions desc = XmlUtil.fromXml(emptyDescriptionsXml);
		
		Map<String, OpiDescription> opis = desc.getOpis();
		assertEquals(opis.size(), 0);
	}
}
