package uk.ac.stfc.isis.ibex.epics.pv;

public interface PV<T> extends Closable {
	PVInfo<T> details();
}
