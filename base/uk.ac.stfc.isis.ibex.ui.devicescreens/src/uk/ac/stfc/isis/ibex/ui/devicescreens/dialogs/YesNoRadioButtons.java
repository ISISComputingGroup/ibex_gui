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
package uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.ui.Utils;

/**
 * This class defines a set of radio buttons for the persistence setting.
 */
public class YesNoRadioButtons extends ModelObject {

    private Button yesButton;

    private Button noButton;

    private Boolean selected;

    /**
     * @param parent
     *            the parent composite
     * @param trueText
     *            the true text
     * @param falseText
     *            the false text
     */
    public YesNoRadioButtons(Composite parent, String trueText, String falseText) {

        DataBindingContext bindingContext = Utils.getNewDatabindingContext();

        yesButton = new Button(parent, SWT.RADIO);
        noButton = new Button(parent, SWT.RADIO);

        yesButton.setText(trueText);
        yesButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        bindingContext.bindValue(WidgetProperties.selection().observe(yesButton),
                BeanProperties.value("yesButtonSelected").observe(this));

        noButton.setText(falseText);
        noButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        bindingContext.bindValue(WidgetProperties.selection().observe(noButton),
                BeanProperties.value("noButtonSelected").observe(this));

        setSelected(true);
    }

    /**
     *
     * @return the noButtonSelected whether no button is selected; if null
     *         returns false;
     */
    public boolean isNoButtonSelected() {
        return selected == null ? false : !selected;
    }

    /**
     * @param noButtonSelected
     *            whether the no button has been selected
     */
    public void setNoButtonSelected(boolean noButtonSelected) {
        setSelected(!noButtonSelected);
    }


    /**
     * @return the yesButtonSelected whether yes button is selected; if null
     *         returns false;
     */
    public boolean isYesButtonSelected() {

        return selected == null ? false : selected;
    }

    /**
     * @param yesButtonSelected
     *            whether the yes button has been selected
     */
    public void setYesButtonSelected(boolean yesButtonSelected) {
        setSelected(yesButtonSelected);
    }

    /**
     * @param selected
     *            if the radio buttons are selected
     */
    public void setSelected(Boolean selected) {
        firePropertyChange("selected", this.selected, this.selected = selected);

        if (selected != null) {
            firePropertyChange("yesButtonSelected", yesButton.getSelection(), selected.booleanValue());
            firePropertyChange("noButtonSelected", noButton.getSelection(), !selected.booleanValue());
        } else {
            firePropertyChange("yesButtonSelected", yesButton.getSelection(), false);
            firePropertyChange("noButtonSelected", noButton.getSelection(), false);
        }

    }

    /**
     * Getter for the selection of radio buttons.
     *
     * @return the selection of radio buttons
     */
    public Boolean getSelected() {
        return selected;
    }

    /**
     * Gets whether the radio buttons are enabled or not.
     *
     * @return whether the radio buttons are enabled
     */
    public boolean getEnabled() {
        return yesButton.isEnabled() && noButton.isEnabled();
    }

    /**
     * Sets whether the radio buttons are enabled or not.
     *
     * @param enabled
     *            whether the radio buttons are enabled
     */
    public void setEnabled(boolean enabled) {
        yesButton.setEnabled(enabled);
        noButton.setEnabled(enabled);
    }

}
