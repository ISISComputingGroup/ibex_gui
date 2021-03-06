
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 * The Class TargetPropertiesDescription GUI description for the selected macro.
 */
public class TargetPropertiesDescription extends Composite {

    /**
     * Instantiates a new target properties description.
     *
     * @param parent the parent
     * @param synopticViewModel the synoptic view model
     */
    public TargetPropertiesDescription(Composite parent, final SynopticViewModel synopticViewModel) {
		super(parent, SWT.NONE);
		
        setLayout(new FillLayout());

        final Text txtDescription = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
        
        synopticViewModel.addPropertyChangeListener("propSelection", new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Property newProperty = (Property) evt.getNewValue();
                if (newProperty != null) {
                    ComponentDescription component = synopticViewModel.getSingleSelectedComp();
                    OpiDescription opi = synopticViewModel.getOpi(component.target().name());
                    txtDescription.setText(opi.getMacroDescription(newProperty.getKey()));
                } else {
                    txtDescription.setText("");
                }
            }
        });
    }
}
