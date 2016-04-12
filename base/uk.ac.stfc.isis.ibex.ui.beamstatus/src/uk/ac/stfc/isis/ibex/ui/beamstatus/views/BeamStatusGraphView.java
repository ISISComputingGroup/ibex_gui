package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

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
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.visualization.xygraph.figures.ToolbarArmedXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraphFlags;
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

public class BeamStatusGraphView extends DataBrowserAwareView implements ModelListener {
    /** View ID registered in plugin.xml */
    final public static String ID = "uk.ac.stfc.isis.ibex.ui.beamstatus.views.BeamStatusGraphView"; //$NON-NLS-1$

    /** XY Graph */
    private XYGraph xygraph;

    /** Model of the currently active Data Browser plot or <code>null</code> */
    private Model model;

    /** Selected model item in model, or <code>null</code> */
    private ModelItem model_item = null;

    /** Color for trace of model_item's current sample */
    private Color color = null;

    /** Waveform for the currently selected sample */
    private BeamStatusGraphDataProvider samples = null;

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
        final LightweightSystem lws = new LightweightSystem(canvas);
        final ToolbarArmedXYGraph plot = new ToolbarArmedXYGraph(new XYGraph(), XYGraphFlags.COMBINED_ZOOM);
        xygraph = plot.getXYGraph();
        // Configure axes
        xygraph.primaryXAxis.setTitle("Time");
        xygraph.primaryYAxis.setTitle("Current");
        lws.setContents(plot);
        selectPV("IN:DEMO:CS:SB:NEW_BLOCK_6");
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
                old_model.removeListener(this);

            if (model != null)
                model.addListener(this);
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
            item.useDefaultArchiveDataSources();
            selectPV(item);
        } catch (Exception ex) {
            MessageDialog.openError(getSite().getShell(), Messages.Error,
                    NLS.bind(Messages.ErrorFmt, ex.getMessage()));
        }
    }

    /** Select given PV item (or <code>null</code>). */
    private void selectPV(final ModelItem new_item) {
        if (new_item == null)
            model_item = null;
        else
            model_item = new_item;

        // Delete all existing traces
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
                new_model.start();
                new_model.addItem(new_item);
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
        for (int i = 0; i < plotSamples.getSize(); i++)
        {
            PlotSample sample = plotSamples.getSample(i);
            samples.addPlotSample(sample);
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
