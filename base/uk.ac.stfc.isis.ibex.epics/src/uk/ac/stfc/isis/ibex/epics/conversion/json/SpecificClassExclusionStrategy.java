
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

package uk.ac.stfc.isis.ibex.epics.conversion.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * A specific class exclusion strategy.
 */
public class SpecificClassExclusionStrategy implements ExclusionStrategy {
    private final Class<?> excludedThisClass;

    /**
     * Constructor.
     * 
     * @param excludedThisClass
     *            the excluded this class
     */
    public SpecificClassExclusionStrategy(Class<?> excludedThisClass) {
        this.excludedThisClass = excludedThisClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return excludedThisClass.equals(clazz);
   }

    /**
     * {@inheritDoc}
     */
    @Override
   public boolean shouldSkipField(FieldAttributes f) {
        return excludedThisClass.equals(f.getDeclaredClass());
   }
}
