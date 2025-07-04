/*
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

package uk.ac.stfc.isis.ibex.ui.alerts;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.alerts.AlertsServer;
import uk.ac.stfc.isis.ibex.alerts.AlertsSetter;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayAlerts;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.RunControlValidator;

/**
 * ViewModel for the run-control, allowing blocks to be obtained from the
 * configuration and run control server to be reset to configuration values.
 */
public class AlertsViewModel extends ErrorMessageProvider {
	private static final Logger LOG = IsisLog.getLogger(AlertsViewModel.class);
    private final Map<DisplayAlerts, AlertsSetter> setters;
    
    private final RunControlValidator validator = new RunControlValidator();

    private boolean sendEnabled;
    
	private Double lowLimit;
    private Double highLimit;
    private boolean enabled;
	private Double delayIn;
    private Double delayOut;
    private DisplayAlerts source;
 
    /**
     * Field that the GUI should bind to to update high limit.
     */
    public static final String HIGH_LIMIT_BINDING_NAME = "highLimitString";
    
    /**
     * Field that the GUI should bind to to update low limit.
     */
	public static final String LOW_LIMIT_BINDING_NAME = "lowLimitString";
	
	/**
     * Field that the GUI should bind to to update enabled flag.
     */
	public static final String ENABLED_BINDING_NAME = "enabled";

	/**
     * Field that the GUI should bind to to update DelayIn flag.
     */
	public static final String DELAYIN_BINDING_NAME = "delayInString";

	/**
	 * Field that the GUI should bind to to update DelayOut flag.
	 */
	public static final String DELAYOUT_BINDING_NAME = "delayOutString";

    /**
     * Creates the view model for changing the alert control settings outside of a
     * configuration.
     * 
     * The setters are created here so as the PVs have some time to connect
     * before writing to them.
     * 
     * @param alerts
     *            The alert to be configured.
     * @param alertsServer
     *            The object for creating the PV names for alerts control settings.
     */
    public AlertsViewModel(Collection<DisplayAlerts> alerts, final AlertsServer alertsServer) {
        setters = Collections.unmodifiableMap(alerts.stream()
                .collect(Collectors.toMap(alert -> alert, alert -> new AlertsSetter(alert.getName(), alertsServer))));
    }

    /**
     * Gets the low limit for the alert control settings.
     * @return the low limit
     */
    public Double getLowLimit() {
		return lowLimit;
	}

    /**
     * Sets the low limit for the alert control settings.
     * @param lowLimit the limit to set
     */
	public void setLowLimit(Double lowLimit) {
		firePropertyChange("lowLimit", this.lowLimit, this.lowLimit = lowLimit);
		firePropertyChange("lowLimitString", null, getLowLimitString());
		validate();
	}

	/**
	 * Gets the high limit for the alert control settings.
	 * 
	 * @return the high limit
	 */
	public Double getHighLimit() {
		return highLimit;
	}

	/**
	 * Sets the high limit for the alert control settings.
	 * 
	 * @param highLimit the limit to set
	 */
	public void setHighLimit(Double highLimit) {
		firePropertyChange("highLimit", this.highLimit, this.highLimit = highLimit);
		firePropertyChange("highLimitString", null, getHighLimitString());
		validate();
	}

	/**
	 * Gets whether the alert control settings are enabled or not.
	 * 
	 * @return True if the alert control settings are enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets whether the alert control settings are enabled or not.
	 * 
	 * @param enabled True to enable the alert control settings.
	 */
	public void setEnabled(boolean enabled) {
		firePropertyChange("enabled", this.enabled, this.enabled = enabled);
		validate();
	}

	/**
     * Gets the enabled flag as a string.
     * @return the enabled flag as a string
     */
	public Double getDelayIn() {
		return delayIn;
	}

	/**
	 * Sets the delayIn for the alert control settings.
	 * 
	 * @param delayIn the delayIn to set
	 */
	public void setDelayIn(Double delayIn) {
		firePropertyChange("delayIn", this.delayIn, this.delayIn = delayIn);
		firePropertyChange("delayInString", null, getDelayInString());
		validate();
	}

	/**
	 * Gets the delayOut for the alert control settings.
	 * 
	 * @return the delayOut
	 */
	public Double getDelayOut() {
		return delayOut;
	}

	/**
	 * Sets the delayOut for the alert control settings.
	 * 
	 * @param delayOut the delayOut to set
	 */
	public void setDelayOut(Double delayOut) {
		firePropertyChange("delayOut", this.delayOut, this.delayOut = delayOut);
		firePropertyChange("delayOutString", null, getDelayOutString());
		validate();
	}

	/**
     * Resets the alerts settings to the original settings.
     */
    public void resetAlertSettings() {
    	LOG.info("Resetting all alerts control settings");
        for (Map.Entry<DisplayAlerts, AlertsSetter> entry : setters.entrySet()) {
	        try {
	        	final DisplayAlerts alert = entry.getKey();
	        	final AlertsSetter setter = entry.getValue();
	        	LOG.info("Resetting alerts control settings for block " + alert.getName());
	        	
	        	setter.setLowLimit(alert.getLowLimit());
	        	setter.setHighLimit(alert.getHighLimit());
	        	setter.setEnabled(alert.getEnabled());
	        	setter.setDelayIn(alert.getDelayIn());
	        	setter.setDelayOut(alert.getDelayOut());
	        } catch (Exception ex) {
	        	LoggerUtils.logErrorWithStackTrace(LOG, ex.getMessage(), ex);
	        }
        }
    }
    
	/**
	 * Sets the source of this display Alerts model.
	 * @param source the new source
	 */
	public void setSource(DisplayAlerts source) {
		this.source = source;
		if (source != null) {
    		setHighLimit(source.getHighLimit());
    		setLowLimit(source.getLowLimit());
    		setEnabled(source.getEnabled());
    		setDelayIn(source.getDelayIn());
    		setDelayOut(source.getDelayOut());
		}
		validate();
	}

    /**
     * Sets the alert changes to the block.
     */
    public void sendChanges() {
    	if (source != null) {
    		AlertsSetter setter = setters.get(source);
    		setter.setEnabled(enabled);
    		setter.setLowLimit(lowLimit);
    		setter.setHighLimit(highLimit);
    		setter.setDelayIn(delayIn);
    		setter.setDelayOut(delayOut);
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
	 * Validates the current settings and updates the error message and send button
	 * accordingly.
	 */
	private void validate() {
		boolean isValid = validator.isValid(lowLimit, highLimit, enabled);
		setError(!isValid, validator.getErrorMessage());
		setSendEnabled(isValid);
	}

	/**
	 * Gets the low limit as a string.
	 * @return the low limit as a string
	 */
	public String getLowLimitString() {
		return lowLimit == null ? "" : lowLimit.toString();
	}
	
	/**
	 * Sets the low limit from a string.
	 * @param lowLimit the limit as a string
	 */
	public void setLowLimitString(String lowLimit) {
		try {
			setLowLimit(Double.valueOf(lowLimit));
		} catch (NumberFormatException | NullPointerException e) {
			this.lowLimit = null;
		}
	}
	
	/**
	 * Gets the high limit as a string.
	 * @return the high limit as a string
	 */
	public String getHighLimitString() {
		return highLimit == null ? "" : highLimit.toString();
	}
	
	/**
	 * Sets the high limit from a string.
	 * @param highLimit the limit as a string
	 */
	public void setHighLimitString(String highLimit) {
		try {
			setHighLimit(Double.valueOf(highLimit));
		} catch (NumberFormatException | NullPointerException e) {
			this.highLimit = null;
		}
	}

	/**
	 * Gets the delayin as a string.
	 * @return the delayIn as a string
	 */
	public String getDelayInString() {
		return delayIn == null ? "" : delayIn.toString();
	}
	
	/**
	 * Sets the delayIn from a string.
	 * @param delayIn the delayIn as a string
	 */
	public void setDelayInString(String delayIn) {
		try {
			setDelayIn(Double.valueOf(delayIn));
		} catch (NumberFormatException | NullPointerException e) {
			this.delayIn = null;
		}
	}

	/**
	 * Gets the delayOut as a string.
	 * 
	 * @return the delayOut as a string
	 */
	public String getDelayOutString() {
		return delayOut == null ? "" : delayOut.toString();
	}

	/**
	 * Sets the delayOut from a string.
	 * 
	 * @param delayOut the delayOut as a string
	 */
	public void setDelayOutString(String delayOut) {
		try {
			setDelayOut(Double.valueOf(delayOut));
		} catch (NumberFormatException | NullPointerException e) {
			this.delayOut = null;
		}
	}

	/**
	 * Resets the alert control settings to the original settings from the source.
	 */
	public void resetFromSource() {
    	Optional<DisplayAlerts> originalAlert = setters.keySet().stream()
    			.filter(source::equals)
    			.findFirst();
    	
    	if (originalAlert.isPresent()) {
    		try {
	    		final DisplayAlerts alert = originalAlert.get();
	    		LOG.info("Resetting alert control settings for block " + alert.getName());
	    		setHighLimit(alert.getHighLimit());
	    		setLowLimit(alert.getLowLimit());
	    		setEnabled(alert.getEnabled());
	    		setDelayIn(alert.getDelayIn());
	    		setDelayOut(alert.getDelayOut());
    		} catch (Exception ex) {
    			LoggerUtils.logErrorWithStackTrace(LOG, ex.getMessage(), ex);
    		}
    	} else {
    		LOG.error(String.format("Attempting to reset alert %s from source failed because the block was not found.", source));
    	}
    }
}
