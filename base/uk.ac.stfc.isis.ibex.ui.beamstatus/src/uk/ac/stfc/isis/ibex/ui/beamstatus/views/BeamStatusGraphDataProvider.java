package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import java.util.ArrayList;

import org.csstudio.trends.databrowser2.model.PlotSample;
import org.eclipse.nebula.visualization.xygraph.dataprovider.IDataProvider;
import org.eclipse.nebula.visualization.xygraph.dataprovider.IDataProviderListener;
import org.eclipse.nebula.visualization.xygraph.dataprovider.ISample;
import org.eclipse.nebula.visualization.xygraph.dataprovider.Sample;
import org.eclipse.nebula.visualization.xygraph.linearscale.Range;

public class BeamStatusGraphDataProvider implements IDataProvider {
    final private ArrayList<IDataProviderListener> listeners = new ArrayList<IDataProviderListener>();

    private ArrayList<PlotSample> samples;

    @Override
    public int getSize() {
        return samples == null ? 0 : samples.size();
    }

    @Override
    public ISample getSample(final int index) {
        if ( samples==null || index<0 || index >= samples.size() )
            return null;
        final PlotSample sample = samples.get(index);
        return new Sample(sample.getXValue(),sample.getYValue());
    }

    @Override
    public Range getXDataMinMax() {
        if (samples == null)
            return null;
        double min = 0.0;
        double max = 0.0;
        for ( PlotSample sample : samples)
        {
            if (sample.getXValue() < min)
                min = sample.getXValue();
            if (sample.getXValue() > max)
                max = sample.getXValue();
        }
        return new Range(min, max);
    }

    @Override
    public Range getYDataMinMax() {
        if (samples == null)
            return null;
        double min = 0.0;
        double max = 0.0;
        for (PlotSample sample : samples) {
            if (sample.getXValue() < min)
                min = sample.getXValue();
            if (sample.getXValue() > max)
                max = sample.getXValue();
        }
        return new Range(min, max);
    }

    @Override
    public boolean isChronological() { // x range is [0..waveform size]
        return true;
    }

    @Override
    public void addDataProviderListener(final IDataProviderListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean removeDataProviderListener(final IDataProviderListener listener) {
        return listeners.remove(listener);
    }

    /**
     * @param value
     */
    public void addPlotSample(PlotSample sample) {
        if (samples == null)
            samples = new ArrayList<PlotSample>();
        samples.add(sample);
        for (IDataProviderListener listener : listeners)
            listener.dataChanged(this);
    }
}
