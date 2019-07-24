 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2019 Science & Technology Facilities Council.
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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.components.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.editing.BlockDuplicateChecker;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateChecker;
import uk.ac.stfc.isis.ibex.configserver.editing.IocDuplicateChecker;

/**
 * Class for testing the configuration duplicate checker. Since DuplicateChecker works with
 * generic types, tests for blocks and for IOCs are pretty much the same
 */
public class DuplicateCheckerTest {

    private DuplicateChecker<Block> blockDuplicateChecker;
    private DuplicateChecker<Ioc> iocDuplicateChecker;
    private Configuration baseConfig;
    private Collection<Configuration> allComps;
    private Collection<Block> configBlocks;
    private Collection<ComponentInfo> configComps;
    private Collection<Ioc> configIocs;

    private final static String BLOCK_NAME_1 = "BLOCK_1";
    private final static String BLOCK_NAME_2 = "BLOCK_2";
    private final static String IOC_NAME_1 = "IOC_1";
    private final static String IOC_NAME_2 = "IOC_2";

    private final static String BASE_CONFIG_NAME = "BASE";
    private final static String COMP_NAME_1 = "COMPONENT_1";
    private final static String COMP_NAME_2 = "COMPONENT_2";
    private final static String COMP_TOADD_NAME_1 = "TEST_COMPONENT_1";
    private final static String COMP_TOADD_NAME_2 = "TEST_COMPONENT_2";

    private Block block1 = mockBlock(BLOCK_NAME_1, "");
    private Block block2 = mockBlock(BLOCK_NAME_2, "");
    
    private Ioc ioc1 = mockIoc(IOC_NAME_1, "");
    private Ioc ioc2 = mockIoc(IOC_NAME_2, "");

    private Block mockBlock(String name, String component) {
        Block block = mock(Block.class);
        when(block.getName()).thenReturn(name);
        when(block.getComponent()).thenReturn(component);
        when(block.inComponent()).thenReturn(component.isEmpty() ? false : component.length() > 0);
        return block;
    }

    private Configuration mockComp(String name, Collection<Block> blocks, Collection<Ioc> iocs) {
        Configuration comp = mock(Configuration.class);
        when(comp.getName()).thenReturn(name);
        when(comp.getBlocks()).thenReturn(blocks);
        when(comp.getIocs()).thenReturn(iocs);
        return comp;
    }
    
    private Ioc mockIoc(String name, String component) {
        Ioc ioc = mock(Ioc.class);
        when(ioc.getName()).thenReturn(name);
        when(ioc.getComponent()).thenReturn(component);
        when(ioc.inComponent()).thenReturn(component.isEmpty() ? false : component.length() > 0);
        return ioc;
    }

    private ComponentInfo mockCompInfo(String name) {
        ComponentInfo compInfo = mock(ComponentInfo.class);
        when(compInfo.getName()).thenReturn(name);
        return compInfo;
    }

    private Configuration mockConfig(String name, Collection<Block> blocks, Collection<ComponentInfo> comps, Collection<Ioc> iocs) {
        Configuration comp = mock(Configuration.class);
        when(comp.getName()).thenReturn(name);
        when(comp.getBlocks()).thenReturn(blocks);
        when(comp.getComponents()).thenReturn(comps);
        when(comp.getIocs()).thenReturn(iocs);
        return comp;
    }

    @Before
    public void setUp() {
        configBlocks = new ArrayList<Block>();
        configComps = new ArrayList<ComponentInfo>();
        configIocs = new ArrayList<Ioc>();
        allComps = new ArrayList<Configuration>();

        blockDuplicateChecker = new BlockDuplicateChecker();
        iocDuplicateChecker = new IocDuplicateChecker();
    }

    @Test
    public void GIVEN_config_blank_WHEN_checking_config_THEN_no_conflicts_returned() {
        // Arrange
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);
        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnLoad();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void GIVEN_config_with_unique_native_blocks_WHEN_checking_config_THEN_no_conflicts_returned() {
        // Arrange
        configBlocks = Arrays.asList(block1, block2);
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnLoad();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void GIVEN_config_with_duplicate_block_in_component_WHEN_checking_config_THEN_conflict_returned() {
        // Arrange
        configBlocks = Arrays.asList(block1);
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Arrays.asList(block1), Collections.emptyList()));

        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnLoad();

        // Assert
        assertTrue(result.containsKey(BLOCK_NAME_1));
    }

    @Test
    public void
            GIVEN_blank_config_WHEN_adding_another_component_with_unique_block_THEN_no_conflicts_returned() {
        // Arrange
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);
        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Collection<Configuration> toAdd = Arrays.asList(mockComp(COMP_TOADD_NAME_1, Arrays.asList(block1), Collections.emptyList()));
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void
            GIVEN_base_config_contains_block_WHEN_adding_another_component_with_unique_block_THEN_no_conflicts_returned() {
        // Arrange
        configBlocks = Arrays.asList(block1);
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Collection<Configuration> toAdd = Arrays.asList(mockComp(COMP_TOADD_NAME_1, Arrays.asList(block2), Collections.emptyList()));
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void
            GIVEN_base_config_contains_block_WHEN_adding_another_component_with_duplicate_block_THEN_conflict_returned() {
        // Arrange
        configBlocks = Arrays.asList(block1);
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Collection<Configuration> toAdd = Arrays.asList(mockComp(COMP_TOADD_NAME_1, Arrays.asList(block1), Collections.emptyList()));
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.containsKey(BLOCK_NAME_1));
    }

    @Test
    public void
            GIVEN_component_in_config_contains_block_WHEN_adding_another_component_with_unique_block_THEN_no_conflicts_returned() {
        // Arrange
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Arrays.asList(block1), Collections.emptyList()));

        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Collection<Configuration> toAdd = Arrays.asList(mockComp(COMP_TOADD_NAME_1, Arrays.asList(block2), Collections.emptyList()));
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void
            GIVEN_component_in_config_contains_block_WHEN_adding_another_component_with_duplicate_block_THEN_conflict_returned() {
        // Arrange
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Arrays.asList(block1), Collections.emptyList()));

        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Collection<Configuration> toAdd = Arrays.asList(mockComp(COMP_TOADD_NAME_1, Arrays.asList(block1), Collections.emptyList()));
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.containsKey(BLOCK_NAME_1));
    }

    @Test
    public void
            GIVEN_components_to_add_contain_duplicate_blocks_between_themselves_WHEN_adding_adding_components_to_config_THEN_conflict_returned() {
        // Arrange
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);
        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Configuration comp1 = mockComp(COMP_TOADD_NAME_1, Arrays.asList(block1), Collections.emptyList());
        Configuration comp2 = mockComp(COMP_TOADD_NAME_2, Arrays.asList(block1), Collections.emptyList());
        Collection<Configuration> toAdd = Arrays.asList(comp1, comp2);
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.containsKey(BLOCK_NAME_1));
    }

    @Test
    public void
            GIVEN_component_in_config_contains_no_duplicate_blocks_WHEN_adding_duplicate_to_component_THEN_conflict_returned() {
        // Arrange
        configBlocks = Arrays.asList(block1);
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Arrays.asList(block2), Collections.emptyList()));

        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Configuration edited = mockComp(COMP_NAME_1, Arrays.asList(block1), Collections.emptyList());
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnEdit(edited);

        // Assert
        assertTrue(result.containsKey(BLOCK_NAME_1));
    }

    @Test
    public void
            GIVEN_component_in_config_contains_duplicate_block_WHEN_duplicate_is_removed_from_component_THEN_no_conflicts_returned() {
        // Arrange
        configBlocks = Arrays.asList(block1);
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Arrays.asList(block1), Collections.emptyList()));

        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Configuration edited = mockComp(COMP_NAME_1, Arrays.asList(block2), Collections.emptyList());
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnEdit(edited);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void GIVEN_component_not_in_config_WHEN_adding_duplicate_block_to_component_THEN_no_conflict_returned() {
        // Arrange
        configBlocks = Arrays.asList(block1);
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Arrays.asList(block2), Collections.emptyList()));

        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Configuration edited = mockComp(COMP_NAME_2, Arrays.asList(block1), Collections.emptyList());
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnEdit(edited);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void
            GIVEN_component_with_duplicate_block_in_config_WHEN_duplicate_is_removed_from_other_component_THEN_conflict_remains() {
        // Arrange
        configBlocks = Arrays.asList(block1);
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Arrays.asList(block1), Collections.emptyList()));

        blockDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Configuration edited = mockComp(COMP_NAME_2, Arrays.asList(block2), Collections.emptyList());
        Map<String, Set<String>> result = blockDuplicateChecker.checkItemsOnEdit(edited);

        // Assert
        assertTrue(result.containsKey(BLOCK_NAME_1));
    }
    
    @Test
    public void GIVEN_config_with_unique_native_iocs_WHEN_checking_config_THEN_no_conflicts_returned() {
        // Arrange
        configIocs = Arrays.asList(ioc1, ioc2);
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnLoad();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void GIVEN_config_with_duplicate_ioc_in_component_WHEN_checking_config_THEN_conflict_returned() {
        // Arrange
        configIocs = Arrays.asList(ioc1);
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Collections.emptyList(), Arrays.asList(ioc1)));

        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnLoad();

        // Assert
        assertTrue(result.containsKey(IOC_NAME_1));
    }

    @Test
    public void
            GIVEN_blank_config_WHEN_adding_another_component_with_unique_ioc_THEN_no_conflicts_returned() {
        // Arrange
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);
        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Collection<Configuration> toAdd = Arrays.asList(mockComp(COMP_TOADD_NAME_1, Collections.emptyList(), Arrays.asList(ioc1)));
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void
            GIVEN_base_config_contains_ioc_WHEN_adding_another_component_with_unique_ioc_THEN_no_conflicts_returned() {
        // Arrange
        configIocs = Arrays.asList(ioc1);
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Collection<Configuration> toAdd = Arrays.asList(mockComp(COMP_TOADD_NAME_1, Collections.emptyList(), Arrays.asList(ioc2)));
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void
            GIVEN_base_config_contains_ioc_WHEN_adding_another_component_with_duplicate_ioc_THEN_conflict_returned() {
        // Arrange
        configIocs = Arrays.asList(ioc1);
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Collection<Configuration> toAdd = Arrays.asList(mockComp(COMP_TOADD_NAME_1, Collections.emptyList(), Arrays.asList(ioc1)));
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.containsKey(IOC_NAME_1));
    }

    @Test
    public void
            GIVEN_component_in_config_contains_ioc_WHEN_adding_another_component_with_unique_ioc_THEN_no_conflicts_returned() {
        // Arrange
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Collections.emptyList(), Arrays.asList(ioc1)));

        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Collection<Configuration> toAdd = Arrays.asList(mockComp(COMP_TOADD_NAME_1, Collections.emptyList(), Arrays.asList(ioc2)));
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void
            GIVEN_component_in_config_contains_ioc_WHEN_adding_another_component_with_duplicate_ioc_THEN_conflict_returned() {
        // Arrange
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Collections.emptyList(), Arrays.asList(ioc1)));

        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Collection<Configuration> toAdd = Arrays.asList(mockComp(COMP_TOADD_NAME_1, Collections.emptyList(), Arrays.asList(ioc1)));
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.containsKey(IOC_NAME_1));
    }

    @Test
    public void
            GIVEN_components_to_add_contain_duplicate_iocs_between_themselves_WHEN_adding_adding_components_to_config_THEN_conflict_returned() {
        // Arrange
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);
        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Configuration comp1 = mockComp(COMP_TOADD_NAME_1, Collections.emptyList(), Arrays.asList(ioc1));
        Configuration comp2 = mockComp(COMP_TOADD_NAME_2, Collections.emptyList(), Arrays.asList(ioc1));
        Collection<Configuration> toAdd = Arrays.asList(comp1, comp2);
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnAdd(toAdd);

        // Assert
        assertTrue(result.containsKey(IOC_NAME_1));
    }

    @Test
    public void
            GIVEN_component_in_config_contains_no_duplicate_iocs_WHEN_adding_duplicate_to_component_THEN_conflict_returned() {
        // Arrange
        configIocs = Arrays.asList(ioc1);
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Collections.emptyList(), Arrays.asList(ioc2)));

        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Configuration edited = mockComp(COMP_NAME_1, Collections.emptyList(), Arrays.asList(ioc1));
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnEdit(edited);

        // Assert
        assertTrue(result.containsKey(IOC_NAME_1));
    }

    @Test
    public void
            GIVEN_component_in_config_contains_duplicate_ioc_WHEN_duplicate_is_removed_from_component_THEN_no_conflicts_returned() {
        // Arrange
        configIocs = Arrays.asList(ioc1);
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Collections.emptyList(), Arrays.asList(ioc1)));

        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Configuration edited = mockComp(COMP_NAME_1, Collections.emptyList(), Arrays.asList(ioc2));
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnEdit(edited);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void GIVEN_component_not_in_config_WHEN_adding_duplicate_ioc_to_component_THEN_no_conflict_returned() {
        // Arrange
        configIocs = Arrays.asList(ioc1);
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Collections.emptyList(), Arrays.asList(ioc2)));

        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Configuration edited = mockComp(COMP_NAME_2, Collections.emptyList(), Arrays.asList(ioc1));
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnEdit(edited);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void
            GIVEN_component_with_duplicate_ioc_in_config_WHEN_duplicate_is_removed_from_other_component_THEN_conflict_remains() {
        // Arrange
        configIocs = Arrays.asList(ioc1);
        configComps = Arrays.asList(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);

        allComps.add(mockComp(COMP_NAME_1, Collections.emptyList(), Arrays.asList(ioc1)));

        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Configuration edited = mockComp(COMP_NAME_2, Collections.emptyList(), Arrays.asList(ioc2));
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnEdit(edited);

        // Assert
        assertTrue(result.containsKey(IOC_NAME_1));
    }

    
    @Test
    public void GIVEN_config_blank_WHEN_adding_ioc_THEN_no_conflict_returned() {
        // Arrange
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);
        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnAddItem(mockIoc(IOC_NAME_1, COMP_NAME_1), COMP_NAME_1);

        // Assert
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void GIVEN_config_with_native_ioc_WHEN_adding_unique_ioc_THEN_no_conflict_returned() {
        // Arrange
        configIocs.add(ioc1);
        allComps.add(mockComp(COMP_NAME_1, Collections.emptyList(), Collections.emptyList()));
        configComps.add(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);
        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnAddItem(mockIoc(IOC_NAME_2, COMP_NAME_1), COMP_NAME_1);

        // Assert
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void GIVEN_config_with_native_ioc_WHEN_adding_duplicate_ioc_THEN_conflict_returned() {
        // Arrange
        configIocs.add(ioc1);
        allComps.add(mockComp(COMP_NAME_1, Collections.emptyList(), Collections.emptyList()));
        configComps.add(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);
        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnAddItem(mockIoc(IOC_NAME_1, COMP_NAME_1), COMP_NAME_1);

        // Assert
        assertTrue(result.containsKey(IOC_NAME_1));
    }
    
    @Test
    public void GIVEN_config_with_ioc_in_component_WHEN_adding_duplicate_ioc_THEN_conflict_returned() {
        // Arrange
        configIocs.add(ioc1);
        allComps.add(mockComp(COMP_NAME_1, Collections.emptyList(), Arrays.asList(ioc1)));
        configComps.add(mockCompInfo(COMP_NAME_1));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);
        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnAddItem(mockIoc(IOC_NAME_1, COMP_NAME_1), COMP_NAME_1);

        // Assert
        assertTrue(result.containsKey(IOC_NAME_1));
    }
    
    @Test
    public void GIVEN_component_not_in_config_WHEN_adding_duplicate_ioc_THEN_no_conflict_returned() {
        // Arrange
        configIocs.add(ioc1);
        allComps.add(mockComp(COMP_NAME_1, Collections.emptyList(), Arrays.asList(ioc1)));
        baseConfig = mockConfig(BASE_CONFIG_NAME, configBlocks, configComps, configIocs);
        iocDuplicateChecker.setBase(baseConfig, allComps);

        // Act
        Map<String, Set<String>> result = iocDuplicateChecker.checkItemsOnAddItem(mockIoc(IOC_NAME_1, COMP_NAME_1), COMP_NAME_1);

        // Assert
        assertTrue(result.isEmpty());
    }
}
