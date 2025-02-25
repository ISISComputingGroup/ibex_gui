
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
import uk.ac.stfc.isis.ibex.beamstatus.TS1Observables;

/**
 * The GUI panel for displaying information from target station 1.
 */
public class TargetStationOnePanel extends BeamInfoComposite {
	private final Label beam;
	private final Label pps;
	private final Label current;
	private final Label uAhSince0830;
	private final Label lastOff;
	private final Label lastOn;
	private final Label methaneTemperature;
	private final Label hydrogenTemperature;
	private final Label muonKicker;
	private final Label muonBeamCurrent;
	private final Label epb1BeamCurrent;



	/**
	 * The constructor.
	 * 
	 * @param parent the parent
	 * @param style  the SWT style
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
		
		
		Label lblMuonBeamCurrent = new Label(this, SWT.NONE);
		lblMuonBeamCurrent.setText("Muon Beam Current");

		muonBeamCurrent = new Label(this, SWT.BORDER | SWT.RIGHT);
		muonBeamCurrent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		Label lblEpb1BeamCurrent = new Label(this, SWT.NONE);
		lblEpb1BeamCurrent.setText("EPB1 Beam Current");

		epb1BeamCurrent = new Label(this, SWT.BORDER | SWT.RIGHT);
		epb1BeamCurrent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		

		if (BeamStatus.getInstance() != null) {
			bind(BeamStatus.getInstance().ts1());
		}
	}

	/**
	 * Binding observable facilityPV with the Label
	 * 
	 * @param ts
	 */
	private void bind(TS1Observables ts) {
		bindAndAddMenu(ts.beam(), beam, this);
		bindAndAddMenu(ts.pps(), pps, this);
		bindAndAddMenu(ts.beamCurrent(), current, this);
		bindAndAddMenu(ts.uAHToday(), uAhSince0830, this);
		bindAndAddMenu(ts.lastBeamOff(), lastOff, this);
		bindAndAddMenu(ts.lastBeamOn(), lastOn, this);
		bindAndAddMenu(ts.methaneTemperature, methaneTemperature, this);
		bindAndAddMenu(ts.hydrogenTemperature, hydrogenTemperature, this);
		bindAndAddMenu(ts.muonKicker, muonKicker, this);
		bindAndAddMenu(ts.muonBeamCurr, muonBeamCurrent, this);
		bindAndAddMenu(ts.epb1BeamCurr, epb1BeamCurrent, this);

	}

}
