
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

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.csstudio.swt.rtplot.RTTimePlot;
import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.model.ArchiveRescale;
import org.csstudio.trends.databrowser2.model.AxisConfig;
import org.csstudio.trends.databrowser2.model.Model;
import org.csstudio.trends.databrowser2.model.ModelItem;
import org.csstudio.trends.databrowser2.model.ModelListener;
import org.csstudio.trends.databrowser2.model.PVItem;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.csstudio.trends.databrowser2.ui.Controller;
import org.csstudio.trends.databrowser2.ui.ModelBasedPlot;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Provides access to the data browser to show data from TS1/TS2 beam currents
 * as a plot within a view.
 * 
 * Always used as derived classes that specify the time duration of the plot
 * (e.g. hourly, daily)
 */
public class BeamGraphView implements ModelListener {
	
    /** View ID registered in plugin.xml. */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.beamstatus.views.BeamStatusGraphView"; //$NON-NLS-1$

    /** Title for the Y-axis. */
    private static final String Y_AXIS_TITLE = "Beam current (μA)";

    /** Upper limit for the beam current. */
    private static final double CURRENT_UPPER = 250.0;

    /** Lower limit for the beam current. */
    private static final double CURRENT_LOWER = 0.0;

    /** TS1 beam current PV name. */
    private static final String TS1_BEAM_CURRENT_PV = "AC:TS1:BEAM:CURR";

    /** TS2 beam current PV name. */
    private static final String TS2_BEAM_CURRENT_PV = "AC:TS2:BEAM:CURR";

    /** Synchotron beam current PV name. */
    private static final String SYNCH_BEAM_CURRENT_PV = "AC:SYNCH:BEAM:CURR";

    /** Plot. */
    private ModelBasedPlot modelPlot;

    /** Model of the currently active Data Browser plot or <code>null</code>. */
    private Model model;

    /** Controller that links model and plot. */
    private Controller controller = null;

    /** Number of milliseconds in an hour. */
    private static final long MILLISECONDS_IN_HOUR = 3600 * 1000;
    
    /** Number of milliseconds in a day. */
    private static final long MILLISECONDS_IN_DAY = MILLISECONDS_IN_HOUR * 24;
    
    /** Current plot time span in milliseconds. */
    private long currentPlotTimespanMilliseconds = MILLISECONDS_IN_DAY;

    /** Custom magenta colour for high-contrast plot traces. */
    private static final RGB MAGENTA = new RGB(255, 0, 170);

    /** Custom green colour for high-contrast plot traces. */
    private static final RGB GREEN = new RGB(0, 200, 0);

    /** Custom blue colour for high-contrast plot traces. */
    private static final RGB BLUE = new RGB(0, 0, 255);

    /** Default colour for undefined plot traces. */
    private static final RGB DEFAULT = new RGB(0, 0, 0);
    
    /** Shell for opening error dialogs. */
    private Shell shell;

    /** Title for the plot. */
    private static final String PLOT_TITLE = "Beam Current";

    /**
     * Creates the Beam Graph view.
     * 
     * @param parent The parent container obtained via dependency injection
     */
    @PostConstruct 
    public void createPartControl(final Composite parent) {
    	// Remember what shell we're using
        shell = parent.getShell();
        
        
//        IExtensionRegistry r = RegistryFactory.getRegistry();
//        for (IConfigurationElement t : r.getConfigurationElementsFor("org.csstudio.archive.reader.ArchiveReader")) {
//        	final String plugin = t.getContributor().getName();
//            final String prefix = t.getAttribute("prefix");
//            System.out.println("HELLO THIS IS DEBUGGGING: " + plugin + ", " + prefix);
//            
//        }
        
        
        // Create the view model
        configureModel();

        // Create the basic panel
        parent.setLayout(new GridLayout(1, false));
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        // Add the radio buttons
        createTimeRangeRadioButtons(parent);
        
        // Create the basic plot
        Composite plotComposite = new Composite(parent, SWT.NONE);
        plotComposite.setLayout(new GridLayout(1, false));
        plotComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        modelPlot = new ModelBasedPlot(plotComposite);

         for (String pv : Arrays.asList(TS1_BEAM_CURRENT_PV, TS2_BEAM_CURRENT_PV, SYNCH_BEAM_CURRENT_PV)) {
        	addTrace(generatePVItem(pv));
         }

        // Create and start controller
		try {
			controller = new Controller(shell, model, modelPlot);
            controller.start();
		} catch (Exception ex) {
            onError(ex);
        }

        createBeamStatusPlot();
    }

    private void createTimeRangeRadioButtons(final Composite parent) {
        Composite controlsComposite = new Composite(parent, SWT.NONE);
        controlsComposite.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
        controlsComposite.setLayout(new RowLayout());

        Button dailyButton = new Button(controlsComposite, SWT.RADIO);
        dailyButton.setText("Last 24 Hours");
        dailyButton.setSelection(true);
        dailyButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setTimeRange(MILLISECONDS_IN_DAY);
            }
        });

        Button hourlyButton = new Button(controlsComposite, SWT.RADIO);
        hourlyButton.setText("Last Hour");
        hourlyButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setTimeRange(MILLISECONDS_IN_HOUR);
            }
        });

    }

    private void createBeamStatusPlot() {
        RTTimePlot rtPlot = modelPlot.getPlot();
        rtPlot.setTitle(Optional.of(PLOT_TITLE));
        rtPlot.setEnabled(false);
        rtPlot.showToolbar(false);
        rtPlot.showLegend(false);
        rtPlot.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    /**
     * Sets various properties for the given axis.
     * 
     * @param axisConfig
     *            The axis configuration whose properties will be updated.
     */
    private void setAxisProperties(AxisConfig axisConfig) {
        axisConfig.setName(Y_AXIS_TITLE);
        axisConfig.useAxisName(true);
        axisConfig.setRange(CURRENT_LOWER, CURRENT_UPPER);
        axisConfig.setAutoScale(false);
        axisConfig.setColor(new RGB(0, 0, 0));
    }

    protected void configureModel() {
    	model = new Model();
    	model.addListener(this);
        model.setArchiveRescale(ArchiveRescale.NONE);
        AxisConfig axis = new AxisConfig("Beam current");
        setAxisProperties(axis);
        model.addAxis(axis);
        updateModelTimeRange();
    }

    /**
     * Add the PV by the specified address to the model for inclusion in the
     * plot.
     * 
     * @param pvAddress
     *            Name of the PV to add to the plot
     */
	private PVItem generatePVItem(final String pvAddress) {
        try {
            PVItem newItem = new PVItem(pvAddress, Preferences.getScanPeriod());
            String displayName;
            RGB rgb;
            switch (pvAddress) {
                case TS1_BEAM_CURRENT_PV:
                    displayName = "TS1";
                    rgb = BLUE;
                    break;
                case TS2_BEAM_CURRENT_PV:
                    displayName = "TS2";
                    rgb = MAGENTA;
                    break;
                case SYNCH_BEAM_CURRENT_PV:
                    displayName = "Synchrotron";
                    rgb = GREEN;
                    break;
                default:
                    rgb = DEFAULT;
                    displayName = newItem.getDisplayName();
            }
            newItem.setDisplayName(displayName);
            newItem.setColor(rgb);
            return newItem;
        } catch (Exception ex) {
        	onError(ex);
            return null;
        }
    }

    /**
     * Add this PV to the model for inclusion in the plot.
     * 
     * @param newItem
     *            PV to add to the plot
     */
    protected void addTrace(final PVItem newItem) {
        newItem.useDefaultArchiveDataSources();
        try {
			model.addItem(newItem);
		} catch (Exception ex) {
			onError(ex);
		}
        updateModelTimeRange();
    }

    private void setTimeRange(final long timerange) {
        currentPlotTimespanMilliseconds = timerange;
        updateModelTimeRange();
    }

    private void updateModelTimeRange() {
    	try {
            model.setTimerange(Instant.now().minusMillis(currentPlotTimespanMilliseconds), Instant.now());
        } catch (Exception ex) {
            onError(ex);
        }
    }
    
    private void onError(Throwable t) {
    	MessageDialog.openError(shell, Messages.Error, NLS.bind(Messages.ErrorFmt, t.toString()));
    	IsisLog.getLogger(getClass()).error(t);
    }
    
    /**
     * Disposes of the model before disposing of this view.
     */
    @PreDestroy
    public void dispose() {
    	if (model != null) {
    		model.removeListener(this);
    	}
    }

    // Following methods are defined as they are mandatory to fulfil
    // ModelListener interface, but they are not used at all to update
    // this sample view.
    /**
     * {@inheritDoc}
     */
    @Override
    public void itemAdded(ModelItem item) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemRemoved(ModelItem item) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedItemLook(ModelItem item) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedArchiveRescale() {
    }

    @Override
    public void changedTimerange() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedItemVisibility(ModelItem item) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedItemDataConfig(PVItem item) {
    }

    @Override
    public void scrollEnabled(boolean scrollEnabled) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedAnnotations() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemRefreshRequested(PVItem item) {
    }

	@Override
	public void changeTimeAxisConfig() {
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void changedAxis(Optional<AxisConfig> arg0) {		
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void changedColorsOrFonts() {		
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public void changedLayout() {		
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void changedSaveChangesBehavior(boolean arg0) {
		
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void changedTiming() {
		
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void changedTitle() {
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void selectedSamplesChanged() {	
	}
}
