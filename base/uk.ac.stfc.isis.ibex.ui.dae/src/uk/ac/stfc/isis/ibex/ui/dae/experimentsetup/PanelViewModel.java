
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.instrument.Instrument;


/**
 * A class containing a set of methods used in the DAE panels. 
 *
 */
public class PanelViewModel extends ModelObject {

    private Color changedColour;
    private Color white;
    private Color unchangedColour;
    private ExperimentSetupViewModel experimentSetupViewModel;
    private ExperimentSetup experimentSetup;
    
    private Map<String, Boolean> isChanged = new HashMap<>();
    
    
    /**
     * The constructor for the class.
     * @param experimentSetup
     *                          The experiment setup panel to which the panels are related.
     * @param display
     *                          The display used by the panels.
     * @param experimentSetupViewModel
     *                          The experiment setup view Model currently used.                        
     */
    @SuppressWarnings({ "checkstyle:magicnumber"})
    public PanelViewModel(ExperimentSetup experimentSetup, Display display, ExperimentSetupViewModel experimentSetupViewModel) {
        this.experimentSetupViewModel = experimentSetupViewModel;
        this.experimentSetup = experimentSetup;
        changedColour = display.getSystemColor(SWT.COLOR_YELLOW);
        white = display.getSystemColor(SWT.COLOR_WHITE);
        unchangedColour = new Color(display, 240, 240, 240);
    }
    
    /**
     * Adds the list of radio buttons to the different radio buttons and adds a selection listener to the button.
     * @param radioBtns
     *                  the list of radio buttons.
     */
    public void setBtnsListToRadioButtons(ArrayList<DaeExperimentSetupRadioButton> radioBtns) {
        for (DaeExperimentSetupRadioButton btn : radioBtns) {
            btn.setBtnsList(radioBtns);
            btn.addRadioBtnSelectionListener();
        }
    }
    
    /**
     * Creates a cache of the applied values for the different radio buttons.
     * @param radioBtns
     *                  the list of radio buttons.
     * @param bnsCachedValue
     *                  the list of cached values for the radio buttons.
     */
    public void createInitialRadioButtonsCachedValues(ArrayList<DaeExperimentSetupRadioButton> radioBtns, ArrayList<Boolean> bnsCachedValue) {
        for (DaeExperimentSetupRadioButton btn : radioBtns) {
            btn.createInitialCachedValue(bnsCachedValue);
        }
    }
    
    /**
     * Creates a cache of the applied values for the different buttons.
     * @param daeExpSetupBtns
     *                  the list of buttons.
     */
    public void createInitialButtonsCachedValues(ArrayList<DaeExperimentSetupButton> daeExpSetupBtns) {
        for (DaeExperimentSetupButton btn : daeExpSetupBtns) {
            btn.createInitialCachedValue();
        }
    }
    
    /**
     * Creates a cache of the applied values for the different spinners.
     * @param spinners
     *                  the list of spinners.
     */
    public void createInitialSpinnersCachedValues(ArrayList<DaeExperimentSetupSpinner> spinners) {
        for (DaeExperimentSetupSpinner spinner : spinners) {
            spinner.createInitialCachedValue();
        }
    }
    
    /**
     * Creates a cache of the applied values for the different text inputs.
     * 
     * @param textInputs 
     *                  the list of textInputs
     */
    public void createInitialTextInputsCachedValues(ArrayList<DaeExperimentSetupText> textInputs) {
        for (DaeExperimentSetupText txt : textInputs) {
            txt.createInitialCachedValue();
        }
    }
    
    /**
     * Creates a cache of the applied values for the different combos.
     * @param combos 
     *                  the list of combos.
     */
    public void createInitialComboCachedValues(ArrayList<DaeExperimentSetupCombo> combos) {
        for (DaeExperimentSetupCombo cmb : combos) {
            cmb.createInitialCachedValue();
        }
    }
    
    /**
     * Creates a cache of the applied values for the different table viewers.
     * @param viewer 
     *                  the viewer.
     */
    public void createInitialTableViewerCachedValues(DaeExperimentSetupTableViewer viewer) {
            viewer.createInitialCachedValue();
    }
    
    /**
     * Resets the cache of the applied values for the different radio buttons.
     * @param radioBtns
     *                  the list of radio buttons.
     * @param bnsCachedValue
     *                  the list of cached values for the radio buttons.
     */
    public void resetRadioButtonsCachedValues(ArrayList<DaeExperimentSetupRadioButton> radioBtns, ArrayList<Boolean> bnsCachedValue) {
        for (DaeExperimentSetupRadioButton btn : radioBtns) {
            btn.resetCachedValue(bnsCachedValue);
        }
    }
    
    /**
     * Resets the cache of the applied values for the different buttons.
     * @param daeExpSetupBtns
     *                  the list of buttons.
     */
    public void resetButtonsCachedValues(ArrayList<DaeExperimentSetupButton> daeExpSetupBtns) {
        for (DaeExperimentSetupButton btn : daeExpSetupBtns) {
            btn.resetCachedValue();
        }
    }
    
    /**
     * Resets the cache of the applied values for the different spinners.
     * @param spinners
     *                  the list of spinners.
     */
    public void resetSpinnersCachedValues(ArrayList<DaeExperimentSetupSpinner> spinners) {
        for (DaeExperimentSetupSpinner spinner : spinners) {
            spinner.resetCachedValue();
        }
    }
    
    /**
     * Resets the cache of the applied values for the different text inputs.
     * 
     * @param textInputs 
     *                  the list of textInputs
     */
    public void resetTextInputsCachedValues(ArrayList<DaeExperimentSetupText> textInputs) {
        for (DaeExperimentSetupText txt : textInputs) {
            txt.resetCachedValue();
        }
    }
    
    /**
     * Resets the cache of the applied values for the different combos.
     * @param combos 
     *                  the list of combos.
     */
    public void resetComboCachedValues(ArrayList<DaeExperimentSetupCombo> combos) {
        for (DaeExperimentSetupCombo cmb : combos) {
            cmb.resetCachedValue();
        }
    }
    
    /**
     * Resets the cache of the applied values for the viewer.
     * @param viewer 
     *                  the viewer.
     */
    public void resetTableViewerCachedValues(DaeExperimentSetupTableViewer viewer) {
            viewer.resetCachedValue();
    }
    
    /**
     * Removes the listeners on the spinners.
     * @param spinners
     *                  the list of spinners.
     */
    public void removesSpinnerListeners(ArrayList<DaeExperimentSetupSpinner> spinners) {
        for (DaeExperimentSetupSpinner spinner : spinners) {
            spinner.removeSpinnerPropertyChangeListeners();
        }
    }
    
    /**
     * Removes the listeners on the radio buttons.
     * @param radioBtns
     *                  the list of radio buttons.
     */
    public void removesRadioButtonsListener(ArrayList<DaeExperimentSetupRadioButton> radioBtns) {
        for (DaeExperimentSetupRadioButton btn : radioBtns) {
            btn.removeRadioBtnSelectionListener();
        }
    }
    
    /**
     * Removes the listeners on the buttons.
     * @param daeExpSetupBtns
     *                  the list of buttons.
     */
    public void removesButtonListeners(ArrayList<DaeExperimentSetupButton> daeExpSetupBtns) {
        for (DaeExperimentSetupButton btn : daeExpSetupBtns) {
            btn.removeBtnSelectionListener();
        }
    }
    
    /**
     * Removes the listeners on the text inputs.
     * @param textInputs 
     *                  the list of textInputs.
     */
    public void removesTextInputListeners(ArrayList<DaeExperimentSetupText> textInputs) {
        for (DaeExperimentSetupText txt : textInputs) {
            txt.removeTextInputPropertyChangeListeners();
        }
    }
    
    /**
     * Removes the listeners on the combos.
     * @param combos 
     *                  the list of combos.
     */
    public void removesCombosListeners(ArrayList<DaeExperimentSetupCombo> combos) {
        for (DaeExperimentSetupCombo cmb : combos) {
            cmb.removeComboSelectionListeners();
        }
    }
    
    /**
     * Removes the listeners on the viewer.
     * @param viewer 
     *                  the viewer.
     */
    public void removesTableViewersListeners(DaeExperimentSetupTableViewer viewer) {
            viewer.removeTableListeners();
    }
    
    /**
     * Removes the listeners on the radio buttons.
     * @param radioBtns
     *                  the list of radio buttons.
     */
    public void ifRadioButtonValuesDifferentFromCachedValuesThenChangeLabel(ArrayList<DaeExperimentSetupRadioButton> radioBtns) {
        for (DaeExperimentSetupRadioButton btn : radioBtns) {
            btn.ifValueDifferentFromCachedValueThenChangeLabel(btn);
        }
    }
    
    /**
     * If the states are different from the values applied on the instrument then a label denoting the change is applied.
     * @param daeExpSetupBtns
     *                  the list of buttons.
     */
    public void ifButtonValuesDifferentFromCachedValueThenChangeLabel(ArrayList<DaeExperimentSetupButton> daeExpSetupBtns) {
        for (DaeExperimentSetupButton btn : daeExpSetupBtns) {
            btn.ifValueDifferentFromCachedValueThenChangeLabel();
        }
    }
    
    /**
     * If the states are different from the values applied on the instrument then a label denoting the change is applied.
     * @param spinners
     *                  the list of spinners.
     */
    public void ifSpinnerValuesDifferentFromCachedValueThenChangeLabel(ArrayList<DaeExperimentSetupSpinner> spinners) {
        for (DaeExperimentSetupSpinner spinner : spinners) {
            spinner.ifValueDifferentFromCachedValueThenChangeLabel();
        }
    }
    
    /**
     * If the states are different from the values applied on the instrument then a label denoting the change is applied.
     * @param textInputs 
     *                  the list of textInputs.
     */
    public void ifTextInputValuesDifferentFromCachedValueThenChangeLabel(ArrayList<DaeExperimentSetupText> textInputs) {
        for (DaeExperimentSetupText txt : textInputs) {
            txt.ifValueDifferentFromCachedValueThenChangeLabel();
        }
    }
    
    /**
     * If the states are different from the values applied on the instrument then a label denoting the change is applied.
     * @param combos 
     *                  the list of combos.
     */
    public void ifComboValuesDifferentFromCachedValueThenChangeLabel(ArrayList<DaeExperimentSetupCombo> combos) {
        for (DaeExperimentSetupCombo cmb : combos) {
            cmb.ifValueDifferentFromCachedValueThenChangeLabel();
        }
    }
    
    /**
     * If the states are different from the values applied on the instrument then a label denoting the change is applied.
     * @param viewer 
     *                  the viewer.
     */
    public void ifTableViewerValuesDifferentFromCachedValueThenChangeLabel(DaeExperimentSetupTableViewer viewer) {
            viewer.ifTableValuesDifferentFromCachedValuesThenChangeLabels();
        
    }

    /**
     * Returns a colour depending on which one is asked for.
     * 
     * @param colour
     *                  The colour that the widget asks to be.
     * @return
     *                  The "changed", the "unchanged" colour or the colour white.
     */
    public Color getColour(String colour) {
        if (colour.equals("changedColour")) {
            return changedColour;
        } else if (colour.equals("white")) {
            return white;
        } else {
            return unchangedColour;
        }
    }
    
    /**
     * Allows to change the value that denotes whether or not changes have been made but not applied.
     * 
     * @param name
     *                  The name of the widget calling the change.
     * @param change
     *                  True is changes have been made but not applied.
     */
    public void setIsChanged(String name, boolean change) {
        if (isChanged.containsKey(name)) {
            isChanged.replace(name, change);
        } else {
            isChanged.put(name, change);
        }
        
        if (change) {
            experimentSetupViewModel.setIsChanged(change);
            experimentSetup.setSendChangeBtnEnableState(change && Instrument.getInstance().isLocalInstrument());
        } else {
            if (!isChanged.containsValue(true)) {
                experimentSetupViewModel.setIsChanged(change);
                experimentSetup.setSendChangeBtnEnableState(change && Instrument.getInstance().isLocalInstrument());
            }
        }
    }
    
    /**
     * Allows to check if there are any changes that haven't been applied to the instrument.
     * 
     * @return
     *          True if there exists changes that haven't been applied to the instrument.
     */
    public Map<String, Boolean> getIsChanged() {
        return isChanged;
    }
    
    /**
     * Allows to clear the map that states if a widget has been changed, allowing to reset this map. 
     */
    public void clearIsChanged() {
        isChanged.clear();
    }
    
    /**
     * Allows to return the experiment setup view model currently used.
     * @return
     *          The experiment setup view model currently used.
     */
    public ExperimentSetupViewModel getExperimentSetupViewModel() {
        return experimentSetupViewModel;
    }

}
