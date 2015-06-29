package uk.ac.stfc.isis.ibex.epics.pv;

import uk.ac.stfc.isis.ibex.epics.writing.BaseWritable;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableWritable;

/**
 * A class for writing to an EPICS process variable.
 *
 * @param <T> the PV type
 */
public abstract class WritablePV<T> extends BaseWritable<T> implements ClosableWritable<T>, PV<T> {

	private final PVInfo<T> info;

	public WritablePV(PVInfo<T> info) {
		this.info = info;
	}

	@Override
	public PVInfo<T> details() {
		return info;
	}
}
