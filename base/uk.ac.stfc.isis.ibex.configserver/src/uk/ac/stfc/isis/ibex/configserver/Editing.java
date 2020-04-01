
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

import uk.ac.stfc.isis.ibex.configserver.editing.ObservableEditableConfiguration;

/**
 * Public interface for editing the config server.
 *
 */
public interface Editing {

    /**
     * Returns the current configuration as an editable configuration.
     * 
     * @return
     *          The current configuration as an editable configuration.
     */
    ObservableEditableConfiguration currentConfig();

    /**
     * Returns a blank configuration as an editable configuration.
     * 
     * @return
     *          A blank configuration as an editable configuration.
     */
    ObservableEditableConfiguration blankConfig();

    /**
     * Returns a given configuration as an editable configuration.
     * 
     * @param configName
     *                  The desired configuration.
     * 
     * @return
     *                  A given configuration as an editable configuration.
     */
    ObservableEditableConfiguration config(String configName);


    /**
     * Returns a given component as an editable component.
     * 
     * @param componentName
     *                  The desired component.
     * 
     * @return
     *                  A given component as an editable configuration.
     */
    ObservableEditableConfiguration component(String componentName);
}
