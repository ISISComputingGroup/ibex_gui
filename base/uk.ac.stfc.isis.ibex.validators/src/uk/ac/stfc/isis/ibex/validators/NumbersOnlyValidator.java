
/**
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

/**
 * 
 */

package uk.ac.stfc.isis.ibex.validators;

import java.util.regex.Pattern;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

/**
 * Validates that a field may only contain numeric characters.
 */
public class NumbersOnlyValidator implements IValidator {

    private final Pattern numbersOnly = Pattern.compile("\\d*");

    /**
     * Checks that a value consists only of digits and is not null. Sends an OK
     * status if so and an error message if not.
     */
    public IStatus validate(Object value) {
        if (value != null && numbersOnly.matcher(value.toString()).matches()) {
            return ValidationStatus.ok();
        }
        return ValidationStatus.error(value + " contains a non-numeric!");
    }
}