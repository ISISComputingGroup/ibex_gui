
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.SystemUtils;
import org.eclipse.core.runtime.FileLocator;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Set of general utility methods and constants used in the UI.
 */
public final class Utils {


    /**
     * Private constructor for utility class.
     */
    private Utils() {

    }

    /**
     * Takes a value and if that value is in the range min_value to max_value returns that value.
     * If it is outside that range the return is constrained to be the closest of either min_value or max_value.
     * If the max value is greater than the min value then the max value is returned.
     * 
     * @param value The value to constrain
     * @param minValue The lower bound to constrain value to
     * @param maxValue The upper bound to constrain value to
     * @return The constrained value
     */
    public static int constrainIntToRange(int value, int minValue, int maxValue) {
	// Return max value if less than min value
	if (maxValue < minValue) {
	    return maxValue;
	}
	// Constrain value between min and max
	if (value > maxValue) {
	    return maxValue;
	} else if (value < minValue) {
	    return minValue;
	} else {
	    return value;
	}
    }

    /**
     * Whether this GUI is allowed to display information relating to current users.
     * 
     * Currently, we disallow viewing any user-identifying information on IDAAS systems,
     * which are currently identified by running linux. In principle, we should find a
     * more accurate way of identifying IDAAS machines and use that here instead.
     */
    public static final boolean SHOULD_HIDE_USER_INFORMATION = SystemUtils.IS_OS_LINUX;
    
    /**
     * Gets a help URL from it's resource file.
     * @param className class used to determine the path of the resources folder.
     * @param linkName
     * @return the help URL
     */
    public static String getHelpLink(Class<?> className, String linkName) {
		Properties linkProps = new Properties();
		try {
			final var resourceFilePath = FileLocator.resolve(className.getResource("/resources/helplink.properties")).getPath();
			try (FileInputStream fis = new FileInputStream(resourceFilePath)) {
				linkProps.load(fis);
			}
		} catch (IOException | IllegalArgumentException ex) {
			LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(className), ex.getMessage(), ex);
		}
		return linkProps.getProperty(linkName);
	}
}

