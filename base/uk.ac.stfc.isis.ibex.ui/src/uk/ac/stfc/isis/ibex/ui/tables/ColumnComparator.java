package uk.ac.stfc.isis.ibex.ui.tables;


import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

/**
 * Compares columns in a table to be sorted.
 * 
 * This class sorts based on the string representation of the cell. To sort based on something else, subclass this and override the method compare.
 * 
 * @param <T> The type of object stored in the table.
 */
public class ColumnComparator<T> extends ViewerComparator {
    /** index of column that was last sorted by. */
    protected int propertyIndex;
    
    /** direction to perform the sort. */
    protected boolean isDescending = false;

    /**
     * The constructor for the comparator.
     */
    public ColumnComparator() {
	this.propertyIndex = 0;
    }

    /**
     * Get the direction that the column is ordered in.
     * @return The direction.
     */
    public int getDirection() {
	return isDescending ? SWT.DOWN : SWT.UP;
    }

    /**
     * Set the column to compare by.
     * @param column The column to compare by.
     */
    public void setColumn(int column) {
	if (column == this.propertyIndex) {
	    // Same column as last sort; toggle the direction
	    isDescending = !isDescending;
	} else {
	    // New column; do an descending sort
	    this.propertyIndex = column;
	    isDescending = true;
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {    	
	int rc = 0;

	TableViewer view = (TableViewer) viewer;
	SortableObservableMapCellLabelProvider<T> provider = (SortableObservableMapCellLabelProvider<T>) view.getLabelProvider(this.propertyIndex);

	rc = provider.comparableForRow((T) e1).compareTo((T) e2);

	// If descending order, flip the direction
	if (isDescending) {
	    rc = -rc;
	}
	return rc;
    }
}
