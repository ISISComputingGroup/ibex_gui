
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.diirt.datasource.ExpressionLanguage.latestValueOf;

import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.vtype.VType;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import uk.ac.stfc.isis.ibex.epics.pv.ObservablePV;
import uk.ac.stfc.isis.ibex.epics.pv.PVInfo;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * A class for observing a PV via PVManager.
 *
 * @param <R> the PV type (must be a VType)
 */
public class PVManagerObservable<R extends VType> extends ObservablePV<R> {
	
	static {
		PVManagerSettings.setUp();
	}

    private static final int UPDATE_FREQUENCY = 10;
    
    private PVReader<R> pv;	
	
	/**
	 * An instance of an inner anonymous class for handling PV changes.
	 * 
	 * Create this in the constructor to ensure it is being run in the current thread,
	 * otherwise PV manager has issues.
	 */
	private PVReaderListener<R> observingListener;
	
	private static final ExecutorService UPDATE_THREADPOOL = Executors.newCachedThreadPool(
			new ThreadFactoryBuilder().setNameFormat("PVManagerObservable-threadpool-%d").build());
	
    /**
     * Create a new PV manager observable.
     * @param info the parameters to create this PV with
     */
	public PVManagerObservable(final PVInfo<R> info) {
		super(info);
		
		Future<?> result = UPDATE_THREADPOOL.submit(new Runnable() {
			/**
			 * IMPORTANT: PVManager requires creating the listener and registering it
			 * to be done in the same thread, and in the same runnable, to avoid missing
			 * values.
			 */
			@Override
			public void run() {
				observingListener = new PVReaderListener<R>() {
					@Override
					public synchronized void pvChanged(PVReaderEvent<R> evt) {
						boolean isConnected = pv.isConnected();
						try {
							if (evt.isConnectionChanged()) {
				                setConnectionStatus(isConnected);
							}
							
							if (evt.isExceptionChanged()) {
								setError(pv.lastException());
							}
							
							if (evt.isValueChanged() && isConnected) {
								setValue(pv.getValue());
				            }
						} catch (RuntimeException e) {
							// Ensure errors get logged not swallowed silently (if they propagate up to this level)
							LoggerUtils.logErrorWithStackTrace(
									IsisLog.getLogger(PVManagerObservable.this.getClass()), e.getMessage(), e);
						}
					}
			    };
			    
			    pv = PVManager
						.read(latestValueOf(channel(info.address(), info.type(), Object.class)))
						.readListener(observingListener)
						.notifyOn(UPDATE_THREADPOOL)
		                .maxRate(ofHertz(UPDATE_FREQUENCY));
			}
		});
		
		try {
			// Wait for above to be finished.
			result.get();
		} catch (ExecutionException | InterruptedException e) {
			IsisLog.getLogger(getClass()).error(e.getMessage(), e);
		}
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
        pv.close();
        super.close();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "PVManagerObservable observing PV " + pv.getName() + " with current value: " + pv.getValue();
	}
}
