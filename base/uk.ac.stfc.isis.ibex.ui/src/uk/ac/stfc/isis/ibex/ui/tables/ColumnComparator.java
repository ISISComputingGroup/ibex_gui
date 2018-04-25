package uk.ac.stfc.isis.ibex.ui.tables;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

/**
 * Compares columns in a table to be sorted.
 */
public abstract class ColumnComparator extends ViewerComparator {
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

    @Override
    public abstract int compare(Viewer viewer, Object e1, Object e2);
}
