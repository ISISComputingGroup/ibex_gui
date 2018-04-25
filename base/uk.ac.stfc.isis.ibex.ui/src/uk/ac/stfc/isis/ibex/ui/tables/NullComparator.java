package uk.ac.stfc.isis.ibex.ui.tables;

import org.eclipse.swt.SWT;

/**
 * A comparator that does nothing.
 * 
 * @param <T> The type of the row we are comparing.
 */
public class NullComparator<T> extends ColumnComparator<T> {

	/**
	 * Show no feedback to user if sorting hasn't been implemented.
	 */
	@Override
    public int getDirection() {
        return SWT.NONE;
    }

	@Override
	public int compare(T e1, T e2) {
		return 0;
	}

}
