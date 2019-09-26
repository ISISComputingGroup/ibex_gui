
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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

package uk.ac.stfc.isis.ibex.ui.journalviewer;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.journal.JournalRow;
import uk.ac.stfc.isis.ibex.journal.JournalSearch;
import uk.ac.stfc.isis.ibex.journal.JournalSort;
import uk.ac.stfc.isis.ibex.journal.SearchNumber;
import uk.ac.stfc.isis.ibex.journal.SearchString;
import uk.ac.stfc.isis.ibex.journal.SearchTime;
import uk.ac.stfc.isis.ibex.ui.journalviewer.models.JournalViewModel;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.tables.NullComparator;

/**
 * Journal viewer main view.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class JournalViewerView {

    /**
     * The view ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.journalviewer.JournalViewerView"; //$NON-NLS-1$

    private Label lblError;
    private Label lblLastUpdate;
    private Label error;

    private final DataBindingContext bindingContext = new DataBindingContext();
    private final JournalViewModel model = JournalViewerUI.getDefault().getModel();
    private static final Display DISPLAY = Display.getCurrent();

    private Button btnRefresh;
    private Spinner spinnerPageNumber;
    private SearchInput searchInput;
    private Button btnSearch;
    private ProgressBar progressBar;

    private DataboundTable<JournalRow> table;

    private Composite searchControls;
    private Button btnClear;
    private Composite basicControls;


    /**
     * Create contents of the view part.
     * 
     * @param parent
     *            The parent view part.
     */
    @PostConstruct
    public void createPartControl(final Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        parent.setLayoutData(gd);

        Label lblTitle = new Label(parent, SWT.NONE);
        lblTitle.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
        lblTitle.setText("Journal Viewer");

        Composite selectedContainer = new Composite(parent, SWT.FILL);
        RowLayout rl = new RowLayout();
        rl.justify = false;
        rl.pack = false;
        rl.type = SWT.HORIZONTAL;
        selectedContainer.setLayout(rl);
        selectedContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Composite controls = new Composite(parent, SWT.FILL);
        RowLayout rlControls = new RowLayout(SWT.HORIZONTAL);
        rlControls.center = true;
        controls.setLayout(rlControls);
        controls.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        basicControls = new Composite(controls, SWT.NONE);
        RowLayout rlBasicControls = new RowLayout(SWT.HORIZONTAL);
        rlBasicControls.marginTop = 7;
        rlBasicControls.center = true;
        basicControls.setLayout(rlBasicControls);

        Label lblPage = new Label(basicControls, SWT.NONE);
        lblPage.setText("Page number: ");

        spinnerPageNumber = new Spinner(basicControls, SWT.BORDER);
        spinnerPageNumber.setMinimum(1);

        btnRefresh = new Button(basicControls, SWT.NONE);
        btnRefresh.setText("Refresh data");
        
        searchControls = new Composite(controls, SWT.NONE);
        RowLayout rlSearchControls = new RowLayout(SWT.HORIZONTAL);
        rlSearchControls.center = true;
        searchControls.setLayout(rlSearchControls);

        searchInput = new SearchInput(searchControls, model);
        RowLayout rlFilterControl = new RowLayout(SWT.HORIZONTAL);
        searchInput.setLayout(rlFilterControl);

        btnSearch = new Button(searchControls, SWT.NONE);
        btnSearch.setLayoutData(new RowData(80, SWT.DEFAULT));
        btnSearch.setText("Search");
        
        btnClear = new Button(searchControls, SWT.NONE);
        btnClear.setText("Clear");
        
        progressBar = new ProgressBar(searchControls, SWT.INDETERMINATE);
        progressBar.setMaximum(80);
        progressBar.setLayoutData(new RowData(100, SWT.DEFAULT));

        error = new Label(searchControls, SWT.NONE);
        error.setSize(30, 10);
        error.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));



        for (final JournalField property : JournalField.values()) {
            final Button checkbox = new Button(selectedContainer, SWT.CHECK);
            checkbox.setText(property.getFriendlyName());
            checkbox.setSelection(model.getFieldSelected(property));
            checkbox.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    super.widgetSelected(e);
                    setProgressIndicatorsVisible(true);
                    model.setFieldSelected(property, checkbox.getSelection()).thenAccept(ignored -> setProgressIndicatorsVisible(false));
                }
            });
        }

		final int tableStyle = SWT.FILL | SWT.FULL_SELECTION;
		table = new DataboundTable<JournalRow>(parent, tableStyle, tableStyle) {
			@Override
			protected void addColumns() {
				changeTableColumns();
			}
			
			@Override
			protected ColumnComparator<JournalRow> comparator() {
				return new NullComparator<>();
			}
			
			// Sort table by selected column when a column header is clicked
			@Override
			protected SelectionAdapter getColumnSelectionAdapter(final TableColumn column, final int index) {
		        SelectionAdapter selectionAdapter = new SelectionAdapter() {
		            @Override
		            public void widgetSelected(SelectionEvent e) {
		                JournalField field = JournalField.getFieldFromFriendlyName(column.getText());
		                setProgressIndicatorsVisible(true);
	                    model.sortBy(field).thenAccept(ignored -> setProgressIndicatorsVisible(false));
		            }
		        };
		        return selectionAdapter;
		    }
		};
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table.initialise();

        lblError = new Label(parent, SWT.NONE);
        lblError.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        lblError.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        lblError.setText("placeholder");

        lblLastUpdate = new Label(parent, SWT.NONE);
        lblLastUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        lblLastUpdate.setText("placeholder");

        bind();
        model.refresh();
        setProgressIndicatorsVisible(false);
    }

    /**
     * Requests that the model perform a search for log messages that match the
     * request parameters.
     */
    private void search() {
        resetPageNumber();
        int fieldIndex = searchInput.getCmbFilterTypeIndex();
        
        JournalSearch search = null;
        final JournalField field = model.getSearchableFields().get(fieldIndex);
        if (field == JournalField.RUN_NUMBER) {
            search = new SearchNumber(field, searchInput.getRunNumberFrom(), searchInput.getRunNumberTo());
        } else if (field == JournalField.START_TIME) {
            search = new SearchTime(field, searchInput.getStartTimeFrom(), searchInput.getStartTimeTo());
        } else {
            search = new SearchString(field, searchInput.getActiveSearchText());
        }

        model.setActiveSearch(search);
        setProgressIndicatorsVisible(true);
        model.setPageNumber(1).thenAccept(ignored -> setProgressIndicatorsVisible(false));
    }

    private void changeTableColumns() {
        // Dispose all the columns and re-add them, otherwise the columns may
        // not be in the expected order.
        for (TableColumn col : table.table().getColumns()) {
            col.dispose();
        }

        for (final JournalField field : JournalField.values()) {
            if (model.getFieldSelected(field)) {
                TableViewerColumn col = table.createColumn(field.getFriendlyName(), 1, true,
                        new DataboundCellLabelProvider<JournalRow>(table.observeProperty("row")) {
                            @Override
                            protected String stringFromRow(JournalRow row) {
                                return row.get(field);
                            }
                        });
                col.getColumn().setText(field.getFriendlyName());

            }
        }

        forceResizeTable();
        updateSortIndicator();
    }

    /**
     * Forces the table to display the columns correctly.
     * 
     * This is a dirty hack but is the only way I found to ensure the columns
     * displayed properly.
     */
    private void forceResizeTable() {
        table.setRedraw(false);
        Point prevSize = table.getSize();
        table.setSize(prevSize.x, prevSize.y - 1);
        table.setSize(prevSize);
        table.setRedraw(true);
    }
    
    /**
     * Updates the sort indicator arrow to the currently sorted column
     */
    private void updateSortIndicator() {
        JournalSort activeSort = model.getActiveSearch().getPrimarySort();
        
        // Goes through the columns of the table,
        // and if it finds the column which is currently sorted, display an indicator
        Arrays.asList(table.table().getColumns()).stream()
            .filter(col -> col.getText() == activeSort.getSortField().getFriendlyName())
            .findFirst()
            .ifPresent(col -> {
                table.table().setSortColumn(col);
                table.table().setSortDirection(model.getSortDirection());
            });
    }
    
    private void setProgressIndicatorsVisible(final boolean visible) {
        DISPLAY.asyncExec(() -> progressBar.setVisible(visible));
    }
    
    private void resetPageNumber() {
        DISPLAY.asyncExec(() -> spinnerPageNumber.setSelection(1));
    }

    private void bind() {
        bindingContext.bindValue(WidgetProperties.text().observe(lblError),
                BeanProperties.value("message").observe(model));
        bindingContext.bindValue(WidgetProperties.text().observe(lblLastUpdate),
                BeanProperties.value("lastUpdate").observe(model));
        bindingContext.bindValue(WidgetProperties.maximum().observe(spinnerPageNumber),
                BeanProperties.value("pageNumberMax").observe(model));
        bindingContext.bindValue(WidgetProperties.text().observe(error),
                BeanProperties.value("errorMessage").observe(model));
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnSearch),
                BeanProperties.value("enableDisableButton").observe(model));

        spinnerPageNumber.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setProgressIndicatorsVisible(true);
                model.setPageNumber(spinnerPageNumber.getSelection()).thenAccept(ignored -> setProgressIndicatorsVisible(false));
            }
        });
        
        btnRefresh.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                resetPageNumber();
                setProgressIndicatorsVisible(true);
                model.setPageNumber(1).thenAccept(ignored -> setProgressIndicatorsVisible(false));
            }
        });
        
        btnSearch.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                search();
            }
        });
        
        btnClear.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                resetPageNumber();
                searchInput.clearInput();
                model.resetActiveSearch();
                setProgressIndicatorsVisible(true);
                model.setPageNumber(1).thenAccept(ignored -> setProgressIndicatorsVisible(false));
            }
        });

        model.addPropertyChangeListener("runs", e -> 
        DISPLAY.asyncExec(() -> {
                setProgressIndicatorsVisible(true);
                changeTableColumns();
                table.setRows(model.getRuns());
                setProgressIndicatorsVisible(false);
        }));
        
        searchInput.addSearchListeners(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
                    search();
                }
            }
        });

        // TODO do this the E4 way
//        // Add a listener to refresh the page whenever it becomes visible
//        try {
//	        partService.addPartListener(new PartAdapter() {
//	        	/**
//	    		 * {@inheritDoc}
//	    		 */
//	    		@Override
//	    		public void partVisible(IWorkbenchPartReference partRef) {
//	    			super.partVisible(partRef);
//	    			model.refresh();
//	    		}
//	        });
//        } catch (NullPointerException e) {
//        	// If getSite or getPage return null then log the error but carry on.
//        	IsisLog.getLogger(getClass()).info("Couldn't add visibility listener to Journal view");
//        }
    }
}
