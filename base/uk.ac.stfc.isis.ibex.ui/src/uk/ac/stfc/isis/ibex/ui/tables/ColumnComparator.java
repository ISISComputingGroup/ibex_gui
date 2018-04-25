package uk.ac.stfc.isis.ibex.ui.tables;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

/**
 * Compares columns in a table to be sorted.
 * 
 * @param <T> The type of object stored in the table.
 */
public abstract class ColumnComparator<T> extends ViewerComparator {
	protected int propertyIndex;
    protected int direction = DESCENDING;
    protected static final int DESCENDING = 1;

    /**
     * The constructor for the comparator.
     */
    public ColumnComparator() {
        this.propertyIndex = 0;
        direction = DESCENDING;
    }

    /**
     * Get the direction that the column is ordered in.
     * @return The direction.
     */
    public int getDirection() {
        return direction == 1 ? SWT.DOWN : SWT.UP;
    }

    /**
     * Set the column to compare by.
     * @param column The column to compare by.
     */
    public void setColumn(int column) {
        if (column == this.propertyIndex) {
            // Same column as last sort; toggle the direction
            direction = 1 - direction;
        } else {
            // New column; do an ascending sort
            this.propertyIndex = column;
            direction = DESCENDING;
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public int compare(Viewer viewer, Object e1, Object e2) {    	
    	int rc = compare((T) e1, (T) e2);
    	
        // If descending order, flip the direction
        if (direction == DESCENDING) {
            rc = -rc;
        }
        return rc;
    }
    
    /**
     * A helper method that compares two booleans.
     * @param b1 The first boolean.
     * @param b2 The second boolean.
     * @return Same as {@link #compare(Viewer, Object, Object) compare} function.
     */
    protected int compareBoolean(boolean b1, boolean b2) {
        if (b1 == b2) {
            return 0;
        } else {
            return (b2 ? 1 : -1);
        }
    }
    
    /**
     * Compares two specific objects of type T.
     * @param e1 The first object.
     * @param e2 The second object.
     * @return Same as {@link #compare(Viewer, Object, Object) compare} function.
     */
    public abstract int compare(T e1, T e2);
}
