
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * The Class TargetPropertiesDescription GUI description for the selected macro.
 */
public class TargetPropertiesDescription extends Composite {

    private Text txtDescription;

    /**
     * Instantiates a new target properties description.
     *
     * @param parent the parent
     * @param synopticViewModel the synoptic view model
     */
    public TargetPropertiesDescription(Composite parent) {
		super(parent, SWT.NONE);
		
//        synopticViewModel.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
//            @Override
//            public void instrumentUpdated(UpdateTypes updateType) {
//                if (updateType == UpdateTypes.EDIT_TARGET) {
//                    txtDescription.setText("");
//                }
//            }
//        });
//
//        synopticViewModel.addPropertySelectionListener(new IPropertySelectionListener() {
//            @Override
//            public void selectionChanged(Property oldProperty, Property newProperty) {
//                if (newProperty != null) {
//                    ComponentDescription component = synopticViewModel.getFirstSelectedComponent();
//                    OpiDescription opi = synopticViewModel.getOpi(component.target().name());
//                    txtDescription.setText(opi.getMacroDescription(newProperty.key()));
//                } else {
//                    txtDescription.setText("");
//                }
//            }
//        });

		createControls(this);
	}

    private void createControls(Composite parent) {
        setLayout(new FillLayout());

        txtDescription = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
    }
}
