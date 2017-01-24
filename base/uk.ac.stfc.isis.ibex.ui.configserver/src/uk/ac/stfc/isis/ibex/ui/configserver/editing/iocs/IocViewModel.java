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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;

/**
 *
 */
// TODO Validations
public class IocViewModel extends ErrorMessageProvider {

    private String name;
    private boolean autoStart;
    private boolean autoRestart;
    private SimLevel simLevel;

    private EditableIoc editingIoc;
    private EditableConfiguration config;

    public IocViewModel(EditableConfiguration config) {
        this.config = config;
        init();
    }

    private void init() {
        if (editingIoc != null) {
            name = editingIoc.getName();
            autoStart = editingIoc.getAutostart();
            autoRestart = editingIoc.getRestart();
            simLevel = editingIoc.getSimLevel();
        } else {
            name = "";
            autoStart = false;
            autoRestart = false;
            simLevel = SimLevel.NONE;
        }
    }

    public EditableIoc getIoc() {
        return this.editingIoc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        firePropertyChange("name", this.name, this.name = name);
    }

    public boolean isAutoRestart() {
        return autoRestart;
    }

    public void setAutoRestart(boolean enabled) {
        firePropertyChange("autoRestart", this.autoRestart, this.autoRestart = enabled);
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean enabled) {
        firePropertyChange("autoStart", this.autoStart, this.autoStart = enabled);
    }

    public int getSimLevel() {
        return this.simLevel.ordinal();
    }

    public void setSimLevel(int index) {
        SimLevel simLevel = SimLevel.values()[index];
        firePropertyChange("simLevel", this.simLevel, this.simLevel = simLevel);
    }

    public void updateIoc() {
        editingIoc.setRestart(autoRestart);
        editingIoc.setAutostart(autoStart);
        editingIoc.setSimLevel(simLevel);
    }

    public void setIoc(EditableIoc ioc) {
        this.editingIoc = ioc;
        init();
    }

    public void setIocByName() {
        for (EditableIoc ioc : config.getSelectedIocs()) {
            if (ioc.getName().equals(name)) {
                editingIoc = ioc;
                break;
            }
        }
        for (EditableIoc ioc : config.getUnselectedIocs()) {
            if (ioc.getName().equals(name)) {
                editingIoc = ioc;
                break;
            }
        }
    }
}
