
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

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * A table which is bound to some data in the standard IBEX style.
 * 
 * @param <TRow> the type for a row within the table
 */
public abstract class DataboundTable<TRow> extends Composite {

    private static final int DEFAULT_FONT_HEIGHT = 10;
    private static final int MIN_TABLE_COLUMN_WIDTH = 50;

    private int tableStyle;
	private Table table;
	private TableViewer viewer;
	private TableColumnLayout tableColumnLayout = new TableColumnLayout();
	private Composite tableComposite;
	private ObservableListContentProvider contentProvider = new ObservableListContentProvider();
	
	private Class<TRow> rowType;
	
    /**
     * Instantiates a new databound table.
     *
     * @param parent the parent
     * @param style the style
     * @param rowType the row type
     * @param tableStyle the table style
     */
	public DataboundTable(Composite parent, int style, Class<TRow> rowType, int tableStyle) {
		super(parent, style);
		this.tableStyle = tableStyle | SWT.BORDER;
		this.rowType = rowType;

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
		
		viewer = createViewer();

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
     * @param rowType the row type
     * @wbp.parser.constructor
     */
	public DataboundTable(Composite parent, int style, Class<TRow> rowType) {
		this(parent, style, rowType, SWT.FULL_SELECTION | SWT.BORDER | SWT.HIDE_SELECTION);
	}

    /**
     * Sets the data rows content.
     *
     * @param rows the new rows
     */
	public void setRows(Collection<TRow> rows) {
		if (!table.isDisposed()) {
			viewer.setInput(new WritableList(rows, rowType.getClass()));
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
		table.setSelection(index);
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
		GridData gridData = (GridData) tableComposite().getLayoutData();
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
     * Table is wrapped in a composite to allow TableColumnLayout to work.
     * 
     * @return table composite
     */
	protected Composite tableComposite() { 
		return tableComposite;
	}
	
    /**
     * Table column layout.
     *
     * @return the table column layout
     */
	protected TableColumnLayout tableColumnLayout() {
		return tableColumnLayout;
	}
	
    /**
     * Viewer should be created with tableComposite() as it's parent.
     * 
     * @return viewer
     */
	protected TableViewer createViewer() {
		return viewer = new TableViewer(tableComposite, tableStyle);
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
		col.addSelectionListener(getSelectionAdapter(col, table.getColumnCount()-1));
		return viewCol;
	}
	
	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
        SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	ColumnComparator comparator = (ColumnComparator) viewer.getComparator();
                comparator.setColumn(index);
                int dir = comparator.getDirection();
                viewer.getTable().setSortDirection(dir);
                viewer.getTable().setSortColumn(column);
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
     * @return the table viewer column
     */
	protected TableViewerColumn createColumn(String title, int widthWeighting) {
		return createColumn(title, widthWeighting, true);
	}
	
    /**
     * Creates a new column in the table at the end of the column
     * list with a weighting for its size.
     *
     * @param title the title of the column
     * @param widthWeighting the width weighting
     * @param resizable whether the column is resizable
     * @return the table viewer column
     */
	public TableViewerColumn createColumn(String title, int widthWeighting, boolean resizable) {
		TableViewerColumn tableColumn = createColumn(title);
        TableColumn col = tableColumn.getColumn();
		tableColumnLayout().setColumnData(col,
                new ColumnWeightData(widthWeighting, MIN_TABLE_COLUMN_WIDTH, resizable));
        col.setResizable(resizable);
		return tableColumn;
	}
	
    /**
     * Observe property.
     *
     * @param propertyName the property name
     * @return the observable map
     */
	public IObservableMap observeProperty(String propertyName) {
		return BeanProperties.value(rowType, propertyName).observeDetail(contentProvider.getKnownElements());
	}	

    @Override
    public void addKeyListener(KeyListener listener) {
        viewer.getTable().addKeyListener(listener);
    }

    /**
     * Gets the item at a point.
     *
     * @param pt the point
     * @return the item at point
     */
    public TRow getItemAtPoint(Point pt) {
        TableItem item = table.getItem(pt);
        if (item == null) {
            return null;
        }
        return firstSelectedRow();
    }
    
    /**
     * Get the comparator for the columns. Tables can override to provide their own.
     * @return The comparator for the table.
     */
	protected ColumnComparator comparator() {
		return new NullComparator();
	}
}

