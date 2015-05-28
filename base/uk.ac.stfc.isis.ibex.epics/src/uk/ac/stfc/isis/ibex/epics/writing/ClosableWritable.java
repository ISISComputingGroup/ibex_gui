package uk.ac.stfc.isis.ibex.epics.writing;

import uk.ac.stfc.isis.ibex.epics.pv.Closable;

public interface ClosableWritable<T> extends Writable<T>, Closable {

}
