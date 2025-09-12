/**
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2025
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

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;


/**
 * The view model for the log settings for a block.
 */
public class BlockAlarmConfigViewModel extends ErrorMessageProvider {
    
    private final EditableBlock editingBlock;
    
    private boolean enabled;
    private boolean latched;
	private Double lowLimit;
    private Double highLimit;
    private Double delay;
    private String guidance;
    
    /**
     * Field that the GUI should bind to to update low limit.
     */
	public static final String LOW_LIMIT_BINDING_NAME = "lowLimitString";

    /**
     * Field that the GUI should bind to to update high limit.
     */
    public static final String HIGH_LIMIT_BINDING_NAME = "highLimitString";

    /**
     * Field that the GUI should bind to to update delay value.
     */
    public static final String DELAY_BINDING_NAME = "delayString";
    
	/**
     * Field that the GUI should bind to to update enabled flag.
     */
	public static final String ENABLED_BINDING_NAME = "enabled";
	
	/**
     * Field that the GUI should bind to to update latched flag.
     */
	public static final String LATCHED_BINDING_NAME = "latched";

    /**
     * Field that the GUI should bind to to update guidance value.
     */
    public static final String GUIDANCE_BINDING_NAME = "guidance";

    /**
     * Constructor.
     * 
     * @param editingBlock the block being edited
     */
    public BlockAlarmConfigViewModel(final EditableBlock editingBlock) {
    	this.editingBlock = editingBlock;
    	enabled = editingBlock.isAlarmEnabled();
		latched = editingBlock.isAlarmLatched();
		lowLimit = editingBlock.getAlarmLowLimit();
		highLimit = editingBlock.getAlarmHighLimit();
		delay = editingBlock.getAlarmDelay();
		guidance = editingBlock.getAlarmGuidance();
    }

    /**
     * Gets whether alarm is enabled.
     * 
     * @return is the alarm enabled
     */
    public boolean getEnabled() {
        return enabled;
    }
    
    /**
     * Sets whether the alarm is enabled.
     * 
     * @param enabled - Is alarm enabled (true) or disabled (false)
     */
    public void setEnabled(boolean enabled) {
        firePropertyChange("enabled", this.enabled, this.enabled = enabled);        
    }

    /**
     * Gets whether alarm is latched.
     * 
     * @return whether alarm is latched
     */
    public boolean getLatched() {
        return latched;
    }

	/**
     * Sets whether alarm is latched.
     * 
     * @param latched - Is alarm latched (true) or not latched (false)
     */
    public void setLatched(boolean latched) {
        firePropertyChange("latched", this.latched, this.latched = latched);        
    }

	/**
	 * Gets the low limit.
	 * 
	 * @return the low limit
	 */
	public Double getLowLimit() {
		return lowLimit;
	}

	/**
	 * Set the low limit.
	 * 
	 * @param lowLimit the lower threshold for the alarm
	 */
	public void setLowLimit(Double lowLimit) {
		firePropertyChange("lowLimit", this.lowLimit, this.lowLimit = lowLimit);
	}
	
	/**
	 * Gets the high limit.
	 * 
	 * @return the high limit
	 */
	public Double getHighLimit() {
		return highLimit;
	}
	
	/**
	 * Set the high limit.
	 * 
	 * @param highLimit the upper threshold for the alarm.
	 */
	public void setHighLimit(Double highLimit) {
		firePropertyChange("highLimit", this.highLimit, this.highLimit = highLimit);
	}

	/**
	 * Gets the delay.
	 * 
	 * @return the delay
	 */
	public Double getDelay() {
		return delay;
	}

	/**
	 * Set the delay.
	 * 
	 * @param delay the delay value waited before triggering the alarm
	 */
	public void setDelay(Double delay) {
		firePropertyChange("delay", this.delay, this.delay = delay);
	}

    /**
     * @return the guidance for the alarm
     */
    public String getGuidance() {
        return guidance;
    }

    /**
     * Set the guidance.
     * 
     * @param guidance the guidance for the alarm
     */
    public void setGuidance(String guidance) {   	
        firePropertyChange("guidance", this.guidance, this.guidance = guidance);
    }

	/**
	 * Gets the low limit as a string.
	 * @return the low limit as a string
	 */
	public String getLowLimitString() {
		Double value = getLowLimit();
		return value == null ? "" : value.toString();
	}
	
	/**
	 * Sets the low limit from a text value.
	 * @param lowLimit the low limit as a string
	 */
	public void setLowLimitString(String lowLimit) {
		try {
			setLowLimit(Double.valueOf(lowLimit));
		} catch (NumberFormatException | NullPointerException e) {
			setLowLimit(null);
		}
	}

	/**
	 * Gets the high limit as a string.
	 * @return the high limit as a string
	 */
	public String getHighLimitString() {
		Double value = getHighLimit();
		return value == null ? "" : value.toString();
	}
	
	/**
	 * Sets the high limit from a text value.
	 * @param highLimit the high limit as a string
	 */
	public void setHighLimitString(String highLimit) {
		try {
			setHighLimit(Double.valueOf(highLimit));
		} catch (NumberFormatException | NullPointerException e) {
			setHighLimit(null);
		}
	}

	/**
	 * Gets the delay value as a string.
	 * @return the delay as a string
	 */
	public String getDelayString() {
		Double value = getDelay();
		return value == null ? "" : value.toString();
	}
	
	/**
	 * Sets the delay value from a text value.
	 * @param delay the delay value as a string
	 */
	public void setDelayString(String delay) {
		try {
			setDelay(Double.valueOf(delay));
		} catch (NumberFormatException | NullPointerException e) {
			setDelay(null);
		}
	}

    /**
     * Update the settings on the block.
     */
    public void updateBlock() {
		editingBlock.setAlarmEnabled(enabled);
		editingBlock.setAlarmLatched(latched);
		editingBlock.setAlarmLowLimit(lowLimit);
		editingBlock.setAlarmHighLimit(highLimit);
		editingBlock.setAlarmDelay(delay);
		editingBlock.setAlarmGuidance(guidance);
    }
}
