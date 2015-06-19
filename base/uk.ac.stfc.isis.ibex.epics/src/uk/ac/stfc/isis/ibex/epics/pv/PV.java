package uk.ac.stfc.isis.ibex.epics.pv;

/**
 * An interface for classes that represent PVs.
 *
 * @param <T> the PV type
 */
public interface PV<T> extends Closable {
	PVInfo<T> details();
}
