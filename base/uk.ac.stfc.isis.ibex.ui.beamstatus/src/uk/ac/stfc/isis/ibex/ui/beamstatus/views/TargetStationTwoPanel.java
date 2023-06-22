
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.beamstatus.BeamStatus;
import uk.ac.stfc.isis.ibex.beamstatus.TS2Observables;

/**
 * The GUI panel for displaying information from target station 2.
 */
public class TargetStationTwoPanel extends BeamInfoComposite {
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
	private final Label decoupledModeratorBeamLimit;
	private final Label decoupledModeratorChargeChangeTime;


	/**
	 * The constructor.
	 * 
	 * @param parent the parent
	 * @param style  the SWT style
	 */
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
		
		Label lbdecoupledModeratorBeamLimit = new Label(this, SWT.NONE);
		lbdecoupledModeratorBeamLimit.setText("Decoupled Moderator Beam Limit");

		decoupledModeratorBeamLimit = new Label(this, SWT.BORDER | SWT.RIGHT);
		decoupledModeratorBeamLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lbdecoupledModeratorChargeChangeTime = new Label(this, SWT.NONE);
		lbdecoupledModeratorChargeChangeTime.setText("Decoupled Moderator Charge Change Time");

		decoupledModeratorChargeChangeTime = new Label(this, SWT.BORDER | SWT.RIGHT);
		decoupledModeratorChargeChangeTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		if (BeamStatus.getInstance() != null) {
			bind(BeamStatus.getInstance().ts2());
		}
	}

	/**
	 * Binding observable facilityPV with the Label
	 * 
	 * @param ts
	 */

	private void bind(TS2Observables ts) {
		bindAndAddMenu(ts.beam(), beam, this);
		bindAndAddMenu(ts.pps(), pps, this);
		bindAndAddMenu(ts.beamCurrent(), current, this);
		bindAndAddMenu(ts.uAHToday(), uAhSince0830, this);
		bindAndAddMenu(ts.lastBeamOff(), lastOff, this);
		bindAndAddMenu(ts.lastBeamOn(), lastOn, this);
		bindAndAddMenu(ts.coupledMethaneTemperature, coupledMethaneTemperature, this);
		bindAndAddMenu(ts.coupledHydrogenTemperature, coupledHydrogenTemperature, this);
		bindAndAddMenu(ts.decoupledMethaneTemperature, decoupledMethaneTemperature, this);
		bindAndAddMenu(ts.decoupledModeratorRuntime, decoupledModeratorRuntime, this);
		bindAndAddMenu(ts.decoupledModeratorRuntimeLimit, decoupledModeratorRuntimeLimit, this);
		bindAndAddMenu(ts.decoupledModeratorAnnealPressure, decoupledModeratorAnnealPressure, this);
		bindAndAddMenu(ts.decoupledModeratorUAHBeam, decoupledModeratoruAhBeam, this);
		bindAndAddMenu(ts.decoupledModeratorBeamLimit, decoupledModeratorBeamLimit, this);
		bindAndAddMenu(ts.decoupledModeratorChargeChangeTime, decoupledModeratorChargeChangeTime, this);

	}

}
