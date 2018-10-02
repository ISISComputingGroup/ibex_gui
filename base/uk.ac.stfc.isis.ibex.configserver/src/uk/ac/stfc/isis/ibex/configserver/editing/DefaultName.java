
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

package uk.ac.stfc.isis.ibex.configserver.editing;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides a default name, appended with a number if needed, for adding or
 * copying items. Separator and whether or not to use brackets can be
 * configured.
 */
public class DefaultName {

    private static final String NUMBER_REGEX = "(\\d+)";

    private final String separator;
    private final String openingBracket;
    private final String closingBracket;
	private final String nameRoot;
	
    /**
     * Provides an object that gives default names such as NAME, NAME_1, NAME_2
     * etc.
     * 
     * @param name
     *            The base name, e.g. NAME
     */
    public DefaultName(String name) {
        this(name, "_", false);
	}

    /**
     * Separator can be chosen and brackets can be optionally used, e.g. NAME,
     * NAME (1), NAME (2) if the separator is a space.
     * 
     * @param name
     *            The base name, e.g. NAME
     * @param separator
     *            The separator between the name and any number
     * @param brackets
     *            True if brackets are desired
     */
    public DefaultName(String name, String separator, boolean brackets) {
        this.separator = separator;
        
        if (brackets) {
            openingBracket = "(";
            closingBracket = ")";
        } else {
            openingBracket = "";
            closingBracket = "";
        }

        nameRoot = getNameRoot(name);
	}
	
    /**
     * Method that returns a unique name given a set of already existing names.
     *
     * @param existingNames
     *                      Names that already exist.
     * @return
     *          A unique name.
     */
	public String getUnique(Collection<String> existingNames) {
        Integer i = 1;
        String proposedName = nameRoot;
        while (existingNames.contains(proposedName)) {
        	proposedName = String.format("%s%s%s%s%s", nameRoot, separator, openingBracket, i, closingBracket);
        	i++;
        }
        return proposedName;
	}

    /**
     * Returns the name minus any relevant suffix, e.g. _(1)
     * 
     * @param name
     *            The original name
     * @return The name without the suffix
     */
    private String getNameRoot(String name) {
        Pattern pattern = Pattern.compile(
        		String.format("%s(%s%s%s)$", separator, Pattern.quote(openingBracket), NUMBER_REGEX, Pattern.quote(closingBracket)));
        Matcher match = pattern.matcher(name);
        return match.find() ? name.substring(0, match.start()) : name;
    }
}
