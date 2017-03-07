 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
 * All rights reserved.
 *
 * This program is distributed in the hope that it will be useful.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution.
 * EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
 * AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
 * OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
 *
 * You should have received a copy of the Eclipse Public License v1.0
 * along with this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or 
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.configserver.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.ui.configserver.DeleteComponentsViewModel;

/**
 *
 */
@SuppressWarnings("checkstyle:methodname")
public class DeleteComponentsViewModelTest {

    private DeleteComponentsViewModel viewModel;
    private Map<String, Collection<String>> dependencies;

    private final static String INDEPENDENT_COMP_1 = "INDEPENDENT_COMP_1";
    private final static String INDEPENDENT_COMP_2 = "INDEPENDENT_COMP_2";
    private final static String INDEPENDENT_COMP_3 = "INDEPENDENT_COMP_3";
    private final static String DEPENDENT_COMP_1 = "DEPENDENT_COMP_1";
    private final static String DEPENDENT_COMP_2 = "DEPENDENT_COMP_2";
    private final static List<String> CONFIGS_EMPTY = new ArrayList<>();
    private final static List<String> CONFIGS_1 = Arrays.asList("CONFIG1");
    private final static List<String> CONFIGS_2 = Arrays.asList("CONFIG2", "CONFIG3");

    private static final String BULLET_POINT = " \u2022";

    @Before
    public void setUp() {
        dependencies = new HashMap<>();
        dependencies.put(INDEPENDENT_COMP_1, CONFIGS_EMPTY);
        dependencies.put(INDEPENDENT_COMP_2, CONFIGS_EMPTY);
        dependencies.put(INDEPENDENT_COMP_3, CONFIGS_EMPTY);
        dependencies.put(DEPENDENT_COMP_1, CONFIGS_1);
        dependencies.put(DEPENDENT_COMP_2, CONFIGS_2);

        viewModel = new DeleteComponentsViewModel(dependencies);
    }

    @Test
    public void GIVEN_viewmodel_initialised_with_dependency_map_THEN_map_stored_in_viewmodel_with_empty_filtered() {
        // Arrange
        Map<String, Collection<String>> expected = new HashMap<>();
        expected.put(DEPENDENT_COMP_1, CONFIGS_1);
        expected.put(DEPENDENT_COMP_2, CONFIGS_2);

        // Act
        Map<String, Collection<String>> actual = viewModel.getDependencies();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_only_independent_components_selected_WHEN_querying_dependencies_THEN_dependencies_are_empty() {
        // Act
        List<String> toDelete = Arrays.asList(INDEPENDENT_COMP_1, INDEPENDENT_COMP_2, INDEPENDENT_COMP_3);
        Map<String, Collection<String>> actual = viewModel.filterSelected(toDelete);

        // Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    public void GIVEN_only_dependent_components_selected_WHEN_querying_dependencies_THEN_dependencies_contain_those_components() {
        // Arrange
        Map<String, Collection<String>> expected = new HashMap<>();
        expected.put(DEPENDENT_COMP_1, CONFIGS_1);
        expected.put(DEPENDENT_COMP_2, CONFIGS_2);

        // Act
        List<String> toDelete = Arrays.asList(DEPENDENT_COMP_1, DEPENDENT_COMP_2);
        Map<String, Collection<String>> actual = viewModel.filterSelected(toDelete);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_some_dependent_components_selected_WHEN_querying_dependencies_THEN_dependencies_contain_only_those_components() {
        // Arrange
        Map<String, Collection<String>> expected = new HashMap<>();
        expected.put(DEPENDENT_COMP_1, CONFIGS_1);

        // Act
        List<String> toDelete = Arrays.asList(INDEPENDENT_COMP_1, DEPENDENT_COMP_1, INDEPENDENT_COMP_2);
        Map<String, Collection<String>> actual = viewModel.filterSelected(toDelete);

        // Assert
        assertEquals(expected, actual);
    }
    
    @Test
    public void
            GIVEN_one_component_has_dependencies_WHEN_generating_warning_message_THEN_message_contains_those_dependencies_and_is_singular() {
        StringBuilder expected = new StringBuilder();
        expected.append("The following component is currently in use and so cannot be deleted:\n");
        expected.append("\n");
        expected.append("Component \"" + DEPENDENT_COMP_1 + "\" used in configuration(s):\n");
        expected.append(BULLET_POINT + " " + CONFIGS_1.get(0) + "\n");
        expected.append("\n");
        expected.append("Please remove the component from these configurations before deleting.");
        
        // Act
        List<String> toDelete = Arrays.asList(DEPENDENT_COMP_1);
        Map<String, Collection<String>> selectedDependencies = viewModel.filterSelected(toDelete);
        String actual = viewModel.buildWarning(selectedDependencies);

        // Assert
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void GIVEN_multiple_components_have_dependencies_WHEN_generating_warning_message_THEN_message_contains_those_dependencies_and_is_plural() {
        StringBuilder expected = new StringBuilder();
        expected.append("The following components are currently in use and so cannot be deleted:\n");
        expected.append("\n");
        expected.append("Component \"" + DEPENDENT_COMP_1 + "\" used in configuration(s):\n");
        expected.append(BULLET_POINT + " " + CONFIGS_1.get(0) + "\n");
        expected.append("\n");
        expected.append("Component \"" + DEPENDENT_COMP_2 + "\" used in configuration(s):\n");
        expected.append(BULLET_POINT + " " + CONFIGS_2.get(0) + "\n");
        expected.append(BULLET_POINT + " " + CONFIGS_2.get(1) + "\n");
        expected.append("\n");
        expected.append("Please remove the components from these configurations before deleting.");

        // Act
        List<String> toDelete = Arrays.asList(DEPENDENT_COMP_1, DEPENDENT_COMP_2);
        Map<String, Collection<String>> selectedDependencies = viewModel.filterSelected(toDelete);
        String actual = viewModel.buildWarning(selectedDependencies);

        // Assert
        assertEquals(expected.toString(), actual);
    }
}
