
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/**
 * DataAcquisitionButton: on press: will change background to a colour indicating a change that hasn't been applied to the instrument.
 *
 */
public class DaeExperimentSetupButton extends Button {

    private PanelViewModel panelViewModel;
    private boolean cachedValue;
    private String name;
    private ExperimentSetupViewModel experimentSetupViewModel;
    
    /**
     * Create a new data acquisition button button.
     * 
     * @param parent a composite control which will be the parent of the new instance (cannot be null)
     * @param style the style of control to construct
     * @param panelViewModel the panelViewModel to help with the editing the panels.
     * @param name the name of the widget, which is used as a key in the cached values map.
     */
    public DaeExperimentSetupButton(Composite parent, int style, PanelViewModel panelViewModel, String name) {
        super(parent, style);
        this.panelViewModel = panelViewModel;
        
        this.name = name;
        experimentSetupViewModel = panelViewModel.getExperimentSetupViewModel();
        
        addBtnSelectionListener();
    }
    
    /**
     * Resets the cached value when the button is created.
     */
    public void resetCachedValue() {
        cachedValue = this.getSelection();
        if (cachedValue) {
            experimentSetupViewModel.addtoCachedValues(name, "true");
        } else {
            experimentSetupViewModel.addtoCachedValues(name, "false");
        }
    }

    @Override
    protected void checkSubclass() {
        // Allow sub-classing
    }
    
    /**
     * Adds a listener to a button to colour its background upon selection of the button.
     */
    public void addBtnSelectionListener() {
        addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ifValueDifferentFromCachedValueThenChangeLabel();
            }
        });
    }
    
    /**
     * Removes all selection listeners on the button.
     * 
     */
    public void removeBtnSelectionListener() {
        Listener[] listeners = this.getListeners(SWT.Selection);
        for (Listener listener : listeners) {
            this.removeListener(SWT.Selection, listener);
        }
    }
    
    
    /**
     * Will set a label denoting a change that has not been applied to the instrument and notifies the dae view model.
     * 
     */
    public void ifValueDifferentFromCachedValueThenChangeLabel() {
        if (getSelection() == cachedValue) {
            setBackground(panelViewModel.getColour("unchangedColour"));
            panelViewModel.setIsChanged(name, false);
        } else {
            setBackground(panelViewModel.getColour("changedColour"));
            panelViewModel.setIsChanged(name, true);
        }
    }
    
    /** 
     * Creates a cached value for the first time, used when after the panels are created when they have been first initialised.
     */
    public void createInitialCachedValue() {
        if (experimentSetupViewModel.getItemFromCachedValues(name).isEmpty()) {
            resetCachedValue();
        } else if (experimentSetupViewModel.getItemFromCachedValues(name).equals("true")) {
            cachedValue = true;
        } else {
            cachedValue = false;
        }
    }
}

