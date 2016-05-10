
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

package uk.ac.stfc.isis.ibex.ui.dae.spectra;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.dae.spectra.Spectrum;

@SuppressWarnings("checkstyle:magicnumber")
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
