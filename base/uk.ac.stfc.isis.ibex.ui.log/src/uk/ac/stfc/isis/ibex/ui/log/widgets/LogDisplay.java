
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.log.widgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.ui.AsyncMessageModerator;
import uk.ac.stfc.isis.ibex.ui.log.comparator.LogMessageComparator;
import uk.ac.stfc.isis.ibex.ui.log.filter.LogMessageFilter;

/**
 * The canvas for displaying the logs.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class LogDisplay extends Canvas {
	private static final Color COLOR_CONNECTION_OK = Display.getDefault()
			.getSystemColor(SWT.COLOR_BLACK);
	private static final Color COLOR_CONNECTION_DOWN = Display.getDefault()
			.getSystemColor(SWT.COLOR_RED);

	private static final String MSG_CONNECTION_OK = "Connection to log message routing server (JMS) functioning normally.";

	private static final String MSG_CONNECTION_DOWN = "Unable to connect to log message routing server (JMS); "
			+ "Connection to log message database may still be available.";

	private static final String MSG_CLEAR_CONFIRM = "This will clear all recent log message history (messages "
			+ "will still be retrievable by searching). Proceed?";

	private static final LogMessageFields[] COLUMNS = {
			LogMessageFields.CLIENT_NAME, LogMessageFields.CLIENT_HOST,
			LogMessageFields.CONTENTS, LogMessageFields.EVENT_TIME,
			LogMessageFields.CREATE_TIME, LogMessageFields.SEVERITY,
			LogMessageFields.TYPE, LogMessageFields.APPLICATION_ID };

    /** The data model. */
	private LogDisplayModel model;

    /** Comparator that specifies how messages should be sorted. */
	private LogMessageComparator comparator;

	private Set<LogMessageFilter> filters = new HashSet<>();

	private SearchControl searchControl;
	private Label lblTableTitle;
	private TableViewer tableViewer;
	private Label jmsStatusLabel;

	private MenuItem mnuClearRecent;
	private MenuItem mnuClearSelected;
	private MenuItem mnuSaveAll;
	private MenuItem mnuSaveSelected;
    private AsyncMessageModerator asyncMessageModerator;

    /**
     * Default constructor.
     * 
     * @param parent Parent component
     * @param model Model to use for the view
     */
	public LogDisplay(Composite parent, LogDisplayModel model) {
		super(parent, SWT.NONE);

		comparator = new LogMessageComparator();

		createLayout();

		if (model != null) {
			setModel(model);
		}
	}

	/**
     * Set the model that will be used to supply the data (i.e. Log messages)
     * for the table.
     * 
     * @param model the view model
     */
	public void setModel(final LogDisplayModel model) {
		this.model = model;
		this.searchControl.setSearcher(model);
        this.asyncMessageModerator = new AsyncMessageModerator(model);

		// Listen for updates to the list of messages to be displayed
		final LogDisplay display = this;
		model.addPropertyChangeListener("message",
				new PropertyChangeListener() {
                    @Override
					public void propertyChange(PropertyChangeEvent event) {
                        if (asyncMessageModerator.requestTaskLock()) {
                            display.setMessageData(model.getMessages());
                        }
					}
				});

		// Listen for updates of the connection status (to JMS)
		model.addPropertyChangeListener("connection",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent event) {
						display.setConnectionStatus((boolean) event
								.getNewValue());
					}
				});

		// Listen for updates to the display mode (live or search)
		model.addPropertyChangeListener("displayMode",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent event) {
						display.setDisplayMode(model.isSearchMode());
					}
				});

		// Listen for error messages (e.g. if a search fails)
		model.addPropertyChangeListener("errorMessage",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent event) {
						display.displayErrorDialog((String) event.getNewValue());
					}
				});

		setMessageData(model.getMessages());
		setConnectionStatus(model.getConnectionStatus());
		setDisplayMode(model.isSearchMode());
	}

	/**
     * Apply a filter that restricts the type of log messages that will be
     * displayed in this widget.
     * 
     * @param filter the message filter to apply
     */
	public void addMessageFilter(final LogMessageFilter filter) {
		this.filters.add(filter);
		updateFilter();
	}
	
	/**
     * Remove a filter that restricts the type of log messages that will be
     * displayed in this widget.
     * 
     * @param filter the message filter to remove
     */
	public void removeMessageFilter(final LogMessageFilter filter) {
		this.filters.remove(filter);
		updateFilter();
	}

    /**
     * Update the filter.
     */
	private void updateFilter() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tableViewer.setFilters(filters.toArray(new ViewerFilter[] {}));
			}
		});		
	}

	/**
     * Set the list of messages to be displayed in the table.
     * 
     * @param messages the messages to display
     */
    private void setMessageData(final List<LogMessage> messages) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
                if (!tableViewer.getControl().isDisposed()) {
                    tableViewer.setInput(messages);
                    asyncMessageModerator.releaseTaskLock();
                }
			}
		});
	}

	/**
     * Set the status of the connection to the message producer (JMS server).
     * 
     * @param connectionOk sets the connection status
     */
	private void setConnectionStatus(final boolean connectionOk) {
		if (jmsStatusLabel != null) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (connectionOk) {
						jmsStatusLabel.setForeground(COLOR_CONNECTION_OK);
						jmsStatusLabel.setText(MSG_CONNECTION_OK);
					} else {
						jmsStatusLabel.setForeground(COLOR_CONNECTION_DOWN);
						jmsStatusLabel.setText(MSG_CONNECTION_DOWN);
					}
				}
			});
		}
	}

	/**
     * Set the current display mode - live messages or search results. Updates
     * the table title accordingly.
     * 
     * @param isSearchMode whether to show search results or live messages
     */
	private void setDisplayMode(final boolean isSearchMode) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (isSearchMode) {
					lblTableTitle.setText("Search Results");
				} else {
					lblTableTitle.setText("Recent Log Messages");
				}
			}
		});
	}

	/**
     * Show an error dialog with a specified message. Used for e.g. when a
     * database operation fails.
     * 
     * @param message Message to display in the dialog
     */
	public void displayErrorDialog(final String message) {
		MessageBox dialog = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
		dialog.setText("Error");
		dialog.setMessage(message);
		dialog.open();
	}

	/**
     * Create all the elements and behaviours of the widget's UI.
     */
	private void createLayout() {
		// Layout
		setLayout(new GridLayout(1, false));

		// Add the search box
		searchControl = new SearchControl(this, model);
		searchControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Add table title label
        lblTableTitle = new Label(this, SWT.NONE);
        lblTableTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		// Add log message table
		tableViewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.setComparator(comparator);
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

		// Add JMS connection status notification
		jmsStatusLabel = new Label(this, SWT.BORDER);
		jmsStatusLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		setConnectionStatus(false);

		// Layout the table columns and link them to the correct properties of
		// the LogMessage object.
		createTableColumns(tableViewer);

		// Make the right-click context menu. Includes options to show/hide
		// each column, clear all entries, etc.
		createContextMenu(tableViewer);

		// Add a listener that displays a dialog window with message details
		// if a message is double clicked
		addDoubleClickListener(tableViewer);

		// Set visibility properties on the table
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Remove selected messages from the current view if delete is pressed
		table.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					clearSelectedMessages();
				}
			}
		});

		// Lazily refresh the state of items in the context menu when it is
		// opened.
		table.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent e) {
				updateContextMenu();
			}
		});

		// Set the ContentProvider
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		setData(new LogMessage[0]);
	}

	/**
     * Create all the table columns (one for each property of LogMessage).
     * 
     * @param viewer the table viewer
     */
	private void createTableColumns(TableViewer viewer) {
		if (viewer == null) {
			return;
		}

		for (final LogMessageFields field : COLUMNS) {
			final TableViewerColumn viewerColumn = new TableViewerColumn(
					viewer, SWT.NONE);
			final TableColumn column = viewerColumn.getColumn();

			String title = field.getDisplayName();
			int width = field.getDefaultColumnWidth();

			column.setText(title);
			column.setWidth(width);
			column.setResizable(width > 0);
			column.setMoveable(true);

			// Sort table by selected column when a column header is clicked
			final Table table = viewer.getTable();
			SelectionAdapter selectionAdapter = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					comparator.setColumn(field);
					int dir = comparator.getDirection();
					table.setSortDirection(dir);
					table.setSortColumn(column);
				}
			};
			column.addSelectionListener(selectionAdapter);

			// Set the message property to be displayed in the column
			viewerColumn.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					LogMessage msg = (LogMessage) element;
					return msg.getProperty(field);
				}
			});
		}
	}

	/**
     * Create and populate the right-click context menu.
     * 
     * @param tableViewer the table viewer
     */
	private void createContextMenu(TableViewer tableViewer) {
		Table table = tableViewer.getTable();

		// Make menu
		Menu contextMenu = new Menu(this.getShell(), SWT.POP_UP);
		table.setMenu(contextMenu);

		// Add show/hide items
		for (TableColumn col : table.getColumns()) {
			addColumnMenuItem(contextMenu, col);
		}

		// Add menu separator
		new MenuItem(contextMenu, SWT.SEPARATOR);

		// Add clear all menu item
		mnuClearRecent = new MenuItem(contextMenu, SWT.NONE);
		mnuClearRecent.setText("Clear All Recent Entries");
		mnuClearRecent.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				clearRecentAllMessages();
			}
		});

		// Add clear selected menu item
		mnuClearSelected = new MenuItem(contextMenu, SWT.NONE);
		mnuClearSelected.setText("Clear Selected");
		mnuClearSelected.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				clearSelectedMessages();
			}
		});

		mnuSaveAll = new MenuItem(contextMenu, SWT.NONE);
		mnuSaveAll.setText("Save All To Log File");
		mnuSaveAll.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				boolean onlySelected = false;
				saveToLogFile(onlySelected);
			}
		});

		mnuSaveSelected = new MenuItem(contextMenu, SWT.NONE);
		mnuSaveSelected.setText("Save Selected To Log File");
		mnuSaveSelected.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				boolean onlySelected = true;
				saveToLogFile(onlySelected);
			}
		});
	}

	/**
     * Add a show/hide item to the context menu for the specified column.
     * 
     * @param contextMenu the context menu
     * @param column the column
     */
	private void addColumnMenuItem(Menu contextMenu, final TableColumn column) {
		final MenuItem itemName = new MenuItem(contextMenu, SWT.CHECK);
		itemName.setText(column.getText());
		itemName.setSelection(column.getResizable());
		itemName.addListener(SWT.Selection, new Listener() {
			@Override
            public void handleEvent(Event event) {
				if (itemName.getSelection()) {
					column.setWidth(150);
					column.setResizable(true);
				} else {
					column.setWidth(0);
					column.setResizable(false);
				}
			}
		});
	}

	/**
     * Update the enabled state of items in the context menu.
     */
	private void updateContextMenu() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();

		boolean anyItems = tableViewer.getTable().getItemCount() > 0;
		boolean anySelected = selection.size() > 0;
		boolean clearAllEnabled = anyItems && !model.isSearchMode();

		mnuClearRecent.setEnabled(clearAllEnabled);
		mnuClearSelected.setEnabled(anySelected);
		mnuSaveAll.setEnabled(anyItems);
		mnuSaveSelected.setEnabled(anySelected);
	}

	/**
     * Add a listener to the table that displays a dialog showing details of the
     * selected message when it is double clicked.
     * 
     * @param viewer the table viewer
     */
	private void addDoubleClickListener(final TableViewer viewer) {
		final Shell shell = this.getShell();
		viewer.getTable().addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {

				// Prepare confirmation dialog
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				LogMessage selectedMessage = (LogMessage) selection
						.getFirstElement();

				if (selectedMessage != null) {
					SingleLogMessageDialog dialog = new SingleLogMessageDialog(
							shell);
					dialog.setLogMessage(selectedMessage);
					dialog.open();
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});
	}

	/**
	 * Remove all messages from the live message list.
	 */
	private void clearRecentAllMessages() {
		// Prepare confirmation dialog
		MessageBox dialog = new MessageBox(getShell(), SWT.ICON_QUESTION
				| SWT.OK | SWT.CANCEL);
		dialog.setText("Confirm clear");
		dialog.setMessage(MSG_CLEAR_CONFIRM);

		// open dialog and await user selection
		int returnCode = dialog.open();

		// if user pressed OK, clear all data
		if (returnCode == SWT.OK) {
			// clear data stored in the table
			setData(new LogMessage[0]);

			// clear data in the model
			if (model != null) {
				model.clearMessageCache();
			}
		}
	}

	/**
     * Remove the currently selected messages from the list.
     */
	private void clearSelectedMessages() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		List<LogMessage> msgs = new ArrayList<LogMessage>();

		for (Object msg : selection.toArray()) {
			msgs.add((LogMessage) msg);
		}

		model.removeMessagesFromCurrentView(msgs);
	}

    /**
     * Save the logs to file.
     * 
     * @param onlySelected save only the selected entries
     */
	private void saveToLogFile(boolean onlySelected) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
		String date = dateFormat.format(new Date());
		String defaultFileName = "ioc-log-" + date + ".log";

		FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);

		dialog.setFilterNames(new String[] {"Log Files (*.log)", 
				"All Files (*.*)" });
		dialog.setFilterExtensions(new String[] {"*.log"});
		dialog.setFilterPath("c:\\");
		dialog.setFileName(defaultFileName);

		String filename = dialog.open();

		if (filename != null) {
			if (onlySelected) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
				List<LogMessage> selected = new ArrayList<LogMessage>();
				for (Object msg : selection.toArray()) {
					selected.add((LogMessage) msg);
				}
				model.saveSelectedToLogFile(selected, filename);
			} else {
				model.saveCurrentViewToLogFile(filename, filters, comparator);
			}
		}
	}
}
