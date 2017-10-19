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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.selector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

/**
 * The view model for the target selector.
 */
public class TargetSelectorViewModel extends ModelObject {
    
    private final SynopticViewModel synopticViewModel;
    private boolean enabled;
    private String name;
    private String opi;
    private int icon;
    private String description;
    
    private static final String NONE_OPI = "NONE";
    private final List<String> componentTypesList = ComponentType.componentTypeAlphaList();
    
    /**
     * An array of component types for populating the drop down icon selector in the synoptic editor.
     */
    public final String[] componentTypesArray = componentTypesList.toArray(new String[0]);
    
    /**
     * View model for the target selector panel.
     * @param synopticViewModel the main synoptic view model
     */
    public TargetSelectorViewModel(final SynopticViewModel synopticViewModel) {
        this.synopticViewModel = synopticViewModel;
        addEnablementListener();
    }
    
    private void addEnablementListener() {
        synopticViewModel.addPropertyChangeListener("selectedComponents", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (synopticViewModel.getSingleSelectedComp() != null) {
                    setEnabled(true);
                    setName(synopticViewModel.getSingleSelectedComp().getName());
                    setIconSelectionIndex(componentTypesList.indexOf(synopticViewModel.getSingleSelectedComp().type().name()));
                    setOpi(synopticViewModel.getSingleSelectedComp().target().name());
                } else {
                    setName("");
                    setIconSelectionIndex(0);
                    setOpi(NONE_OPI);
                    setEnabled(false);
                }
            }
        });
    }
    
    /**
     * Whether the panel is enabled.
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Sets whether the panel should be enabled.
     * @param enabled true to enable, false to disable
     */
    public void setEnabled(boolean enabled) {
        firePropertyChange("enabled", this.enabled, this.enabled = enabled);
    }
    
    /**
     * Gets the name of this component.
     * @return the name of this component
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the name of this component.
     * @param name the new name
     */
    public void setName(String name) {
        if (synopticViewModel.getSingleSelectedComp() == null) {
            return;
        }
        firePropertyChange("name", this.name, this.name = name);
        synopticViewModel.getSingleSelectedComp().setName(name);
        synopticViewModel.refreshTreeView();
    }
    
    /**
     * Gets the index of the icon within the TargetSelectorPanelViewModel.componentTypesList.
     * @return the selection index
     */
    public int getIconSelectionIndex() {
        return icon;
    }
    
    /**
     * Sets the index of the icon within the TargetSelectorPanelViewModel.componentTypesList.
     * @param icon the selection index.
     */
    public void setIconSelectionIndex(int icon) { 
        if (synopticViewModel.getSingleSelectedComp() == null) {
            return;
        }
        
        synopticViewModel.getSingleSelectedComp().setType(ComponentType.valueOf(componentTypesList.get(icon)));
        
        firePropertyChange("iconSelectionIndex", this.icon, this.icon = icon);
        synopticViewModel.refreshTreeView();
    }
    
    /**
     * The selected OPI name.
     * @return the opi name
     */
    public String getOpi() {
        return opi;
    }
    
    /**
     * Sets the selected OPI by name.
     * @param opi the selected OPI name
     */
    public void setOpi(String opi) {
        if (synopticViewModel.getSingleSelectedComp() == null) {
            return;
        }
        synopticViewModel.getSingleSelectedComp().setTarget(new TargetDescription(opi, TargetType.OPI));
        synopticViewModel.broadcastInstrumentUpdate(UpdateTypes.EDIT_TARGET);
        
        setDescription(synopticViewModel.getOpi(opi).getDescription());
        
        firePropertyChange("opi", this.opi, this.opi = opi);
    }
    
    /**
     * Gets the description of the currently selected OPI.
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description of the currently selected OPI.
     * @param description the new description
     */
    public void setDescription(String description) {
        firePropertyChange("description", this.description, this.description = description);
    }
    
    /**
     * Clears the target OPI.
     */
    public void clearTarget() {
        setOpi(NONE_OPI);
    }

    /**
     * Gets the list of OPI names in categories.
     * @return a map of lists where each index in the map is a category, and the list is the OPIs within that category
     */
    public Map<String, List<String>> getAvailableOpis() {
        return synopticViewModel.getAvailableOpis();
    }
}
