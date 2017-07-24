
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
import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlSetter;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.RunControlValidator;

/**
 * ViewModel for the run-control, allowing blocks to be obtained from the
 * configuration and run control server to be reset to configuration values.
 */
public class RunControlViewModel extends ErrorMessageProvider {

    private Map<DisplayBlock, RunControlSetter> setters = new HashMap<>();

    private RunControlValidator runControlValidator = new RunControlValidator();
    
    private String txtHighLimit = "";
    private String txtLowLimit = "";
    private boolean rcEnabled;

    private boolean sendEnabled;
    
    private DisplayBlock currentBlock;

    /**
     * Creates the view model for changing the run control settings outside of a
     * configuration.
     * 
     * The setters are created here so as the PVs have some time to connect
     * before writing to them.
     * 
     * @param blocks
     *            The blocks to set run control on.
     * @param runControlServer
     *            The object for creating the PV names for run control.
     */
    public RunControlViewModel(Collection<DisplayBlock> blocks, final RunControlServer runControlServer) {
        for (DisplayBlock block : blocks) {
            setters.put(block, new RunControlSetter(block.getName(), runControlServer));
        }
    }

    /**
     * Resets the current values to those of the current block's configuration.
     */
    public void resetCurrentBlock() {
        setTxtHighLimit(currentBlock.getConfigurationHighLimit());
        setTxtLowLimit(currentBlock.getConfigurationLowLimit());
        setRcEnabled(currentBlock.getConfigurationEnabled());

        setSendEnabled(true);
    }

    /**
     * Resets the run control server settings to the configuration block run
     * control settings.
     */
    public void resetAllRunControlSettings() {
        for (Map.Entry<DisplayBlock, RunControlSetter> e : setters.entrySet()) {
            e.getValue().setLowLimit(e.getKey().getConfigurationLowLimit());
            e.getValue().setHighLimit(e.getKey().getConfigurationHighLimit());
            e.getValue().setEnabled(e.getKey().getConfigurationEnabled());
        }
    }
    
    private void checkIsValid(String lowLimit, String highLimit) {
        boolean isValid = runControlValidator.isValid(lowLimit, highLimit);
		setSendEnabled(isValid);
		setError(!(isValid), runControlValidator.getErrorMessage());
    }

    /**
     * Set the block that the view model is looking at.
     * 
     * @param block
     *            The block to get/set rc values on.
     */
    public void setBlock(DisplayBlock block) {
        this.currentBlock = block;

        if (currentBlock == null) {
            setSendEnabled(false);
            setTxtLowLimit("");
            setTxtHighLimit("");
            setRcEnabled(false);

            // This isn't actually an error
            setError(false, null);
            return;
        }

        setTxtLowLimit(block.getLowLimit().trim());
        setTxtHighLimit(block.getHighLimit().trim());
        setRcEnabled(block.getEnabled());
    }

    /**
     * Send the currently inputed values to the server.
     */
    public void sendChanges() {
        if (currentBlock != null) {
            RunControlSetter setter = setters.get(currentBlock);
            setter.setLowLimit(txtLowLimit);
            setter.setHighLimit(txtHighLimit);
            setter.setEnabled(rcEnabled);
        }

        setSendEnabled(false);
    }

    /**
     * Get the low limit for the block. Required for databinding.
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
     * Get the high limit for the block. Required for databinding.
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
     * Set whether run control is enabled for the block.
     * 
     * @param enabled
     *            True to enable run control
     */
    public void setRcEnabled(boolean enabled) {
        checkIsValid(txtLowLimit, txtHighLimit);
        firePropertyChange("rcEnabled", this.rcEnabled, this.rcEnabled = enabled);
    }

    /**
     * Get whether run control is enabled on the block. Required for
     * databinding.
     * 
     * @return True is run control is enabled.
     */
    public boolean getRcEnabled() {
        return rcEnabled;
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
