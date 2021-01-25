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
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.databinding.observable.list.WritableList;
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
	private static final String TAB = "\t";
	private static final String NEW_LINE = "\r\n";
	private final ScriptGeneratorViewModel scriptGeneratorViewModel;
	private boolean shiftCellFocusToNewlyAddedRow = false;
	private static final  Integer NON_EDITABLE_COLUMNS_ON_RIGHT = 2;
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
		
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {	 
				if (e.character == CTRL_C) {	
					 scriptGeneratorViewModel.copyActions(getSelectedTableData());
			
				} else if (e.character == CTRL_V) {
					scriptGeneratorViewModel.pasteActions(getColumnsLabel(), table.getSelectionIndex());
				}
			}
		});
		
    }
	
	/**
	 * Get labels from table column in order.
	 * @return list of labels from the table
	 */
	protected ArrayList<String> getColumnsLabel() {
		ArrayList<String> values = new ArrayList<String>();
		TableColumn[] tableColumns = table.getColumns();
		for (int idx = 0; idx < tableColumns.length; idx++) {
			values.add(tableColumns[idx].getText());
		}
		return values;
	}
	
    /**
     * Format String data such that copying and pasting into excel would work. Clipboard does not support
     * transferring/pasting data to excel so we format plain text data ourselves.
     * @return table rows data as one string, tab separated. After end of each row, new line is added. "1\t2\r\n1\t2 "
     */
    public String getSelectedTableData() {
    	String data = "";
    	for (TableItem item : table.getSelection()) {
			int size = ((ScriptGeneratorAction) item.getData()).getSize();
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
					
					// Add new action if tab is pressed by user in the last cell of the table.
					if (nextCell.getNeighbor(ViewerCell.BELOW, false).getElement() == null
					        && (viewer.getTable().getColumnCount() - NON_EDITABLE_COLUMNS_ON_RIGHT == currentlyFocusedColumn)) {
					    
                    	scriptGeneratorViewModel.addEmptyAction();
                    	shiftCellFocusToNewlyAddedRow = true;
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
	
	/**
	 * Sets Rows. Save where the focus was before re writing the table and set the focus back to the cell after
	 * re writing the table.
	 */
	
	@Override
	public void setRows(Collection<ScriptGeneratorAction> rows) {
		if (!viewer.getTable().isDisposed()) {
			int focusRow = getSelectionIndex();
			ScriptGeneratorAction previousSelection = firstSelectedRow();
			int focusColumn = NON_EDITABLE_COLUMNS_ON_LEFT;
			
			if (shiftCellFocusToNewlyAddedRow) {
				focusRow = viewer.getTable().getSelectionIndex() + 1;
			} else if (focusRow != -1) {
				// When no focus is selected getFocusCell returns null
				var focusCell = Optional.ofNullable(viewer.getColumnViewerEditor().getFocusCell());
				if (focusCell.isPresent()) {
					focusColumn = focusCell.get().getColumnIndex();
				}
			}
			
			viewer.setInput(new WritableList<ScriptGeneratorAction>(rows, null));
			
			if (selectedRows().size() == 1) {
				// If the action on the specified row has changed then don't return focus to it
				if (previousSelection.equals((ScriptGeneratorAction) viewer.getElementAt(focusRow)) || shiftCellFocusToNewlyAddedRow) {
					setCellFocus(focusRow, focusColumn);
					if (!shiftCellFocusToNewlyAddedRow) {
					    // Fixes issue see in https://github.com/ISISComputingGroup/IBEX/issues/5708 (hopefully temporary)
					    editingSupports.get(focusColumn).resetSelectionAfterFocus();
					}
					shiftCellFocusToNewlyAddedRow = false;
				}
			}
		}
	} 
	
	/**
	 * Sets focus of cell.
	 * @param row row number of table
	 * @param column column number of table
	 */
	public void setCellFocus(int row, int column) {
		if (row >= 0) {
			viewer.editElement(viewer.getElementAt(row), column);
		}
	}
	
}
