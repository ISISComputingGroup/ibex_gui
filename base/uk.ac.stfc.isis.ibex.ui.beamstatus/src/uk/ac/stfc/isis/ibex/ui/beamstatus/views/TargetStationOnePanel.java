
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
import uk.ac.stfc.isis.ibex.beamstatus.TS1Observables;

/**
 * The GUI panel for displaying information from target station 1.
 */
public class TargetStationOnePanel extends Composite {
    private final Label beam;
    private final Label pps;
    private final Label current;
    private final Label uAhSince0830;
    private final Label lastOff;
    private final Label lastOn;
    private final Label methaneTemperature;
    private final Label hydrogenTemperature;
    private final Label muonKicker;

    /**
     * The constructor.
     * 
     * @param parent the parent
     * @param style the SWT style
     */
    public TargetStationOnePanel(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(2, false));

        Label lblTS1Beam = new Label(this, SWT.NONE);
        lblTS1Beam.setText("TS1 Beam");
        lblTS1Beam.setAlignment(SWT.RIGHT);

        beam = new Label(this, SWT.BORDER | SWT.RIGHT);
        beam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblTS1PPS = new Label(this, SWT.NONE);
        lblTS1PPS.setText("TS1 PPS");

        pps = new Label(this, SWT.BORDER | SWT.RIGHT);
        pps.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblBeamCurrent = new Label(this, SWT.NONE);
        lblBeamCurrent.setText("TS1 Beam Current");

        current = new Label(this, SWT.BORDER | SWT.RIGHT);
        current.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblTS1uAhSince0830 = new Label(this, SWT.NONE);
        lblTS1uAhSince0830.setText("TS1 µAh Since 08:30");

        uAhSince0830 = new Label(this, SWT.BORDER | SWT.RIGHT);
        uAhSince0830.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblLastOff = new Label(this, SWT.NONE);
        lblLastOff.setText("TS1 Last Beam Off");

        lastOff = new Label(this, SWT.BORDER | SWT.RIGHT);
        lastOff.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblLastOn = new Label(this, SWT.NONE);
        lblLastOn.setText("TS1 Last Beam On");

        lastOn = new Label(this, SWT.BORDER | SWT.RIGHT);
        lastOn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblMethaneTemperature = new Label(this, SWT.NONE);
        lblMethaneTemperature.setText("Methane Temperature");

        methaneTemperature = new Label(this, SWT.BORDER | SWT.RIGHT);
        methaneTemperature.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblHydrogenTemperature = new Label(this, SWT.NONE);
        lblHydrogenTemperature.setText("Hydrogen Temperature");

        hydrogenTemperature = new Label(this, SWT.BORDER | SWT.RIGHT);
        hydrogenTemperature.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblMuonKicker = new Label(this, SWT.NONE);
        lblMuonKicker.setText("Muon Kicker");

        muonKicker = new Label(this, SWT.BORDER | SWT.RIGHT);
        muonKicker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        if (BeamStatus.getInstance() != null) {
            bind(BeamStatus.getInstance().ts1());
        }
    }

    private void bind(TS1Observables ts) {
        DataBindingContext bindingContext = new DataBindingContext();
        
        BeamInfoMenu beamMenu = new BeamInfoMenu(ts.beam());
    	beam.setMenu(beamMenu.createContextMenu(this));
        bindingContext.bindValue(WidgetProperties.text().observe(beam),
                BeanProperties.value("value").observe(ts.beam().updatedValue));
        
        BeamInfoMenu ppsMenu = new BeamInfoMenu(ts.pps());
    	pps.setMenu(ppsMenu.createContextMenu(this));
        bindingContext.bindValue(WidgetProperties.text().observe(pps), BeanProperties.value("value").observe(ts.pps().updatedValue));
       
        BeamInfoMenu beamCurrentMenu = new BeamInfoMenu(ts.beamCurrent());
    	current.setMenu(beamCurrentMenu.createContextMenu(this));
        bindingContext.bindValue(WidgetProperties.text().observe(current),
                BeanProperties.value("value").observe(ts.beamCurrent().updatedValue));
        
        BeamInfoMenu uAHTodayMenu= new BeamInfoMenu(ts.uAHToday());
    	uAhSince0830.setMenu(uAHTodayMenu.createContextMenu(this));
        bindingContext.bindValue(WidgetProperties.text().observe(uAhSince0830),
                BeanProperties.value("value").observe(ts.uAHToday().updatedValue));
        
        BeamInfoMenu lastBeamOffMenu = new BeamInfoMenu(ts.lastBeamOff());
    	lastOff.setMenu(lastBeamOffMenu.createContextMenu(this));
        bindingContext.bindValue(WidgetProperties.text().observe(lastOff),
                BeanProperties.value("value").observe(ts.lastBeamOff().updatedValue));
        
        BeamInfoMenu lastBeamOnMenu = new BeamInfoMenu(ts.lastBeamOn());
    	lastOn.setMenu(lastBeamOnMenu.createContextMenu(this));
        bindingContext.bindValue(WidgetProperties.text().observe(lastOn),
                BeanProperties.value("value").observe(ts.lastBeamOn().updatedValue));

        BeamInfoMenu methaneTempMenu = new BeamInfoMenu(ts.methaneTemperature);
    	methaneTemperature.setMenu(methaneTempMenu.createContextMenu(this));
        bindingContext.bindValue(WidgetProperties.text().observe(methaneTemperature),
                BeanProperties.value("value").observe(ts.methaneTemperature.updatedValue));
        
        BeamInfoMenu hydrogenTempMenu = new BeamInfoMenu(ts.hydrogenTemperature);
    	hydrogenTemperature.setMenu(hydrogenTempMenu.createContextMenu(this));
        bindingContext.bindValue(WidgetProperties.text().observe(hydrogenTemperature),
                BeanProperties.value("value").observe(ts.hydrogenTemperature.updatedValue));
        
        BeamInfoMenu muonKickerMenu = new BeamInfoMenu(ts.muonKicker);
    	muonKicker.setMenu(muonKickerMenu.createContextMenu(this));
        bindingContext.bindValue(WidgetProperties.text().observe(muonKicker),
                BeanProperties.value("value").observe(ts.muonKicker.updatedValue));
    }

}
