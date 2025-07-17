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

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.alerts.AlertsServer;
import uk.ac.stfc.isis.ibex.alerts.AlertsSetter;
import uk.ac.stfc.isis.ibex.alerts.AlertsTopLevelSetter;
import uk.ac.stfc.isis.ibex.configserver.Displaying;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayAlerts;
import uk.ac.stfc.isis.ibex.configserver.displaying.TopLevelAlertSettings;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.AlertValidator;

/**
 * ViewModel for the run-control, allowing blocks to be obtained from the
 * configuration and run control server to be reset to configuration values.
 */
public class AlertsViewModel extends ErrorMessageProvider {
	private static final Logger LOG = IsisLog.getLogger(AlertsViewModel.class);
	private static final String ASTERISK = "*";
	private static final String SEMI_COLON = ";";
	private static final String SPACE = " ";
	private static final String EMAIL_MASK = "(?<=.{2}).(?=.*@)";
	private static final String MOBILE_MASK = "(?<=.{2}).(?=.{2})";
    private final Map<DisplayAlerts, AlertsSetter> setters;
    
    private final AlertValidator validator = new AlertValidator();

    private boolean sendEnabled;
    
	private Double lowLimit;
    private Double highLimit;
    private boolean enabled;
	private Double delayIn;
    private Double delayOut;
    private String message;
    private String emails;
    private String mobiles;
    private DisplayAlerts source;
    private final AlertsTopLevelSetter topLevelSetters;
    private final TopLevelAlertSettings topLevelSource;
    private boolean canApplyTopLevelChanges	 = true; // Flag to indicate if top level changes can be applied;
 
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
     * Field that the GUI should bind to to update DelayIn.
     */
	public static final String DELAYIN_BINDING_NAME = "delayInString";

	/**
	 * Field that the GUI should bind to to update DelayOut.
	 */
	public static final String DELAYOUT_BINDING_NAME = "delayOutString";

	/**
	 * Field that the GUI should bind to to update alert message.
	 */
	public static final String MESSAGE_BINDING_NAME = "message";
	
	/**
	 * Field that the GUI should bind to to update email.
	 */
	public static final String EMAILS_BINDING_NAME = "maskedEmails";
	
	/**
	 * Field that the GUI should bind to to update mobile.
	 */
	public static final String MOBILES_BINDING_NAME = "maskedMobiles";

	/**
	 * Field that the GUI should to allow top level changes to be applied.
	 */
	public static final String CAN_CHANGE_TOPLEVEL_BINDING_NAME = "canApplyTopLevelChanges";
	
    /**
     * Creates the view model for changing the alert control settings outside of a
     * configuration.
     * 
     * The setters are created here so as the PVs have some time to connect
     * before writing to them.
     * 
     * @param config the configuration containing the alerts settings.
     * @param alertsServer
     *            The object for creating the PV names for alerts control settings.
     */
    public AlertsViewModel(final Displaying config, final AlertsServer alertsServer) {
        setters = Collections.unmodifiableMap(config.getDisplayAlerts().stream()
                .collect(Collectors.toMap(alert -> alert, alert -> new AlertsSetter(alert.getName(), alertsServer), (v1, v2) -> v1)));
        topLevelSetters = new AlertsTopLevelSetter(alertsServer);
        topLevelSource = config.getTopLevelAlertSettings();
        resetTopLevelSettings(this);
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
		validateDetails();
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
		validateDetails();
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
		validateDetails();
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
		validateDetails();
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
		validateDetails();
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		firePropertyChange("message", this.message, this.message = message);
	}

	/**
	 * @return the emails
	 */
	public String getEmails() {
		return emails;
	}

	/**
	 * @param emails the emails to set
	 */
	public void setEmails(String emails) {
		firePropertyChange("emails", this.emails, this.emails = emails);
		firePropertyChange("maskedEmails", null, getMaskedEmails());
		validateTopLevel();
	}

	/**
	 * @return the mobiles
	 */
	public String getMobiles() {
		return mobiles;
	}

	/**
	 * @param mobiles the mobiles to set
	 */
	public void setMobiles(String mobiles) {
		firePropertyChange("mobiles", this.mobiles, this.mobiles = mobiles);
		firePropertyChange("maskedMobiles", null, getMaskedMobiles());
		validateTopLevel();
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
		validateDetails();
	}

    /**
     * Sets the alert changes to the block.
     * @param model the model to apply changes to
     */
    public void applyBlockLevelChanges(AlertsViewModel model) {
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
	 * Gets whether the top level changes can be applied or not.
	 * @return True if the top level changes can be applied
	 */
	public boolean getCanApplyTopLevelChanges() {
		return canApplyTopLevelChanges;
	}
	
	/**
	 * Sets whether the top level changes can be applied or not.
	 * 
	 * @param canApplyTopLevelChanges True if the top level changes can be applied
	 */
	public void setCanApplyTopLevelChanges(boolean canApplyTopLevelChanges) {
		firePropertyChange("canApplyTopLevelChanges", this.canApplyTopLevelChanges,
				this.canApplyTopLevelChanges = canApplyTopLevelChanges);
	}

	/**
	 * Validates the current settings and updates the error message and send button
	 * accordingly.
	 */
	private void validateDetails() {
		boolean isValid = validator.alertDetailsAreValid(lowLimit, highLimit, enabled);
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
	 * Gets the masked emails.
	 * 
	 * @return the masked emails
	 */
	public String getMaskedEmails() {
        return setMask(emails, EMAIL_MASK);
    }
	
	/**
	 * Sets the masked emails.
	 * @param emails
	 */
	public void setMaskedEmails(String emails) {
		setEmails(emails);
	}
	
	/**
	 * Gets the masked mobiles.
	 * 
	 * @return the masked mobiles
	 */
	public String getMaskedMobiles() {
        return setMask(mobiles, MOBILE_MASK);
    }
	
	/**	
	 * Sets the masked mobiles.
	 * @param mobiles
	 */
	public void setMaskedMobiles(String mobiles) {
		setMobiles(mobiles);
	}

	/**
	 * Resets the alert control settings to the original settings from the source.
	 * @param model the model to reset
	 */
	public void resetBlockLevelSettings(AlertsViewModel model) {
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

    /**
     * Sets the top level alert changes.
     * @param model the model to apply changes to
     */
    public void applyTopLevelChanges(AlertsViewModel model) {
    	validateTopLevel();
    	topLevelSetters.setEmails(emails);
    	topLevelSetters.setMobiles(mobiles);
        // Only set the message if it is changed
    	if (!message.equals(topLevelSource.getMessage())) {
    		topLevelSetters.setMessage(message);
		}
    }
 
	/**
	 * Resets the alert control settings to the original settings from the source.
	 * @param model the model to reset
	 */
	public void resetTopLevelSettings(AlertsViewModel model) {
		LOG.info("Resetting top level alert settings");
		setMessage(topLevelSource.getMessage());
		setEmails(topLevelSource.getEmails());
		setMobiles(topLevelSource.getMobiles());
	}

	/**
	 * Sets the message to new values, possibly for testing.
	 * @param model the model to set the message for
	 */
	public void setAlertMessage(AlertsViewModel model) {
		topLevelSetters.setMessage(message);
	}

	/**
	 * Sets the mask for the given values.
	 * 
	 * @param values the values to mask
	 * @param mask   the regex mask to apply
	 * @return the masked values
	 */
	private String setMask(String values, String mask) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        String result = Arrays.stream(values.split(SEMI_COLON)).map(value -> value.trim().replaceAll(mask, ASTERISK))
                .collect(Collectors.joining(SEMI_COLON + SPACE));
        return result.trim();
	}

	/**
	 * Validates the current settings and updates the error message and send button
	 * accordingly.
	 */
	private void validateTopLevel() {
		boolean isValid = validator.topLevelAlertDetailsAreValid(emails, mobiles);
		setError(!isValid, validator.getErrorMessage());
		setCanApplyTopLevelChanges(isValid);
	}
}
