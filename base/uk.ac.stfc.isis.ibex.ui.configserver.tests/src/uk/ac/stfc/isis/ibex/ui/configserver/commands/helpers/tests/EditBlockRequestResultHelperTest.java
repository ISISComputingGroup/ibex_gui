package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.EditBlockRequestResult;

/**
*
*/
public class EditBlockRequestResultHelperTest {

    private EditableConfiguration mockConfig() {
        return mock(EditableConfiguration.class);
   }

   @Test
    public void WHEN_new_resulted_created_THEN_has_error_and_no_config() {
       // Arrange
        EditBlockRequestResult result = new EditBlockRequestResult();

       // Assert
        assertFalse(result.hasConfig());
        assertNotNull(result.getError());
   }

   @Test
    public void GIVEN_new_result_WHEN_error_is_changed_THEN_result_still_has_no_config() {
       // Arrange
        EditBlockRequestResult result = new EditBlockRequestResult();
        assertFalse(result.hasConfig());

        // Act
        result.setError("This is an error");

       // Assert
        assertFalse(result.hasConfig());
   }

   @Test
    public void GIVEN_result_with_no_error_WHEN_error_is_set_THEN_error_is_updated() {
       // Arrange
        EditBlockRequestResult result = new EditBlockRequestResult();

       // Act
        final String expectedError = "This is a new error";
        result.setError(expectedError);

       // Assert
        assertEquals(expectedError, result.getError());
   }

   @Test
    public void GIVEN_result_with_config_WHEN_error_is_set_THEN_result_has_no_config() {
       // Arrange
        EditBlockRequestResult result = new EditBlockRequestResult();
        result.setConfig(mockConfig(), false);
        assertTrue(result.hasConfig());

        // Act
        result.setError("This is a new error");

        // Assert
        assertFalse(result.hasConfig());
    }

    @Test
    public void GIVEN_result_with_config_WHEN_error_is_set_THEN_result_has_error() {
        // Arrange
        EditBlockRequestResult result = new EditBlockRequestResult();
        result.setConfig(mockConfig(), false);
        assertTrue(result.hasConfig());

        // Act
        result.setError("This is a new error");

        // Assert
        assertTrue(result.hasError());
   }

    @Test
    public void GIVEN_new_result_WHEN_config_is_set_as_not_component_THEN_result_reports_not_a_component() {
        // Arrange
        EditBlockRequestResult result = new EditBlockRequestResult();
        result.setConfig(mockConfig(), false);

        // Assert
        assertFalse(result.isComponent());
    }

    @Test
    public void GIVEN_new_result_WHEN_config_is_set_as_component_THEN_result_reports_is_component() {
        // Arrange
        EditBlockRequestResult result = new EditBlockRequestResult();
        result.setConfig(mockConfig(), true);

        // Assert
        assertTrue(result.isComponent());
    }

    @Test
    public void GIVEN_result_in_error_WHEN_config_is_set_THEN_has_no_error() {
        // Arrange
        EditBlockRequestResult result = new EditBlockRequestResult();
        result.setError("Has error");
        assertTrue(result.hasError());

        // Act
        result.setConfig(mockConfig(), true);

        // Assert
        assertFalse(result.hasError());
    }

    @Test
    public void GIVEN_result_with_config_WHEN_config_is_requested_THEN_correct_config_provided() {
        // Arrange
        EditBlockRequestResult result = new EditBlockRequestResult();

        // Act
        EditableConfiguration expectedConfig = mockConfig();
        result.setConfig(expectedConfig, true);

        // Assert
        assertEquals(expectedConfig, result.getConfig());
    }
}
