/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/**
 * DataAcquisitionCombo: on input change: will change background to a colour indicating a change that hasn't been applied to the instrument.
 *
 */
public class DaeExperimentSetupCombo extends Combo {

    private Composite panel;
    private String cachedValue;
    private PanelViewModel panelViewModel;
    private String name;
    private ExperimentSetupViewModel experimentSetupViewModel;
    
    /**
     * Create a new data acquisition text input.
     * 
     * @param parent a composite control which will be the parent of the new instance (cannot be null).
     * @param style the style of control to construct.
     * @param panelViewModel the panelViewModel to help with the editing the panels.
     * @param name the name of the widget, which is used as a key in the cached values map.
     */
    public DaeExperimentSetupCombo(Composite parent, int style, PanelViewModel panelViewModel, String name) {
        super(parent, style);
        this.panelViewModel = panelViewModel;
        this.panel = parent;
        
        this.name = name;
        experimentSetupViewModel = panelViewModel.getExperimentSetupViewModel();
        
        addComboSelectionListeners();
    }
    
    @Override
    protected void checkSubclass() {
        // Allow sub-classing
    }
    
    /**
     * Adds a listener to a combo box to colour the background of the associated label upon change to the boxes' selection.
     * The associated panel's background is coloured as colouring the combo box colours the selection inside it.
     * 
     */
    public void addComboSelectionListeners() {
        addSelectionListener(new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                ifValueDifferentFromCachedValueThenChangeLabel();
            }
        });
    }
    
    /**
     * Removes all selection listeners on the given combo box.
     */
    public void removeComboSelectionListeners() {
        Listener[] listeners = getListeners(SWT.Selection);
        for (Listener listener : listeners) {
            removeListener(SWT.Selection, listener);
        }
    }
    
    
    /**
     * Will set a label denoting a change that has not been applied to the instrument and notifies the dae view model.
     */
    public void ifValueDifferentFromCachedValueThenChangeLabel() {
        if ((getText()).equals(cachedValue)) {
            panel.setBackground(panelViewModel.getColour("unchangedColour"));
            panelViewModel.setIsChanged(name, false);
        } else {
            panel.setBackground(panelViewModel.getColour("changedColour"));
            panelViewModel.setIsChanged(name, true);
        }
    }

    /**
     * Resets the cached value.
     */
    public void resetCachedValue() {
        cachedValue = getText();
        experimentSetupViewModel.addtoCachedValues(name, cachedValue);
    }
    
    /**
     * Sets cached value for the combo.
     * 
     * @param cachedValue
     *                  The value to use as cached value.
     */
    public void setCachedValue(String cachedValue) {
        this.cachedValue = cachedValue;
        experimentSetupViewModel.addtoCachedValues(name, cachedValue);
    }
    
    /** 
     * Creates a cached value for the first time, used when after the panels are created when they have been first initialised.
     */
    public void createInitialCachedValue() {
        if (experimentSetupViewModel.getItemFromCachedValues(name).isEmpty()) {
            resetCachedValue();
        } else {
            cachedValue = experimentSetupViewModel.getItemFromCachedValues(name);
        }
    }
}
