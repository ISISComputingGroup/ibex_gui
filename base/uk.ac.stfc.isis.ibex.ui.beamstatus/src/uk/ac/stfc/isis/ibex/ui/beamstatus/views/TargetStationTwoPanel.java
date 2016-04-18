
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.beamstatus.BeamStatus;
import uk.ac.stfc.isis.ibex.beamstatus.Observables.TargetStation2;

public class TargetStationTwoPanel extends Composite {
	
	private final Label beam;
	private final Label pps;
	private final Label current;
	private final Label uAhSince0830;
	private final Label lastOff;
	private final Label lastOn;
	private final Label coupledMethaneTemperature;
	private final Label coupledHydrogenTemperature;
	private final Label decoupledMethaneTemperature;
	private final Label decoupledModeratorRuntime;
	private final Label decoupledModeratorRuntimeLimit;
	private final Label decoupledModeratorAnnealPressure;
	private final Label decoupledModeratoruAhBeam;
	
	public TargetStationTwoPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblTS2Beam = new Label(this, SWT.NONE);
		lblTS2Beam.setText("TS2 Beam");
		lblTS2Beam.setAlignment(SWT.RIGHT);
		
		beam = new Label(this, SWT.BORDER | SWT.RIGHT);
		beam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTS2PPS = new Label(this, SWT.NONE);
		lblTS2PPS.setText("TS2 PPS");
		
		pps = new Label(this, SWT.BORDER | SWT.RIGHT);
		pps.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblBeamCurrent = new Label(this, SWT.NONE);
		lblBeamCurrent.setText("TS2 Beam Current");
		
		current = new Label(this, SWT.BORDER | SWT.RIGHT);
		current.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTS2uAhSince0830 = new Label(this, SWT.NONE);
		lblTS2uAhSince0830.setText("TS2 µAh Since 08:30");
		
		uAhSince0830 = new Label(this, SWT.BORDER | SWT.RIGHT);
		uAhSince0830.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLastOff = new Label(this, SWT.NONE);
		lblLastOff.setText("TS2 Last Beam Off");
		
		lastOff = new Label(this, SWT.BORDER | SWT.RIGHT);
		lastOff.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLastOn = new Label(this, SWT.NONE);
		lblLastOn.setText("TS2 Last Beam On");
		
		lastOn = new Label(this, SWT.BORDER | SWT.RIGHT);
		lastOn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCoupledMethaneTemperature = new Label(this, SWT.NONE);
		lblCoupledMethaneTemperature.setText("Coupled Methane Temperature");
		
		coupledMethaneTemperature = new Label(this, SWT.BORDER | SWT.RIGHT);
		coupledMethaneTemperature.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCoupledHydrogenTemperature = new Label(this, SWT.NONE);
		lblCoupledHydrogenTemperature.setText("Coupled Hydrogen Temperature");
		
		coupledHydrogenTemperature = new Label(this, SWT.BORDER | SWT.RIGHT);
		coupledHydrogenTemperature.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDecoupledMethaneTemperature = new Label(this, SWT.NONE);
		lblDecoupledMethaneTemperature.setText("Decoupled Methane Temperature");
		
		decoupledMethaneTemperature = new Label(this, SWT.BORDER | SWT.RIGHT);
		decoupledMethaneTemperature.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDecoupledModeratorRuntime = new Label(this, SWT.NONE);
		lblDecoupledModeratorRuntime.setText("Decoupled Moderator Runtime");
		
		decoupledModeratorRuntime = new Label(this, SWT.BORDER | SWT.RIGHT);
		decoupledModeratorRuntime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDecoupledModeratorRuntimeLimit = new Label(this, SWT.NONE);
		lblDecoupledModeratorRuntimeLimit.setText("Decoupled Moderator Runtime Limit");
		
		decoupledModeratorRuntimeLimit = new Label(this, SWT.BORDER | SWT.RIGHT);
		decoupledModeratorRuntimeLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDecoupledModeratorAnnealPressure = new Label(this, SWT.NONE);
		lblDecoupledModeratorAnnealPressure.setText("Decoupled Moderator Anneal Pressure Low");
		
		decoupledModeratorAnnealPressure = new Label(this, SWT.BORDER | SWT.RIGHT);
		decoupledModeratorAnnealPressure.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblDecoupledModeratoraAhBeam = new Label(this, SWT.NONE);
		lblDecoupledModeratoraAhBeam.setText("Decoupled Moderator µAh Beam");
		
		decoupledModeratoruAhBeam = new Label(this, SWT.BORDER | SWT.RIGHT);
		decoupledModeratoruAhBeam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		if (BeamStatus.getInstance() != null) {
			bind(BeamStatus.getInstance().observables().ts2);
		}
	}

	private void bind(TargetStation2 ts) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(beam), BeanProperties.value("value").observe(ts.beam()));
		bindingContext.bindValue(WidgetProperties.text().observe(pps), BeanProperties.value("value").observe(ts.pps()));
		bindingContext.bindValue(WidgetProperties.text().observe(current), BeanProperties.value("value").observe(ts.beamCurrent()));
		bindingContext.bindValue(WidgetProperties.text().observe(uAhSince0830), BeanProperties.value("value").observe(ts.uAHToday()));
		bindingContext.bindValue(WidgetProperties.text().observe(lastOff), BeanProperties.value("value").observe(ts.lastBeamOff()));
		bindingContext.bindValue(WidgetProperties.text().observe(lastOn), BeanProperties.value("value").observe(ts.lastBeamOn()));
		
		bindingContext.bindValue(WidgetProperties.text().observe(coupledMethaneTemperature), BeanProperties.value("value").observe(ts.coupledMethaneTemperature));
		bindingContext.bindValue(WidgetProperties.text().observe(coupledHydrogenTemperature), BeanProperties.value("value").observe(ts.coupledHydrogenTemperature));
		bindingContext.bindValue(WidgetProperties.text().observe(decoupledMethaneTemperature), BeanProperties.value("value").observe(ts.decoupledMethaneTemperature));
		bindingContext.bindValue(WidgetProperties.text().observe(decoupledModeratorRuntime), BeanProperties.value("value").observe(ts.decoupledModeratorRuntime));
		bindingContext.bindValue(WidgetProperties.text().observe(decoupledModeratorRuntimeLimit), BeanProperties.value("value").observe(ts.decoupledModeratorRuntimeLimit));
		bindingContext.bindValue(WidgetProperties.text().observe(decoupledModeratorAnnealPressure), BeanProperties.value("value").observe(ts.decoupledModeratorAnnealPressure));
		bindingContext.bindValue(WidgetProperties.text().observe(decoupledModeratoruAhBeam), BeanProperties.value("value").observe(ts.decoupledModeratorUAHBeam));
	}

}
