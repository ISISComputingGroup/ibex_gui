
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
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.map.ObservableMap;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.journal.JournalRow;
import uk.ac.stfc.isis.ibex.journal.JournalSearch;
import uk.ac.stfc.isis.ibex.journal.JournalSort;
import uk.ac.stfc.isis.ibex.journal.SearchNumber;
import uk.ac.stfc.isis.ibex.journal.SearchString;
import uk.ac.stfc.isis.ibex.journal.SearchTime;
import uk.ac.stfc.isis.ibex.ui.Utils;
import uk.ac.stfc.isis.ibex.ui.journalviewer.models.JournalViewModel;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.tables.NullComparator;
import uk.ac.stfc.isis.ibex.validators.NumbersOnlyListener;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonBuilder;

/**
 * Journal viewer main view.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class JournalViewerView {

	private static final String JOURNAL_VIEWER_UNAVAILABLE = "The journal viewer is not available on this platform.";

	/**
	 * The view ID.
	 */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.journalviewer.JournalViewerView"; //$NON-NLS-1$

	private Label lblError;
	private Label lblLastUpdate;
	private Label error;
	private Label lblResults;

	private final DataBindingContext bindingContext = new DataBindingContext();
	private final JournalViewModel model = JournalViewerUI.getDefault().getModel();
	private static final Display DISPLAY = Display.getCurrent();

	private Button btnRefresh;
	private Text textPageNumber;
	private Button btnPrevPage;
	private Button btnNextPage;
	private Button btnFirstPage;
	private Button btnLastPage;
	private SearchInput searchInput;
	private Button btnSearch;
	private ProgressBar progressBar;

	private DataboundTable<JournalRow> journalTable;

	private Composite searchControls;
	private Button btnClear;
	private Composite basicControls;

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent The parent view part.
	 */
	@PostConstruct
	public void createPartControl(final Composite parent) {
		if (Utils.SHOULD_HIDE_USER_INFORMATION) {
			var label = new Label(parent, SWT.NONE);
			label.setText(JOURNAL_VIEWER_UNAVAILABLE);
			return;
		}
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

		lblResults = new Label(basicControls, SWT.LEFT | SWT.HORIZONTAL | SWT.BORDER);
		lblResults.setText("placeholder");
		lblResults.setToolTipText("Currently displayed entries out of total.");
		RowData lblResultsData = new RowData();
		lblResultsData.width = 180;
		lblResults.setLayoutData(lblResultsData);

		btnFirstPage = new IBEXButtonBuilder(basicControls, SWT.NONE).text("<<").tooltip("Go to the first page.")
				.listener(e -> {
					setProgressIndicatorsVisible(true);
					model.firstPage().thenAccept(ignored -> setProgressIndicatorsVisible(false));
				}).customLayoutData(IBEXButtonBuilder.defaultRow).build();

		btnPrevPage = new IBEXButtonBuilder(basicControls, SWT.NONE).text("< Prev")
				.tooltip("Go to the previous page.").listener(e -> {
					setProgressIndicatorsVisible(true);
					model.prevPage().thenAccept(ignored -> setProgressIndicatorsVisible(false));
				}).customLayoutData(IBEXButtonBuilder.defaultRow).build();

		textPageNumber = new Text(basicControls, SWT.BORDER);
		RowData textPageNumberData = new RowData();
		textPageNumberData.width = 40;
		textPageNumber.setLayoutData(textPageNumberData);
		textPageNumber.setTextLimit(9);

		btnNextPage = new IBEXButtonBuilder(basicControls, SWT.NONE).text("Next >")
				.tooltip("Go to the next page.").listener(e -> {
					setProgressIndicatorsVisible(true);
					model.nextPage().thenAccept(ignored -> setProgressIndicatorsVisible(false));
				}).customLayoutData(IBEXButtonBuilder.defaultRow).build();

		btnLastPage = new IBEXButtonBuilder(basicControls, SWT.NONE).text(">>").tooltip("Go to the last page.")
				.listener(e -> {
					setProgressIndicatorsVisible(true);
					model.lastPage().thenAccept(ignored -> setProgressIndicatorsVisible(false));
				}).customLayoutData(IBEXButtonBuilder.defaultRow).build();

		btnRefresh = new IBEXButtonBuilder(basicControls, SWT.NONE).text("Refresh")
				.tooltip("Refresh the journal.").listener(e -> {
					resetPageNumber();
					setProgressIndicatorsVisible(true);
					model.setPageNumber(1).thenAccept(ignored -> setProgressIndicatorsVisible(false));
				}).customLayoutData(IBEXButtonBuilder.defaultRow).build();

		searchControls = new Composite(controls, SWT.NONE);
		RowLayout rlSearchControls = new RowLayout(SWT.HORIZONTAL);
		rlSearchControls.center = true;
		searchControls.setLayout(rlSearchControls);

		searchInput = new SearchInput(searchControls, model);
		RowLayout rlFilterControl = new RowLayout(SWT.HORIZONTAL);
		searchInput.setLayout(rlFilterControl);

		btnSearch = new IBEXButtonBuilder(searchControls, SWT.NONE).text("Search").tooltip("Search the journal.")
				.listener(e -> search()).customLayoutData(new RowData(80, SWT.DEFAULT)).build();

		btnClear = new IBEXButtonBuilder(searchControls, SWT.NONE).text("Clear").tooltip("Clear the search.")
				.listener(e -> {
					resetPageNumber();
					searchInput.clearInput();
					model.resetActiveSearch();
					setProgressIndicatorsVisible(true);
					model.setPageNumber(1).thenAccept(ignored -> setProgressIndicatorsVisible(false));
				}).customLayoutData(IBEXButtonBuilder.defaultRow).build();

		progressBar = new ProgressBar(searchControls, SWT.INDETERMINATE);
		progressBar.setMaximum(80);
		progressBar.setLayoutData(new RowData(100, SWT.DEFAULT));

		error = new Label(searchControls, SWT.NONE);
		error.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
		error.setLayoutData(new RowData(200, SWT.DEFAULT));

		for (final JournalField property : JournalField.values()) {
			final Button checkbox = new IBEXButtonBuilder(selectedContainer, SWT.CHECK)
					.customLayoutData(IBEXButtonBuilder.defaultRow).text(property.getFriendlyName()).build();

			checkbox.setSelection(model.getFieldSelected(property));
			checkbox.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					setProgressIndicatorsVisible(true);
					model.setFieldSelected(property, checkbox.getSelection())
							.thenAccept(ignored -> setProgressIndicatorsVisible(false));
				}
			});
		}

		final int tableStyle = SWT.FILL | SWT.FULL_SELECTION;
		journalTable = new DataboundTable<JournalRow>(parent, tableStyle, tableStyle) {
			@Override
			protected void addColumns() {
				for (final JournalField field : JournalField.values()) {
					if (model.getFieldSelected(field)) {
						TableViewerColumn col = journalTable.createColumn(field.getFriendlyName(), 1, true,
								new DataboundCellLabelProvider<JournalRow>(
										new ObservableMap<JournalRow, String>(Collections.emptyMap())) {
									@Override
									protected String stringFromRow(JournalRow row) {
										return row.get(field);
									}
								});
						col.getColumn().setText(field.getFriendlyName());
					}
				}
			}

			@Override
			protected ColumnComparator<JournalRow> comparator() {
				return new NullComparator<>();
			}

			// Sort table by selected column when a column header is clicked
			@Override
			protected SelectionAdapter getColumnSelectionAdapter(final TableColumn column, final int index) {
				return new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						JournalField field = JournalField.getFieldFromFriendlyName(column.getText());
						setProgressIndicatorsVisible(true);
						model.sortBy(field).thenAccept(ignored -> setProgressIndicatorsVisible(false));
					}
				};
			}
		};
		journalTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		journalTable.initialise();

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

	/**
	 * Updates the sort indicator arrow to the currently sorted column
	 */
	private void updateSortIndicator() {
		JournalSort activeSort = model.getActiveSearch().getPrimarySort();

		// Goes through the columns of the table,
		// and if it finds the column which is currently sorted, display an indicator
		Arrays.asList(journalTable.table().getColumns()).stream()
				.filter(col -> col.getText() == activeSort.getSortField().getFriendlyName()).findFirst()
				.ifPresent(col -> {
					journalTable.table().setSortColumn(col);
					journalTable.table().setSortDirection(model.getSortDirection());
				});
	}

	private void setProgressIndicatorsVisible(final boolean visible) {
		DISPLAY.asyncExec(() -> progressBar.setVisible(visible));
	}

	private void resetPageNumber() {
		DISPLAY.asyncExec(() -> textPageNumber.setText("1"));
	}

	private void bind() {
		bindingContext.bindValue(WidgetProperties.text().observe(lblError),
				BeanProperties.value("message").observe(model));
		bindingContext.bindValue(WidgetProperties.text().observe(lblLastUpdate),
				BeanProperties.value("lastUpdate").observe(model));

		IConverter<Integer, String> convertToString = IConverter.<Integer, String>create(Integer.class, String.class,
				(arg) -> Integer.toString(arg));

		UpdateValueStrategy<Integer, String> updateStrategy = UpdateValueStrategy.create(convertToString);

		bindingContext.bindValue(WidgetProperties.text().observe(textPageNumber),
				BeanProperties.<JournalViewModel, Integer>value("pageNumber").observe(model), null, updateStrategy);
		bindingContext.bindValue(WidgetProperties.tooltipText().observe(textPageNumber),
				BeanProperties.value("pageNumber").observe(model));
		bindingContext.bindValue(WidgetProperties.text().observe(error),
				BeanProperties.value("errorMessage").observe(model));
		bindingContext.bindValue(WidgetProperties.text().observe(lblResults),
				BeanProperties.value("resultsInfo").observe(model));
		bindingContext.bindValue(WidgetProperties.tooltipText().observe(lblResults),
				BeanProperties.value("resultsInfo").observe(model));

		bindingContext.bindValue(WidgetProperties.enabled().observe(btnPrevPage),
				BeanProperties.value("btnPrevPageEnabled").observe(model));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnFirstPage),
				BeanProperties.value("btnPrevPageEnabled").observe(model));

		bindingContext.bindValue(WidgetProperties.enabled().observe(btnNextPage),
				BeanProperties.value("btnNextPageEnabled").observe(model));
		bindingContext.bindValue(WidgetProperties.enabled().observe(btnLastPage),
				BeanProperties.value("btnNextPageEnabled").observe(model));

		bindingContext.bindValue(WidgetProperties.enabled().observe(btnSearch),
				BeanProperties.value("searchButtonEnabled").observe(model));

		textPageNumber.addVerifyListener(new NumbersOnlyListener());

		textPageNumber.addListener(SWT.Traverse, e -> {
			if (e.detail == SWT.TRAVERSE_RETURN) {
				setProgressIndicatorsVisible(true);
				model.goToPage(Integer.parseInt(textPageNumber.getText()))
						.thenAccept(ignored -> setProgressIndicatorsVisible(false));
			}
			textPageNumber.selectAll();
		});

		textPageNumber.addListener(SWT.FocusIn, e -> {
			textPageNumber.selectAll();
		});

		model.addPropertyChangeListener("runs", e -> DISPLAY.asyncExec(() -> {
			setProgressIndicatorsVisible(true);
			journalTable.updateTableColumns();
			updateSortIndicator();
			journalTable.setRows(model.getRuns());

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
