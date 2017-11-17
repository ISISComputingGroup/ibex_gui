
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.BufferedObservablePair;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;

public class ObservedSpectrum extends UpdatableSpectrum implements Closable {
		
	private final DaeObservables observables;
    private BufferedObservablePair<Integer, float[]> xData;
    private BufferedObservablePair<Integer, float[]> yData;
	
    private abstract class DataObserver extends BaseObserver<Pair<Integer, float[]>> {
        protected double[] toDoubleArray(float[] value, int length) {
            double[] doubles = new double[length];
            for (int i = 0; i < length; i++) {
                doubles[i] = value[i];
            }
            return doubles;
        }
	}
	
	private final DataObserver xDataObserver = new DataObserver() {
		@Override
        public void onValue(Pair<Integer, float[]> value) {
            setXData(toDoubleArray(value.second, value.first));
        }
	};

	private final DataObserver yDataObserver = new DataObserver() {
		@Override
        public void onValue(Pair<Integer, float[]> value) {
            setYData(toDoubleArray(value.second, value.first));
        }
	};

	private Subscription xSubscription;
	private Subscription ySubscription;

    private ForwardingObservable<Integer> xLengthObservable;
    private ForwardingObservable<Integer> yLengthObservable;

    private ForwardingObservable<float[]> xValObservable;
    private ForwardingObservable<float[]> yValObservable;

    public ObservedSpectrum(DaeObservables observables) {
		this.observables = observables;
	}
	
	@Override
	public void update() {
		updateSubscriptions();
        super.update();
	}
	
	private void updateSubscriptions() {

        close();

        xLengthObservable = observables.spectrumXDataLength(getNumber(), getPeriod());
        yLengthObservable = observables.spectrumYDataLength(getNumber(), getPeriod());
        
        xValObservable = observables.spectrumXData(getNumber(), getPeriod());
        yValObservable = observables.spectrumYData(getNumber(), getPeriod());

        xData = new BufferedObservablePair<>(xLengthObservable, xValObservable);
        yData = new BufferedObservablePair<>(yLengthObservable, yValObservable);

        xSubscription = xData.addObserver(xDataObserver);
        ySubscription = yData.addObserver(yDataObserver);
    }

	@Override
	public void close() {
        closeObservables();
		cancelSubscriptions();
        disconnectObservers();
	}

    private void closeObservables() {

        List<ClosableObservable<?>> oldObservables = new ArrayList<>();
        oldObservables.add(xData);
        oldObservables.add(yData);
        oldObservables.add(xLengthObservable);
        oldObservables.add(xValObservable);
        oldObservables.add(yLengthObservable);
        oldObservables.add(yValObservable);
        for (ClosableObservable<?> o : oldObservables) {
            if (o != null) {
                o.close();
            }
        }
    }

    private void cancelSubscriptions() {
        List<Subscription> oldSubscriptions = new ArrayList<>();
        oldSubscriptions.add(xSubscription);
        oldSubscriptions.add(ySubscription);
        for (Subscription s : oldSubscriptions) {
            if (s != null) {
                s.removeObserver();
            }
        }
    }

    private void disconnectObservers() {
        List<DataObserver> oldObservers = new ArrayList<>();
        oldObservers.add(xDataObserver);
        oldObservers.add(yDataObserver);
        for (DataObserver d : oldObservers) {
            if (d != null) {
                d.onConnectionStatus(false);
            }
        }
	}
}
