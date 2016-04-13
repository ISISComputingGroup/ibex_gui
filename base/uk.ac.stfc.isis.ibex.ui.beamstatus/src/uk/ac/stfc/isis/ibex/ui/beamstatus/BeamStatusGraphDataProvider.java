
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

package uk.ac.stfc.isis.ibex.ui.beamstatus;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.csstudio.swt.xygraph.dataprovider.IDataProvider;
import org.csstudio.swt.xygraph.dataprovider.IDataProviderListener;
import org.csstudio.swt.xygraph.dataprovider.ISample;
import org.csstudio.swt.xygraph.dataprovider.Sample;
import org.csstudio.swt.xygraph.linearscale.Range;
import org.csstudio.trends.databrowser2.model.PlotSample;

public class BeamStatusGraphDataProvider implements IDataProvider {

    final private ArrayList<IDataProviderListener> listeners = new ArrayList<IDataProviderListener>();

    private ArrayList<Point2D.Double> dataPoints;

    @Override
    public int getSize() {
        return dataPoints == null ? 0 : dataPoints.size();
    }

    @Override
    public boolean isChronological() { // x range is [0..waveform size]
        return true;
    }

    /**
     * @param value
     */
    public void addPlotSample(PlotSample sample) {
        if (dataPoints == null)
            dataPoints = new ArrayList<Point2D.Double>();
        dataPoints.add(new Point2D.Double(sample.getXValue(), sample.getYValue()));
        for (IDataProviderListener listener : listeners)
            listener.dataChanged(this);
    }

    /**
     * @param index
     * @return
     */
    @Override
    public ISample getSample(int index) {
        if (dataPoints == null || index < 0 || index >= dataPoints.size())
            return null;
        final Point2D.Double dataPoint = dataPoints.get(index);
        return new Sample(dataPoint.x, dataPoint.y);
    }

    /**
     * @return
     */
    @Override
    public Range getXDataMinMax() {
        if (dataPoints == null || dataPoints.size() == 0)
            return null;
        double min = dataPoints.get(0).x;
        double max = dataPoints.get(0).x;
        for (Point2D.Double point : dataPoints) {
            min = Math.min(min, point.x);
            max = Math.max(max, point.x);
        }
        return new Range(min, max);
    }

    /**
     * @return
     */
    @Override
    public Range getYDataMinMax() {
        if (dataPoints == null || dataPoints.size() == 0)
            return null;
        double min = dataPoints.get(0).y;
        double max = dataPoints.get(0).y;
        for (Point2D.Double point : dataPoints) {
            min = Math.min(min, point.y);
            max = Math.max(max, point.y);
        }
        return new Range(min, max);
    }

    /**
     * @param listener
     */
    @Override
    public void addDataProviderListener(IDataProviderListener listener) {
        listeners.add(listener);
    }

    /**
     * @param listener
     * @return
     */
    @Override
    public boolean removeDataProviderListener(IDataProviderListener listener) {
        return listeners.remove(listener);
    }
}
