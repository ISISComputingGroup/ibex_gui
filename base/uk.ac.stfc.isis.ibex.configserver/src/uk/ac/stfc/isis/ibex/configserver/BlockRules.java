
/*
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

package uk.ac.stfc.isis.ibex.configserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Rules for creating and editing blocks in the configuration. These are
 * typically set from the block server
 */
public class BlockRules {
	
    /** The regex which the block name must obey. */
	private String regex;

    /** The regex_message. */
    private String regexMessage;

    /** The disallowed. */
	private List<String> disallowed = new ArrayList<>();
	
    /**
     * Instantiates new block rules.
     *
     * @param regex the regex to use for the name
     * @param regexMessage the regex message to display if the name fails the
     *            regex
     * @param disallowed a list of disallowed names
     */
	public BlockRules(String regex, String regexMessage, List<String> disallowed) {
		this.regex = regex;
        this.regexMessage = regexMessage;
		this.disallowed = disallowed;
	}
	
    /**
     * Gets disallowed names.
     *
     * @return the disallowed names
     */
	public List<String> getDisallowed() {
		return disallowed;
	}
	
    /**
     * Gets the regex which the name should conform to.
     *
     * @return the regex for the name
     */
	public String getRegex() {
		return regex;
	}
	
    /**
     * @return the regex error message to be displayed if the name does not
     *         conform to the regex
     */
	public String getRegexErrorMessage() {
        return regexMessage;
	}
	
}
