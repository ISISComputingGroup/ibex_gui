
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

import org.apache.logging.log4j.Logger;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider.UpdateMode;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.dae.spectra.Spectrum;
import uk.ac.stfc.isis.ibex.dae.spectra.SpectrumYAxisTypes;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.ui.Utils;

/**
 * A single spectrum plot UI component.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SpectrumPlot extends Canvas {

    /** Logger. */
    private static final Logger LOG = IsisLog.getLogger(SpectrumPlot.class);

    /**
     * Buffer size for spectrum data, should be the same size as
     * IN:INSTRUMENT:DAE:SPEC:1:2:Y.
     */
    private static final int DAE_SPECTRUM_BUFFER_SIZE = 8000;

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

	private final PropertyChangeListener typeSelectionIndexListener =  new PropertyChangeListener() {	
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			updateYAxisTitle();
		}
	};
	
    /**
     * Instantiates a new spectrum plot.
     *
     * @param parent the parent
     * @param style the style
     */
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
        traceDataProvider.setBufferSize(DAE_SPECTRUM_BUFFER_SIZE);
		traceDataProvider.setUpdateDelay(1000);
        traceDataProvider.setConcatenate_data(false);
        traceDataProvider.setUpdateMode(UpdateMode.X_OR_Y);

		trace = new Trace("Spectrum", plot.getPrimaryXAxis(), plot.getPrimaryYAxis(), traceDataProvider);
		trace.setAntiAliasing(true);
		
        plot.getPrimaryXAxis().setTitle("Time-of-flight (" + Utils.MU + "s)");
		plot.getPrimaryXAxis().setDashGridLine(true);
		plot.getPrimaryXAxis().setAutoScale(true);
        plot.getPrimaryXAxis().setAutoScaleThreshold(0);
        plot.getPrimaryXAxis().setFormatPattern("0");

        plot.getPrimaryYAxis().setTitle("");
		plot.getPrimaryYAxis().setDashGridLine(true);
		plot.getPrimaryYAxis().setAutoScale(true);
        plot.getPrimaryYAxis().setAutoScaleThreshold(0);
        plot.getPrimaryYAxis().setFormatPattern("0");
        
		plot.addTrace(trace);
	}

    /**
     * Sets the model for the plot.
     *
     * @param newSpectrum the new spectrum model
     */
	public void setModel(final Spectrum newSpectrum) {
		if (spectrum != null) {
			spectrum.removePropertyChangeListener(xDataListener);
			spectrum.removePropertyChangeListener(yDataListener);
			spectrum.removePropertyChangeListener(typeSelectionIndexListener);
		}
		
		spectrum = newSpectrum;
		spectrum.addPropertyChangeListener("xData", xDataListener);
		spectrum.addPropertyChangeListener("yData", yDataListener);
		spectrum.addPropertyChangeListener("typeSelectionIndex", typeSelectionIndexListener);

		if (spectrum.xData().length > DAE_SPECTRUM_BUFFER_SIZE || spectrum.yData().length > DAE_SPECTRUM_BUFFER_SIZE) {
            LOG.warn("DAE graph is clipped because DAE_SPECTRUM_BUFFER_SIZE is not large enough.");
		}
		
		traceDataProvider.clearTrace();
		
		updateYAxisTitle();
        
		updateData();
	}
	
	private void updateYAxisTitle() {
		plot.getPrimaryYAxis().setTitle(
				SpectrumYAxisTypes.values()[spectrum.getTypeSelectionIndex()].toString());
	}

    /**
     * Update both the x and y data.
     */
    private void updateData() {
        setXData();
		setYData();
    }

	private void setXData() {
		DISPLAY.asyncExec(new Runnable() {	
			@Override
			public void run() {
				traceDataProvider.setCurrentXDataArray(spectrum.xData());
			}
		});
	}
	
	private void setYData() {
		DISPLAY.asyncExec(new Runnable() {	
			@Override
			public void run() {
				traceDataProvider.setCurrentYDataArray(spectrum.yData());
			}
		});
	}
}
