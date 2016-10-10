
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.devicescreens.models.DeviceScreensDescriptionViewModel;

/**
 * Shows the target properties editor part.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class TargetPropertiesView extends Composite {
    /**
     * The view model.
     */
    private DeviceScreensDescriptionViewModel viewModel;

    /**
     * Creates a new instance of the target properties view.
     * 
     * @param parent this view's parent
     * @param viewModel the view model
     */
    public TargetPropertiesView(Composite parent, DeviceScreensDescriptionViewModel viewModel) {
        super(parent, SWT.FILL);
		
        setLayout(new GridLayout(2, false));

        this.viewModel = viewModel;
		
		createControls(this);
	}
	
    /**
     * Creates the controls.
     * 
     * @param parent the parent
     */
    private void createControls(Composite parent) {
        TargetPropertiesWidget properties = new TargetPropertiesWidget(parent, viewModel);
        properties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
}
