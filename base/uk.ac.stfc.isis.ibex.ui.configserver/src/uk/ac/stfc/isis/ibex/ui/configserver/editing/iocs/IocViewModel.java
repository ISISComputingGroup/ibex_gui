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
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A view model storing an IOCs properties for displaying and editing in the
 * GUI, to be saved in the IOC once changes have been confirmed.
 */
public class IocViewModel extends ModelObject {

    private String name;
    private boolean autoStart;
    private boolean autoRestart;
    private SimLevel simLevel;

    private EditableIoc editingIoc;
    private EditableConfiguration config;
    private Collection<Macro> macros;
    private Collection<PVDefaultValue> pvVals;
    private Collection<PVSet> pvSets;

    /**
     * Constructor for the IOC view model.
     * 
     * @param config
     *            The configuration this IOC is part of.
     */
    public IocViewModel(EditableConfiguration config) {
        this.config = config;
        init();
    }

    /**
     * Initialises view model values.
     */
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

    /**
     * Makes an editing copy of the macros available to the IOC.
     * 
     * @param macros
     *            The available macros.
     * @return A copy of the available macros.
     */
    private Collection<Macro> copyMacros(Collection<Macro> macros) {
        Collection<Macro> copied = new ArrayList<Macro>();
        for (Macro macro : macros) {
            copied.add(new Macro(macro));
        }
        return copied;
    }

    /**
     * Makes an editing copy of the IOCs default PV values.
     * 
     * @param pvVals
     *            The IOCs default PV values.
     * @return A copy of the default PV values.
     */
    private Collection<PVDefaultValue> copyPvVals(Collection<PVDefaultValue> pvVals) {
        Collection<PVDefaultValue> copied = new ArrayList<PVDefaultValue>();
        for (PVDefaultValue pvVal : pvVals) {
            copied.add(new PVDefaultValue(pvVal));
        }
        return copied;
    }

    /**
     * Makes an editing copy of the PV sets available to the IOC.
     * 
     * @param macros
     *            The available PV sets.
     * @return A copy of the available PV sets.
     */
    private Collection<PVSet> copyPvSets(Collection<PVSet> pvSets) {
        Collection<PVSet> copied = new ArrayList<PVSet>();
        for (PVSet pvSet : pvSets) {
            copied.add(new PVSet(pvSet));
        }
        return copied;
    }

    /**
     * Saves the IOC with new values from the view model.
     */
    public void saveIoc() {
        editingIoc.setRestart(autoRestart);
        editingIoc.setAutostart(autoStart);
        editingIoc.setSimLevel(simLevel);
        editingIoc.setMacros(macros);
        editingIoc.setPvs(pvVals);
        editingIoc.setPvSets(pvSets);
    }

    /**
     * @return The editing IOC
     */
    public EditableIoc getIoc() {
        return this.editingIoc;
    }

    /**
     * Sets the IOC.
     * 
     * @param ioc
     *            The IOC
     */
    public void setIoc(EditableIoc ioc) {
        firePropertyChange("ioc", this.editingIoc, this.editingIoc = ioc);
        init();
    }

    /**
     * Sets the IOC by IOC name.
     * 
     * @param name
     *            The IOC name
     */
    public void setIocByName(String name) {
        for (EditableIoc ioc : config.getSelectedIocs()) {
            if (ioc.getName().equals(name)) {
                setIoc(ioc);
                break;
            }
        }
        for (EditableIoc ioc : config.getAvailableIocs()) {
            if (ioc.getName().equals(name)) {
                setIoc(new EditableIoc(ioc));
                break;
            }
        }
    }

    /**
     * Updates the IOC associated to the view model based on the currently set
     * IOC name.
     */
    public void updateIoc() {
        setIocByName(name);
    }

    /**
     * @return The IOC name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the IOC name.
     * 
     * @param name
     *            The IOC name
     */
    public void setName(String name) {
        firePropertyChange("name", this.name, this.name = name);
    }

    /**
     * @return Whether this IOC is set to Auto-Restart
     */
    public boolean isAutoRestart() {
        return autoRestart;
    }

    /**
     * Sets the IOCs Auto-Restart.
     * 
     * @param enabled
     *            The Auto-Restart enabled status.
     */
    public void setAutoRestart(boolean enabled) {
        firePropertyChange("autoRestart", this.autoRestart, this.autoRestart = enabled);
    }

    /**
     * @return Whether this IOC is set to Auto-Start
     */
    public boolean isAutoStart() {
        return autoStart;
    }

    /**
     * Sets the IOCs Auto-Start.
     * 
     * @param enabled
     *            The Auto-Start enabled status.
     */
    public void setAutoStart(boolean enabled) {
        firePropertyChange("autoStart", this.autoStart, this.autoStart = enabled);
    }

    /**
     * @return The ordinal of the IOCs Simulation Level.
     */
    public int getSimLevel() {
        return this.simLevel.ordinal();
    }

    /**
     * Sets the IOCs Simulation Level by ordinal.
     * 
     * @param index
     *            The Simulation Level index.
     */
    public void setSimLevel(int index) {
        SimLevel simLevel = SimLevel.values()[index];
        firePropertyChange("simLevel", this.simLevel, this.simLevel = simLevel);
    }

    /**
     * @return the IOCs macros
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
     * @return the IOCs pvVals
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
     * @return the IOCs pvSets
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
