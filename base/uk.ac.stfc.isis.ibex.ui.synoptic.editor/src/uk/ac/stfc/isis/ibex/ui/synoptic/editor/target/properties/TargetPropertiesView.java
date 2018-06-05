
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 * Shows the synoptic editor part that allows selection of a target, normally an
 * OPI or a Java SWT screen for the DAE. OPI can be chosen, but DAE has a fixed
 * target.
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class TargetPropertiesView extends Composite {
    /**
     * Creates a new instance of the target properties view.
     * 
     * @param parent this view's parent
     * @param synopticViewModel the view model for the synoptic
     */
    public TargetPropertiesView(Composite parent, final SynopticViewModel synopticViewModel) {
        super(parent, SWT.FILL);
		
        setLayout(new GridLayout(2, false));
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
        Label lblProperties = new Label(this, SWT.NONE);
        lblProperties.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblProperties.setText("Properties");

        TargetPropertyTable properties = new TargetPropertyTable(this, synopticViewModel);
        properties.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Label lblPropertyDescription = new Label(this, SWT.NONE);
        lblPropertyDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblPropertyDescription.setText("Description");

        TargetPropertiesDescription propertyDescription = new TargetPropertiesDescription(this,
                synopticViewModel);
        GridData gdPropertyDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdPropertyDescription.heightHint = 70;
        propertyDescription.setLayoutData(gdPropertyDescription);

	}
}
