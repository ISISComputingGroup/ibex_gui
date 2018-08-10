
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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provides a default name, appended with a number if needed, for adding or
 * copying items. Separator and whether or not to use brackets can be
 * configured.
 *
 */
public class DefaultName {

    private static final String NUMBER_REGEX = "(\\d+)";
    private static final String OPENING_GROUP_REGEX = "?(";
    private static final String CLOSING_GROUP_REGEX = ")?";

	private final String name;
    private final String separator;
    private final String openingBracket;
    private final String closingBracket;
	private final Pattern namePattern;
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
        this.name = name;
        this.separator = separator;

        if (brackets) {
            openingBracket = "(";
            closingBracket = ")";
        } else {
            openingBracket = "";
            closingBracket = "";
        }

        nameRoot = getNameRoot(name);
        namePattern = Pattern.compile(nameRoot + OPENING_GROUP_REGEX + Pattern.quote(separator) + Pattern.quote(openingBracket)
                + NUMBER_REGEX + Pattern.quote(closingBracket) + CLOSING_GROUP_REGEX + "$");
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
		if (!existingNames.contains(name)) {
			// If the name is not already taken we can avoid all the computation below.
			return name;
		}
		
		Set<Integer> taken = existingNames.stream()
				.map(name -> namePattern.matcher(name))
				.filter(match -> match.matches())
				.map(match -> match.group(2))
				.filter(str -> str != null)
				.map(str -> Integer.parseInt(str))
				.collect(Collectors.toSet());

        return nameRoot + separator + openingBracket + nextAvailable(taken) + closingBracket;
	}

	private String nextAvailable(Set<Integer> taken) {
		Integer i = 1;
		while (taken.contains(i)) {
			i++;
		}
		return i.toString();
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
        
        if (match.matches()) {
            return name.substring(0, match.start());
        } else {
        	return name;
        }
    }
}
