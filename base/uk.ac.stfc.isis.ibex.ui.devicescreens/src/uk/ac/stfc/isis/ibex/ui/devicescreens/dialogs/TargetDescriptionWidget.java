
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.ui.devicescreens.models.DeviceScreensDescriptionViewModel;

/**
 * A widget for showing an OPI's description.
 */
public class TargetDescriptionWidget extends Composite {

    /** The view model. **/
    private DeviceScreensDescriptionViewModel viewModel;

    /**
     * The constructor.
     * 
     * @param parent the parent composite
     * @param viewModel the view model for the device screen editor
     */
    public TargetDescriptionWidget(Composite parent, DeviceScreensDescriptionViewModel viewModel) {
		super(parent, SWT.NONE);
        this.viewModel = viewModel;

        setLayout(new FillLayout());
        Text txtDescription = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);

        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtDescription),
                BeanProperties.value("currentDescription").observe(viewModel), null, null);
    }
}
