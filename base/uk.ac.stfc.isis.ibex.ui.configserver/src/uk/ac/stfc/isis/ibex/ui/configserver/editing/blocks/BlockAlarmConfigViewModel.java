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
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;

/**
 * The view model for the log settings for a block.
 */
public class BlockAlarmConfigViewModel extends ErrorMessageProvider {
    
    private final EditableBlock editingBlock;
    
    private boolean enabled;
    private boolean latched;
    private Double delay;
    private String guidance;

    // Observables for alarm limits
    ObservableFactory observableFactory = new ObservableFactory(OnInstrumentSwitch.CLOSE);
    private ForwardingObservable<Double> alarmLowLimitObservable = null;
    private ForwardingObservable<Double> alarmHighLimitObservable = null;
    private String lowLimit = null;
    private String highLimit = null;

    private final BaseObserver<Double> alarmLowLimitAdapter = new BaseObserver<Double>() {
        @Override
        public void onValue(Double value) {
        	setLowLimit((null == value) ? "" : value.toString());
        }

        @Override
        public void onError(Exception e) {
        	lowLimit = "";
            IsisLog.getLogger(getClass()).error("Exception in alarm low limit adapter: " + e.getMessage());
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
            	lowLimit = "";
            }
        }
    };

	private final BaseObserver<Double> alarmHighLimitAdapter = new BaseObserver<Double>() {
		@Override
		public void onValue(Double value) {
			setHighLimit((null == value) ? "" : value.toString());
		}

		@Override
		public void onError(Exception e) {
			highLimit = "";
			IsisLog.getLogger(getClass()).error("Exception in alarm high limit adapter: " + e.getMessage());
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				highLimit = "";
			}
		}
	};

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
     * Field that the GUI should bind to to update lowLimit value.
     */
    public static final String LOWLIMIT_BINDING_NAME = "lowLimit";
    
    /**
     * Field that the GUI should bind to to update highLimit value.
     */
    public static final String HIGHLIMIT_BINDING_NAME = "highLimit";
    
    /**
     * Constructor.
     * 
     * @param editingBlock the block being edited
     */
    public BlockAlarmConfigViewModel(final EditableBlock editingBlock) {
    	this.editingBlock = editingBlock;
    	enabled = editingBlock.isAlarmEnabled();
		latched = editingBlock.isAlarmLatched();
		delay = editingBlock.getAlarmDelay();
		guidance = editingBlock.getAlarmGuidance();

		// Create the observables to monitor the alarm limits
		createAlarmObservables();
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
	 * Gets the low limit .
	 * @return the low limit 
	 */
	public String getLowLimit() {
		return lowLimit;
	}

	/**
	 * Sets the low limit.
	 * @param lowLimit
	 */
    public void setLowLimit(String lowLimit) {;
    	firePropertyChange("lowLimit", this.lowLimit, this.lowLimit = lowLimit);
	}
    
	/**
	 * Gets the high limit.
	 * @return the high limit
	 */
	public String getHighLimit() {
		return highLimit;
	}

	/**
	 * Sets the High limit.
	 * @param highLimit
	 */
    public void setHighLimit(String highLimit) {;
    	firePropertyChange("highLimit", this.highLimit, this.highLimit = highLimit);
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
		editingBlock.setAlarmDelay(delay);
		editingBlock.setAlarmGuidance(guidance);
    }

    /**
     * Creates an observable for the PV holding the current state of this banner
     * item.
     */
    private void createAlarmObservables() {
		alarmLowLimitObservable = observableFactory.getSwitchableObservable(new DoubleChannel(),
				editingBlock.getPV() + ".LOW");
		alarmHighLimitObservable = observableFactory.getSwitchableObservable(new DoubleChannel(),
				editingBlock.getPV() + ".HIGH");
		alarmLowLimitObservable.subscribe(alarmLowLimitAdapter);
		alarmHighLimitObservable.subscribe(alarmHighLimitAdapter);
    }  
}
