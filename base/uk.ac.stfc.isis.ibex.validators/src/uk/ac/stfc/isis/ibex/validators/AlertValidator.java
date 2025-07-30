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

package uk.ac.stfc.isis.ibex.validators;

/**
 * Provides validation for Alert PV items.
 */
public class AlertValidator {

	/**
	 * Semi-colon character used to separate multiple emails or mobile numbers.
	 */
	private static final String SEMI_COLON = ";";
	
	/**
	 * Valid email address pattern
	 */
	private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
	
	/**
	 * Valid mobile number pattern
	 */
	private static final String MOBILE_PATTERN = "^\\+?[0-9]{10,15}$";
	
    /** 
     * The error message displayed if the low limit is higher than the high limit.    
     */
    public static final String LOW_LIMIT_LESS = "The high limit must be greater than the low limit";
    
    /** 
     * The error message displayed if the limits are invalid.    
     */
    public static final String INVALID_LIMIT = "The limits must be numbers";
 
    /** 
     * The error message displayed emails are invalid.    
     */
    public static final String INVALID_EMAILS = "Emails must be a semi-colon separated list of valid email addresses";
    
    /** 
     * The error message displayed if the mobile numbers are invalid.    
     */
    public static final String INVALID_MOBILE = "Mobiles must be a semi-colon separated list of valid mobile numbers";
 
    /** 
     * The message displayed if they are no errors.    
     */
    public static final String NO_ERROR = "";
    
    private String errorMessage = NO_ERROR;
    
    
	/**
	 * Checks if the current inputs are valid.
	 * 
	 * @param lowLimit  The low limit input.
	 * @param highLimit The high limit input.
	 * @param enabled   True if the run control is enabled.
	 * @return True if all the inputs are valid.
	 */
	public boolean alertDetailsAreValid(Double lowLimit, Double highLimit, boolean enabled) {
		boolean isValid = true;
		if (!enabled) {
			setErrorMessage(NO_ERROR);
		} else if (lowLimit == null || highLimit == null) {
			setErrorMessage(INVALID_LIMIT);
			isValid = false;
		} else if (lowLimit > highLimit) {
			setErrorMessage(LOW_LIMIT_LESS);
			isValid = false;
		} else {
			setErrorMessage(NO_ERROR);
		}
		return isValid;
	}

	/**
	 * Checks if the emails are valid.
	 * 
	 * @param emails  The emails input.
	 * @return True if all emails are valid.
	 */
	public boolean emailsAreValid(String emails) {
		boolean isValid = true;
		if (emails == null || emails.isBlank()) {
			isValid = true;
		} else {
			String[] emailList = emails.split(SEMI_COLON);
			for (String email : emailList) {
				if (!email.trim().matches(EMAIL_PATTERN)) {
					isValid = false;
					break; // No need to check further if one is invalid
				}
			}
		}
		return isValid;
	}

	/**
	 * Checks if the mobiles are valid.
	 * 
	 * @param mobiles The mobiles input.
	 * @return True if all mobiles are valid.
	 */
	public boolean mobilesAreValid(String mobiles) {
		boolean isValid = true;
		if (mobiles == null || mobiles.isBlank()) {
			isValid = true;
		} else {
			String[] mobileList = mobiles.split(SEMI_COLON);
			for (String mobile : mobileList) {
				if (!mobile.trim().matches(MOBILE_PATTERN)) {
					isValid = false;
					break; // No need to check further if one is invalid
				}
			}
		}
		return isValid;
	}

	/**
	 * Checks if the top-level alert details are valid.
	 * 
	 * @param emails  The emails input.
	 * @param mobiles The mobiles input.
	 * @return True if all top-level alert details are valid.
	 */
	public boolean topLevelAlertDetailsAreValid(String emails, String mobiles) {
		setErrorMessage(NO_ERROR);
		boolean emailsAreValid = emailsAreValid(emails);
		boolean mobilesAreValid = mobilesAreValid(mobiles);
		if (!emailsAreValid) {
            setErrorMessage(INVALID_EMAILS);
        }
		if (!mobilesAreValid) {
			if (getErrorMessage().isBlank()) {
				setErrorMessage(INVALID_MOBILE);
			} else {
				setErrorMessage(getErrorMessage() + "\n" + INVALID_MOBILE);
			}
		}
		return emailsAreValid && mobilesAreValid;
	}

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * Returns the current error message.
     * 
     * @return
     *          The current error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
