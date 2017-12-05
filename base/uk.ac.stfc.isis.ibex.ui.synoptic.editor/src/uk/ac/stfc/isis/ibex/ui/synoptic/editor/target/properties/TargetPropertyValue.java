
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.properties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

/**
 * GUI Composite for the target property value display and change.
 */
public class TargetPropertyValue extends Composite {
    private Text valueText;
    private SynopticViewModel synopticViewModel;

    private boolean updateLock;

    private final IInstrumentUpdateListener instrumentListener = new IInstrumentUpdateListener() {
        @Override
        public void instrumentUpdated(UpdateTypes updateType) {
            if (updateType == UpdateTypes.ADD_TARGET || updateType == UpdateTypes.EDIT_TARGET) {
                Property selected = synopticViewModel.getSelectedProperty();
                setProperty(selected);
            }
        }
    };

    private final PropertyChangeListener propertyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setProperty((Property) evt.getNewValue());
        }
    };

    /**
     * Instantiates a new target property value.
     *
     * @param parent the parent
     * @param synopticViewModel the synoptic view model
     */
    public TargetPropertyValue(Composite parent, final SynopticViewModel synopticViewModel) {
        super(parent, SWT.NONE);

        this.synopticViewModel = synopticViewModel;
        this.synopticViewModel.addPropertyChangeListener("propSelection", propertyListener);
        this.synopticViewModel.addInstrumentUpdateListener(instrumentListener);

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
                    synopticViewModel.updateOrAddSelectedProperty(new Property(key, valueText.getText()));
                    updateLock = false;
                }
            }
        });
        valueText.setEnabled(false);
    }

    private void setProperty(Property newProperty) {
        if (newProperty != null && !updateLock) {
            valueText.setEnabled(true);
            valueText.setText(newProperty.value());
        } else if (newProperty == null) {
            valueText.setEnabled(false);
            valueText.setText("");
        }
    }
}
