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
import java.util.List;

import org.eclipse.swt.graphics.Image;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.ui.devicescreens.ComponentIcons;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;

/**
 *
 */
public class ComponentDetailViewModel extends ErrorMessageProvider {
    private final SynopticViewModel model;
    private ComponentDescription component;
    private static final String UNIQUE_COMPONENT_NAME = "Component names (%s) must be unique";

    /**
     * The list of possible types for components.
     */
    public static List<String> typeList = ComponentType.componentTypeAlphaList();

    private boolean selectionVisible = false;
    private String componentName;
    private Image typeIcon;
    private String compType;

    public ComponentDetailViewModel(final SynopticViewModel synopticViewModel) {
        model = synopticViewModel;

        synopticViewModel.addPropertyChangeListener("compSelection", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                component = synopticViewModel.getSingleSelectedComp();
                showComponent(component);
            }
        });

        showComponent(null);
    }

    /**
     * Display a synoptic component in the view.
     * 
     * @param component
     *            the component to display
     */
    public void showComponent(ComponentDescription component) {
        if (component != null) {
            setSelectionVisible(true);

            setComponentName(component.name());

            setCompType(component.type().name());

            updateTypeIcon();
        } else {
            setSelectionVisible(false);
        }
    }

    /**
     * Updates the component type based on the user input.
     * 
     * @param modelType
     *            The type to updaet the model to.
     * @param isFinalUpdate
     *            Whether this is the final update for the user
     */
    public void updateModelType(String modelType, boolean isFinalUpdate) {
        if (component != null) {
            setCompType(modelType);
            ComponentType type = ComponentType.valueOf(compType);
            component.setType(type);

            if (isFinalUpdate) {
                model.addTargetToSelectedComponent(true);
            } else {
                model.addTargetToSelectedComponent(false);
            }

            updateTypeIcon();
        }
    }

    private void updateTypeIcon() {
        ComponentType enteredType = ComponentType.valueOf(compType);
        Image icon = null;

        if (enteredType.equals(ComponentType.UNKNOWN)) {
            icon = ComponentIcons.thumbnailForType(ComponentType.UNKNOWN);
        } else {
            icon = ComponentIcons.thumbnailForType(enteredType);
        }

        setTypeIcon(icon);
    }

    /**
     * @param selection
     *            set whether a component has been selected or not
     */
    public void setSelectionVisible(boolean selection) {
        firePropertyChange("selectionVisible", selectionVisible, selectionVisible = selection);
    }

    /**
     * @return get whether a component has been selected
     */
    public boolean getSelectionVisible() {
        return selectionVisible;
    }

    /**
     * @param name
     *            set the name of the component
     */
    public void setComponentName(String name) {
        component.setName(name);
        if (model.getHasDuplicatedName()) {
            setError(true, String.format(UNIQUE_COMPONENT_NAME, name));
        } else {
            setError(false, null);
        }
        firePropertyChange("componentName", componentName, componentName = name);
        model.refreshTreeView();
    }

    /**
     * @return get the name of the component
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param type
     *            set the type of the component
     */
    public void setCompType(String type) {
        firePropertyChange("compType", compType, compType = type);
    }

    /**
     * @return get the type of the component
     */
    public String getCompType() {
        return compType;
    }

    /**
     * @param icon
     *            set the icon for the component
     */
    public void setTypeIcon(Image icon) {
        firePropertyChange("typeIcon", typeIcon, typeIcon = icon);
        model.refreshTreeView();
    }

    /**
     * @return get the icon for the component
     */
    public Image getTypeIcon() {
        return typeIcon;
    }

}
