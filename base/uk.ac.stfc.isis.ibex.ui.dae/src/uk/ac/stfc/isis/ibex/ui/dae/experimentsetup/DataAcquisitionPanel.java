
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeTimingSource;
import uk.ac.stfc.isis.ibex.dae.updatesettings.AutosaveUnit;

public class DataAcquisitionPanel extends Composite {
	private Combo wiringTable;
	private Combo detectorTable;
	private Combo spectraTable;
	private Text from;
	private Text to;
	private Text autosaveFrequency;
	private Spinner monitorSpectrum;
	private Combo daeTimingSource;
	private Combo autosaveUnits;
	private Button btnVeto0;
	private Button btnVeto1;
	private Button btnVeto2;
	private Button btnVeto3;
	private Button btnFermiChopper;
	private Button btnSMP;
	private Button btnTs2Pulse;
	private Button btnIsisHz;
	
	private Button btnMuonMsMode;
	private Button btnMuonPulseFirst;
	
	private Label fcDelay;
	private Label fcWidth;
	
	private DataBindingContext bindingContext;
	
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
	public DataAcquisitionPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Group grpTables = new Group(this, SWT.NONE);
		grpTables.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpTables.setText("Tables");
		grpTables.setBounds(0, 0, 70, 82);
		grpTables.setLayout(new GridLayout(2, false));
		
		Label lblWiringTable = new Label(grpTables, SWT.NONE);
		lblWiringTable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWiringTable.setSize(70, 15);
		lblWiringTable.setText("Wiring Table:");
		
		wiringTable = new Combo(grpTables, SWT.DROP_DOWN);	
		GridData gd_wiringTable = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_wiringTable.widthHint = 500;
		wiringTable.setLayoutData(gd_wiringTable);
		wiringTable.setSize(317, 25);
				
		Label lblDetectorTable = new Label(grpTables, SWT.NONE);
		lblDetectorTable.setSize(80, 15);
		lblDetectorTable.setText("Detector Table:");
		
		detectorTable = new Combo(grpTables, SWT.DROP_DOWN);
		GridData gd_detectorTable = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_detectorTable.widthHint = 500;
		detectorTable.setLayoutData(gd_detectorTable);
		detectorTable.setSize(317, 25);
		
		Label lblSpectraTable = new Label(grpTables, SWT.NONE);
		lblSpectraTable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpectraTable.setSize(74, 15);
		lblSpectraTable.setText("Spectra Table:");
		
		spectraTable = new Combo(grpTables, SWT.DROP_DOWN);
		GridData gd_spectraTable = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_spectraTable.widthHint = 500;
		spectraTable.setLayoutData(gd_spectraTable);
		spectraTable.setSize(317, 21);
		
		Group grpMonitor = new Group(this, SWT.NONE);
		grpMonitor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpMonitor.setText("Monitor");
		grpMonitor.setLayout(new GridLayout(8, false));
		
		Label lblSpectrum = new Label(grpMonitor, SWT.NONE);
		lblSpectrum.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpectrum.setText("Spectrum:");
		
		monitorSpectrum = new Spinner(grpMonitor, SWT.BORDER);
		
		Label lblNewLabel_1 = new Label(grpMonitor, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_1.widthHint = 20;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
				
		Label lblFrom = new Label(grpMonitor, SWT.NONE);
		lblFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFrom.setText("From:");
		
		from = new Text(grpMonitor, SWT.BORDER);
		GridData gd_from = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_from.widthHint = 70;
		from.setLayoutData(gd_from);
		
		Label lblTo = new Label(grpMonitor, SWT.NONE);
		lblTo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTo.setText("To:");
		
		to = new Text(grpMonitor, SWT.BORDER);
		GridData gd_to = new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1);
		gd_to.widthHint = 70;
		to.setLayoutData(gd_to);
		new Label(grpMonitor, SWT.NONE);
		
		Group grpVetos = new Group(this, SWT.NONE);
		GridData gd_grpVetos = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpVetos.widthHint = 550;
		grpVetos.setLayoutData(gd_grpVetos);
		grpVetos.setText("Vetos");
		grpVetos.setLayout(new GridLayout(5, true));
		
		btnFermiChopper = new Button(grpVetos, SWT.CHECK);
		btnFermiChopper.setText("Fermi Chopper");
		
		Label lblFcDelay = new Label(grpVetos, SWT.NONE);
		lblFcDelay.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblFcDelay.setText("Delay (μs):");
		
		fcDelay = new Label(grpVetos, SWT.NONE);
		fcDelay.setText("UNKNOWN");
		
		Label lblNewLabel_3 = new Label(grpVetos, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_3.setText("Width (μs):");
		
		fcWidth = new Label(grpVetos, SWT.NONE);
		fcWidth.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		fcWidth.setText("UNKNOWN");
		
		btnSMP = new Button(grpVetos, SWT.CHECK);
		btnSMP.setText("SMP (Chopper)");
		
		btnTs2Pulse = new Button(grpVetos, SWT.CHECK);
		btnTs2Pulse.setText("TS2 Pulse");
		
		btnIsisHz = new Button(grpVetos, SWT.CHECK);
		btnIsisHz.setText("ISIS 50 Hz");
		new Label(grpVetos, SWT.NONE);
		new Label(grpVetos, SWT.NONE);
		
		btnVeto0 = new Button(grpVetos, SWT.CHECK);
		btnVeto0.setText("Veto 0");
		
		btnVeto1 = new Button(grpVetos, SWT.CHECK);
		btnVeto1.setText("Veto 1");
		
		btnVeto2 = new Button(grpVetos, SWT.CHECK);
		btnVeto2.setText("Veto 2");
		
		btnVeto3 = new Button(grpVetos, SWT.CHECK);
		btnVeto3.setText("Veto 3");
		new Label(grpVetos, SWT.NONE);
		
		Group grpMuons = new Group(this, SWT.NONE);
		grpMuons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpMuons.setText("Muons");
		grpMuons.setLayout(new GridLayout(2, false));
		
		Label lblMuonMsMode = new Label(grpMuons, SWT.NONE);
		lblMuonMsMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMuonMsMode.setSize(88, 15);
		lblMuonMsMode.setText("Muon ms mode:");
		
		btnMuonMsMode = new Button(grpMuons, SWT.CHECK);
		btnMuonMsMode.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		btnMuonMsMode.setSize(63, 16);
		btnMuonMsMode.setText("Enabled");
		
		Label lblMuonCerenkovPulse = new Label(grpMuons, SWT.NONE);
		lblMuonCerenkovPulse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMuonCerenkovPulse.setSize(119, 15);
		lblMuonCerenkovPulse.setText("Muon Cerenkov pulse:");
		
		Composite muonPulseComposite = new Composite(grpMuons, SWT.BORDER);
		muonPulseComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		muonPulseComposite.setSize(118, 26);
		muonPulseComposite.setLayout(new GridLayout(2, false));

		btnMuonPulseFirst = new Button(muonPulseComposite, SWT.FLAT | SWT.RADIO);
		btnMuonPulseFirst.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnMuonPulseFirst.setText("First");
		
		Button btnMuonPulseSecond = new Button(muonPulseComposite, SWT.FLAT | SWT.RADIO);
		btnMuonPulseSecond.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnMuonPulseSecond.setText("Second");
		
		Group grpTiming = new Group(this, SWT.NONE);
		grpTiming.setText("Timing");
		grpTiming.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpTiming.setLayout(new GridLayout(3, false));
		
		Label lblDaeTimeingSource = new Label(grpTiming, SWT.NONE);
		lblDaeTimeingSource.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDaeTimeingSource.setText("DAE Timing Source:");
		
		daeTimingSource = new Combo(grpTiming, SWT.DROP_DOWN | SWT.READ_ONLY);
		daeTimingSource.setItems(DaeTimingSource.allToString().toArray(new String[0]));
		GridData gd_daeTimingSource = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_daeTimingSource.widthHint = 100;
		daeTimingSource.setLayoutData(gd_daeTimingSource);
		new Label(grpTiming, SWT.NONE);
		
		Label lblAutosaveEvery = new Label(grpTiming, SWT.NONE);
		lblAutosaveEvery.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAutosaveEvery.setText("Autosave every:");
		
		autosaveFrequency = new Text(grpTiming, SWT.BORDER);
		GridData gd_autosaveFrequency = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_autosaveFrequency.widthHint = 100;
		autosaveFrequency.setLayoutData(gd_autosaveFrequency);
		
		autosaveUnits = new Combo(grpTiming, SWT.DROP_DOWN | SWT.READ_ONLY);
		autosaveUnits.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		autosaveUnits.setItems(AutosaveUnit.allToString().toArray(new String[0]));
	}
	
	public void setModel(DataAcquisitionViewModel model) {
		bindingContext = new DataBindingContext();
		
		bindingContext.bindList(WidgetProperties.items().observe(wiringTable), BeanProperties.list("wiringTableList").observe(model));
		bindingContext.bindValue(WidgetProperties.selection().observe(wiringTable), BeanProperties.value("wiringTable").observe(model));
	
		bindingContext.bindList(WidgetProperties.items().observe(detectorTable), BeanProperties.list("detectorTableList").observe(model));
		bindingContext.bindValue(WidgetProperties.selection().observe(detectorTable), BeanProperties.value("detectorTable").observe(model));
		
		bindingContext.bindList(WidgetProperties.items().observe(spectraTable), BeanProperties.list("spectraTableList").observe(model));
		bindingContext.bindValue(WidgetProperties.selection().observe(spectraTable), BeanProperties.value("spectraTable").observe(model));
		
		bindingContext.bindValue(WidgetProperties.selection().observe(monitorSpectrum), BeanProperties.value("monitorSpectrum").observe(model));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(from), BeanProperties.value("from").observe(model));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(to), BeanProperties.value("to").observe(model));
		
		bindingContext.bindValue(WidgetProperties.selection().observe(btnVeto0), BeanProperties.value("veto0").observe(model));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnVeto1), BeanProperties.value("veto1").observe(model));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnVeto2), BeanProperties.value("veto2").observe(model));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnVeto3), BeanProperties.value("veto3").observe(model));
		
		bindingContext.bindValue(WidgetProperties.selection().observe(btnSMP), BeanProperties.value("smpVeto").observe(model));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnFermiChopper), BeanProperties.value("fermiChopperVeto").observe(model));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnTs2Pulse), BeanProperties.value("ts2PulseVeto").observe(model));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnIsisHz), BeanProperties.value("isis50HzVeto").observe(model));
		
		bindingContext.bindValue(WidgetProperties.text().observe(fcDelay), BeanProperties.value("fcDelay").observe(model));
		bindingContext.bindValue(WidgetProperties.text().observe(fcWidth), BeanProperties.value("fcWidth").observe(model));

		bindingContext.bindValue(WidgetProperties.selection().observe(btnMuonMsMode), BeanProperties.value("muonMsMode").observe(model));
		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(daeTimingSource), BeanProperties.value("timingSource").observe(model));
		
		bindingContext.bindValue(WidgetProperties.selection().observe(btnMuonPulseFirst), BeanProperties.value("muonCerenkovPulse").observe(model));	
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(autosaveFrequency), BeanProperties.value("autosaveFrequency").observe(model));
		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(autosaveUnits), BeanProperties.value("autosaveUnits").observe(model));
	}
}
