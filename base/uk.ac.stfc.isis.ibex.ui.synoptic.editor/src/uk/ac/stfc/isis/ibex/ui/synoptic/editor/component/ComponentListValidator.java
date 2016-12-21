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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.validators.ErrorMessage;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;

/**
 *
 */
public class ComponentListValidator extends ErrorMessageProvider {
    private SynopticDescription synoptic;
    private List<ComponentDescription> components;

    private List<ErrorMessageProvider> compDetailsErrors = new ArrayList<>();

    private static final String UNIQUE_COMPONENT_NAME = "Component names (%s) must be unique";

    private PropertyChangeListener detailsErrorListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!error.isError()) {
                checkCompDetailsErrors();
            }
        }
    };

    private PropertyChangeListener componentNameListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("name") || evt.getPropertyName().equals("componentRemoved")) {
                String duplicate = getDuplicateName();
                if (!duplicate.isEmpty()) {
                    setError(true, String.format(UNIQUE_COMPONENT_NAME, duplicate));
                } else {
                    checkCompDetailsErrors();
                }
            }
        }
    };

    private PropertyChangeListener componentAddedListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            addListeners((ComponentDescription) evt.getNewValue());
        }
    };

    public ComponentListValidator(SynopticDescription synoptic) {
        this.synoptic = synoptic;

        components = synoptic.components();

        for (ComponentDescription comp : components) {
            addListeners(comp);
        }

        synoptic.addPropertyChangeListener(componentNameListener);
        synoptic.addPropertyChangeListener("componentAdded", componentAddedListener);
    }

    private void addListeners(ComponentDescription comp) {
        comp.addPropertyChangeListener(componentNameListener);
        comp.addPropertyChangeListener("componentAdded", componentAddedListener);

        ErrorMessageProvider detailsError = new ComponentDetailsValidator(comp);
        detailsError.addPropertyChangeListener("error", detailsErrorListener);
        compDetailsErrors.add(detailsError);
    }

    /**
     * Checks for duplicate names in the components.
     * 
     * @return The name of the first duplicated component, empty string if there
     *         are no duplicates.
     */
    private String getDuplicateName() {
        List<String> comps = synoptic.getComponentNameListWithChildren();
        Set<String> s = new HashSet<String>();
        for (String comp : comps) {
            if (!s.add(comp)) {
                return comp;
            }
        }
        return "";
    }

    private void checkCompDetailsErrors() {
        for (ErrorMessageProvider entry : compDetailsErrors) {
            ErrorMessage newError = entry.getError();
            if (newError.isError()) {
                setError(newError.isError(), newError.getMessage());
                return;
            }
        }

        setError(false, null);
    }
}
