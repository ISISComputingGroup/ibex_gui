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

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.PVDefaultValue;
import uk.ac.stfc.isis.ibex.configserver.configuration.PVSet;
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
    private Collection<Macro> macros;
    private Collection<PVDefaultValue> pvVals;
    private Collection<PVSet> pvSets;

    public IocViewModel(EditableConfiguration config) {
        this.config = config;
        init();
    }

    private void init() {
        if (editingIoc != null) {
            setName(editingIoc.getName());
            setAutoStart(editingIoc.getAutostart());
            setAutoRestart(editingIoc.getRestart());
            setSimLevel(editingIoc.getSimLevel().ordinal());
            setMacros(copyMacros(editingIoc.getMacros()));
            setPvVals(copyPvVals(editingIoc.getPvs()));
            setPvSets(copyPvSets(editingIoc.getPvSets()));
        } else {
            setName("");
            setAutoStart(false);
            setAutoRestart(false);
            setSimLevel(SimLevel.NONE.ordinal());
            setMacros(new ArrayList<Macro>());
            setPvVals(new ArrayList<PVDefaultValue>());
            setPvSets(new ArrayList<PVSet>());
        }
    }

    private Collection<Macro> copyMacros(Collection<Macro> macros) {
        Collection<Macro> copied = new ArrayList<Macro>();
        for (Macro macro : macros) {
            copied.add(new Macro(macro));
        }
        return copied;
    }

    private Collection<PVDefaultValue> copyPvVals(Collection<PVDefaultValue> pvVals) {
        Collection<PVDefaultValue> copied = new ArrayList<PVDefaultValue>();
        for (PVDefaultValue pvVal : pvVals) {
            copied.add(new PVDefaultValue(pvVal));
        }
        return copied;
    }

    private Collection<PVSet> copyPvSets(Collection<PVSet> pvSets) {
        Collection<PVSet> copied = new ArrayList<PVSet>();
        for (PVSet pvSet : pvSets) {
            copied.add(new PVSet(pvSet));
        }
        return copied;
    }

    public void updateIoc() {
        editingIoc.setRestart(autoRestart);
        editingIoc.setAutostart(autoStart);
        editingIoc.setSimLevel(simLevel);
        editingIoc.setMacros(macros);
        editingIoc.setPvs(pvVals);
        editingIoc.setPvSets(pvSets);
    }

    public EditableIoc getIoc() {
        return this.editingIoc;
    }

    public void setIoc(EditableIoc ioc) {
        firePropertyChange("ioc", this.editingIoc, this.editingIoc = ioc);
        init();
    }

    public void setIocByName(String name) {
        for (EditableIoc ioc : config.getSelectedIocs()) {
            if (ioc.getName().equals(name)) {
                setIoc(ioc);
                break;
            }
        }
        for (EditableIoc ioc : config.getUnselectedIocs()) {
            if (ioc.getName().equals(name)) {
                setIoc(new EditableIoc(ioc));
                break;
            }
        }
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

    /**
     * @return the macros
     */
    public Collection<Macro> getMacros() {
        return macros;
    }

    /**
     * @param macros
     *            the macros to set
     */
    public void setMacros(Collection<Macro> macros) {
        firePropertyChange("macros", this.macros, this.macros = macros);
    }

    /**
     * @return the pvVals
     */
    public Collection<PVDefaultValue> getPvVals() {
        return pvVals;
    }

    /**
     * @param pvVals
     *            the pvVals to set
     */
    public void setPvVals(Collection<PVDefaultValue> pvVals) {
        firePropertyChange("pvVals", this.pvVals, this.pvVals = pvVals);
    }

    /**
     * @return the pvSets
     */
    public Collection<PVSet> getPvSets() {
        return pvSets;
    }

    /**
     * @param pvSets
     *            the pvSets to set
     */
    public void setPvSets(Collection<PVSet> pvSets) {
        firePropertyChange("pvSets", this.pvSets, this.pvSets = pvSets);
    }

}
