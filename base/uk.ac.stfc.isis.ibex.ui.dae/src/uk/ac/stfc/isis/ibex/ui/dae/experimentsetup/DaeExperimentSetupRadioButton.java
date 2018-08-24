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

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/**
 * A class to construct a radio button used in the Dae experiment setup tab.
 *
 */
public class DaeExperimentSetupRadioButton extends Button {
    private PanelViewModel panelViewModel;
    private ArrayList<DaeExperimentSetupRadioButton> btns = new ArrayList<DaeExperimentSetupRadioButton>();
    private ArrayList<Boolean> btnsCachedValues = new ArrayList<Boolean>();
    private String name;
    private ExperimentSetupViewModel experimentSetupViewModel;
    
    /**
     * Create a new data acquisition button button.
     * 
     * @param parent a composite control which will be the parent of the new instance (cannot be null)
     * @param style the style of control to construct
     * @param panelViewModel the panelViewModel to help with the editing the panels.
     * @param name The name for the pair of radio buttons to get the cached values from the view model. This needs to be the same for the set of btns.
     */
    public DaeExperimentSetupRadioButton(Composite parent, int style, PanelViewModel panelViewModel, String name) {
        super(parent, style);
        this.panelViewModel = panelViewModel;
        experimentSetupViewModel = panelViewModel.getExperimentSetupViewModel();
        this.name = name;
    }
    
    /**
     * Adds a listener to a pair of radio buttons to colour their backgrounds upon selection of one of the radio buttons.
     * 
     */
    public void addRadioBtnSelectionListener() {
        SelectionListener listener = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                for (DaeExperimentSetupRadioButton btn : btns) {
                    ifValueDifferentFromCachedValueThenChangeLabel(btn);
                }
            }
        };
        for (Button btn : btns) {
            btn.addSelectionListener(listener);
        }
    }
    
    @Override
    protected void checkSubclass() {
        // Allow sub-classing
    }
    
    /**
     * Removes all selection listeners on the radio buttons contained in the list.
     * 
     */
    public void removeRadioBtnSelectionListener() {
        for (Button btn : btns) {
            Listener[] listeners = btn.getListeners(SWT.Selection);
            for (Listener listener : listeners) {
                btn.removeListener(SWT.Selection, listener);
            }
        }
    }
    
    /**
     * Will set a label denoting a change that has not been applied to the instrument and notifies the dae view model.
     * 
     * @param btn
     *              The widget on which to check for changes.
     */
    public void ifValueDifferentFromCachedValueThenChangeLabel(DaeExperimentSetupRadioButton btn) {
        if (btn.getSelection()) {
            if (btn.getSelection() == btnsCachedValues.get(btns.indexOf(btn))) {
                setBackground(panelViewModel.getColour("unchangedColour"));
                panelViewModel.setIsChanged(name, false);
            } else {
                setBackground(panelViewModel.getColour("changedColour"));
                panelViewModel.setIsChanged(name, true);
            }
        } else {
            btn.setBackground(panelViewModel.getColour("unchangedColour"));
        }
    }
    
    /**
     * Resets the cached value.
     * 
     * @param btnsCachedValues
     *              The list of cached values
     */
    public void resetCachedValue(ArrayList<Boolean> btnsCachedValues) {
        this.btnsCachedValues = btnsCachedValues;
        experimentSetupViewModel.addtoRadioBtnsCachedValues(name, btnsCachedValues);
    }
    
    /**
     * Sets the list of radio btns.
     * 
     * @param btns
     *              The list of radio buttons.
     */
    public void setBtnsList(ArrayList<DaeExperimentSetupRadioButton> btns) {
        this.btns = btns;
    }
    
    /** 
     * Creates a cached value for the first time, used when after the panels are created when they have been first initialised.
     * @param btnsCachedValues
     *                          The list of cached values for the radio buttons.
     */
    public void createInitialCachedValue(ArrayList<Boolean> btnsCachedValues) {
        if (experimentSetupViewModel.getItemFromRadioBtnsCachedValues(name).isEmpty()) {
            resetCachedValue(btnsCachedValues);
        } else {
            this.btnsCachedValues = experimentSetupViewModel.getItemFromRadioBtnsCachedValues(name);
        }
    }
}
