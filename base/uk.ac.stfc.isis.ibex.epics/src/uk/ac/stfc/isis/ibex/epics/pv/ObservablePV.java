package uk.ac.stfc.isis.ibex.epics.pv;

import org.epics.vtype.VType;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;


/**
 * A class for observing an EPICS process variable.
 *
 * @param <T> the PV type (must be a VType)
 */
public abstract class ObservablePV<T extends VType> extends BaseCachingObservable<T> implements PV<T>, ClosableCachingObservable<T> {

	private final PVInfo<T> info;

	public ObservablePV(PVInfo<T> info) {
		this.info = info;
	}
	
	@Override
	public PVInfo<T> details() {
		return info;
	}
}
