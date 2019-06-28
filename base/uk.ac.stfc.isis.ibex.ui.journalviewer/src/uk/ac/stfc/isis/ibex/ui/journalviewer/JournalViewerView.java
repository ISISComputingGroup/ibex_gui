
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.journal.JournalRow;
import uk.ac.stfc.isis.ibex.ui.journalviewer.models.JournalViewModel;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.tables.NullComparator;
import org.eclipse.swt.layout.RowData;

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

    private final DataBindingContext bindingContext = new DataBindingContext();
    private final JournalViewModel model = JournalViewerUI.getDefault().getModel();
    private static final Display DISPLAY = Display.getCurrent();

    private Button btnRefresh;
    private Spinner spinnerPageNumber;
    private SearchInput searchInput;
    private Button btnSearch;
    private ProgressBar progressBar;

    private DataboundTable<JournalRow> table;

    private static final JournalField[] FIELDS = {JournalField.RUN_NUMBER, JournalField.TITLE, JournalField.START_TIME,
            JournalField.RB_NUMBER, JournalField.USERS};
    private Composite searchControls;
    private Button btnClear;

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

        Label lblPage = new Label(controls, SWT.NONE);
        lblPage.setText("Page number: ");

        spinnerPageNumber = new Spinner(controls, SWT.BORDER);
        spinnerPageNumber.setMinimum(1);

        btnRefresh = new Button(controls, SWT.NONE);
        btnRefresh.setText("Refresh data");
        
        searchControls = new Composite(controls, SWT.NONE);
        RowLayout rlSearchControls = new RowLayout(SWT.HORIZONTAL);
        rlSearchControls.center = true;
        searchControls.setLayout(rlSearchControls);

        searchInput = new SearchInput(searchControls);
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
        setProgressIndicatorsVisible(false);

        for (final JournalField property : JournalField.values()) {
            final Button checkbox = new Button(selectedContainer, SWT.CHECK);
            checkbox.setText(property.getFriendlyName());
            checkbox.setSelection(model.getFieldSelected(property));
            checkbox.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    super.widgetSelected(e);
                    model.setFieldSelected(property, checkbox.getSelection());
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
    }

    /**
     * Requests that the model perform a search for log messages that match the
     * request parameters.
     */
    private void search() {
        if (model != null) {
            int fieldIndex = searchInput.getCmbFilterTypeIndex();

            if (fieldIndex != -1) {
                final JournalField field = FIELDS[fieldIndex];
                String value = null;
                Integer fromNumber = null;
                Integer toNumber = null;
                Calendar fromTime = null;
                Calendar toTime = null;
                if (field == JournalField.RUN_NUMBER) {
                    fromNumber = searchInput.getRunNumberFrom();
                    toNumber = searchInput.getRunNumberTo();
                } else if (field == JournalField.START_TIME) {
                    fromTime = searchInput.getStartTimeFrom();
                    toTime = searchInput.getStartTimeTo();
                } else {
                    value = searchInput.getActiveSearchText();
                }

                runSearchJob(field, value, fromNumber, toNumber, fromTime, toTime);

            }
        }
    }

    private void runSearchJob(final JournalField field, final String value, Integer fromNumber, Integer toNumber,
            final Calendar fromTime, final Calendar toTime) {

        final Runnable searchJob = new Runnable() {
            @Override
            public void run() {
                setProgressIndicatorsVisible(true);
                model.search(field, value, fromNumber, toNumber, fromTime, toTime);
                setProgressIndicatorsVisible(false);
            }
        };

        Thread searchJobThread = new Thread(searchJob);

        searchJobThread.start();
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
    
    private void setProgressIndicatorsVisible(final boolean visible) {

        DISPLAY.asyncExec(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisible(visible);
            }
        });

    }

    private void bind() {
        bindingContext.bindValue(WidgetProperties.text().observe(lblError),
                BeanProperties.value("message").observe(model));
        bindingContext.bindValue(WidgetProperties.text().observe(lblLastUpdate),
                BeanProperties.value("lastUpdate").observe(model));
        bindingContext.bindValue(WidgetProperties.maximum().observe(spinnerPageNumber),
                BeanProperties.value("pageNumberMax").observe(model));

        spinnerPageNumber.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setProgressIndicatorsVisible(true);
                model.setPageNumber(spinnerPageNumber.getSelection());
                setProgressIndicatorsVisible(false);
            }
        });
        
        btnRefresh.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setProgressIndicatorsVisible(true);
                model.refresh();
                setProgressIndicatorsVisible(false);
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
                setProgressIndicatorsVisible(true);
                searchInput.clearInput();
                model.resetActiveParameters();
                model.refresh();
                setProgressIndicatorsVisible(false);
            }
        });

        model.addPropertyChangeListener("runs", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        setProgressIndicatorsVisible(true);
                        changeTableColumns();
                        table.setRows(model.getRuns());
                        setProgressIndicatorsVisible(false);
                    }
                });
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
