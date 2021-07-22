
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
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

package uk.ac.stfc.isis.ibex.ui.beamstatus.views;


import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.beamstatus.BeamStatus;
import uk.ac.stfc.isis.ibex.beamstatus.FacilityPV;
import uk.ac.stfc.isis.ibex.beamstatus.SynchrotronObservables;


/**
 * The GUI panel for showing the synchrotron information.
 */
public class SynchrotronPanel extends Composite {
    private final Label beamCurrent;
    private final Label beamFrequency;
    private final Label beamEnergy;
    private final Label bunchLength;
    private final Label extractDelay;

    /**
     * The constructor.
     * 
     * @param parent the parent
     * @param style the SWT style
     */
    public SynchrotronPanel(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(2, false));

        beamCurrent = createRow("Beam Current");
        beamFrequency = createRow("Beam Frequency");
        beamEnergy = createRow("Beam Energy");
        bunchLength = createRow("Bunch Length");
        extractDelay = createRow("Extract Delay");

        if (BeamStatus.getInstance() != null) {
            bind(BeamStatus.getInstance().synchrotron());
        }
    }
    
    private Label createRow(String name) {
    	new Label(this, SWT.NONE).setText(name);

        Label display = new Label(this, SWT.BORDER | SWT.RIGHT);
        display.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        return display;
    }
    

   

    private void bind(SynchrotronObservables sync) {
        DataBindingContext bindingContext = new DataBindingContext();
        
        BeamInfoMenu beamCurrentMenu = new BeamInfoMenu(sync.beamCurrent);
    	beamCurrent.setMenu(beamCurrentMenu.createContextMenu(this));
        bindingContext.bindValue(WidgetProperties.text().observe(beamCurrent), BeanProperties.value("value").observe(sync.beamCurrent.updatedValue));
        
        BeamInfoMenu beamFrequencyMenu  = new BeamInfoMenu(sync.beamFrequency);
    	beamFrequency.setMenu(beamFrequencyMenu.createContextMenu(this));
    	bindingContext.bindValue(WidgetProperties.text().observe(beamFrequency), BeanProperties.value("value").observe(sync.beamFrequency.updatedValue));
    	
    	BeamInfoMenu beamEnergyMenu = new BeamInfoMenu(sync.beamEnergy);
     	beamEnergy.setMenu(beamEnergyMenu.createContextMenu(this));
    	bindingContext.bindValue(WidgetProperties.text().observe(beamEnergy), BeanProperties.value("value").observe(sync.beamEnergy.updatedValue));
    	
    	BeamInfoMenu bunchLengthMenu = new BeamInfoMenu(sync.bunchLength);
     	bunchLength.setMenu(bunchLengthMenu.createContextMenu(this));
    	bindingContext.bindValue(WidgetProperties.text().observe(bunchLength), BeanProperties.value("value").observe(sync.bunchLength.updatedValue));
    	
    	BeamInfoMenu extractDelayMenu = new BeamInfoMenu(sync.extractDelay);
     	extractDelay.setMenu(extractDelayMenu.createContextMenu(this));
    	bindingContext.bindValue(WidgetProperties.text().observe(extractDelay), BeanProperties.value("value").observe(sync.extractDelay.updatedValue));
        
        
        
    
 
    }

    

}
