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

/**
 * This class defines a set of radio buttons for the persistence setting.
 */
public class YesNoRadioButtons extends ModelObject {


    private Boolean selected;

    private Button yesButton;
    private Button noButton;

    private DataBindingContext bindingContext;

    /**
     * 
     */
    public YesNoRadioButtons(Composite parent, String trueText, String falseText) {

        bindingContext = new DataBindingContext();
        
        yesButton = new Button(parent, SWT.RADIO);
        noButton = new Button(parent, SWT.RADIO);
        
        yesButton.setText(trueText);
        yesButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        bindingContext.bindValue(WidgetProperties.selection().observe(yesButton),
                BeanProperties.value("yesButtonSelected").observe(this), null, null);
        
        noButton.setText(falseText);
        noButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        bindingContext.bindValue(WidgetProperties.selection().observe(noButton),
                BeanProperties.value("noButtonSelected").observe(this), null, null);
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
     *            the noButtonSelected to set
     */
    public void setNoButtonSelected(boolean noButtonSelected) {
        if (noButtonSelected) {
            setSelected(false);
        }
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
     *            the yesButtonSelected to set
     */
    public void setYesButtonSelected(boolean yesButtonSelected) {
        if (yesButtonSelected) {
            setSelected(true);
        }
    }

    /**
     * @param selected
     */
    public void setSelected(Boolean selected) {

        boolean previousSelected = this.selected == null ? false : this.selected.booleanValue();
        firePropertyChange("selected", this.selected, this.selected = selected);

        if (selected != null) {
            firePropertyChange("yesButtonSelected", previousSelected, selected.booleanValue());
            firePropertyChange("noButtonSelected", !previousSelected, !selected.booleanValue());
        } else {
            firePropertyChange("yesButtonSelected", previousSelected, false);
            firePropertyChange("noButtonSelected", !previousSelected, false);
        }

    }

    public Boolean getSelected() {
        return selected;
    }

}
