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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aggregates a number of error message providers into one.
 */
public abstract class ErrorAggregator extends ErrorMessageProvider {
    protected Map<ErrorMessageProvider, ErrorMessage> childErrors = new HashMap<>();

    protected PropertyChangeListener errorListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            childErrors.put((ErrorMessageProvider) evt.getSource(), (ErrorMessage) evt.getNewValue());
            updateErrors();
        }
    };

    /**
     * Build a message made up of all the aggregate messages.
     */
    protected void updateErrors() {
        for (ErrorMessage e : childErrors.values()) {
            if (e.isError()) {
                setError(true, constructMessage());
                return;
            }
        }

        super.clearError();
    }

    /**
     * Get the list of error messages from all children.
     * 
     * @return The list of error messages.
     */
    protected List<String> getErrorMessages() {
        List<String> errs = new ArrayList<>();
        for (ErrorMessage e : childErrors.values()) {
            if (e.isError()) {
                errs.add(e.getMessage());
            }
        }
        return errs;
    }

    @Override
    public void clearError() {
        for (ErrorMessageProvider e : childErrors.keySet()) {
            e.clearError();
        }
        childErrors.clear();
        super.clearError();
    }

    /**
     * Construct the error message for this parent. This message is
     * reconstructed any time one of the children goes into error.
     * 
     * @return The error message for this class.
     */
    public abstract String constructMessage();

}