
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
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.beamstatus.BeamStatusGraphDataProvider;

/**
 * Provides access to the data browser to show data from TS1/TS2 beam currents
 * as a plot within a view
 * 
 * Always used as derived classes that specify the time duration of the plot
 * (e.g. hourly, daily)
 * 
 * @author Adrian Potter
 */
public abstract class BeamStatusGraphView extends DataBrowserAwareView implements ModelListener {

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
                    model.removeListener(BeamStatusGraphView.this);
                }
            }
        });

        final GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);

        // =====================
        // ======= Plot ========
        // =====================

        // The canvas has to be wrapped in a composite so that the canvas has
        // (0,0) coordinate.
        // This is a work around for the inconsistency between
        // figure.getBounds() and gc.getclipping().
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, layout.numColumns, 1));
        composite.setLayout(new FillLayout());

        // Double_BUFFERED is required to force RAP to use NativeGraphicsSource.
        // By default, it uses BufferedGraphicsSource which has problem to
        // render it in web browser.
        final Canvas canvas = new Canvas(composite, SWT.DOUBLE_BUFFERED);

        // Create plot with basic configuration

        plot = Plot.forCanvas(canvas);
        plot.getXYGraph().setTitle(getPlotTitle());

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
     * Sets the color for the given axis.
     * 
     * @param axis
     *            The axis configuration whose properties will be updated.
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
            switch (pvAddress) {
                case TS1_BEAM_CURRENT_PV:
                    displayName = "TS1";
                    break;
                case TS2_BEAM_CURRENT_PV:
                    displayName = "TS2";
                    break;
                case SYNCH_BEAM_CURRENT_PV:
                    displayName = "Synchotron";
                    break;
                default:
                    displayName = newItem.getDisplayName();
            }
            newItem.setDisplayName(displayName);
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
        }

    /**
     * Get the title for the plot.
     * 
     * @return Plot title
     */
    protected abstract String getPlotTitle();

    /**
     * Used by the specific implementation to define the time range in
     * milliseconds for the plot.
     * 
     * @return time range of the plot in milliseconds
     */
    protected abstract long getTimeRangeInMilliseconds();

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
