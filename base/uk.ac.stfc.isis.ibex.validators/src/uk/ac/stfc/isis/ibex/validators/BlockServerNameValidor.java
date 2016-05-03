
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
package uk.ac.stfc.isis.ibex.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Validate a name based on rules from the block server.
 */
public class BlockServerNameValidor {

    /** Logger. */
    private static final Logger LOG = IsisLog.getLogger(BlockServerNameValidor.class);

    /** Error message for empty value. */
    private static final String EMPTY_NAME_MESSAGE = " must not be empty";

    /** Error message when a name appears in the disallowed names list. */
    public static final String FORBIDDEN_NAME_MESSAGE = " cannot be ";

    /** The regex which the block name must obey. */
    private String regex;

    /** The error message if the regex fails. */
    private String regexMessage;

    /** The list of disallowed values. */
    private List<String> disallowed = new ArrayList<>();

    /**
     * Instantiates a new block server name validation.
     *
     * @param regex the regex the block name must obey
     * @param regexMessage the regex message if the regex fails.
     * @param disallowed the list of disallowed names
     */
    public BlockServerNameValidor(String regex, String regexMessage, List<String> disallowed) {
        this.regex = regex;
        this.regexMessage = regexMessage;
        this.disallowed = disallowed;
    }

    /**
     * Validate the name against the rules.
     *
     * @param name the name to validate
     * @param what what it is being validated for the error message
     * @return the status
     */
    public IStatus validate(String name, String what) {

        if (name == null || name.isEmpty()) {
            return ValidationStatus.error(what + EMPTY_NAME_MESSAGE);
        }
        
        if (disallowed == null) {
            LOG.error("Disallowed names are null but they should be a list, did the block server not server them");
        } else {
            for (String disallowName : disallowed) {
                if (name.equalsIgnoreCase(disallowName)) {
                    return ValidationStatus.error(what + FORBIDDEN_NAME_MESSAGE + getListAsString(disallowed));
                }
            }
        }

        if (regex == null) {
            LOG.error("Regex for names is null did the block server not server it");
        } else if (!(name.matches(regex))) {
            return ValidationStatus.error(regexMessage);
        }

        return ValidationStatus.ok();
    }

    /**
     * get a list of strings as a single string.
     * 
     * @param list to be made into a string
     * @return string of items separated by commas
     */
    private String getListAsString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 2) {
                sb.append(", ");
            } else if (i == list.size() - 2) {
                sb.append(" or ");
            }
        }
        return sb.toString();
    }

    /**
     * @return a default validator which checks that the item is not blank
     */
    public static BlockServerNameValidor getDefaultInstance() {
        return new BlockServerNameValidor(".*", "", Collections.<String> emptyList());
    }
}
