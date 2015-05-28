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
		public void onConnectionChanged(boolean isConnected) {			
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
		xSubscription = observables.spectrumXData(getNumber(), getPeriod()).subscribe(xDataObserver);
		ySubscription = observables.spectrumYData(getNumber(), getPeriod()).subscribe(yDataObserver);
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
