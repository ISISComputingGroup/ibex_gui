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
    private String opi = NONE_OPI;
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
                    setOpi(synopticViewModel.getSingleSelectedComp().target().name(), false);
                    setIconSelectionIndex(componentTypesList.indexOf(synopticViewModel.getSingleSelectedComp().type().name()));
                } else {
                    setName("");
                    setIconSelectionIndex(0);
                    setOpi(NONE_OPI, false);
                    setEnabled(false);
                }
                
                // Re-fire the event so that it can be listened to in the view
                firePropertyChange("selectedComponents", evt.getOldValue(), evt.getNewValue());
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
        name = name == null ? null : name.trim();
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
     * Sets the selected OPI by name. Decides whether to set the icon based on whether the current OPI has a default icon.
     * @param opi the selected OPI name
     */
    public void setOpi(String opi) { 
        if (this.opi.equals(opi)) {
            return; // Do nothing if reselecting current OPI.
        }
        String currentlySelectedOpiType = synopticViewModel.getOpi(this.opi).getType();
        int iconSelectionIndexForCurrentlySelectedOpi = componentTypesList.indexOf(currentlySelectedOpiType);        
        boolean isIconDefault = getIconSelectionIndex() == 0 || iconSelectionIndexForCurrentlySelectedOpi == getIconSelectionIndex();
        
        setOpi(opi, isIconDefault);
        
        clearProperties();
    }
    
    private void clearProperties() {
        if (synopticViewModel.getSingleSelectedComp() == null) {
            return;
        }
        synopticViewModel.getSingleSelectedComp().target().clearProperties();
        synopticViewModel.broadcastInstrumentUpdate(UpdateTypes.EDIT_TARGET);
    }
    
    /**
     * Sets the selected OPI.
     * @param opi the selected OPI name
     * @param changeIcon whether to change the component icon to the default for the new OPI.
     */
    private void setOpi(String opi, boolean changeIcon) {
        if (synopticViewModel.getSingleSelectedComp() == null) {
            return;
        }
        
        TargetDescription currentTarget = synopticViewModel.getSingleSelectedComp().target();
        TargetDescription newTarget;
        
        if (currentTarget == null) {
            newTarget = new TargetDescription(opi, TargetType.OPI);
        } else {
            newTarget = new TargetDescription(currentTarget);
            newTarget.setType(TargetType.OPI);
            newTarget.setName(opi);
        }
        
        synopticViewModel.getSingleSelectedComp().setTarget(newTarget);    
        
        setDescription(synopticViewModel.getOpi(opi).getDescription());
        
        firePropertyChange("opi", this.opi, this.opi = opi);
        
        if (changeIcon) {
            int index = componentTypesList.indexOf(synopticViewModel.getOpi(opi).getType());
            setIconSelectionIndex(index < 0 ? 0 : index);
        }
        synopticViewModel.broadcastInstrumentUpdate(UpdateTypes.EDIT_TARGET);
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
        setIconSelectionIndex(0);
    }

    /**
     * Gets the list of OPI names in categories.
     * @return a map of lists where each index in the map is a category, and the list is the OPIs within that category
     */
    public Map<String, List<String>> getAvailableOpis() {
        return synopticViewModel.getAvailableOpis();
    }
}
