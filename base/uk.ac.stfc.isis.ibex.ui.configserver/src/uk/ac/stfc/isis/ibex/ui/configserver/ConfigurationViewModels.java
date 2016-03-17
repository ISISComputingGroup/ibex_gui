
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
package uk.ac.stfc.isis.ibex.ui.configserver;

import uk.ac.stfc.isis.ibex.configserver.Editing;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.groups.GroupEditorViewModel;

/**
 * This class can hold a number of different view models, all with the same
 * observable configuration.
 */
public class ConfigurationViewModels {

    private Editing editingModel;
    private UpdatedObservableAdapter<EditableConfiguration> observableConfigModel;

    private GroupEditorViewModel groupEditorViewModel;

    public ConfigurationViewModels() {
        groupEditorViewModel = new GroupEditorViewModel();
    }

    public void bind(Editing editingModel) {
        this.editingModel = editingModel;
        setModelAsCurrentConfig();
    }

    public GroupEditorViewModel groupEditorViewModel() {
        return groupEditorViewModel;
    }

    public void setModelAsCurrentConfig() {
        observableConfigModel = new UpdatedObservableAdapter<>(getCurrentConfig());
        updateViewModels();
    }

    public void setModelAsConfig(String configName) {
        observableConfigModel = new UpdatedObservableAdapter<>(getConfig(configName));
        updateViewModels();
    }

    public void setModelAsComponent(String componentName) {
        observableConfigModel = new UpdatedObservableAdapter<>(getComponent(componentName));
        updateViewModels();
    }

    public void setModelAsBlankConfig() {
        observableConfigModel = new UpdatedObservableAdapter<>(getBlankConfig());
        updateViewModels();
    }

    private void updateViewModels() {
        groupEditorViewModel.updateModel(this.observableConfigModel);
    }

    private ForwardingObservable<EditableConfiguration> getCurrentConfig() {
        return editingModel.currentConfig();
    }

    private ForwardingObservable<EditableConfiguration> getConfig(String configName) {
        return editingModel.config(configName);
    }

    private ForwardingObservable<EditableConfiguration> getComponent(String componentName) {
        return editingModel.component(componentName);
    }

    private ForwardingObservable<EditableConfiguration> getBlankConfig() {
        return editingModel.blankConfig();
    }

    public UpdatedObservableAdapter<EditableConfiguration> getConfigModel() {
        return observableConfigModel;
    }

}
