 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2019 Science & Technology Facilities Council.
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
package uk.ac.stfc.isis.ibex.ui.devicescreens.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The view model for the information specific to the properties of a particular
 * target.
 */
public class TargetPropertiesViewModel extends ModelObject {
    private EditDeviceScreensDescriptionViewModel viewModel;

    private Boolean tableEnabled = false;

    private String descriptionText;

    private List<PropertyDescription> properties;

    /**
     * Default constructor for the properties view model.
     * 
     * @param viewModel
     *            The general editor view model that contains information on
     *            which OPI is being looked at.
     */
    public TargetPropertiesViewModel(final EditDeviceScreensDescriptionViewModel viewModel) {
        this.viewModel = viewModel;

        viewModel.addPropertyChangeListener("targetScreen", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                DeviceDescriptionWrapper target = (DeviceDescriptionWrapper) evt.getNewValue();
                if (target == null) {
                    setProperties(new ArrayList<PropertyDescription>());
                } else {
                    setProperties(target.getProperties());
                }
            }
        });

        viewModel.addPropertyChangeListener("key", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String key = (String) evt.getNewValue();
                if (key == null || key == "") {
                    setProperties(new ArrayList<PropertyDescription>());
                } else {
                    setProperties(viewModel.getTargetScreen().getProperties());
                }
            }
        });
    }

    /**
     * Set the available properties.
     * 
     * @param newProperties
     *            The properties that can be set on the OPI.
     */
    public void setProperties(List<PropertyDescription> newProperties) {
        boolean hasProperties = !newProperties.isEmpty();
        if (hasProperties) {
            setTableSelection(newProperties.get(0));
        } else {
            setTableSelection(null);
        }
        setTableEnabled(hasProperties);
        firePropertyChange("properties", properties, properties = newProperties);
    }

    /**
     * Get the current available properties.
     * 
     * @return The available properties.
     */
    public List<PropertyDescription> getProperties() {
        return properties;
    }

    /**
     * Set the selected property in the table.
     * 
     * @param selected
     *            The selected property.
     */
    public void setTableSelection(PropertyDescription selected) {
        if (selected != null) {
            String descText = viewModel.getTargetScreen().getMacroDescription(selected.getKey());
            setDescriptionText(descText);
        } else {
            setDescriptionText("");
        }
    }

    private void setTableEnabled(Boolean enabled) {
        firePropertyChange("tableEnabled", tableEnabled, tableEnabled = enabled);
    }

    /**
     * Get whether the table is enabled. (Used in databinding, do not delete)
     * 
     * @return True if the table is enabled
     */
    public Boolean getTableEnabled() {
        return tableEnabled;
    }

    /**
     * Set the text describing a specific property. (Used in databinding, do not
     * delete)
     * 
     * @param text
     *            The text describing the property.
     */
    public void setDescriptionText(String text) {
        firePropertyChange("descriptionText", descriptionText, descriptionText = text);
    }

    /**
     * Get the text describing a specific property. (Used in databinding, do not
     * delete)
     * 
     * @return The text describing the property.
     */
    public String getDescriptionText() {
        return descriptionText;
    }

}
