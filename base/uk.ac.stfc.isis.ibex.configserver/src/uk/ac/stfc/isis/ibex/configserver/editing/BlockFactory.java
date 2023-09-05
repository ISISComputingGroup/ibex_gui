
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
package uk.ac.stfc.isis.ibex.configserver.editing;


import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;

/**
 * 
 */
public class BlockFactory {

    private static final String DEFAULT_BLOCK_NAME = "NEW_BLOCK";
    private final DefaultName blockName = new DefaultName(DEFAULT_BLOCK_NAME);
    EditableConfiguration config;
    private Boolean local = true;

    /**
     * This class is responsible for the creation of editable blocks to be
     * registered in the configuration.
     * 
     * @param config the configuration the blocks are registered with.
     */
    public BlockFactory(EditableConfiguration config) {
        this.config = config;
    }
    
    /**
     * This class is responsible for the creation of editable blocks to be
     * registered in the configuration.
     * 
     * @param config the configuration the blocks are registered with.
     * @param defaultLocal whether the block should default to local
     */
    public BlockFactory(EditableConfiguration config, Boolean defaultLocal) {
        this.config = config;
        this.local = defaultLocal;
    }
   

    /**
     * Create a new block with a unique name.
     * 
     * @param newPV the name of the PV for the block
     * 
     * @return the new EditableBlock object
     */
    public EditableBlock createNewBlock(Optional<String> newPV) {
        String name = blockName.getUnique(blockNames());
        EditableBlock block = new EditableBlock(new Block(name, newPV.orElse(""), true, local));
        return block;
    }

    private Collection<String> blockNames() {    
        return config.transformBlocks().stream()
    			.map(b -> b.getName())
    			.collect(Collectors.toList());
    }
}
