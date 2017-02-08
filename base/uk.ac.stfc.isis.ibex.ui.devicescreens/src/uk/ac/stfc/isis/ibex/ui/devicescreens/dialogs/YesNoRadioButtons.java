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
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.EditDeviceScreensDescriptionViewModel;

/**
 * This class defines a set of radio buttons for the persistence setting.
 */
public class YesNoRadioButtons extends ModelObject {

    String yesText = "Save this device screen";
    String noText = "Remove this device screen when IBEX is closed";

    private Boolean persistence = true;

    private EditDeviceScreensDescriptionViewModel viewModel;
    private Button yesButton;
    private Button noButton;

    public void createButtons(EditDeviceScreensDescriptionViewModel viewModel, Composite detailsComposite,
            SelectionListener selectionListener) {

        this.viewModel = viewModel;

        yesButton = new Button(detailsComposite, SWT.RADIO);
        noButton = new Button(detailsComposite, SWT.RADIO);

        String yesText = "Save this device screen";
        String noText = "Remove this device screen when IBEX is closed";

        yesButton.setText(yesText);
        yesButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        yesButton.addSelectionListener(selectionListener);

        noButton.setText(noText);
        noButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        noButton.addSelectionListener(selectionListener);
    }

    /**
     * @param persistence
     */
    public void setCurrentPersistence(Boolean persistence) {
        this.persistence = persistence;
        if (persistence == null) {
            yesButton.setSelection(false);
            noButton.setSelection(false);
        } else {
            yesButton.setSelection(persistence);
            noButton.setSelection(!persistence);
        }
    }

    public void bind(DataBindingContext bindingContext) {

        IObservableValue a = BeanProperties.value("currentPersistence").observe(this);

        bindingContext.bindValue(a,
                BeanProperties.value("currentPersistence").observe(viewModel), null, null);
    }

    public Boolean getCurrentPersistence() {
        return persistence;
    }

}
