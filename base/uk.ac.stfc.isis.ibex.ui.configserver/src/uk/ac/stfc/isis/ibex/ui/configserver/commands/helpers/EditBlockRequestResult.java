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
package uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

/**
 * The result of a request to edit a block.
 */
public class EditBlockRequestResult {
    /** The configuration/component that a block belongs to. */
    private EditableConfiguration config = null;

    /** The error associated with the request to edit a block. */
    private String error = "An unspecified error has occurred finding the configuration associated with this block.";

    /**
     * Whether the configuration is a component. Can't trust the configuration
     * to tell you.
     */
    private boolean isComponent = false;
    
    public void setConfig(EditableConfiguration config, boolean isComponent) {
        this.error = null;
        this.config = config;
        this.isComponent = isComponent;
    }

    public void setError(String error) {
        this.config = null;
        this.error = error;
    }

    /**
     * @return Whether an error occurred during the request
     */
    public boolean hasError() {
        return error != null;
    }

    /**
     * @return Whether a configuration (or component) was located
     */
    public boolean hasConfig() {
        return config != null;
    }

    /**
     * @return A configuration exists for the request and it is a component
     */
    public boolean isComponent() {
        return hasConfig() && isComponent;
    }

    /**
     * @return The error associated with the request. May be null
     */
    public String getError() {
        return error;
    }

    /**
     * @return The configuration (or component) associated with the request. May
     *         be null
     */
    public EditableConfiguration getConfig() {
        return hasConfig() ? config : null;
    }
}
