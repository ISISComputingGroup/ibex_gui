
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

package uk.ac.stfc.isis.ibex.ui.dae;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.dae.detectordiagnostics.DetectorDiagnosticsPanel;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.ExperimentSetup;
import uk.ac.stfc.isis.ibex.ui.dae.run.RunSummary;
import uk.ac.stfc.isis.ibex.ui.dae.runinformation.RunInformationPanel;
import uk.ac.stfc.isis.ibex.ui.dae.spectra.SpectraPlotsPanel;
import uk.ac.stfc.isis.ibex.ui.dae.vetos.VetosPanel;

/**
 * Main DAE panel.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class DaeView extends ViewPart {

    /**
     * Listener to determine which tab is currently selected
     */
    private final class TabSelectionListener implements SelectionListener {
        /**
         * The tab item containing detector diagnostics
         */
        private final CTabItem tbtmDiagnostics;

        /**
         * Constructor.
         * 
         * @param tbtmDiagnostics
         *            The tab item containing detector diagnostics
         */
        private TabSelectionListener(CTabItem tbtmDiagnostics) {
            this.tbtmDiagnostics = tbtmDiagnostics;
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            Widget item = e.item;
            onSelectionChanged(item);
        }

        /**
         * Update the model with the tab that was just selected
         * 
         * @param item
         *            the tab that was just selected; null means no tab is
         *            selected or no tab is visible i.e. when a new perspective
         *            is chosen
         */
        private void onSelectionChanged(Widget item) {
            if (item == tbtmDiagnostics) {
                model.setActiveTab(DaeViewModel.ActiveTab.DETECTOR_DIAGNOSTICS);
            } else {
                model.setActiveTab(DaeViewModel.ActiveTab.OTHER);
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            // do nothing
        }
    }

    /**
     * Listener for changes to a part, to see if it has become visible. When it
     * is visible attach the selection listener.
     */
    class PartListener implements IPartListener2 {

        private ViewPart daeViewPart;
        private CTabFolder tabFolder;
        private TabSelectionListener tabSelectionListener;


        /**
         * Instantiates a new part listener.
         *
         * @param daeViewPart
         *            the dae view par
         * @param tabFolder
         *            the tab folder
         * @param tabSelectionListener
         *            the tab selection listener
         */
        PartListener(ViewPart daeViewPart, CTabFolder tabFolder, TabSelectionListener tabSelectionListener) {
            this.daeViewPart = daeViewPart;
            this.tabFolder = tabFolder;
            this.tabSelectionListener = tabSelectionListener;
        }

        @Override
        public void partVisible(IWorkbenchPartReference partRef) {
            IWorkbenchPart part = partRef.getPart(false);
            if (part == daeViewPart && tabSelectionListener != null) {
                tabFolder.addSelectionListener(tabSelectionListener);
                tabSelectionListener.onSelectionChanged(tabFolder.getSelection());
            }
        }

        @Override
        public void partHidden(IWorkbenchPartReference partRef) {
            IWorkbenchPart part = partRef.getPart(false);
            if (part == daeViewPart && tabSelectionListener != null) {
                tabFolder.removeSelectionListener(tabSelectionListener);
                tabSelectionListener.onSelectionChanged(null);
            }

        }

        @Override
        public void partActivated(IWorkbenchPartReference partRef) {
            //
        }

        @Override
        public void partBroughtToTop(IWorkbenchPartReference partRef) {
            //
        }

        @Override
        public void partClosed(IWorkbenchPartReference partRef) {
            //
        }

        @Override
        public void partDeactivated(IWorkbenchPartReference partRef) {
            //
        }

        @Override
        public void partOpened(IWorkbenchPartReference partRef) {
            //
        }

        @Override
        public void partInputChanged(IWorkbenchPartReference partRef) {
            //
        }

    }
	
    /**
     * The view ID.
     */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.dae.views.DaeView"; //$NON-NLS-1$
	
	private RunSummary runSummary;
	private ExperimentSetup experimentSetup;
	private VetosPanel vetosPanel;
	private RunInformationPanel runInformation;
	private SpectraPlotsPanel spectraPanel;
	private DaeViewModel model;

	
	private static final Display DISPLAY = Display.getCurrent();

    /** property that the model is running, for deregistering the listener. */
    private UpdatedValue<Boolean> modelIsRunningProperty;

    /** Listener for changes in experimental change. **/
    private PropertyChangeListener experimentalChangeListener;
	
    /**
     * Sets the model for the DAE view.
     *
     * @param viewModel the new model
     */
	public void setModel(final DaeViewModel viewModel) {
		runSummary.setModel(viewModel.runSummary());		
		
        experimentalChangeListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				configureExperimentSetupForRunState(viewModel.isRunning().getValue());
			}
        };
        modelIsRunningProperty = viewModel.isRunning();
        modelIsRunningProperty.addPropertyChangeListener(experimentalChangeListener, true);
		
		experimentSetup.bind(viewModel.experimentSetup());	
		vetosPanel.setModel(viewModel);
		runInformation.setModel(viewModel);
		spectraPanel.setModel(viewModel.spectra());

	}
	
	@Override
	public void dispose() {
        if (modelIsRunningProperty != null) {
            modelIsRunningProperty.removePropertyChangeListener(experimentalChangeListener);
        }
		super.dispose();
		model.close();
	}
	
    private void configureExperimentSetupForRunState(final Boolean isRunning) {
		DISPLAY.asyncExec(new Runnable() {	
			@Override
			public void run() {
                experimentSetup.setChildrenEnabled(isRunning != null && !isRunning);
			}
		});
	}
	
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		
		model = DaeUI.getDefault().viewModel();
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite container = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(container);
		
        GridData gdContainer = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		container.setLayoutData(gdContainer);
		GridLayout glContainer = new GridLayout(1, false);
		glContainer.horizontalSpacing = 0;
		glContainer.verticalSpacing = 0;
		glContainer.marginWidth = 0;
		glContainer.marginHeight = 0;
		container.setLayout(glContainer);
		
		Composite titleComposite = new Composite(container, SWT.NONE);
		titleComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		titleComposite.setLayout(new GridLayout(1, false));
		
		Label lblTitle = new Label(titleComposite, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblTitle.setAlignment(SWT.CENTER);
		lblTitle.setFont(SWTResourceManager.getFont("Arial", 16, SWT.NORMAL));
		lblTitle.setText("DAE Control Program");
		
		CTabFolder tabFolder = new CTabFolder(container, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tbtmRunSummary = new CTabItem(tabFolder, SWT.NONE);
		tbtmRunSummary.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/play.png"));
		tbtmRunSummary.setText("Run Summary");
			
		Composite runSummaryComposite = new Composite(tabFolder, SWT.NONE);
		tbtmRunSummary.setControl(runSummaryComposite);
		runSummaryComposite.setLayout(new GridLayout(1, false));
		runSummary = new RunSummary(runSummaryComposite, SWT.NONE, model.runSummary());
		runSummary.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		CTabItem tbtmExperimentSetup = new CTabItem(tabFolder, SWT.NONE);
		tbtmExperimentSetup.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/setup.png"));
		tbtmExperimentSetup.setText("Experiment Setup");
		
		Composite experimentalSetupComposite = new Composite(tabFolder, SWT.NONE);
		tbtmExperimentSetup.setControl(experimentalSetupComposite);
		experimentalSetupComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		experimentSetup = new ExperimentSetup(experimentalSetupComposite, SWT.NONE);
		
		CTabItem tbtmRunInformation = new CTabItem(tabFolder, SWT.NONE);
		tbtmRunInformation.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/info.png"));
		tbtmRunInformation.setText("Run Information");
		
		Composite runInformationComposite = new Composite(tabFolder, SWT.NONE);
		tbtmRunInformation.setControl(runInformationComposite);
		runInformationComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		runInformation = new RunInformationPanel(runInformationComposite, SWT.NONE);
		
		CTabItem tbtmSpectraPlots = new CTabItem(tabFolder, SWT.NONE);
		tbtmSpectraPlots.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/plot.png"));
		tbtmSpectraPlots.setText("Spectra Plots");
		
		Composite spectraComposite = new Composite(tabFolder, SWT.NONE);
		tbtmSpectraPlots.setControl(spectraComposite);
		spectraComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		spectraPanel = new SpectraPlotsPanel(spectraComposite, SWT.NONE);

        final CTabItem tbtmDiagnostics = new CTabItem(tabFolder, SWT.NONE);
		tbtmDiagnostics.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/monitor.png"));
		tbtmDiagnostics.setText("Detector Diagnostics");
		
		Composite diagnosticsComposite = new Composite(tabFolder, SWT.NONE);
        tbtmDiagnostics.setControl(diagnosticsComposite);
        diagnosticsComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
        

		new DetectorDiagnosticsPanel(diagnosticsComposite, SWT.NONE);

        TabSelectionListener tabSelectionListener = new TabSelectionListener(tbtmDiagnostics);
        DaeView.PartListener innerObject = this.new PartListener(this, tabFolder, tabSelectionListener);
        getSite().getPage().addPartListener(innerObject);


		CTabItem tbtmVetos = new CTabItem(tabFolder, SWT.NONE);
		tbtmVetos.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/veto.png"));
		tbtmVetos.setText("Vetos");
		
		Composite vetosComposite = new Composite(tabFolder, SWT.NONE);
		tbtmVetos.setControl(vetosComposite);
		vetosComposite.setLayout(new GridLayout(1, false));
		
		vetosPanel = new VetosPanel(vetosComposite, SWT.NONE);
		vetosPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		createActions();
		initializeToolBar();
		initializeMenu();
		
		setModel(DaeUI.getDefault().viewModel());
		tabFolder.setSelection(0);
        scrolledComposite.setMinSize(new Point(600, 500));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		GridData gdComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gdComposite.widthHint = 600;
		composite.setLayoutData(gdComposite);
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	@SuppressWarnings("unused")
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	@SuppressWarnings("unused")
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {		
	}
}
