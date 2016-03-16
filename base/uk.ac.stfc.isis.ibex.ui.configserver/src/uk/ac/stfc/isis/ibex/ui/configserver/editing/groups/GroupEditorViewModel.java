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

import uk.ac.stfc.isis.ibex.configserver.Editing;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableGroup;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;

/**
 * 
 */
public class GroupEditorViewModel {

    private Editing editingModel;
    private UpdatedObservableAdapter<EditableConfiguration> observableConfigModel;

    boolean canEditSelected = true;

    public GroupEditorViewModel() {

    }

    public void bind(Editing editingModel) {
        this.editingModel = editingModel;
        setNewModel();
    }

    public void setNewModel() {
        observableConfigModel = new UpdatedObservableAdapter<>(
                ConfigurationServerUI.getDefault().groupEditorViewModel().getCurrentConfigModel());
    }

    public ForwardingObservable<EditableConfiguration> getCurrentConfigModel() {
        return editingModel.currentConfig();
    }

    public UpdatedObservableAdapter<EditableConfiguration> getObservableConfig() {
        return observableConfigModel;
    }

    public void addNewGroup() {
        observableConfigModel.getValue().addNewGroup();
    }

    public void removeGroup(int selectedGroup) {
        if (canEditSelected && selectedGroup != -1) {
            ArrayList<EditableGroup> groups = new ArrayList<>(observableConfigModel.getValue().getEditableGroups());

            EditableGroup group = groups.get(selectedGroup);
            observableConfigModel.getValue().removeGroup(group);
        }
    }

    public void moveGroupUp(int selectedGroup) {
        ArrayList<EditableGroup> groups = new ArrayList<>(observableConfigModel.getValue().getEditableGroups());

        if (selectedGroup > 0) {
            swapGroups(groups, selectedGroup, selectedGroup - 1);
        }
    }

    public void moveGroupDown(int selectedGroup) {
        ArrayList<EditableGroup> groups = new ArrayList<>(observableConfigModel.getValue().getEditableGroups());

        if (selectedGroup < groups.size() - 1) {
            swapGroups(groups, selectedGroup, selectedGroup + 1);
        }
    }

    private void swapGroups(ArrayList<EditableGroup> groups, int selectedGroup, int groupToSwapWith) {
        EditableGroup group1 = groups.get(selectedGroup);
        EditableGroup group2 = groups.get(groupToSwapWith);

        if (group1 != null && group2 != null) {
            observableConfigModel.getValue().swapGroups(group1, group2);
        }
    }

}
