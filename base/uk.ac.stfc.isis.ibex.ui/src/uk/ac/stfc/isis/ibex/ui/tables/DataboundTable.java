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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public abstract class DataboundTable<TRow> extends Composite {

	private int tableStyle;
	private Table table;
	private TableViewer viewer;
	private TableColumnLayout tableColumnLayout = new TableColumnLayout();
	private Composite tableComposite;
	private ObservableListContentProvider contentProvider = new ObservableListContentProvider();
	
	private Class<TRow> rowType;
	
	public DataboundTable(Composite parent, int style, Class<TRow> rowType, int tableStyle) {
		super(parent, style);
		this.tableStyle = tableStyle;
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
	 * @wbp.parser.constructor
	 */
	public DataboundTable(Composite parent, int style, Class<TRow> rowType) {
		this(parent, style, rowType, SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.HIDE_SELECTION);
	}

	public void setRows(Collection<TRow> rows) {
		if (!table.isDisposed()) {
			viewer.setInput(new WritableList(rows, rowType.getClass()));
		}
	}
	
	public TRow firstSelectedRow() {
		List<TRow> rows = selectedRows();
	    return rows.isEmpty() ? null : rows.get(0);
	}
	
	public int getSelectionIndex() {
		return table.getSelectionIndex();
	}
	
	public void setSelectionIndex(int index) {
		table.setSelection(index);
	}
	
	@SuppressWarnings("unchecked")
	public List<TRow> selectedRows() {
	    IStructuredSelection selection = (IStructuredSelection) viewer().getSelection();
	    List<TRow> rows = new ArrayList<>();
	    for (Iterator<IStructuredSelection> i = selection.iterator(); i.hasNext();) {
	    	rows.add((TRow) i.next());
	    }
	    
	    return rows;
	}
	
	public void refresh() {
		TableViewer viewer = viewer();
		if (!viewer.getTable().isDisposed()) {
			viewer.refresh();
			table = viewer.getTable();
		}
	}
	
	public void setSelected(TRow selected) {
		viewer.setSelection(new StructuredSelection(selected));
	}
	
	public void deselectAll() {
		table().deselectAll();
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
	
	/*
	 * Sets whether the table should be included in layout calculations
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
	
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		viewer().addSelectionChangedListener(listener);
	}

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
	
	/*
	 * Completes the setup of the table.
	 */
	protected void initialise() {
		addColumns();
		viewer.setContentProvider(contentProvider);	
				
		table = viewer.getTable();
		configureTable();
	}
	
	protected TableViewer viewer() { 
		return viewer;
	}

	protected Table table() { 
		return table;
	}

	/*
	 * Table is wrapped in a composite to allow TableColumnLayout to work.
	 */
	protected Composite tableComposite() { 
		return tableComposite;
	}
	
	protected TableColumnLayout tableColumnLayout() {
		return tableColumnLayout;
	}
	
	/*
	 * Viewer should be created with tableComposite() as it's parent.
	 */
	protected TableViewer createViewer() {
		return viewer = new TableViewer(tableComposite, tableStyle);
	}
	
	/*
	 * Adds columns to the table. Call {@link #initialise() initialise} method to complete.
	 */
	protected abstract void addColumns();

	protected void configureTable() {
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));		
	}
	
	protected TableViewerColumn createColumn(String title) {
		TableViewerColumn viewCol = new TableViewerColumn(viewer, SWT.LEFT);
		TableColumn col = viewCol.getColumn();
		col.setText(title);
		col.setResizable(false);
		
		return viewCol;
	}
	
	protected TableViewerColumn createColumn(String title, int widthWeighting) {
		TableViewerColumn tableColumn = createColumn(title);
		tableColumnLayout().setColumnData(tableColumn.getColumn(), new ColumnWeightData(widthWeighting, 50, false));	

		return tableColumn;
	}
	
	protected IObservableMap observeProperty(String propertyName) {
		return BeanProperties.value(rowType, propertyName).observeDetail(contentProvider.getKnownElements());
	}	
}
