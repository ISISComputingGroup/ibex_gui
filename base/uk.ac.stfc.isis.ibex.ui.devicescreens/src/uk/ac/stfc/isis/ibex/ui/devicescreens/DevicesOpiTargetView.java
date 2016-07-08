
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
package uk.ac.stfc.isis.ibex.ui.devicescreens;

import java.util.LinkedHashMap;

import uk.ac.stfc.isis.ibex.ui.targets.OpiTargetView;

/**
 * 
 */
public class DevicesOpiTargetView extends OpiTargetView {

    /**
     * Class ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.devicescreens.DevicesOpiTargetView"; //$NON-NLS-1$

    /**
     * 
     * @param title - Title for the OPI
     * @param opiName - Name of the OPI used to locate it from file
     */
    public void setOpi(String title, String opiName) {
        this.setOpi(title, opiName, new LinkedHashMap<String, String>());
    }
}
