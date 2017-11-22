
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;

/**
 * 
 */
public class BlockFactory {

    private static final String DEFAULT_BLOCK_NAME = "NEW_BLOCK";
    private final DefaultName blockName = new DefaultName(DEFAULT_BLOCK_NAME);
    EditableConfiguration config;

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
     * Create a new block with a unique name.
     * 
     * @return the new EditableBlock object
     */
    public EditableBlock createNewBlock() {
        String name = blockName.getUnique(blockNames());
        EditableBlock block = new EditableBlock(new Block(name, "", true, true));
        return block;
    }

    private Collection<String> blockNames() {
        List<String> names = new ArrayList<>();
        for (Block block : config.transformBlocks()) {
            names.add(block.getName());
        }

        return names;
    }
}
