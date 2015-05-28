package uk.ac.stfc.isis.ibex.epics.pv;

import java.util.ArrayList;

public class ClosableList<T extends Closable> extends ArrayList<T> implements Closable {

	private static final long serialVersionUID = 8636379885861243532L;

	@Override
	public void close() {
		for (T item : this) {
			item.close();
		}
	}
}
