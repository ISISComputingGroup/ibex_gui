
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
import java.util.Optional;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlSetter;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.AbstractRunControlViewModel;

/**
 * ViewModel for the run-control, allowing blocks to be obtained from the
 * configuration and run control server to be reset to configuration values.
 */
public class RunControlViewModel extends AbstractRunControlViewModel {

    private Map<DisplayBlock, RunControlSetter> setters = new HashMap<>();

    private boolean sendEnabled;

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
    
    @Override
	public void resetFromSource() {
    	Optional<DisplayBlock> sourceBlock = setters.keySet().stream()
    			.filter(block -> (block == source))
    			.findFirst();
    	
    	if (sourceBlock.isPresent()) {
    		setRunControlHighLimit(sourceBlock.get().getConfigurationHighLimit());
    		setRunControlLowLimit(sourceBlock.get().getConfigurationHighLimit());
    		setRunControlEnabled(sourceBlock.get().getConfigurationEnabled());
    	}
    }
    
    public void sendChanges() {
    	if (source != null) {
    		RunControlSetter setter = setters.get(source);
    		setter.setLowLimit(getRunControlLowLimit());
    		setter.setHighLimit(getRunControlHighLimit());
    		setter.setEnabled(getRunControlEnabled());
    	}
    	setSendEnabled(false);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onValidate(boolean validationPassed) {
		setSendEnabled(validationPassed);
	}	
}
