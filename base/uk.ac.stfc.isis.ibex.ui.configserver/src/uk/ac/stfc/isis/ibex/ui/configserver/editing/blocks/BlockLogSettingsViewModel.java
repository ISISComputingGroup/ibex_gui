
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class BlockLogSettingsViewModel extends ModelObject {
    public static String PERIODIC_STRING = "Periodic Scan for Change";
    public static String MONITOR_STRING = "Monitor With Deadband";

    public static String DEADBAND_LABEL = "Deadband:";
    public static String SCAN_LABEL = "Scan Rate:";

    public static String COMBO_TOOLTIP = "Periodic: Log on change but no more often than the specified period.\n"
            + "Monitor: Log when the value changes by the absolute amount specified, this amount is in the same units as the block.";

    private final Block editingBlock;
    
    private boolean enabled = true;
    private String comboText;
    private String labelText;
    private String textBoxText;

    private boolean periodic;
    private int rate;
    private float deadband;

    private void updatePeriodic(boolean periodic, boolean updateComboText) {
    	this.periodic = periodic;
    	if (periodic) {
            setLabelText(SCAN_LABEL);
            setTextBoxText(Integer.toString(rate));
            if (updateComboText)
            	setComboText(PERIODIC_STRING);
        } else {
            setLabelText(DEADBAND_LABEL);
            setTextBoxText(Float.toString(deadband));
            if (updateComboText)
            	setComboText(MONITOR_STRING);
        }
    }

    public BlockLogSettingsViewModel(final Block editingBlock) {
    	this.editingBlock = editingBlock;
    	
    	rate = editingBlock.getLogRate();
    	deadband = editingBlock.getLogDeadband();
    	
        updatePeriodic(editingBlock.getLogPeriodic(), true);
    	
        if (periodic && rate == 0) {
            setEnabled(false);
        }
    }

    public void setEnabled(boolean enabled) {
        if (!enabled) {
            rate = 0;
            updatePeriodic(true, true);
        }

        firePropertyChange("enabled", this.enabled, this.enabled = enabled);

    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setLabelText(String text) {
        firePropertyChange("labelText", this.labelText, this.labelText = text);
    }

    public String getLabelText() {
        return labelText;
    }

    public void setComboText(String selection) {
        if (selection.equals(PERIODIC_STRING)) {
        	updatePeriodic(true, false);
        } else if (selection.equals(MONITOR_STRING)) {
        	updatePeriodic(false, false);
        }
        firePropertyChange("comboText", this.comboText, this.comboText = selection);
    }
    
    public String getComboText() {
    	return comboText;
    }

    public void setTextBoxText(String text) {
        if (periodic) {
            rate = Integer.parseInt(text);
        } else {
            deadband = Float.parseFloat(text);
        }
        firePropertyChange("textBoxText", this.textBoxText, this.textBoxText = text);
    }

    public String getTextBoxText() {
        return textBoxText;
    }
    
    public void updateBlock() {
    	editingBlock.setLogPeriodic(periodic);
    	if (periodic) {
    		editingBlock.setLogRate(rate);
    	} else {
    		editingBlock.setLogDeadband(deadband);
    	}
    }
}
