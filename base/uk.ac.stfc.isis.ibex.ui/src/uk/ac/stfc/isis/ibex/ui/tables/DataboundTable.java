
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * A table which is bound to some data in the standard IBEX style.
 * 
 * @param <TRow> the type for a row within the table
 */
public abstract class DataboundTable<TRow> extends Composite {

    private ColumnComparator<TRow> comparator = new ColumnComparator<TRow>();

    private static final int DEFAULT_FONT_HEIGHT = 10;
    private static final int MIN_TABLE_COLUMN_WIDTH = 50;
    private int tableStyle;
    /** The underlying table. */
    protected Table table;
    /** View for the data held in this table. */
    protected TableViewer viewer;
    private TableColumnLayout tableColumnLayout = new TableColumnLayout();
    private Composite tableComposite;
    private ObservableListContentProvider<TRow> contentProvider = new ObservableListContentProvider<TRow>();

    /*A listener for whenever the contents of the table are sorted.*/
    private Optional<Runnable> sortAction = Optional.empty();

    /**
     * Instantiates a new databound table.
     *
     * @param parent the parent
     * @param style the style
     * @param tableStyle the table style
     */
    public DataboundTable(Composite parent, int style, int tableStyle) {
    this(parent, style, tableStyle, false);
    }
    
    /**
     * Constructor for creating table with empty row.
     * @param parent the parent
     * @param style the style
     * @param tableStyle the table style
     * @param emptyRow to add empty row or not
     */
    public DataboundTable(Composite parent, int style, int tableStyle, boolean emptyRow) {
    super(parent, style);
    this.tableStyle = tableStyle | SWT.BORDER;

    // GridLayout is used so that the table can be excluded from a view
    // using the exclude property that is not present on other layouts
    GridLayout gridLayout = new GridLayout(1, false);
    gridLayout.horizontalSpacing = 0;
    gridLayout.verticalSpacing = 0;
    gridLayout.marginWidth = 0;
    gridLayout.marginHeight = 0;
    setLayout(gridLayout);

    tableComposite = new Composite(this, style);
    tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    tableComposite.setLayout(tableColumnLayout);
    	
    viewer = createViewer(emptyRow);
    table = viewer.getTable();
    }
    /**
     * Instantiates a new databound table with default table style.
     *
     * Default table style is: vertical scroll, with border, full selection of
     * items and selection is hidden on loss of focus
     * 
     * @param parent the parent
     * @param style the style
     * @wbp.parser.constructor
     */
    public DataboundTable(Composite parent, int style) {
	this(parent, style, SWT.FULL_SELECTION | SWT.BORDER | SWT.HIDE_SELECTION, false);
    }

    /**
     * Sets the data rows content.
     *
     * @param rows the new rows
     */
    public void setRows(Collection<TRow> rows) {
	if (!table.isDisposed()) {
	    viewer.setInput(new WritableList<TRow>(rows, null));
	}
    }

    /**
     * First selected row.
     *
     * @return the t row
     */
    public TRow firstSelectedRow() {
	List<TRow> rows = selectedRows();
	return rows.isEmpty() ? null : rows.get(0);
    }

    /**
     * Gets the selection index.
     *
     * @return the selection index
     */
    public int getSelectionIndex() {
	return table.getSelectionIndex();
    }
	
    /**
     * Sets the selection index.
     *
     * @param index the new selection index
     */
    public void setSelectionIndex(int index) {
	table.select(index);
	// Forces the viewer to refresh the selection and call events
	IStructuredSelection selected = viewer.getStructuredSelection();
	viewer.setSelection(selected);
    }

    /**
     * Sets the selected row based on a row.
     *
     * @param selected the newly selected row
     */
    public void setSelected(TRow selected) {
	viewer.setSelection(new StructuredSelection(selected));
    }
    
    /**
     * Sets the selected rows based on multiple rows.
     *
     * @param selected the newly selected rows
     * @param reveal whether or not the row is revealed
     */
    public void setSelected(List<TRow> selected, boolean reveal) {
       viewer.setSelection(new StructuredSelection(selected), reveal);
    }

    /**
     * Deselect all rows.
     */
    public void deselectAll() {
	table().deselectAll();
    }

    /**
     * Selected rows.
     *
     * @return the selected rows
     */
    @SuppressWarnings("unchecked")
    public List<TRow> selectedRows() {
	IStructuredSelection selection = (IStructuredSelection) viewer().getSelection();
	List<TRow> rows = new ArrayList<>();
	for (Iterator<IStructuredSelection> i = selection.iterator(); i.hasNext();) {
	    rows.add((TRow) i.next());
	}

	return rows;
    }

    /**
     * Refresh the viewer of the data.
     */
    public void refresh() {
	TableViewer viewer = viewer();
	if (!viewer.getTable().isDisposed()) {
	    viewer.refresh();
	    table = viewer.getTable();
	}
    }

    @Override
    public void setBackground(Color color) {
	super.setBackground(color);
	table().setBackground(color);
    }

    @Override
    public void setFont(Font font) {
	super.setFont(font);
	table().setFont(font);
    }

    /**
     * Sets whether the table should be included in layout calculations.
     * 
     * @param isExcluded true for included; false otherwise
     */
    public void setExcluded(boolean isExcluded) {
	GridData gridData = (GridData) tableComposite.getLayoutData();
	gridData.exclude = isExcluded;	
    }

    @Override
    public void addMouseListener(MouseListener listener) {
	super.addMouseListener(listener);
	table.addMouseListener(listener);
    }

    @Override
    public void removeMouseListener(MouseListener listener) {
	super.removeMouseListener(listener);
	table.addMouseListener(listener);
    }

    /**
     * Sets the an action which will be run whenever the items of the table 
     * are sorted.
     * @param runnable the new sort action of the table.
     */
    public void setSortAction(Runnable runnable) {
	sortAction = Optional.of(runnable);
    }

    /**
     * Adds the selection changed listener.
     *
     * @param listener the listener
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
	viewer().addSelectionChangedListener(listener);
    }

    /**
     * Removes the selection changed listener.
     *
     * @param listener the listener
     */
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
	viewer().removeSelectionChangedListener(listener);
    }

    @Override
    public void addFocusListener(FocusListener listener) {
	super.addFocusListener(listener);
	table().addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(FocusListener listener) {
	super.removeFocusListener(listener);
	table().removeFocusListener(listener);
    }

    @Override
    public void setEnabled(boolean enabled) {
	super.setEnabled(enabled);
	table.setEnabled(enabled);
	tableComposite.setEnabled(enabled);
    }

    /**
     * Completes the setup of the table.
     */
    public void initialise() {
	viewer.setComparator(comparator());

	addColumns();

	viewer.setContentProvider(contentProvider);	

	table = viewer.getTable();

	addColumnMinWidth(table);

	configureTable();
    }

    /**
     * Viewer for the table.
     *
     * @return the table viewer
     */
    public TableViewer viewer() { 
	return viewer;
    }

    /**
     * Underlying table.
     *
     * @return the table
     */
    public Table table() { 
	return table;
    }

    /**
     * Viewer should be created with tableComposite() as it's parent.
     * @param addEmptyRow to create table with one "permanent" empty row 
     * @return viewer
     */
    private TableViewer createViewer(boolean addEmptyRow) {
    if (addEmptyRow) {
    	viewer = new TableViewerEmptyRow(tableComposite, tableStyle);
    } else {
    	viewer = new TableViewer(tableComposite, tableStyle);
    }
	return viewer;
    }

    /**
     * Adds columns to the table. Call {@link #initialise() initialise} method
     * to complete.
     */
    protected abstract void addColumns();

    /**
     * Configure table properties after table creation but during
     * initialisation. Override to customise table appearance.
     */
    protected void configureTable() {
	table.setLinesVisible(true);
	table.setHeaderVisible(true);
	table.setFont(SWTResourceManager.getFont("Arial", DEFAULT_FONT_HEIGHT, SWT.NORMAL));
    }

    /**
     * Adds a resize listener to all columns that cause them not to be resized
     * to below the minimum width.
     * 
     * @param table The columns to add the listener to
     */
    private void addColumnMinWidth(final Table table) {
	final TableColumn[] cols = table.getColumns();
	for (final TableColumn col: cols) {
	    col.addControlListener(new ControlAdapter() {
		@Override
		public void controlResized(ControlEvent e) {
		    for (TableColumn otherCol : cols) {
			// Column can't be smaller than minimum width
			if (otherCol.getWidth() < MIN_TABLE_COLUMN_WIDTH) {
			    otherCol.setWidth(MIN_TABLE_COLUMN_WIDTH);
			}
		    }
		}
	    });
	}

    }

    /**
     * Creates a new resizeable column in the table at the end of the column
     * list.
     *
     * @param title the title of the column
     * @return the table viewer column
     */
    protected TableViewerColumn createColumn(String title) {
	TableViewerColumn viewCol = new TableViewerColumn(viewer, SWT.LEFT);
	TableColumn col = viewCol.getColumn();
	col.setText(title);
	col.setResizable(true);
	col.addSelectionListener(getColumnSelectionAdapter(col, table.getColumnCount() - 1));
	return viewCol;
    }

    /**
     * Gets a selection adapter for when a column is selected.
     * Tables can override to provide their own.
     * 
     * @param column the column to create the adapter for
     * @param index the index of the column
     * @return the selection adapter
     */
    protected SelectionAdapter getColumnSelectionAdapter(final TableColumn column, final int index) {
	SelectionAdapter selectionAdapter = new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		ColumnComparator<TRow> comparator = comparator();
		comparator.setColumn(index);
		int dir = comparator.getDirection();
		viewer.getTable().setSortDirection(dir);
		viewer.getTable().setSortColumn(column);

		sortAction.ifPresent(runnable -> runnable.run());

		viewer.refresh();
	    }
	};
	return selectionAdapter;
    }

    /**
     * Creates a new resizeable column in the table at the end of the column
     * list with a weighting for its size.
     *
     * @param title the title of the column
     * @param widthWeighting the width weighting
     * @param cellProvider the label provider for the cell's text
     * @return the table viewer column
     */
    protected TableViewerColumn createColumn(String title, int widthWeighting, CellLabelProvider cellProvider) {
	return createColumn(title, widthWeighting, true, cellProvider);
    }

    /**
     * Creates a new column in the table at the end of the column
     * list with a weighting for its size.
     *
     * @param title the title of the column
     * @param widthWeighting the width weighting
     * @param resizable whether the column is resizable
     * @param cellProvider the label provider for the cell's text
     * @return the table viewer column
     */
    public TableViewerColumn createColumn(String title, int widthWeighting, boolean resizable, CellLabelProvider cellProvider) {
	TableViewerColumn tableColumn = createColumn(title);
	TableColumn col = tableColumn.getColumn();
	tableColumnLayout.setColumnData(col,
		new ColumnWeightData(widthWeighting, MIN_TABLE_COLUMN_WIDTH, resizable));
	col.setResizable(resizable);
	tableColumn.setLabelProvider(cellProvider);
	return tableColumn;
    }
    
    /**
     * Sets the tooltip text of an existing column.
     *
     * @param column the column
     * @param tooltip the tooltip text
     */
    public void setColumnToolTipText(TableViewerColumn column, String tooltip) {
    	TableColumn col = column.getColumn();
    	col.setToolTipText(tooltip);
    }

    /**
     * Observe property.
     *
     * @param propertyName the property name
     * @return the observable map
     */
    public IObservableMap<TRow, ?> observeProperty(String propertyName) {
	return BeanProperties.value(propertyName).observeDetail(contentProvider.getKnownElements());
    }	

    @Override
    public void addKeyListener(KeyListener listener) {
	viewer.getTable().addKeyListener(listener);
    }

    /**
     * Get the comparator for the columns. Tables can override to provide their own.
     * @return The comparator for the table.
     */
    protected ColumnComparator<TRow> comparator() {
	return comparator;
    }

    /**
     * Disposes and re-adds the columns so that they update from the model again. 
     */
    public void updateTableColumns() {
	for (TableColumn col : table().getColumns()) {
	    col.dispose();
	}

	addColumns();

	forceResizeTable();
    }

    /**
     * Forces the table to display the columns correctly.
     * 
     * This is a dirty hack but is the only way I found to ensure the columns
     * displayed properly.
     */
    private void forceResizeTable() {
	setRedraw(false);
	Point prevSize = table.getSize();
	setSize(prevSize.x, prevSize.y - 1);
	setSize(prevSize);
	setRedraw(true);
    }
}

