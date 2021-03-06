
/**
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;

/**
 * The view model for the log settings for a block.
 */
public class BlockLogSettingsViewModel extends ErrorMessageProvider {
    /** The value for the periodic string. */
    public static final String PERIODIC_STRING = "Periodic Scan for Change";

    /** The value for the monitor string. */
    public static final String MONITOR_STRING = "Monitor With Deadband";

    /** The value for the dead-band label. */
    public static final String DEADBAND_LABEL = "Deadband:";

    /** The value for the scan label. */
    public static final String SCAN_LABEL = "Rate/seconds:";

    /** The message for the combo-box tool tip. */
    public static final String COMBO_TOOLTIP = "Periodic: Log on change but no more often than the specified period.\n"
            + "Monitor: Log when the value changes by the absolute amount specified, this amount is in the same units as the block.";

    /**
     * Periodic scan validation
     */

    /**
     * Error to emit in periodic scan mode if value is not integer.
     */
    private static final String SCAN_TYPE_ERROR = "Scan rate must be an integer number of seconds";

    /**
     * Error to emit in periodic scan mode if value is not positive.
     */
    private static final String SCAN_VALUE_ERROR = "Scan rate must be strictly positive";

    /**
     * Error to emit in periodic scan mode if value is empty.
     */
    private static final String SCAN_EMPTY = "Scan rate cannot be empty";

    /**
     * Deadband validation
     */

    /**
     * Error to emit in deadband mode if value is negative.
     */
    private static final String FLOAT_ERROR = "Deadband must be a decimal number";

    /**
     * Error to emit in deadband mode if value is empty.
     */
    private static final String DEADBAND_EMPTY = "Deadband cannot be empty";

    /**
     * Error to emit in deadband mode if value is negative.
     */
    private static final String DEADBAND_NEGATIVE = "Deadband cannot be negative";
    
    private final Block editingBlock;
    
    private boolean enabled = false;
    private String comboText;
    private String labelText;
    
    private String textBoxText;

    private boolean periodic;
    private int rate;
    private float deadband;

    /**
     * Constructor.
     * 
     * @param editingBlock the block being edited
     */
    public BlockLogSettingsViewModel(final Block editingBlock) {
    	this.editingBlock = editingBlock;
    	
        // Don't accept negative rates as input
        rate = Math.max(editingBlock.getLogRate(), 0);
        deadband = editingBlock.getLogDeadband();
    	
        updatePeriodic(editingBlock.getLogPeriodic(), true);
    	
        // Define enabling behaviour explicitly to avoid confusion
        assert (rate >= 0);
        if (periodic) {
            if (rate == 0) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
        } else {
            if (deadband < 0.0f) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
        }
    }
    
    private void updatePeriodic(boolean periodic, boolean updateComboText) {
    	this.periodic = periodic;
    	if (periodic) {
            setLabelText(SCAN_LABEL);
            setTextBoxText(Integer.toString(rate));
            if (updateComboText) {
            	setComboText(PERIODIC_STRING);
            }
        } else {
            setLabelText(DEADBAND_LABEL);
            setTextBoxText(Float.toString(deadband));
            if (updateComboText) {
            	setComboText(MONITOR_STRING);
            }
        }
    }

    /**
     * Sets whether logging is enabled. Sets to default value if zero value on
     * enable
     * 
     * @param enabled - Is logging being enabled (true) or disabled (false)
     */
    public void setEnabled(boolean enabled) {
        // Update enabled value first. We might need the new value in
        // updatePeriodic.
        firePropertyChange("enabled", this.enabled, this.enabled = enabled);

        if (!enabled) {
            rate = 0;
            updatePeriodic(true, true);
        } else if (enabled && rate == 0) {
            rate = Block.DEFAULT_SCAN_RATE;
            updatePeriodic(true, true);
        }

    }

    /**
     * Gets whether logging is enabled.
     * 
     * @return whether logging is enabled
     */
    public boolean getEnabled() {
        return enabled;
    }

    /**
     * Set the label text.
     * 
     * @param text the new text
     */
    public void setLabelText(String text) {
        firePropertyChange("labelText", this.labelText, this.labelText = text);
    }

    /**
     * @return the label text
     */
    public String getLabelText() {
        return labelText;
    }

    /**
     * Set the combo-box text.
     * 
     * @param selection the new text
     */
    public void setComboText(String selection) {
        if (selection.equals(PERIODIC_STRING)) {
        	updatePeriodic(true, false);
        } else if (selection.equals(MONITOR_STRING)) {
        	updatePeriodic(false, false);
        }
        firePropertyChange("comboText", this.comboText, this.comboText = selection);
    }
    
    /**
     * @return the combo-box text
     */
    public String getComboText() {
    	return comboText;
    }

    /**
     * Checks and sets the value in the log settings value text box. An error
     * will be sent to the config block dialog if the value is invalid.
     * 
     * @param text Value to put in text box
     */
    public void setTextBoxText(String text) {
    	if (periodic) {
    		try {
    			rate = Integer.parseInt(text);
                // Value set to 0 on disabled. Don't print error in this case.
                if (rate <= 0 && enabled) {
                    setError(true, SCAN_VALUE_ERROR);
                } else {
                    setError(false, null);
                }
    		} catch (NumberFormatException e) {
    	    	if (text.isEmpty()) {
    	    		setError(true, SCAN_EMPTY);
    	    	} else {
        			setError(true, SCAN_TYPE_ERROR);
    	    	}
    		}
        } else {
        	try {
		        deadband = Float.parseFloat(text);
                if (deadband < 0) {
                    setError(true, DEADBAND_NEGATIVE);
                } else {
		            setError(false, null);
		        }
			} catch (NumberFormatException e) {
    	    	if (text.isEmpty()) {
    	    		setError(true, DEADBAND_EMPTY);
    	    	} else {
        			setError(true, FLOAT_ERROR);
    	    	}
			}
        }
        firePropertyChange("textBoxText", this.textBoxText, this.textBoxText = text);
    }

    /**
     * @return the text-box text
     */
    public String getTextBoxText() {
        return textBoxText;
    }
    
    /**
     * Update the settings on the block.
     */
    public void updateBlock() {
    	editingBlock.setLogPeriodic(periodic);
    	if (periodic) {
    		editingBlock.setLogRate(rate);
    	} else {
    		editingBlock.setLogDeadband(deadband);
    	}
    }
}
