package uk.ac.stfc.isis.ibex.epics.pvmanager;

import static org.epics.pvmanager.ExpressionLanguage.channel;
import static org.epics.util.time.TimeDuration.ofHertz;

import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import org.epics.vtype.VType;

import uk.ac.stfc.isis.ibex.epics.pv.ObservablePV;
import uk.ac.stfc.isis.ibex.epics.pv.PVInfo;

/**
 * A class for observing a PV via PVManager.
 *
 * @param <R> the PV type (must be a VType)
 */
public class PVManagerObservable<R extends VType> extends ObservablePV<R> {

	private final PVReader<R> pv;	
	
	/**
	 * An instance of an inner anonymous class for handling PV changes.
	 */
	private final PVReaderListener<R> observingListener = new PVReaderListener<R>() {
		@Override
		public void pvChanged(PVReaderEvent<R> evt) {
			boolean isConnected = pv.isConnected();
			if (evt.isConnectionChanged()) {
				setConnectionChanged(isConnected);
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
				.maxRate(ofHertz(10));
	}	
	
	@Override
	public void close() {
		pv.close();
	}
}
