package uk.ac.stfc.isis.ibex.opis.tests.desc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;

@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename", "checkstyle:methodname" })
public class OpiDescriptionTest {

	@Before
    public void setUp() {
	}

	@Test
    public void GIVEN_no_macros_WHEN_get_desciption_by_key_THEN_default_answer_returned() {
        OpiDescription opiDescription = new OpiDescription("", "", "", new ArrayList<MacroInfo>());

        String result = opiDescription.getMacroDescription("name");

        assertEquals("", result);
	}

    @Test
    public void
            GIVEN_macros_with_description_WHEN_get_desciption_by_key_THEN_description_is_returned() {
        ArrayList<MacroInfo> macros = new ArrayList<MacroInfo>();
        String expectedDescription = "desc";
        String expectedName = "name";
        macros.add(new MacroInfo(expectedName, expectedDescription));
        OpiDescription opiDescription = new OpiDescription("", "", "", macros);

        String result = opiDescription.getMacroDescription(expectedName);

        assertEquals(expectedDescription, result);
    }

}
