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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.validators;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;

/**
 * This validator will check the list of all components and make sure that there
 * are no duplicate names.
 */
public class ComponentListValidator extends ErrorMessageProvider {
    private SynopticDescription synoptic;

    private static final String UNIQUE_COMPONENT_NAME = "Component names (%s) must be unique";

    private PropertyChangeListener componentNameListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("componentName") || evt.getPropertyName().equals("componentRemoved")) {
                updateErrors();
            }
        }
    };

    private void updateErrors() {
        String duplicate = getDuplicateComponentName();
        if (!duplicate.isEmpty()) {
            setError(true, String.format(UNIQUE_COMPONENT_NAME, duplicate));
        } else {
            setError(false, null);
        }
    }

    public ComponentListValidator(SynopticDescription synoptic) {
        this.synoptic = synoptic;

        // This will add a listener to all underlying components as the property
        // change from child components will pass through.
        synoptic.addPropertyChangeListener(componentNameListener);

        updateErrors();
    }

    /**
     * Checks for duplicate names in the components.
     * 
     * @return The name of the first duplicated component, empty string if there
     *         are no duplicates.
     */
    private String getDuplicateComponentName() {
        List<String> comps = synoptic.getComponentNameListWithChildren();
        Set<String> s = new HashSet<String>();
        for (String comp : comps) {
            if (!s.add(comp)) {
                return comp;
            }
        }
        return "";
    }
}
