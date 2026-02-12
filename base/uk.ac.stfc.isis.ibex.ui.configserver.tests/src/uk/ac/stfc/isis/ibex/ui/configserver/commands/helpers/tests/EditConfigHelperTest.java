package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.tests;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.configserver.configuration.GlobalMacro;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.EditConfigHelper;
@RunWith(org.mockito.junit.MockitoJUnitRunner.StrictStubs.class)
public class EditConfigHelperTest {

    EditableConfiguration config;

    @Before
    public void setUp() {
        config = Mockito.mock(EditableConfiguration.class);
        when(config.getName()).thenReturn("current");
    }

    @Test
    public void testOpenDialogs_WhenNoGlobalMacros_CheckSubtitle() throws IOException {
        // Arrange
        when(config.getGlobalmacros()).thenReturn(null);
        String expectedSubTitle = "Editing the current configuration";
        // Assert
        assertEquals(expectedSubTitle, EditConfigHelper.createSubTitle(config, config.getName())); 
    }
    
    @Test
    public void testOpenDialogs_WhenGlobalMacrosExist_CheckSubtitle() throws IOException {
        // Arrange
    	List<GlobalMacro> globalMacros = List.of(new GlobalMacro("macro1"), new GlobalMacro("macro2"));
        when(config.getGlobalmacros()).thenReturn(globalMacros);
    	String expectedSubTitle = "Editing the current configuration\nNote: There also are global macros defined. "
    			+ "See the Global Macros tab.\nThey over-ride IOC level macro"; 

        // Assert
        assertEquals(expectedSubTitle, EditConfigHelper.createSubTitle(config, config.getName())); 
    }

    @Test
    public void testOpenDialogs_WhenEMptyGlobalMacros_CheckSubtitle() throws IOException {
        // Arrange
        when(config.getGlobalmacros()).thenReturn(new ArrayList<GlobalMacro>());
        String expectedSubTitle = "Editing the current configuration";
        // Assert
        assertEquals(expectedSubTitle, EditConfigHelper.createSubTitle(config, config.getName())); 
    }
}
