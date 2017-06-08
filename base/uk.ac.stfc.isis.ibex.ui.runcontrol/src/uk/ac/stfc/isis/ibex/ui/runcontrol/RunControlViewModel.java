
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
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writer;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.RunControlValidator;

/**
 * ViewModel for the run-control, allowing blocks to be obtained from the
 * configuration and run control server to be reset to configuraiton values.
 */
public class RunControlViewModel extends ErrorMessageProvider {

    private ForwardingObservable<Configuration> currentConfigObserver;
    private RunControlServer runControlServer;

    private RunControlValidator runControlValidator = new RunControlValidator();
    
    private String txtHighLimit = "";
    private String txtLowLimit = "";
    private boolean sendEnabled;
    
    public RunControlViewModel(final ConfigServer configServer, final RunControlServer runControlServer) {
        this.currentConfigObserver = configServer.currentConfig();
        this.runControlServer = runControlServer;
    }

    /**
     * Return a named block from the current configuration.
     * 
     * @param blockName
     *            The name of the block
     * @return The block from the configuration, or null if block name does not
     *         exist
     */
    public Block getCurrentConfigBlock(String blockName) {
        Collection<Block> blocks = currentConfigObserver.getValue().getBlocks();

        for (Block block : blocks) {
            if (block.getName().equals(blockName)) {
                return block;
            }
        }
        return null;
    }

    /**
     * Resets the run control server settings to the configuration block run
     * control settings.
     */
    public void resetRunControlSettings() {
        Collection<Block> blocks = currentConfigObserver.getValue().getBlocks();
        
        for (Block block : blocks) {
            resetLowLimit(block);
            resetHighLimit(block);
            resetEnabled(block);
        }
    }

    private void resetLowLimit(Block configBlock) {
        Writer<String> writer = runControlServer.blockRunControlLowLimitSetter(configBlock.getName());
        writer.uncheckedWrite(Float.toString(configBlock.getRCLowLimit()));
    }

    private void resetHighLimit(Block configBlock) {
        Writer<String> writer = runControlServer.blockRunControlHighLimitSetter(configBlock.getName());
        writer.uncheckedWrite(Float.toString(configBlock.getRCHighLimit()));
    }

    private void resetEnabled(Block configBlock) {
        Writer<String> writer = runControlServer.blockRunControlEnabledSetter(configBlock.getName());
        writer.uncheckedWrite(configBlock.getRCEnabled() ? "YES" : "NO");
    }
    
    public String getTxtLowLimit() {
		return txtLowLimit;
	}
    
	public void setTxtLowLimit(String txtLowLimit) {
		boolean isValid = runControlValidator.isValid(txtLowLimit, txtHighLimit);
		setSendEnabled(isValid);
		setError(!(isValid), runControlValidator.getErrorMessage());
		
		firePropertyChange("txtLowLimit", this.txtLowLimit, this.txtLowLimit = txtLowLimit);
	}
	
	public String getTxtHighLimit() {
		return txtHighLimit;
	}

	public void setTxtHighLimit(String txtHighLimit) {
		boolean isValid = runControlValidator.isValid(txtLowLimit, txtHighLimit);
		setSendEnabled(isValid);
		setError(!(isValid), runControlValidator.getErrorMessage());
		
		firePropertyChange("txtHighLimit", this.txtHighLimit, this.txtHighLimit = txtHighLimit);
	}

	public boolean isValid() {
		return runControlValidator.isValid(txtLowLimit, txtHighLimit);
	}
	
	public boolean getSendEnabled() {
		return sendEnabled;
	}

	public void setSendEnabled(boolean sendEnabled) {
		firePropertyChange("sendEnabled", this.sendEnabled, this.sendEnabled = sendEnabled);
	}	
}
