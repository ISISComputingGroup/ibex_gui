
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

import java.util.ArrayList;

import org.csstudio.swt.xygraph.dataprovider.IDataProvider;
import org.csstudio.swt.xygraph.dataprovider.IDataProviderListener;
import org.csstudio.swt.xygraph.dataprovider.ISample;
import org.csstudio.swt.xygraph.linearscale.Range;

/**
 * Data provider for the XYGraph that shows the beam current from TS1 and TS2.
 * Most functionality is implemented to fit the interface. Main function is to
 * provide listeners for data changes in the model
 * 
 * @author Adrian Potter
 */
public class BeamStatusGraphDataProvider implements IDataProvider {

    /** List of listeners associated with the data. */
    private final ArrayList<IDataProviderListener> listeners = new ArrayList<IDataProviderListener>();

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public boolean isChronological() {
        return true;
    }

    @Override
    public ISample getSample(int index) {
        return null;
    }

    @Override
    public Range getXDataMinMax() {
        return null;
    }

    @Override
    public Range getYDataMinMax() {
        return null;
    }

    @Override
    public void addDataProviderListener(IDataProviderListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean removeDataProviderListener(IDataProviderListener listener) {
        return listeners.remove(listener);
    }
}
