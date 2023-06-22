 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2019 Science & Technology Facilities Council.
 * All rights reserved.
 *
 * This program is distributed in the hope that it will be useful.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution.
 * EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
 * AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
 * OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
 *
 * You should have received a copy of the Eclipse Public License v1.0
 * along with this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or 
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CellNavigationStrategy;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.tables.NullComparator;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

/**
 * A table that holds the properties for a target.
 */
public class ActionsViewTable extends DataboundTable<ScriptGeneratorAction> {

	/*
	 * final character that results after all modifiers have been
	 * applied.  For example, when the user types Ctrl+A, the character value
	 * is 0x01.
	*/
	private static final int CTRL_C = 0x003; 
	private static final int CTRL_V = 0x016;
	private static final int CTRL_A = 0x001;
	private static final int DEL = 0x07F;
	private static final String TAB = "\t";
	private static final String NEW_LINE = "\r\n";
	private final ScriptGeneratorViewModel scriptGeneratorViewModel;
	private static final  Integer FIXED_NON_EDITABLE_COLUMNS_ON_RIGHT = 2;
	private int dynamicNonEditableColumnsOnRight = 0;
	
	/**
	 * The number of read only columns on the left of the table.
	 */
	protected static final Integer NON_EDITABLE_COLUMNS_ON_LEFT = 1;
	private List<StringEditingSupport<ScriptGeneratorAction>> editingSupports = new ArrayList<StringEditingSupport<ScriptGeneratorAction>>();
	
	/**
     * Default constructor for the table. Creates all the correct columns.
     * 
     * @param parent
     *            The parent composite that this table belongs to.
     * @param style
     *            The SWT style of the composite that this creates.
     * @param tableStyle
     *            The SWT style of the table.
     * @param scriptGeneratorViewModel 
     * 			  The table of actions (rows) to display/write data to.
     */
    public ActionsViewTable(Composite parent, int style, int tableStyle, ScriptGeneratorViewModel scriptGeneratorViewModel) {
        super(parent, style, tableStyle | SWT.BORDER, true);
        this.scriptGeneratorViewModel = scriptGeneratorViewModel;
        this.dynamicNonEditableColumnsOnRight = 0;
        initialise();
        scriptGeneratorViewModel.addActionParamPropertyListener(this);
		TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(viewer,
		        new FocusCellOwnerDrawHighlighter(viewer, false), new CellNavigationStrategy());
		ColumnViewerEditorActivationStrategy activationStrategy = createEditorActivationStrategy(viewer);
		
		TableViewerEditor.create(viewer, focusCellManager, activationStrategy, 
				ColumnViewerEditor.TABBING_HORIZONTAL 
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_CYCLE_IN_VIEWER
				| ColumnViewerEditor.TABBING_VERTICAL);
		MenuManager manager = new MenuManager();
		viewer.getControl().setMenu(manager.createContextMenu(viewer.getControl()));
		Action copyAction = new Action("Copy Selected Actions") {
		    @Override
		    public void run() {
		    	scriptGeneratorViewModel.copyActions(getSelectedTableData());
		    }
		    
		};
		manager.add(copyAction);
		Action pasteAction = new Action("Paste Actions") {
		    @Override
		    public void run() {
		    	scriptGeneratorViewModel.pasteActions(table.getSelectionIndex());
		    }
		};
		manager.add(pasteAction);
		Action clearClipboard = new Action("Clear Clipboard") {
		    @Override
		    public void run() {
		    	scriptGeneratorViewModel.clearClipboard();
		    }
		};
		manager.add(clearClipboard);
		manager.add(new Separator("Clipboard Section")); 
		Action deleteAction = new Action("Delete Selected Actions") {
		    @Override
		    public void run() {
		    	List<ScriptGeneratorAction> selected = selectedRows();
				if (selected != null && !selected.isEmpty())  {
					scriptGeneratorViewModel.deleteAction(selected);
				}
		    }
		};
		manager.add(deleteAction);
		manager.add(new Separator("Select Section")); 
		manager.add(new Action("Select All Actions") {
		    @Override
		    public void run() {
		    	table.selectAll();
		    }
		});
		manager.add(new Separator("Duplicate Section")); 
		Action dupeAction = new Action("Duplicate Selected Actions Below") {
		    @Override
		    public void run() {
		    	scriptGeneratorViewModel.copyActions(getSelectedTableData());
		    	scriptGeneratorViewModel.pasteActions(table.getSelectionIndex() + 1);
		    }
		};
		manager.add(dupeAction);
		manager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				if (table.getSelection().length == 0) {
					copyAction.setEnabled(false);
					deleteAction.setEnabled(false);
					dupeAction.setEnabled(false);
				} else {
					copyAction.setEnabled(true);
					deleteAction.setEnabled(true);
					dupeAction.setEnabled(true);
				}
				if (scriptGeneratorViewModel.checkClipboard()) {
					pasteAction.setEnabled(true);
					clearClipboard.setEnabled(true);
				} else {
					pasteAction.setEnabled(false);
					clearClipboard.setEnabled(false);
				}
				manager.update();
			}
		});
		
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {	 
				if (e.character == CTRL_C) {	
					 scriptGeneratorViewModel.copyActions(getSelectedTableData());
				} else if (e.character == CTRL_V) {
					scriptGeneratorViewModel.pasteActions(table.getSelectionIndex());
				} else if (e.character == CTRL_A) {
					table.selectAll();
				} else if (e.character == DEL) {
					List<ScriptGeneratorAction> selected = selectedRows();
					if (selected != null && !selected.isEmpty())  {
						scriptGeneratorViewModel.deleteAction(selected);
					}
				}
			}
		});
    }
		
    /**
     * Format String data such that copying and pasting into excel would work. Clipboard does not support
     * transferring/pasting data to excel so we format plain text data ourselves.
     * @return table rows data as one string, tab separated. After end of each row, new line is added. "1\t2\r\n1\t2 "
     */
    public String getSelectedTableData() {
    	String data = "";
    	for (TableItem item : table.getSelection()) {
			int size = table.getColumnCount() - NON_EDITABLE_COLUMNS_ON_LEFT - (FIXED_NON_EDITABLE_COLUMNS_ON_RIGHT + dynamicNonEditableColumnsOnRight);
			// TableItem.getText() ensure the values are in order using index.
			for (int idx = 0; idx < size; idx++) {
				  data += item.getText(idx + NON_EDITABLE_COLUMNS_ON_LEFT);
				  // if not final iteration keep appending tab
				  if (idx != size - 1) {
					  data += TAB;
				  } else {
					  data += NEW_LINE;
				  }
			}
		}
    	return data;
    }
    	
    /**
     * Strategy for editing cell.
     * @param tableViewer our table viewer.
     * @return Editor Activation strategy.
     */
	private ColumnViewerEditorActivationStrategy createEditorActivationStrategy(TableViewer tableViewer) {
		return new ColumnViewerEditorActivationStrategy(tableViewer) {
			@Override
            protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
				boolean isEditorActivationEvent = true;
				
				if (event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL) {
 					ColumnViewerEditor editor = this.getViewer().getColumnViewerEditor();
 					ViewerCell nextCell = editor.getFocusCell().getNeighbor(ViewerCell.RIGHT, false);
 					
 					/* The reason this variable is a 1 based index and not a 0 base index 
 					 * is because we want to go to a new row when we are on the penultimate
 					 * column rather than when we are on the last column. This is because the 
 					 * last column is a non editable validity column.*/
					int currentlyFocusedColumn = editor.getFocusCell().getColumnIndex() + 1;
					
					Optional<ViewerCell> neighbour = Optional.ofNullable(nextCell.getNeighbor(ViewerCell.BELOW, false));
					boolean noActionBelow = neighbour.isEmpty() || neighbour.get().getElement() == null;
					if (noActionBelow
					        && (viewer.getTable().getColumnCount() - (FIXED_NON_EDITABLE_COLUMNS_ON_RIGHT + dynamicNonEditableColumnsOnRight) <= currentlyFocusedColumn)) {
					    
                    	scriptGeneratorViewModel.addEmptyAction();
                    	// return false as we will handle this specific case of traversal
                    	isEditorActivationEvent = false;
                    }
					return isEditorActivationEvent;
				} else {
					return event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION 
					        ||  event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
				}
			}
		};
	}

    /**
     * Using a null comparator here stops the columns getting reordered in the UI.
     */
	@Override
	protected ColumnComparator<ScriptGeneratorAction> comparator() {
 		return new NullComparator<>();
	}
	
	/**
	 * Updates the table columns after a script definition change.
	 */
	@Override
	public void updateTableColumns() {
		super.updateTableColumns();
		
		setRows(new ArrayList<ScriptGeneratorAction>());
	}

	/**
	 * Add action parameter and validity check columns.
	 */
	@Override
	protected void addColumns() {
	    editingSupports.clear();
		scriptGeneratorViewModel.addColumns(this);
	}
	
	/**
	 * Create a TableViewerColumn.
	 * 
	 * @param columnName The title of the column.
	 * @param widthWeighting The sizing weight for the width.
	 * @param labelProvider The object to provide labels for the TableViewerColumn.
	 * @return the table viewer column.
	 */
	@Override
	public TableViewerColumn createColumn(String columnName, int widthWeighting,
			CellLabelProvider labelProvider) {
		return super.createColumn(columnName, widthWeighting, labelProvider);
	}

	/**
	 * Create a list of the editingSupport for each column. Required for resetting the selection after focus change.
	 * @param editingSupport The editing support for the column
	 */
	public void addEditingSupport(StringEditingSupport<ScriptGeneratorAction> editingSupport) {
	    editingSupports.add(editingSupport);
	}
	
	private boolean validityChanged(String columnHeader, TableItem item, int column, ScriptGeneratorAction action) {
		var validityText = item.getText(column);
		var validityDisplay = ValidityDisplay.fromText(validityText);
		return columnHeader.equals(ScriptGeneratorViewModel.VALIDITY_COLUMN_HEADER) && !validityDisplay.equalsAction(action);
	}
	
	private boolean executingStatusChanged(String columnHeader, TableItem item, int column, ScriptGeneratorAction action) {
		var lineNumberImage = Optional.ofNullable(item.getImage(column));
		var statusFromLastDisplay = ExecutingStatusDisplay.fromImage(lineNumberImage);
		return columnHeader.equals(ScriptGeneratorViewModel.ACTION_NUMBER_COLUMN_HEADER) && !ExecutingStatusDisplay.equalsAction(statusFromLastDisplay, action);
	}
	
	private boolean parameterValueChanged(Optional<String> parameterValue, TableItem item, int columnNumber) {
		return parameterValue.isPresent() && !parameterValue.get().equals(item.getText(columnNumber));
	}
	
	private boolean estimatedTimeChanged(String columnHeader, TableItem item, int column, ScriptGeneratorAction action) {
		var estimatedTimeText = item.getText(column);
		return columnHeader.equals(ScriptGeneratorViewModel.ESTIMATED_RUN_TIME_COLUMN_HEADER) 
				&& (
						(action.getEstimatedTime().isEmpty() && !estimatedTimeText.equals(ScriptGeneratorViewModel.UNKNOWN_TEXT)) 
						|| (action.getEstimatedTime().isPresent() && !estimatedTimeText.equals(ScriptGeneratorViewModel.changeSecondsToTimeFormat(action.getEstimatedTime().get().longValue())))
				);
	}
	
	private boolean estimatedCustomChanged(String columnHeader, TableItem item, int column, ScriptGeneratorAction action) {
		var estimatedCustom = action.getEstimatedCustom();
		return estimatedCustom.isPresent() && !Objects.equals(item.getText(column), estimatedCustom.get().get(columnHeader));
	}
	
	/**
	 * Detect if the values displayed by the table item cells and the action parameter values are different.
	 * 
	 * @param item The table row to compare the action to.
	 * @param columns The columns that identify the action parameters and table item cells.
	 * @param action The action to check if the cells are different to.
	 * @return true if any values differ, false if none differ.
	 */
	private boolean valuesDiffer(TableItem item, TableColumn[] columns, ScriptGeneratorAction action) {
		
		int columnNumber = 0;
		for (TableColumn column : columns) {
			var columnHeader = column.getText();
			var actionParameterValues = action.getActionParameterValueMapAsStrings();
			Optional<String> parameterValue = Optional.ofNullable(actionParameterValues.get(columnHeader));
			if (parameterValueChanged(parameterValue, item, columnNumber) 
					|| validityChanged(columnHeader, item, columnNumber, action) 
					|| executingStatusChanged(columnHeader, item, columnNumber, action)
					|| estimatedTimeChanged(columnHeader, item, columnNumber, action)
					|| estimatedCustomChanged(columnHeader, item, columnNumber, action)) {
				return true;
			} 
			columnNumber++;
		}
		return false;
	}
	
	private boolean actionChanged(ScriptGeneratorAction tableAction, ScriptGeneratorAction newAction) {
		return tableAction == null 
				|| !tableAction.equals(newAction) 
				|| tableAction.isValid() != newAction.isValid() 
				|| tableAction.getEstimatedTime() != newAction.getEstimatedTime()
				|| !Objects.equals(tableAction.getEstimatedCustom(), newAction.getEstimatedCustom());
	}
	
	/**
	 * Remove any actions being displayed that are not in the newActions list.
	 * 
	 * @param newActions The actions that we want to display.
	 */
	private void removeDeletedRows(List<ScriptGeneratorAction> newActions) {
		List<Object> elementsToRemove = new ArrayList<Object>();
		var itemCount = viewer.getTable().getItemCount();
		for (int i = 0; i < itemCount; i++) {
			ScriptGeneratorAction action = (ScriptGeneratorAction) viewer.getElementAt(i);
			if (action != null && !newActions.contains(action)) {
				elementsToRemove.add(action);
			}
		}
		viewer.remove(elementsToRemove.toArray());
	}
	
	/**
	 * Update the values of the action at the given index if they are different to the current display,
	 *  or if the action doesn't exist add it.
	 * 
	 * @param action The action to update or add.
	 * @param index The index of the action.
	 * @param columns The columns of the table to compare the action to the current display against.
	 */
	private void updateAction(ScriptGeneratorAction action, int index, TableColumn[] columns) {
		ScriptGeneratorAction tableAction = (ScriptGeneratorAction) viewer.getElementAt(index);
		if (0 <= index && index < viewer.getTable().getItemCount()) {
			TableItem item = viewer.getTable().getItem(index);
			if (tableAction == null || actionChanged(tableAction, action) || valuesDiffer(item, columns, tableAction)
					|| Integer.valueOf(index).toString() != item.getText(0)) {
				viewer.replace(action, index);
			}
		} else {
			viewer.add(action);
		}
	}
	
	/**
	 * Update the actions being displayed by this table.
	 * 
	 * @param newActions The actions to display.
	 */
	public void updateActions(List<ScriptGeneratorAction> newActions) {
		removeDeletedRows(newActions);
		var columns = viewer.getTable().getColumns();
		for (int i = 0; i < newActions.size(); i++) {
			updateAction(newActions.get(i), i, columns);
		}
	}
	
	/**
	 * Sets focus of cell.
	 * @param row row number of table
	 * @param column column number of table
	 */
	public void setCellFocus(int row, int column) {
		var element = viewer.getElementAt(row);
		if (row >= 0 && element != null) {
			viewer.editElement(element, column);
		}
	}
	
	/**
	 * Sets the dynamic non editable columns on the right.
	 * @param num The number of custom outputs
	 */
	public void setDynamicNonEditableColumnsOnRight(int num) {
		dynamicNonEditableColumnsOnRight = num;
	}
}
