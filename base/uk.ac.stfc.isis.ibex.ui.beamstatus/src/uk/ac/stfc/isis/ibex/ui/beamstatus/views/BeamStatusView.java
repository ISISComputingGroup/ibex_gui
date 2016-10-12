
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

import java.util.Calendar;

import org.csstudio.apputil.time.AbsoluteTimeParser;
import org.csstudio.swt.xygraph.figures.Trace;
import org.csstudio.swt.xygraph.figures.Trace.PointStyle;
import org.csstudio.swt.xygraph.figures.XYGraph;
import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.editor.DataBrowserAwareView;
import org.csstudio.trends.databrowser2.model.ArchiveRescale;
import org.csstudio.trends.databrowser2.model.AxisConfig;
import org.csstudio.trends.databrowser2.model.Model;
import org.csstudio.trends.databrowser2.model.ModelItem;
import org.csstudio.trends.databrowser2.model.ModelListener;
import org.csstudio.trends.databrowser2.model.PVItem;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.csstudio.trends.databrowser2.ui.Controller;
import org.csstudio.trends.databrowser2.ui.Plot;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.beamstatus.BeamStatusGraphDataProvider;

/**
 * Provides access to the data browser to show data from TS1/TS2 beam currents
 * as a plot within a view.
 * 
 * Always used as derived classes that specify the time duration of the plot
 * (e.g. hourly, daily)
 */
public class BeamStatusView extends DataBrowserAwareView implements ModelListener {
	
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
    private Plot plot;

    /** Model of the currently active Data Browser plot or <code>null</code>. */
    private Model model;

    /** Controller that links model and plot. */
    private Controller controller = null;

    /** Number of milliseconds in a day. */
    private static final long MILLISECONDS_IN_DAY = 3600 * 1000 * 24;

    /** Number of milliseconds in an hour. */
    private static final long MILLISECONDS_IN_HOUR = 3600 * 1000;
    
    /** Current plot time span in milliseconds. */
    private long currentPlotTimespanMilliseconds = MILLISECONDS_IN_DAY;

    /** Title for the plot. */
    private static final String PLOT_TITLE = "Beam Current";

    private static final RGB MAGENTA = new RGB(204, 0, 153); // SWTResourceManager.getColor(204,
                                                             // 0, 153);
    private static final RGB GREEN = new RGB(0, 255, 0); // SWTResourceManager.getColor(0,
                                                         // 255, 0);
    private static final RGB BLUE = new RGB(0, 0, 255); // SWTResourceManager.getColor(0,
                                                        // 0, 255);
    private static final RGB DEFAULT = new RGB(0, 0, 0);

//    private static final Color[] traceCols = { MAGENTA, GREEN, BLUE };
    private int numTraces = 0;

    /** {@inheritDoc} */
    @Override
    protected void doCreatePartControl(final Composite parent) {
        // Arrange disposal
        parent.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {

                // Be ignorant of any change of the current model after
                // this view is disposed.
                if (model != null) {
                    model.removeListener(BeamStatusView.this);
                }
            }
        });

        parent.setLayout(new GridLayout(2, false));
        Composite graphPanel = new Composite(parent, SWT.NONE);
        graphPanel.setLayout(new GridLayout(1, false));
        graphPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        createTimeRangeRadioButtons(graphPanel);
        createBeamStatusPlot(graphPanel);

        BeamStatusPanel statusPanel =
                new BeamStatusPanel(parent, SWT.BORDER | SWT.V_SCROLL);
        GridData gdStatus = new GridData(SWT.CENTER, SWT.CENTER, false, true);
        statusPanel.setLayoutData(gdStatus);

        selectPV(TS1_BEAM_CURRENT_PV);
        selectPV(TS2_BEAM_CURRENT_PV);
        selectPV(SYNCH_BEAM_CURRENT_PV);

        if (model != null && model.getAxisCount() > 0) {
            setAxisProperties(model.getAxis(model.getAxisCount() - 1));
        }

        // Create and start controller
        try {
            controller = new Controller(parent.getShell(), model, plot);
            controller.start();
        } catch (Exception ex) {
            MessageDialog.openError(parent.getShell(), Messages.Error,
                    NLS.bind(Messages.ControllerStartErrorFmt, ex.getMessage()));
        }

    }

    private void createTimeRangeRadioButtons(final Composite parent) {
        Composite controlsComposite = new Composite(parent, SWT.NONE);

        GridData layoutData = new GridData();
        layoutData.grabExcessHorizontalSpace = false;
        layoutData.horizontalAlignment = SWT.CENTER;
        controlsComposite.setLayoutData(layoutData);

        controlsComposite.setLayout(new RowLayout());

        Button dailyButton = new Button(controlsComposite, SWT.RADIO);
        dailyButton.setText("Last 24 Hours");
        dailyButton.setSelection(true);
        dailyButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setTimeRangeDaily();
            }
        });

        Button hourlyButton = new Button(controlsComposite, SWT.RADIO);
        hourlyButton.setText("Last Hour");
        hourlyButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setTimeRangeHourly();
            }
        });

    }

    private void createBeamStatusPlot(final Composite parent) {
        // The canvas has to be wrapped in a composite so that the canvas has
        // (0,0) coordinate.
        // This is a work around for the inconsistency between
        // figure.getBounds() and gc.getclipping().
        Composite plotComposite = new Composite(parent, SWT.NONE);
        GridData layoutData = new GridData();
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.grabExcessVerticalSpace = true;
        layoutData.horizontalAlignment = SWT.FILL;
        layoutData.verticalAlignment = SWT.FILL;
        plotComposite.setLayoutData(layoutData);

        plotComposite.setLayout(new FillLayout());

        // Double_BUFFERED is required to force RAP to use NativeGraphicsSource.
        // By default, it uses BufferedGraphicsSource which has problem to
        // render it in web browser.
        final Canvas canvas = new Canvas(plotComposite, SWT.DOUBLE_BUFFERED);

        // Create plot with basic configuration
        plot = Plot.forCanvas(canvas);
        plot.getXYGraph().setTitle(getPlotTitle());
        plot.setToolbarVisible(false);
    }

    /**
     * Sets various properties for the given axis.
     * 
     * @param axisConfig
     *            The axis configuration whose properties will be updated.
     */
    private void setAxisProperties(AxisConfig axisConfig) {
        setYAxisName(axisConfig);
        setYAxisColor(axisConfig);
        setYAxisRange(axisConfig);
    }

    /**
     * Sets the title for the given axis.
     * 
     * @param axis
     *            The axis configuration whose properties will be updated.
     */
    private void setYAxisName(AxisConfig axis) {
        axis.setName(Y_AXIS_TITLE);
    }

    /**
     * Sets the range for the given axis.
     * 
     * @param axis
     *            The axis configuration whose properties will be updated.
     */
    private void setYAxisRange(AxisConfig axis) {
        axis.setRange(CURRENT_LOWER, CURRENT_UPPER);
        axis.setAutoScale(false);
        model.setArchiveRescale(ArchiveRescale.NONE);
    }

    /**
     * Sets the colour for the given axis.
     * 
     * @param axis The axis configuration whose properties will be updated.
     */
    private void setYAxisColor(AxisConfig axis) {
        axis.setColor(new RGB(0, 0, 0));
    }

    @Override
    public void setFocus() {
            return;
        }

    @Override
    protected void updateModel(final Model oldModel, final Model newModel) {
        if (newModel == null) {
            return;
        }

        model = newModel;
        if (oldModel != newModel) {
            if (oldModel != null) {
                oldModel.removeListener(this);
            }

            if (newModel != null) {
                newModel.addListener(this);
            }
        }
    }

    /**
     * Add the PV by the specified address to the model for inclusion in the
     * plot.
     * 
     * @param pvAddress
     *            Name of the PV to add to the plot
     */
    private void selectPV(final String pvAddress) {
        try {
            PVItem newItem = new PVItem(pvAddress, Preferences.getScanPeriod());
            String displayName;
            RGB rgb;
            switch (pvAddress) {
                case TS1_BEAM_CURRENT_PV:
                    displayName = "TS1";
                    rgb = MAGENTA;
                    break;
                case TS2_BEAM_CURRENT_PV:
                    displayName = "TS2";
                    rgb = BLUE;
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
            selectPV(newItem);
        } catch (Exception ex) {
            MessageDialog.openError(getSite().getShell(), Messages.Error, NLS.bind(Messages.ErrorFmt, ex.getMessage()));
            }
        }

    /**
     * Add this PV to the model for inclusion in the plot.
     * 
     * @param newItem
     *            PV to add to the plot
     */
    protected void selectPV(final PVItem newItem) {

        // No or unknown PV name?
        if (newItem == null) {
            return;
            }

        // Delete all existing traces
        if (plot == null) {
            return;
        }

        XYGraph xygraph = plot.getXYGraph();

        Model newModel;
        if (model == null) {
            newModel = new Model();
        } else {
            newModel = model;
        }
        updateModel(newModel, newModel);
        try {
            newModel.addItem(newItem);
            newItem.useDefaultArchiveDataSources();
            newModel.setTimerange(getStartSpec(), getEndSpec());
        } catch (Exception ex) {
            MessageDialog.openError(getSite().getShell(), Messages.Error, NLS.bind(Messages.ErrorFmt, ex.getMessage()));
        }

        // Create trace for waveform
        final Trace trace = new Trace(newItem.getDisplayName(), xygraph.primaryXAxis, xygraph.primaryYAxis,
                new BeamStatusGraphDataProvider());
        trace.setLineWidth(newItem.getLineWidth());
        trace.setPointStyle(PointStyle.POINT);
        trace.setPointSize(1);
        // Add to graph
        xygraph.addTrace(trace);
        numTraces++;
        }

    private void setTimeRangeDaily() {
        currentPlotTimespanMilliseconds = MILLISECONDS_IN_DAY;
        updateModelTimeRange();
    }

    private void setTimeRangeHourly() {
        currentPlotTimespanMilliseconds = MILLISECONDS_IN_HOUR;
        updateModelTimeRange();
    }

    private void updateModelTimeRange() {
        if (model == null) {
            System.out.println("Unable to set model time range. Model is null");
        } else {
            try {
                model.setTimerange(getStartSpec(), getEndSpec());
            } catch (Exception ex) {
                MessageDialog.openError(getSite().getShell(), Messages.Error,
                        NLS.bind(Messages.ErrorFmt, ex.getMessage()));
            }
        }
    }

    /**
     * Get the title for the plot.
     * 
     * @return Plot title
     */
    private long getTimeRangeInMilliseconds() {
        return currentPlotTimespanMilliseconds;
    }

    /**
     * Used by the specific implementation to define the time range in
     * milliseconds for the plot.
     * 
     * @return time range of the plot in milliseconds
     */
    private String getPlotTitle() {
        return PLOT_TITLE;
    }

    /**
     * Gets a calendar formatted time specification defined as the number of
     * milliseconds from 0 internal base time.
     * 
     * @param milliseconds
     *            The number of milliseconds after 0 base time for the time
     *            specification
     * 
     * @return calendar formatted start time specification for the plot
     */
    private String getCalendarSpec(Long milliseconds) {
        // Use absolute start/end time
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        return AbsoluteTimeParser.format(cal);
    }

    /**
     * Gets the start time specification for the plot. The start time is set to
     * 0 calendar time so that the duration can be easily defined via the
     * endspec
     * 
     * @return calendar formatted start time specification for the plot
     */
    private String getStartSpec() {
        return getCalendarSpec(0L);
    }

    /**
     * Gets the end time specification for the plot. This is used to define a
     * time range for the plot relative to 0 calendar time
     * 
     * @return calendar formatted end time specification for the plot
     */
    private String getEndSpec() {
        return getCalendarSpec(getTimeRangeInMilliseconds());
    }

    // Following methods are defined as they are mandatory to fulfil
    // ModelListener interface, but they are not used at all to update
    // this sample view.
    @Override
    public void itemAdded(ModelItem item) {
    }

    @Override
    public void itemRemoved(ModelItem item) {
    }

    @Override
    public void changedItemLook(ModelItem item) {
    }

    @Override
    public void changedColors() {
    }

    @Override
    public void changedUpdatePeriod() {
    }

    @Override
    public void changedArchiveRescale() {
    }

    @Override
    public void changedTimerange() {
    }

    @Override
    public void changedAxis(AxisConfig axis) {
    }

    @Override
    public void changedItemVisibility(ModelItem item) {
    }

    @Override
    public void changedItemDataConfig(PVItem item) {
    }

    @Override
    public void scrollEnabled(boolean scrollEnabled) {
    }

    @Override
    public void changedAnnotations() {
    }

    @Override
    public void changedXYGraphConfig() {
    }

    @Override
    public void itemRefreshRequested(PVItem item) {
    }

    @Override
    public void cursorDataChanged() {
    }
    }
