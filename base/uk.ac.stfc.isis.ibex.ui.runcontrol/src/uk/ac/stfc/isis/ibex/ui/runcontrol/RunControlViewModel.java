
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
import uk.ac.stfc.isis.ibex.runcontrol.EditableRunControlSetting;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.RunControlValidator;

/**
 * ViewModel for the run-control, allowing blocks to be obtained from the
 * configuration and run control server to be reset to configuration values.
 */
public class RunControlViewModel extends ErrorMessageProvider {

    private ForwardingObservable<Configuration> currentConfigObserver;
    private RunControlServer runControlServer;

    private RunControlValidator runControlValidator = new RunControlValidator();
    
    private String txtHighLimit = "";
    private String txtLowLimit = "";
    private boolean sendEnabled;
    
    /**
     * Creates the view model for changing the run control settings outside of a
     * configuration.
     * 
     * @param configServer
     *            The object to get/send info to the block server.
     * @param runControlServer
     *            The object for creating the PV names for run control.
     */
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
            EditableRunControlSetting setter = new EditableRunControlSetting(block.getName(), runControlServer);
            setter.setLowLimit(Float.toString(block.getRCLowLimit()));
            setter.setHighLimit(Float.toString(block.getRCHighLimit()));
            setter.setEnabled(block.getRCEnabled());
        }
    }
    
    private void checkIsValid(String lowLimit, String highLimit) {
        boolean isValid = runControlValidator.isValid(lowLimit, highLimit);
		setSendEnabled(isValid);
		setError(!(isValid), runControlValidator.getErrorMessage());
    }

    /**
     * Get the low limit for the block.
     * 
     * @return The low limit for the block.
     */
    public String getTxtLowLimit() {
        return txtLowLimit;
    }

    /**
     * Set the low limit for the block.
     * 
     * @param txtLowLimit
     *            The new low limit for the block.
     */
    public void setTxtLowLimit(String txtLowLimit) {
        checkIsValid(txtLowLimit, txtHighLimit);
		
		firePropertyChange("txtLowLimit", this.txtLowLimit, this.txtLowLimit = txtLowLimit);
	}
	
    /**
     * Get the high limit for the block.
     * 
     * @return The high limit for the block.
     */
	public String getTxtHighLimit() {
		return txtHighLimit;
	}

    /**
     * Set the high limit for the block.
     * 
     * @param txtHighLimit
     *            The new high limit for the block.
     */
	public void setTxtHighLimit(String txtHighLimit) {
        checkIsValid(txtLowLimit, txtHighLimit);
		
		firePropertyChange("txtHighLimit", this.txtHighLimit, this.txtHighLimit = txtHighLimit);
	}

    /**
     * Get whether the currently set values of low and high limit are valid.
     * 
     * @return True if they are valid.
     */
    public boolean isCurrentlyValid() {
		return runControlValidator.isValid(txtLowLimit, txtHighLimit);
	}
	
    /**
     * Gets whether the send changes button is enabled or not.
     * 
     * @return True if the send button is enabled.
     */
	public boolean getSendEnabled() {
		return sendEnabled;
	}

    /**
     * Set whether the send changes button is enabled or not.
     * 
     * @param sendEnabled
     *            True to enable the button.
     */
	public void setSendEnabled(boolean sendEnabled) {
		firePropertyChange("sendEnabled", this.sendEnabled, this.sendEnabled = sendEnabled);
	}	
}
