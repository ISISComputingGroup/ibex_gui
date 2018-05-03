package uk.ac.stfc.isis.ibex.ui.tables;


import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;

/**
 * A comparator that does not sort columns in a databound table.
 * 
 * @param <T> The type of object stored in the table.
 */
public class NullComparator<T> extends ColumnComparator<T> {    
    /**
     * Get the direction that the column is ordered in, always none.
     * @return The direction.
     */
    public int getDirection() {
        return SWT.NONE;
    }

	@Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        return 0;
    }
}
