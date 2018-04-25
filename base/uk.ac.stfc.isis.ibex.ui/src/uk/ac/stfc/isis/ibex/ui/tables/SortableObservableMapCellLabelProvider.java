package uk.ac.stfc.isis.ibex.ui.tables;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;

/**
 * A cell provider that can be sorted.
 * @param <TRow> The type of the row that this cell provider gives sorting data for.
 */
public abstract class SortableObservableMapCellLabelProvider<TRow> extends ObservableMapCellLabelProvider {
	/**
	 * Constructor for the observable label provider.
	 * @param attributeMap A map of the attributes that this cell will observe.
	 */
	protected SortableObservableMapCellLabelProvider(IObservableMap attributeMap) {
		super(attributeMap);
	}
	
    /**
     * Get the text to be displayed in a cell.
     *
     * @param row the row
     * @return the string to set in the cell
     */
	protected abstract String stringFromRow(TRow row);
	
    /**
     * Get the comparable for the cell compared to others in it's column.
     * 
     * Defaults to comparing the cell's string representation.
     *
     * @param row the row
     * @return the comparator
     */
	public Comparable<TRow> comparableForRow(final TRow row) {
		return new Comparable<TRow>() {
			@Override
			public int compareTo(TRow arg0) {
				return stringFromRow(row).compareTo(stringFromRow(arg0));
			}
		};
	}
}
