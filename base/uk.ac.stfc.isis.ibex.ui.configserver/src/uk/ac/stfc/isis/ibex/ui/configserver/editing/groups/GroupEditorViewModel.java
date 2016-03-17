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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.groups;

import java.util.ArrayList;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableGroup;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;

/**
 * This class contains a view model for the groups editor, responsible for the
 * display logic of the list of groups with their associated blocks.
 */
public class GroupEditorViewModel {

    private UpdatedObservableAdapter<EditableConfiguration> observableConfigModel;

    public GroupEditorViewModel() {
    }

    public void updateModel(UpdatedObservableAdapter<EditableConfiguration> observableConfigModel) {
        this.observableConfigModel = observableConfigModel;
    }

    public EditableGroup getSelectedGroup(int selectedGroup) {
        ArrayList<EditableGroup> groups = new ArrayList<>(observableConfigModel.getValue().getEditableGroups());
        
        if (selectedGroup < 0) {
            return null;
        } else {
            return groups.get(selectedGroup);
        }
    }

    public int getNumberOfGroups() {
        return observableConfigModel.getValue().getEditableGroups().size();
    }

    public void addNewGroup() {
        observableConfigModel.getValue().addNewGroup();
    }

    public void removeGroup(int selectedGroup) {
        EditableGroup group = getSelectedGroup(selectedGroup);

        if (group != null) {
            observableConfigModel.getValue().removeGroup(group);
        }
    }

    public void moveGroupUp(int selectedGroup) {
        if (selectedGroup > 0) {
            swapGroups(selectedGroup, selectedGroup - 1);
        }
    }

    public void moveGroupDown(int selectedGroup) {
        if (selectedGroup < getNumberOfGroups() - 1) {
            swapGroups(selectedGroup, selectedGroup + 1);
        }
    }

    private void swapGroups(int selectedGroup, int groupToSwapWith) {
        EditableGroup group1 = getSelectedGroup(selectedGroup);
        EditableGroup group2 = getSelectedGroup(groupToSwapWith);

        if (group1 != null && group2 != null) {
            observableConfigModel.getValue().swapGroups(group1, group2);
        }
    }

    public boolean canEditSelected(int selectionIndex) {
        EditableGroup group = getSelectedGroup(selectionIndex);

        if (group == null) {
            return false;
        } else {
            return group.isEditable();
        }
    }

    public String componentDetail(int selectionIndex) {
        EditableGroup group = getSelectedGroup(selectionIndex);
        
        if (group == null || group.isEditable()) {
            return "";
        } else {        
            String componentName = group.getComponent() != null ? group.getComponent() : "unknown";
            return "contributed by " + componentName;
        }
    }

}
