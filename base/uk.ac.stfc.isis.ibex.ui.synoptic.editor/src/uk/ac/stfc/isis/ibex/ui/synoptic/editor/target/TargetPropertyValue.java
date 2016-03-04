
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IPropertySelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 * 
 */
public class TargetPropertyValue extends Composite {
    private Text text;
    private SynopticViewModel synopticViewModel;

    private final IPropertySelectionListener propertyListener = new IPropertySelectionListener() {
        @Override
        public void selectionChanged(Property oldProperty, Property newProperty) {
            System.out.println("Setting!");
            if (newProperty != null) {
                System.out.println(newProperty.value());
                text.setText(newProperty.value());
            }
        }
    };

//    private final Listener propertyUpdateListener = new Listener() {
//        @Override
//        public void handleEvent(Event event) {
//            if (!updateLock && model != null) {
//                model.updateSelectedProperty(new Property(key.getText(), value.getText()));
//            }
//        }
//    };

    public TargetPropertyValue(Composite parent, final SynopticViewModel synopticViewModel) {
        super(parent, SWT.NONE);

        this.synopticViewModel = synopticViewModel;
        this.synopticViewModel.addPropertySelectionListener(propertyListener);

        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);

        text = new Text(this, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    }
}
