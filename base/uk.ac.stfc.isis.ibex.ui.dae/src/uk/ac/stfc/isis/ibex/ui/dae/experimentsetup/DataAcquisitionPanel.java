
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import java.util.ArrayList;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettingsProperties;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeTimingSource;
import uk.ac.stfc.isis.ibex.dae.updatesettings.AutosaveUnit;

/**
 * Panel to show the information regarding the data acquisition of the DAE.
 */
public class DataAcquisitionPanel extends Composite {
	private DaeExperimentSetupCombo wiringTableSelector;
	private DaeExperimentSetupCombo detectorTableSelector;
	private DaeExperimentSetupCombo spectraTableSelector;
	private DaeExperimentSetupText from;
	private DaeExperimentSetupText to;
	private DaeExperimentSetupText autosaveFrequency;
	private DaeExperimentSetupSpinner monitorSpectrum;
	private DaeExperimentSetupCombo daeTimingSource;
	private DaeExperimentSetupCombo autosaveUnits;
	private DaeExperimentSetupButton btnVeto0;
	private DaeExperimentSetupButton btnVeto1;
	private DaeExperimentSetupButton btnVeto2;
	private DaeExperimentSetupButton btnVeto3;
	private DaeExperimentSetupButton btnFermiChopper;
	private DaeExperimentSetupButton btnSMP;
	private DaeExperimentSetupButton btnTs2Pulse;
	private DaeExperimentSetupButton btnIsisHz;
	private ArrayList<DaeExperimentSetupRadioButton> radioBtns = new ArrayList<DaeExperimentSetupRadioButton>();
	private ArrayList<Boolean> radioBtnsRB = new ArrayList<Boolean>();

	private ArrayList<DaeExperimentSetupButton> daeExpSetupBtns = new ArrayList<DaeExperimentSetupButton>();

	private ArrayList<DaeExperimentSetupCombo> combos = new ArrayList<DaeExperimentSetupCombo>();

	private ArrayList<DaeExperimentSetupText> textInputs = new ArrayList<DaeExperimentSetupText>();

	private ArrayList<DaeExperimentSetupSpinner> spinners = new ArrayList<DaeExperimentSetupSpinner>();

	private Composite wiringTablePanel;
	private Composite detectorTablePanel;
	private Composite spectraTablePanel;
	private Composite cmpTimeingSource;
	private Composite cmpAutosaveUnits;

	private DaeExperimentSetupButton btnMuonMsMode;
	private DaeExperimentSetupRadioButton btnMuonPulseFirst;
	private DaeExperimentSetupRadioButton btnMuonPulseSecond;

	private Label fcDelay;
	private Label fcWidth;
	private Label wiringTableRB;
	private Label detectorTableRB;
	private Label spectraTableRB;

	private DataBindingContext bindingContext;

	private PanelViewModel panelViewModel;

	/**
	 * The maximum spectrum number that can be set in the data acquisition tab.
	 */
	private static final int MAXIMUM_MONITOR_SPECTRUM = 1000000;

	/**
	 * The default constructor for the panel.
	 * 
	 * @param parent         The parent composite that this panel belongs to.
	 * @param style          The SWT flags giving the style of the panel.
	 * @param panelViewModel The viewModel that helps manipulate the panels.
	 */
	@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
	public DataAcquisitionPanel(Composite parent, int style, PanelViewModel panelViewModel) {
		super(parent, style);
		this.panelViewModel = panelViewModel;
		setLayout(new GridLayout(1, false));

		GridData gdLabels = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gdLabels.widthHint = 80;

		Group grpTables = new Group(this, SWT.NONE);
		grpTables.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpTables.setText("Tables");
		grpTables.setBounds(0, 0, 70, 82);
		GridLayout glGrpTables = new GridLayout(2, false);
		glGrpTables.verticalSpacing = 10;
		grpTables.setLayout(glGrpTables);

		// Wiring table selection
		wiringTablePanel = new Composite(grpTables, SWT.NONE);
		wiringTablePanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		wiringTablePanel.setLayout(new GridLayout(3, false));
		wiringTablePanel.setBackgroundMode(SWT.INHERIT_DEFAULT);

		Label lblWiring = new Label(wiringTablePanel, SWT.NONE);
		lblWiring.setLayoutData(gdLabels);
		lblWiring.setText("Wiring Table:");

		Label lblWiringRB = new Label(wiringTablePanel, SWT.NONE);
		lblWiringRB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWiringRB.setText("Current:");

		wiringTableRB = new Label(wiringTablePanel, SWT.NONE);
		wiringTableRB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		wiringTableRB.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));

		new Label(wiringTablePanel, SWT.NONE);

		Label lblWiringChange = new Label(wiringTablePanel, SWT.NONE);
		lblWiringChange.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWiringChange.setText("Change:");

		wiringTableSelector = new DaeExperimentSetupCombo(wiringTablePanel, SWT.DROP_DOWN | SWT.READ_ONLY,
				panelViewModel, "wiringTableSelector");
		wiringTableSelector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		// Detector table selection
		detectorTablePanel = new Composite(grpTables, SWT.NONE);
		detectorTablePanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		detectorTablePanel.setLayout(new GridLayout(3, false));
		detectorTablePanel.setBackgroundMode(SWT.INHERIT_DEFAULT);

		Label lblDetector = new Label(detectorTablePanel, SWT.NONE);
		lblDetector.setLayoutData(gdLabels);
		lblDetector.setText("Detector Table:");

		Label lblDetectorRB = new Label(detectorTablePanel, SWT.NONE);
		lblDetectorRB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDetectorRB.setText("Current:");

		detectorTableRB = new Label(detectorTablePanel, SWT.NONE);
		detectorTableRB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		detectorTableRB.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));

		new Label(detectorTablePanel, SWT.NONE);

		Label lblDetectorChange = new Label(detectorTablePanel, SWT.NONE);
		lblDetectorChange.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDetectorChange.setText("Change:");

		detectorTableSelector = new DaeExperimentSetupCombo(detectorTablePanel, SWT.DROP_DOWN | SWT.READ_ONLY,
				panelViewModel, "detectorTableSelector");
		detectorTableSelector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		// Spectra table selection
		spectraTablePanel = new Composite(grpTables, SWT.NONE);
		spectraTablePanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		spectraTablePanel.setLayout(new GridLayout(3, false));
		spectraTablePanel.setBackgroundMode(SWT.INHERIT_DEFAULT);

		Label lblSpectra = new Label(spectraTablePanel, SWT.NONE);
		lblSpectra.setLayoutData(gdLabels);
		lblSpectra.setText("Spectra Table:");

		Label lblSpectraRB = new Label(spectraTablePanel, SWT.NONE);
		lblSpectraRB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpectraRB.setText("Current:");

		spectraTableRB = new Label(spectraTablePanel, SWT.NONE);
		spectraTableRB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		spectraTableRB.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));

		new Label(spectraTablePanel, SWT.NONE);

		Label lblSpectraChange = new Label(spectraTablePanel, SWT.NONE);
		lblSpectraChange.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpectraChange.setText("Change:");

		spectraTableSelector = new DaeExperimentSetupCombo(spectraTablePanel, SWT.DROP_DOWN | SWT.READ_ONLY,
				panelViewModel, "spectraTableSelector");
		spectraTableSelector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Group grpMonitor = new Group(this, SWT.NONE);
		grpMonitor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpMonitor.setText("Monitor");
		grpMonitor.setLayout(new GridLayout(8, false));

		Label lblSpectrum = new Label(grpMonitor, SWT.NONE);
		lblSpectrum.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpectrum.setText("Spectrum:");

		monitorSpectrum = new DaeExperimentSetupSpinner(grpMonitor, SWT.BORDER, panelViewModel, "monitorSpectrum",
				"monitorSpectrum");
		monitorSpectrum.setMaximum(MAXIMUM_MONITOR_SPECTRUM);

		Label lblNewLabel_1 = new Label(grpMonitor, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_1.widthHint = 20;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);

		Label lblFrom = new Label(grpMonitor, SWT.NONE);
		lblFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFrom.setText("From:");

		from = new DaeExperimentSetupText(grpMonitor, SWT.BORDER, panelViewModel, "from", "DataAcquisitonPanel",
				"from");
		GridData gd_from = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_from.widthHint = 70;
		from.setLayoutData(gd_from);

		Label lblTo = new Label(grpMonitor, SWT.NONE);
		lblTo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTo.setText("To:");

		to = new DaeExperimentSetupText(grpMonitor, SWT.BORDER, panelViewModel, "to", "DataAcquisitonPanel", "to");
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

		btnFermiChopper = new DaeExperimentSetupButton(grpVetos, SWT.CHECK, panelViewModel, "btnFermiChopper");
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

		btnSMP = new DaeExperimentSetupButton(grpVetos, SWT.CHECK, panelViewModel, "btnSMP");
		btnSMP.setText("SMP (Chopper)");

		btnTs2Pulse = new DaeExperimentSetupButton(grpVetos, SWT.CHECK, panelViewModel, "btnTs2Pulse");
		btnTs2Pulse.setText("TS2 Pulse");

		btnIsisHz = new DaeExperimentSetupButton(grpVetos, SWT.CHECK, panelViewModel, "btnIsisHz");
		btnIsisHz.setText("ISIS 50 Hz");

		new Label(grpVetos, SWT.NONE);
		new Label(grpVetos, SWT.NONE);

		btnVeto0 = new DaeExperimentSetupButton(grpVetos, SWT.CHECK, panelViewModel, "btnVeto0");
		btnVeto0.setText("Veto 0");

		btnVeto1 = new DaeExperimentSetupButton(grpVetos, SWT.CHECK, panelViewModel, "btnVeto1");
		btnVeto1.setText("Veto 1");

		btnVeto2 = new DaeExperimentSetupButton(grpVetos, SWT.CHECK, panelViewModel, "btnVeto2");
		btnVeto2.setText("Veto 2");

		btnVeto3 = new DaeExperimentSetupButton(grpVetos, SWT.CHECK, panelViewModel, "btnVeto3");
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

		btnMuonMsMode = new DaeExperimentSetupButton(grpMuons, SWT.CHECK, panelViewModel, "btnMuonMsMode");
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

		btnMuonPulseFirst = new DaeExperimentSetupRadioButton(muonPulseComposite, SWT.FLAT | SWT.RADIO, panelViewModel,
				"dataAcBtns");
		btnMuonPulseFirst.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnMuonPulseFirst.setText("First");

		btnMuonPulseSecond = new DaeExperimentSetupRadioButton(muonPulseComposite, SWT.FLAT | SWT.RADIO, panelViewModel,
				"dataAcBtns");
		btnMuonPulseSecond.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnMuonPulseSecond.setText("Second");

		Group grpTiming = new Group(this, SWT.NONE);
		grpTiming.setText("Timing");
		grpTiming.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpTiming.setLayout(new GridLayout(2, false));

		// Panel to change timeing source
		cmpTimeingSource = new Composite(grpTiming, SWT.NONE);
		GridLayout gl_cmpTimeingSource = new GridLayout(2, false);
		gl_cmpTimeingSource.marginWidth = 0;
		gl_cmpTimeingSource.horizontalSpacing = 20;
		cmpTimeingSource.setLayout(gl_cmpTimeingSource);
		cmpTimeingSource.setBackgroundMode(SWT.INHERIT_DEFAULT);

		Label lblDaeTimeingSource = new Label(cmpTimeingSource, SWT.NONE);
		lblDaeTimeingSource.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDaeTimeingSource.setText("DAE Timing Source:");

		daeTimingSource = new DaeExperimentSetupCombo(cmpTimeingSource, SWT.DROP_DOWN | SWT.READ_ONLY, panelViewModel,
				"daeTimingSource");
		daeTimingSource.setItems(DaeTimingSource.allToString().toArray(new String[0]));
		GridData gd_daeTimingSource = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_daeTimingSource.widthHint = 100;
		daeTimingSource.setLayoutData(gd_daeTimingSource);
		new Label(grpTiming, SWT.NONE);

		// Panel to change autosave frequency.
		Composite cmpAutosaveFreq = new Composite(grpTiming, SWT.NONE);
		GridLayout gl_cmpAutosaveFreq = new GridLayout(2, false);
		gl_cmpAutosaveFreq.marginWidth = 0;
		gl_cmpAutosaveFreq.horizontalSpacing = 20;
		cmpAutosaveFreq.setLayout(gl_cmpAutosaveFreq);
		cmpAutosaveFreq.setBackgroundMode(SWT.INHERIT_DEFAULT);

		Label lblAutosaveEvery = new Label(cmpAutosaveFreq, SWT.NONE);
		lblAutosaveEvery.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAutosaveEvery.setText("Autosave every:");

		autosaveFrequency = new DaeExperimentSetupText(cmpAutosaveFreq, SWT.BORDER, panelViewModel, "autosaveFrequency",
				"DataAcquisitonPanel", "autosaveFrequency");
		GridData gd_autosaveFrequency = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_autosaveFrequency.widthHint = 100;
		autosaveFrequency.setLayoutData(gd_autosaveFrequency);

		// Panel to change autosave units.
		cmpAutosaveUnits = new Composite(grpTiming, SWT.NONE);
		GridLayout gl_cmpAutosaveUnits = new GridLayout(1, false);
		gl_cmpAutosaveUnits.marginWidth = 0;
		gl_cmpAutosaveUnits.horizontalSpacing = 20;
		cmpAutosaveUnits.setLayout(gl_cmpAutosaveUnits);
		cmpAutosaveUnits.setBackgroundMode(SWT.INHERIT_DEFAULT);

		autosaveUnits = new DaeExperimentSetupCombo(cmpAutosaveUnits, SWT.DROP_DOWN | SWT.READ_ONLY, panelViewModel,
				"autosaveUnits");
		autosaveUnits.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		autosaveUnits.setItems(AutosaveUnit.allToString().toArray(new String[0]));
		fillWidgetLists();

		this.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				removeListeners();
			}

		});

	}

	/**
	 * Binds model data to the relevant UI elements for automatic update.
	 * 
	 * @param viewModel the model holding the DAE settings.
	 */
	public void setModel(DataAcquisitionViewModel viewModel) {
		bindingContext = new DataBindingContext();

		bindingContext.bindList(WidgetProperties.items().observe(wiringTableSelector),
				BeanProperties.list("wiringTableList").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text().observe(wiringTableRB),
				BeanProperties.value(DaeSettingsProperties.WIRING_TABLE).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(wiringTableSelector),
				BeanProperties.value(DaeSettingsProperties.NEW_WIRING_TABLE).observe(viewModel));

		bindingContext.bindList(WidgetProperties.items().observe(detectorTableSelector),
				BeanProperties.list("detectorTableList").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text().observe(detectorTableRB),
				BeanProperties.value(DaeSettingsProperties.DETECTOR_TABLE).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(detectorTableSelector),
				BeanProperties.value(DaeSettingsProperties.NEW_DETECTOR_TABLE).observe(viewModel));

		bindingContext.bindList(WidgetProperties.items().observe(spectraTableSelector),
				BeanProperties.list("spectraTableList").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text().observe(spectraTableRB),
				BeanProperties.value(DaeSettingsProperties.SPECTRA_TABLE).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(spectraTableSelector),
				BeanProperties.value(DaeSettingsProperties.NEW_SPECTRA_TABLE).observe(viewModel));

		bindingContext.bindValue(WidgetProperties.spinnerSelection().observe(monitorSpectrum),
				BeanProperties.value(DaeSettingsProperties.MONITOR_SPECTRUM).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(from),
				BeanProperties.value(DaeSettingsProperties.FROM).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(to),
				BeanProperties.value(DaeSettingsProperties.TO).observe(viewModel));

		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnVeto0),
				BeanProperties.value(DaeSettingsProperties.VETO_0).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnVeto1),
				BeanProperties.value(DaeSettingsProperties.VETO_1).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnVeto2),
				BeanProperties.value(DaeSettingsProperties.VETO_2).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnVeto3),
				BeanProperties.value(DaeSettingsProperties.VETO_3).observe(viewModel));

		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnSMP),
				BeanProperties.value(DaeSettingsProperties.SMP_VETO).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnFermiChopper),
				BeanProperties.value(DaeSettingsProperties.FERMI_CHOPPER_VETO).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnTs2Pulse),
				BeanProperties.value(DaeSettingsProperties.TS2_PULSE_VETO).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnIsisHz),
				BeanProperties.value(DaeSettingsProperties.ISIS_50HZ_VETO).observe(viewModel));

		bindingContext.bindValue(WidgetProperties.text().observe(fcDelay),
				BeanProperties.value(DaeSettingsProperties.FC_DELAY).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.text().observe(fcWidth),
				BeanProperties.value(DaeSettingsProperties.FC_WIDTH).observe(viewModel));

		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnMuonMsMode),
				BeanProperties.value(DaeSettingsProperties.MUON_MS_MODE).observe(viewModel));
		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(daeTimingSource),
				BeanProperties.value(DaeSettingsProperties.TIMING_SOURCE).observe(viewModel));

		bindingContext.bindValue(WidgetProperties.buttonSelection().observe(btnMuonPulseFirst),
				BeanProperties.value(DaeSettingsProperties.MUON_CERENKOV_PULSE).observe(viewModel));

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(autosaveFrequency),
				BeanProperties.value("autosaveFrequency").observe(viewModel));
		bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(autosaveUnits),
				BeanProperties.value("autosaveUnits").observe(viewModel));

		btnMuonPulseFirst.setSelection(viewModel.getMuonCerenkovPulse());
		btnMuonPulseSecond.setSelection(!viewModel.getMuonCerenkovPulse());
	}

	/**
	 * Fills the lists of widgets.
	 */
	private void fillWidgetLists() {
		if (!daeExpSetupBtns.isEmpty()) {
			daeExpSetupBtns.clear();
		}
		daeExpSetupBtns.add(btnFermiChopper);
		daeExpSetupBtns.add(btnIsisHz);
		daeExpSetupBtns.add(btnMuonMsMode);
		daeExpSetupBtns.add(btnSMP);
		daeExpSetupBtns.add(btnTs2Pulse);
		daeExpSetupBtns.add(btnVeto0);
		daeExpSetupBtns.add(btnVeto1);
		daeExpSetupBtns.add(btnVeto2);
		daeExpSetupBtns.add(btnVeto3);

		if (!combos.isEmpty()) {
			combos.clear();
		}
		combos.add(detectorTableSelector);
		combos.add(wiringTableSelector);
		combos.add(spectraTableSelector);
		combos.add(daeTimingSource);
		combos.add(autosaveUnits);

		if (!textInputs.isEmpty()) {
			textInputs.clear();
		}
		textInputs.add(to);
		textInputs.add(from);
		textInputs.add(autosaveFrequency);

		if (!spinners.isEmpty()) {
			spinners.clear();
		}
		spinners.add(monitorSpectrum);

		if (!radioBtns.isEmpty()) {
			radioBtns.clear();
		}
		radioBtns.add(btnMuonPulseFirst);
		radioBtns.add(btnMuonPulseSecond);
		panelViewModel.setBtnsListToRadioButtons(radioBtns);
	}

	/**
	 * Creates a cache of the applied values for the different widgets.
	 */
	public void createInitialCachedValues() {
		panelViewModel.createInitialButtonsCachedValues(daeExpSetupBtns);
		panelViewModel.createInitialSpinnersCachedValues(spinners);
		panelViewModel.createInitialTextInputsCachedValues(textInputs);
		panelViewModel.createInitialComboCachedValues(combos);

		if (!radioBtnsRB.isEmpty()) {
			radioBtnsRB.clear();
		}
		radioBtnsRB.add(btnMuonPulseFirst.getSelection());
		radioBtnsRB.add(btnMuonPulseSecond.getSelection());
		panelViewModel.createInitialRadioButtonsCachedValues(radioBtns, radioBtnsRB);
	}

	/**
	 * Resets the cache of the applied values for the different widgets.
	 */
	public void resetCachedValue() {
		panelViewModel.resetButtonsCachedValues(daeExpSetupBtns);
		panelViewModel.resetSpinnersCachedValues(spinners);
		panelViewModel.resetTextInputsCachedValues(textInputs);
		panelViewModel.resetComboCachedValues(combos);

		if (!radioBtnsRB.isEmpty()) {
			radioBtnsRB.clear();
		}
		radioBtnsRB.add(btnMuonPulseFirst.getSelection());
		radioBtnsRB.add(btnMuonPulseSecond.getSelection());
		panelViewModel.resetRadioButtonsCachedValues(radioBtns, radioBtnsRB);
	}

	/**
	 * Removes the listeners out dated when changes were applied.
	 */
	public void removeListeners() {
		panelViewModel.removesRadioButtonsListener(radioBtns);
		panelViewModel.removesSpinnerListeners(spinners);
		panelViewModel.removesTextInputListeners(textInputs);
		panelViewModel.removesButtonListeners(daeExpSetupBtns);
		panelViewModel.removesCombosListeners(combos);
	}

	/**
	 * Resets the cached values for the different widgets.
	 */
	public void updateListeners() {
		resetCachedValue();
	}

	/**
	 * Goes over every widget and adds a label to a widget if its value is different
	 * from the one applied on the instrument.
	 */
	public void ifWidgetValueDifferentFromCachedValueThenChangeLabel() {
		panelViewModel.ifButtonValuesDifferentFromCachedValueThenChangeLabel(daeExpSetupBtns);
		panelViewModel.ifSpinnerValuesDifferentFromCachedValueThenChangeLabel(spinners);
		panelViewModel.ifTextInputValuesDifferentFromCachedValueThenChangeLabel(textInputs);
		panelViewModel.ifComboValuesDifferentFromCachedValueThenChangeLabel(combos);
		panelViewModel.ifRadioButtonValuesDifferentFromCachedValuesThenChangeLabel(radioBtns);
	}

}
