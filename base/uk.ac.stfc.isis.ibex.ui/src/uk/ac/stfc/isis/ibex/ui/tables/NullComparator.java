package uk.ac.stfc.isis.ibex.ui.tables;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;

/**
 * A comparator that does nothing.
 */
public class NullComparator extends ColumnComparator {

	/**
	 * Show no feedback to user if sorting hasn't been implemented.
	 */
	@Override
    public int getDirection() {
        return SWT.NONE;
    }
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		return 0;
	}

}
