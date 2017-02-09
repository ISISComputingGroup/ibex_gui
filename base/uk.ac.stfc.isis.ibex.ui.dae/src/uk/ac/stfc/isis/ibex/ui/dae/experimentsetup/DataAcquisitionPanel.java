
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

/**
 * Panel to show the information regarding the data acquisition of the DAE.
 */
public class DataAcquisitionPanel extends Composite {
    private Combo wiringTableSelector;
    private Combo detectorTableSelector;
    private Combo spectraTableSelector;
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
    private Label wiringTableRB;
    private Label detectorTableRB;
    private Label spectraTableRB;

    private DataBindingContext bindingContext;
    private DataAcquisitionViewModel model;


    /**
     * The default constructor for the panel.
     * 
     * @param parent
     *            The parent composite that this panel belongs to.
     * @param style
     *            The SWT flags giving the style of the panel.
     */
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
    public DataAcquisitionPanel(Composite parent, int style) {
        super(parent, style);
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
        Composite wiringTablePanel = new Composite(grpTables, SWT.NONE);
        wiringTablePanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        wiringTablePanel.setLayout(new GridLayout(3, false));

        Label lblWiring = new Label(wiringTablePanel, SWT.NONE);
        lblWiring.setLayoutData(gdLabels);
        lblWiring.setText("Wiring Table:");

        Label lblWiringRB = new Label(wiringTablePanel, SWT.NONE);
        lblWiringRB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblWiringRB.setText("Current:");

        wiringTableRB = new Label(wiringTablePanel, SWT.NONE);
        wiringTableRB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        wiringTableRB.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));

        Label lblWiringSpacer = new Label(wiringTablePanel, SWT.NONE);

        Label lblWiringChange = new Label(wiringTablePanel, SWT.NONE);
        lblWiringChange.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblWiringChange.setText("Change:");

        wiringTableSelector = new Combo(wiringTablePanel, SWT.DROP_DOWN | SWT.READ_ONLY);
        wiringTableSelector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        // Detector table selection
        Composite detectorTablePanel = new Composite(grpTables, SWT.NONE);
        detectorTablePanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        detectorTablePanel.setLayout(new GridLayout(3, false));

        Label lblDetector = new Label(detectorTablePanel, SWT.NONE);
        lblDetector.setLayoutData(gdLabels);
        lblDetector.setText("Detector Table:");

        Label lblDetectorRB = new Label(detectorTablePanel, SWT.NONE);
        lblDetectorRB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblDetectorRB.setText("Current:");

        detectorTableRB = new Label(detectorTablePanel, SWT.NONE);
        detectorTableRB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        detectorTableRB.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));

        Label lblDetectorSpacer = new Label(detectorTablePanel, SWT.NONE);

        Label lblDetectorChange = new Label(detectorTablePanel, SWT.NONE);
        lblDetectorChange.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblDetectorChange.setText("Change:");

        detectorTableSelector = new Combo(detectorTablePanel, SWT.DROP_DOWN | SWT.READ_ONLY);
        detectorTableSelector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        // Spectra table selection
        Composite spectraTablePanel = new Composite(grpTables, SWT.NONE);
        spectraTablePanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        spectraTablePanel.setLayout(new GridLayout(3, false));

        Label lblSpectra = new Label(spectraTablePanel, SWT.NONE);
        lblSpectra.setLayoutData(gdLabels);
        lblSpectra.setText("Spectra Table:");

        Label lblSpectraRB = new Label(spectraTablePanel, SWT.NONE);
        lblSpectraRB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblSpectraRB.setText("Current:");

        spectraTableRB = new Label(spectraTablePanel, SWT.NONE);
        spectraTableRB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        spectraTableRB.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));

        Label lblSpectraSpacer = new Label(spectraTablePanel, SWT.NONE);

        Label lblSpectraChange = new Label(spectraTablePanel, SWT.NONE);
        lblSpectraChange.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblSpectraChange.setText("Change:");

        spectraTableSelector = new Combo(spectraTablePanel, SWT.DROP_DOWN | SWT.READ_ONLY);
        spectraTableSelector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        Group grpMonitor = new Group(this, SWT.NONE);
        grpMonitor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        grpMonitor.setText("Monitor");
        grpMonitor.setLayout(new GridLayout(8, false));

        Label lblSpectrum = new Label(grpMonitor, SWT.NONE);
        lblSpectrum.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblSpectrum.setText("Spectrum:");

        monitorSpectrum = new Spinner(grpMonitor, SWT.BORDER);
        monitorSpectrum.setMaximum(1000000);

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

        wiringTableSelector.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.setNewWiringTable(wiringTableSelector.getText());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        detectorTableSelector.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.setNewDetectorTable(detectorTableSelector.getText());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        spectraTableSelector.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.setNewSpectraTable(spectraTableSelector.getText());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    /**
     * Binds model data to the relevant UI elements for automatic update.
     * 
     * @param viewModel the model holding the DAE settings.
     */
    public void setModel(DataAcquisitionViewModel viewModel) {
        this.model = viewModel;

        bindingContext = new DataBindingContext();

        bindingContext.bindList(WidgetProperties.items().observe(wiringTableSelector),
                BeanProperties.list("wiringTableList").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text().observe(wiringTableRB),
                BeanProperties.value("wiringTable").observe(viewModel));

        bindingContext.bindList(WidgetProperties.items().observe(detectorTableSelector),
                BeanProperties.list("detectorTableList").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text().observe(detectorTableRB),
                BeanProperties.value("detectorTable").observe(viewModel));

        bindingContext.bindList(WidgetProperties.items().observe(spectraTableSelector),
                BeanProperties.list("spectraTableList").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text().observe(spectraTableRB),
                BeanProperties.value("spectraTable").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.selection().observe(monitorSpectrum),
                BeanProperties.value("monitorSpectrum").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(from),
                BeanProperties.value("from").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(to),
                BeanProperties.value("to").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.selection().observe(btnVeto0),
                BeanProperties.value("veto0").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.selection().observe(btnVeto1),
                BeanProperties.value("veto1").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.selection().observe(btnVeto2),
                BeanProperties.value("veto2").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.selection().observe(btnVeto3),
                BeanProperties.value("veto3").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.selection().observe(btnSMP),
                BeanProperties.value("smpVeto").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.selection().observe(btnFermiChopper),
                BeanProperties.value("fermiChopperVeto").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.selection().observe(btnTs2Pulse),
                BeanProperties.value("ts2PulseVeto").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.selection().observe(btnIsisHz),
                BeanProperties.value("isis50HzVeto").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.text().observe(fcDelay),
                BeanProperties.value("fcDelay").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text().observe(fcWidth),
                BeanProperties.value("fcWidth").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.selection().observe(btnMuonMsMode),
                BeanProperties.value("muonMsMode").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(daeTimingSource),
                BeanProperties.value("timingSource").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.selection().observe(btnMuonPulseFirst),
                BeanProperties.value("muonCerenkovPulse").observe(viewModel));

        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(autosaveFrequency),
                BeanProperties.value("autosaveFrequency").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(autosaveUnits),
                BeanProperties.value("autosaveUnits").observe(viewModel));
    }
}
