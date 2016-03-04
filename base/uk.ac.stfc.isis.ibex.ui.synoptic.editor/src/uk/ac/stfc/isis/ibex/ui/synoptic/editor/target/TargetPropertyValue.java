
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
    private Text valueText;
    private SynopticViewModel synopticViewModel;

    private boolean updateLock;

    private final IPropertySelectionListener propertyListener = new IPropertySelectionListener() {
        @Override
        public void selectionChanged(Property oldProperty, Property newProperty) {
            if (newProperty != null && !updateLock) {
                valueText.setText(newProperty.value());
            }
        }
    };

    public TargetPropertyValue(Composite parent, final SynopticViewModel synopticViewModel) {
        super(parent, SWT.NONE);

        this.synopticViewModel = synopticViewModel;
        this.synopticViewModel.addPropertySelectionListener(propertyListener);

        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);

        valueText = new Text(this, SWT.BORDER);
        valueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        valueText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (valueText.isFocusControl()) {
                    updateLock = true;
                    String key = synopticViewModel.getSelectedProperty().key();
                    synopticViewModel.updateSelectedProperty(new Property(key, valueText.getText()));
                    updateLock = false;
                }
            }
        });
    }
}
