package uk.ac.stfc.isis.ibex.ui.dae.spectra;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

import uk.ac.stfc.isis.ibex.dae.spectra.Spectrum;

public class SpectrumPlot extends Canvas {

	private XYGraph plot;
	private Trace trace;
	private CircularBufferDataProvider traceDataProvider;	
	private Spectrum spectrum;
	
	private static final Display DISPLAY = Display.getCurrent();
	
	private final PropertyChangeListener xDataListener =  new PropertyChangeListener() {	
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			setXData();
		}
	};

	private final PropertyChangeListener yDataListener =  new PropertyChangeListener() {	
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			setYData();
		}
	};
	
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:localvariablename"})
	public SpectrumPlot(Composite parent, int style) {
		super(parent, SWT.BORDER);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		plot = new XYGraph();
		plot.setTitle("Spectrum");
		plot.setShowLegend(false);
		plot.setShowTitle(false);
		
		final LightweightSystem lws = new LightweightSystem(this);
		lws.setContents(plot);
		
		traceDataProvider = new CircularBufferDataProvider(false);
		traceDataProvider.setBufferSize(5000);
		traceDataProvider.setUpdateDelay(1000);
		
		trace = new Trace("Spectrum", plot.primaryXAxis, plot.primaryYAxis, traceDataProvider);
		trace.setAntiAliasing(true);
		
		plot.primaryXAxis.setTitle("Time");
		plot.primaryXAxis.setDashGridLine(true);
		plot.primaryXAxis.setAutoScale(true);
		plot.primaryXAxis.setAutoScaleThreshold(0);
				
		plot.primaryYAxis.setTitle("Amplitude");
		plot.primaryYAxis.setDashGridLine(true);
		plot.primaryYAxis.setAutoScale(true);
		plot.primaryYAxis.setAutoScaleThreshold(0);

		plot.addTrace(trace);
	}

	public void setModel(final Spectrum newSpectrum) {
		if (spectrum != null) {
			spectrum.removePropertyChangeListener(xDataListener);
			spectrum.removePropertyChangeListener(yDataListener);
		}
		
		spectrum = newSpectrum;

		spectrum.addPropertyChangeListener("xData", xDataListener);
		spectrum.addPropertyChangeListener("yData", yDataListener);
		
		traceDataProvider.clearTrace();
		setXData();
		setYData();
	}

	private void setXData() {
		DISPLAY.asyncExec(new Runnable() {	
			@Override
			public void run() {
				traceDataProvider.clearTrace();
				traceDataProvider.setCurrentXDataArray(spectrum.xData());
			}
		});
	}
	
	private void setYData() {
		DISPLAY.asyncExec(new Runnable() {	
			@Override
			public void run() {
				traceDataProvider.clearTrace();
				traceDataProvider.setCurrentYDataArray(spectrum.yData());
			}
		});
	}
}
