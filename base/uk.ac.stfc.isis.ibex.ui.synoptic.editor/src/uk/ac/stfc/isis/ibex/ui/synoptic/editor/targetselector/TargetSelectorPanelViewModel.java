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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.targetselector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

/**
 *
 */
public class TargetSelectorPanelViewModel extends ModelObject {
    
    private final SynopticViewModel synopticViewModel;
    private boolean enabled = false;
    private String name = "";
    private String opi = "";
    private int icon = 0;
    private String description = "";
    
    private static final String NONE_OPI = "NONE";
    private final List<String> component_types_list = ComponentType.componentTypeAlphaList();
    
    public final String[] component_types_array = component_types_list.toArray(new String[0]);
    
     
    public TargetSelectorPanelViewModel(final SynopticViewModel synopticViewModel){
        this.synopticViewModel = synopticViewModel;
        
        synopticViewModel.addPropertyChangeListener("selectedComponents", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (synopticViewModel.getSingleSelectedComp() != null) {
                    setEnabled(true);
                    setName(synopticViewModel.getSingleSelectedComp().getName());
                    setIcon(component_types_list.indexOf(synopticViewModel.getSingleSelectedComp().type().name()));
                    setOpi(synopticViewModel.getSingleSelectedComp().target().name());
                    
                } else {
                    setEnabled(false);
                    setName("");
                    setIcon(0);
                    setOpi(NONE_OPI);
                }
            }
        });
    }
    
    public boolean isEnabled(){
        return enabled;
    }
    
    public void setEnabled(boolean enabled){
        firePropertyChange("enabled", this.enabled, this.enabled = enabled);
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        if(synopticViewModel.getSingleSelectedComp() == null) {
            return;
        }
        firePropertyChange("name", this.name, this.name = name);
        synopticViewModel.getSingleSelectedComp().setName(name);
        synopticViewModel.refreshTreeView();
    }
    
    public int getIcon(){
        return icon;
    }
    
    public void setIcon(int icon){       
        if (synopticViewModel.getSingleSelectedComp() == null) {
            return;
        }
        
        synopticViewModel.getSingleSelectedComp().setType(ComponentType.valueOf(component_types_list.get(icon)));
        
        firePropertyChange("icon", this.icon, this.icon = icon);
        synopticViewModel.refreshTreeView();
    }
    
    public String getOpi() {
        return opi;
    }
    
    public void setOpi(String opi) {
        if (synopticViewModel.getSingleSelectedComp() == null) {
            return;
        }
        synopticViewModel.getSingleSelectedComp().setTarget(new TargetDescription(opi, TargetType.OPI));
        synopticViewModel.broadcastInstrumentUpdate(UpdateTypes.EDIT_TARGET);
        
        setDescription(synopticViewModel.getOpi(opi).getDescription());
        
        firePropertyChange("opi", this.opi, this.opi = opi);
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        firePropertyChange("description", this.description, this.description = description);
    }
    
    public void clearTarget(){
        setOpi(NONE_OPI);
    }
}
