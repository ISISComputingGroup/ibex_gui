
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

package uk.ac.stfc.isis.ibex.epics.pvmanager;

import static org.diirt.datasource.ExpressionLanguage.channel;
import static org.diirt.util.time.TimeDuration.ofHertz;

import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.vtype.VType;

import uk.ac.stfc.isis.ibex.epics.pv.ObservablePV;
import uk.ac.stfc.isis.ibex.epics.pv.PVInfo;

/**
 * A class for observing a PV via PVManager.
 *
 * @param <R> the PV type (must be a VType)
 */
public class PVManagerObservable<R extends VType> extends ObservablePV<R> {

    private static final int UPDATE_FREQUENCY = 10;

    private PVReader<R> pv;	
	
	/**
	 * An instance of an inner anonymous class for handling PV changes.
	 */
	private final PVReaderListener<R> observingListener = new PVReaderListener<R>() {
		@Override
		public void pvChanged(PVReaderEvent<R> evt) {
			boolean isConnected = pv.isConnected();
			if (evt.isConnectionChanged()) {
                setConnectionStatus(isConnected);
			}
			
			if (evt.isExceptionChanged()) {
				setError(pv.lastException());
			}
			
			if (evt.isValueChanged() && isConnected) {
				setValue(pv.getValue());
            }
		}
    };
	
	public PVManagerObservable(PVInfo<R> info) {
		super(info);
		
		pv = PVManager
				.read(channel(info.address(), info.type(), Object.class))
				.readListener(observingListener)
				.notifyOn(new CurrentThreadExecutor())
                .maxRate(ofHertz(UPDATE_FREQUENCY));
	}	
	
	@Override
	public void close() {
        pv.close();
        super.close();
	}

}
