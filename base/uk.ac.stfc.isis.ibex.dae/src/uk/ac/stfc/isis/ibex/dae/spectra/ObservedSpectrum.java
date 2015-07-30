
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

package uk.ac.stfc.isis.ibex.dae.spectra;

import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;

public class ObservedSpectrum extends UpdatableSpectrum implements Closable {
		
	private final DaeObservables observables;
	
	private abstract class DataObserver extends BaseObserver<float[]> {
		@Override
		public void onError(Exception e) {			
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {			
		}
		
		protected double[] toDoubleArray(float[] value) {
			double[] doubles = new double[value.length];
			for (int i = 0; i < value.length; i++) {
				doubles[i] = value[i];
			}
			
			return doubles;
		}
	}
	
	private final DataObserver xDataObserver = new DataObserver() {
		@Override
		public void onValue(float[] value) {
			setXData(toDoubleArray(value));
		}
	};

	private final DataObserver yDataObserver = new DataObserver() {
		@Override
		public void onValue(float[] value) {
			setYData(toDoubleArray(value));
		}
	};
	
	private Subscription xSubscription;
	private Subscription ySubscription;
	
	public ObservedSpectrum(DaeObservables observables) {
		this.observables = observables;
	}
	
	@Override
	public void setNumber(int value) {
		super.setNumber(value);
	}

	@Override
	public void setPeriod(int value) {
		super.setPeriod(value);
	}
	
	@Override
	public void update() {
		super.update();
		updateSubscriptions();
	}
	
	private void updateSubscriptions() {
		cancelSubscriptions();
		xSubscription = observables.spectrumXData(getNumber(), getPeriod()).addObserver(xDataObserver);
		ySubscription = observables.spectrumYData(getNumber(), getPeriod()).addObserver(yDataObserver);
	}

	@Override
	public void close() {
		cancelSubscriptions();
	}

	private void cancelSubscriptions() {
		if (xSubscription != null) {
			xSubscription.cancel();
		}
		
		if (ySubscription != null) {
			ySubscription.cancel();
		}
	}
}
