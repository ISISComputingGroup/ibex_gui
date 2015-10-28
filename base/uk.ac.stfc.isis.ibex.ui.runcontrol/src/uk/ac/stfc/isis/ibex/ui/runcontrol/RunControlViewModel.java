
/**
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.runcontrol;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writer;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

public class RunControlViewModel extends ModelObject {

    private InitialiseOnSubscribeObservable<Configuration> currentConfigObserver;
    private RunControlServer runControlServer;

    public RunControlViewModel(final ConfigServer configServer, final RunControlServer runControlServer) {
        this.currentConfigObserver = configServer.currentConfig();
        this.runControlServer = runControlServer;
    }

    public Block getCurrentConfigBlock(String blockName) {
        Collection<Block> blocks = currentConfigObserver.getValue().getBlocks();

        for (Block block : blocks) {
            if (block.getName().equals(blockName)) {
                return block;
            }
        }
        return null;
    }

    public void resetRunControlSettings() {
        Collection<Block> blocks = currentConfigObserver.getValue().getBlocks();
        
        for(Block block : blocks) {
            resetLowLimit(block);
            resetHighLimit(block);
            resetEnabled(block);
        }
    }

    private void resetLowLimit(Block configBlock) {
        Writer<String> writer = runControlServer.blockRunControlLowLimitSetter(configBlock.getName());
        writer.write(Float.toString(configBlock.getRCLowLimit()));
    }

    private void resetHighLimit(Block configBlock) {
        Writer<String> writer = runControlServer.blockRunControlHighLimitSetter(configBlock.getName());
        writer.write(Float.toString(configBlock.getRCHighLimit()));
    }

    private void resetEnabled(Block configBlock) {
        Writer<String> writer = runControlServer.blockRunControlEnabledSetter(configBlock.getName());
        writer.write(configBlock.getRCEnabled() ? "YES" : "NO");
    }
}
