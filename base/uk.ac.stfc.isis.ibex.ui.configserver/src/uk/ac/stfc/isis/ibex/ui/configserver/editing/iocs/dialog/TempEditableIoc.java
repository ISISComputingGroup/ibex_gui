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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

/**
 * A temporary IOC that is a copy of the original but is only saved to the
 * configuration when explicitly called.
 */
public class TempEditableIoc extends EditableIoc {
    private final EditableIoc editingIoc;
    private Collection<Macro> macros;

    /**
     * Constructor for the temp IOC.
     * 
     * @param ioc
     *            The IOC that this temp ioc is based on
     */
    public TempEditableIoc(EditableIoc ioc) {
        super(ioc);
        this.editingIoc = ioc;
        
        this.macros = deepCopyMacros(editingIoc.getMacros());
    }
    
    @Override
    public Collection<Macro> getMacros() {
        return macros;
    }
    
    @Override
    public void setMacros(final Collection<Macro> macros) {
        this.macros = deepCopyMacros(macros);
    }

    /**
     * Saves the information in this IOC to the actual one.
     */
    public void saveIoc() {
        editingIoc.setRestart(getRestart());
        editingIoc.setAutostart(getAutostart());
        editingIoc.setSimLevel(getSimLevel());
        editingIoc.setMacros(getMacros());
        editingIoc.setPvs(getPvs());
        editingIoc.setPvSets(getPvSets());
        editingIoc.setRemotePvPrefix(getRemotePvPrefix());
    }
    
    /**
     * Create a deep, fully independent copy of a collection of macros.
     * @param macros the macros to copy from
     * @return a deep copy of the macros.
     */
    private static final Collection<Macro> deepCopyMacros(final Collection<Macro> macros) {
        var result = new ArrayList<Macro>();
        for (Macro macro : macros) {
            result.add(new Macro(macro));
        }
        return result;
    }

}
