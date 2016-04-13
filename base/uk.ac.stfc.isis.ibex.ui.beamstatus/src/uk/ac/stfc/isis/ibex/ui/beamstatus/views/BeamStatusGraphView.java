
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

import org.csstudio.swt.xygraph.figures.Trace;
import org.csstudio.swt.xygraph.figures.Trace.PointStyle;
import org.csstudio.swt.xygraph.figures.XYGraph;
import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.editor.DataBrowserAwareView;
import org.csstudio.trends.databrowser2.model.AxisConfig;
import org.csstudio.trends.databrowser2.model.Model;
import org.csstudio.trends.databrowser2.model.ModelItem;
import org.csstudio.trends.databrowser2.model.ModelListener;
import org.csstudio.trends.databrowser2.model.PVItem;
import org.csstudio.trends.databrowser2.model.PlotSample;
import org.csstudio.trends.databrowser2.model.PlotSamples;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.csstudio.trends.databrowser2.ui.Controller;
import org.csstudio.trends.databrowser2.ui.Plot;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.beamstatus.BeamStatusGraphDataProvider;

abstract public class BeamStatusGraphView extends DataBrowserAwareView implements ModelListener {
    /** View ID registered in plugin.xml */
    final public static String ID = "uk.ac.stfc.isis.ibex.ui.beamstatus.views.BeamStatusGraphView"; //$NON-NLS-1$

    /** Plot */
    private Plot plot;

    /** Model of the currently active Data Browser plot or <code>null</code> */
    private Model model;

    /** Selected model item in model, or <code>null</code> */
    private ModelItem model_item = null;

    /** Color for trace of model_item's current sample */
    private Color color = null;

    /** Waveform for the currently selected sample */
    private BeamStatusGraphDataProvider samples = null;

    /** Controller that links model and plot */
    private Controller controller = null;

    /** {@inheritDoc} */
    @Override
    protected void doCreatePartControl(final Composite parent) {
        // Arrange disposal
        parent.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                if (color != null)
                    color.dispose();

                // Be ignorant of any change of the current model after
                // this view is disposed.
                if (model != null)
                    model.removeListener(BeamStatusGraphView.this);
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
        XYGraph xygraph = plot.getXYGraph();
        xygraph.primaryXAxis.setTitle("Time");
        xygraph.primaryYAxis.setTitle("Current");

        // selectPV("IN:DEMO:CS:SB:NEW_BLOCK_6");
        // selectPV("AC:SYNCH:BEAM:CURR");
        selectPV("AC:TS1:BEAM:CURR");

        // Create and start controller
        try {
            controller = new Controller(parent.getShell(), model, plot);
            controller.start();
        } catch (Exception ex) {
            MessageDialog.openError(parent.getShell(), Messages.Error,
                    NLS.bind(Messages.ControllerStartErrorFmt, ex.getMessage()));
        }
    }

    /** {@inheritDoc} */
    // Remove Override annotation for RAP
    // @Override
    @Override
    public void setFocus() {
        return;
    }

    /** {@inheritDoc} */
    @Override
    protected void updateModel(final Model old_model, final Model model) {
        this.model = model;
        if (old_model != model) {
            if (old_model != null)
            {
                old_model.removeListener(this);
            }

            if (model != null)
            {
                model.addListener(this);
            }
        }
        update(old_model != model);
    }

    /**
     * Update combo box of this view.
     * 
     * @param model_changed
     *            set true if the model was changed
     */
    private void update(final boolean model_changed) {
        if (model == null)
            return;

        // Show PV names
        final String names[] = new String[model.getItemCount() + 1];
        names[0] = "TEXT 8";
        for (int i = 1; i < names.length; ++i)
            names[i] = model.getItem(i - 1).getName();
        if (!model_changed) {
            // Is the previously selected item still valid?
            if (model.indexOf(model_item) != -1) {
                return;
            }
        }
    }

    private void selectPV(final String pvAddress) {

        final double period = Preferences.getScanPeriod();
        try {
            final PVItem item = new PVItem(pvAddress, period);
            selectPV(item);
        } catch (Exception ex) {
            MessageDialog.openError(getSite().getShell(), Messages.Error,
                    NLS.bind(Messages.ErrorFmt, ex.getMessage()));
        }
    }

    /** Select given PV item (or <code>null</code>). */
    private void selectPV(final PVItem new_item) {
        model_item = new_item;

        // Delete all existing traces
        if (plot == null)
            return;
        XYGraph xygraph = plot.getXYGraph();
        int N = xygraph.getPlotArea().getTraceList().size();
        while (N > 0)
            xygraph.removeTrace(xygraph.getPlotArea().getTraceList().get(--N));

        // No or unknown PV name?
        if (model_item == null) {
            return;
        }
        else {
            Model new_model = new Model();
            updateModel(new_model, new_model);
            try {
                new_model.addItem(new_item);
                new_item.useDefaultArchiveDataSources();
            } catch (Exception e1) {
                return;
            }
        }

        // Prepare to show waveforms of model item in plot
        addSampleData();

        // Create trace for waveform
        final Trace trace = new Trace(model_item.getDisplayName(), xygraph.primaryXAxis, xygraph.primaryYAxis, samples);
        trace.setLineWidth(model_item.getLineWidth());
        trace.setPointStyle(PointStyle.POINT);
        trace.setPointSize(5);
        // Add to graph
        xygraph.addTrace(trace);
    }

    private void addSampleData() {
        if (samples == null) {
            samples = new BeamStatusGraphDataProvider();
        }
        PlotSamples plotSamples = model_item.getSamples();
        synchronized (plotSamples) {
            for (int i = 0; i < plotSamples.getSize(); i++) {
                PlotSample sample = plotSamples.getSample(i);
                samples.addPlotSample(sample);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void itemAdded(ModelItem item) {
        // Be aware of the addition of a new item to update combo box.
        update(false);
    }

    /** {@inheritDoc} */
    @Override
    public void itemRemoved(ModelItem item) {
        // Be aware of the addition of a new item to update combo box.
        update(false);
    }

    /** {@inheritDoc} */
    @Override
    public void changedItemLook(ModelItem item) {
        // Be aware of the change of the item name.
        update(false);
    }

    /** {@inheritDoc} */
    @Override
    public void changedColors() {
        // Be aware of the change of the item color.
        // TODO: this update does not trigger color change. Fix it.
        update(false);
    }

    // Following methods are defined as they are mandatory to fulfill
    // ModelListener interface, but they are not used at all to update
    // this sample view.
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
    public void scrollEnabled(boolean scroll_enabled) {
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
