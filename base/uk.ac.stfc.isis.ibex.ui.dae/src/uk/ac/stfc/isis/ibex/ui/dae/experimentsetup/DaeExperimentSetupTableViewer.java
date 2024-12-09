/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/**
 * A table view that will highlight its cells if the value contained has not been applied to the instrument.
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber"})
public class DaeExperimentSetupTableViewer extends TableViewer {
    
    private PanelViewModel panelViewModel;
    private ArrayList<String> cachedValues = new ArrayList<String>();
    private String name;
    private ExperimentSetupViewModel experimentSetupViewModel;
    
    private int x = 0;
    private int y = 0;
    private int width = 50;
    private int height = 20;
    private int smallXIncrement = 5;
    private int headerHeight = 25;
    private int xStart = 9;
    private int yStart = 30;
    private ViewerCell recordedCell;
    private String recordedValue = "";
    private int recordedRowIndexCoefficient;
    private MouseListener mouseListener;
    private KeyListener keyboardListener;
    private Table table;
    
    /**
     * A table view that will highlight its cells if the value contained has not been applied to the instrument.
     * 
     * @param parent a composite control which will be the parent of the new instance (cannot be null)
     * @param style the style of control to construct
     * @param panelViewModel the panelViewModel to help with the editing the panels.
     * @param name the name of the widget, which is used as a key in the cached values map.
     */
    public DaeExperimentSetupTableViewer(Composite parent, int style, PanelViewModel panelViewModel, String name) {
        super(parent, style);
        setPanelViewModel(panelViewModel);
        setName(name);
        table = getTable();
        setExperimentSetupViewModel(panelViewModel.getExperimentSetupViewModel());
        
        addTableListeners();
    }
    
    /**
     * Adds a listener for a table that will label an unique cell when it is changed.
     */
    public void addTableListeners() {
        mouseListener = new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent event) {
                if (!table.isDisposed()) {
                    if (cachedValues.isEmpty()) {
                        resetCachedValue();
                    }
                    Point pt = new Point(event.x, event.y);
                    ViewerCell cell = getCell(pt);
                    int rowIndex = (int) Math.floor((event.y - headerHeight) / height);
                    tryToChangeBackgroundOfCell();
                    if (cell != null) {
                        setRecordedCell(cell);
                        setRecordedCellValue(cell.getText());
                        setRecordedRowIndexCoefficient(rowIndex * table.getColumnCount());
                    }
                }
            }
        };
        
        keyboardListener = new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent event) {
                tryToChangeBackgroundOfCell();
            }
            
        };

        if (table != null) {
            if (mouseListener != null && keyboardListener != null) {
                table.addMouseListener(mouseListener);
                table.addKeyListener(keyboardListener);
            }
        }
    }
    
    /**
     * Removes all mouse and keyboard listeners on the given table.
     * 
     */
    public void removeTableListeners() {
        table.removeMouseListener(mouseListener);
        table.removeKeyListener(keyboardListener);
    }
    
    /**
     * A method that tries to change the background of a recorded cell. If the change happens it also notifies the daeViewModel.
     * 
     */
    public void tryToChangeBackgroundOfCell() {
        tryToChangeBackgroundWhenRecordedValueAndCellAreNotEmpty();
    }
    
    /**
     * Checks that the recorded value and cell are not empty.
     */
    private void tryToChangeBackgroundWhenRecordedValueAndCellAreNotEmpty() {
        if (!recordedValue.isEmpty() && recordedCell != null) {
            tryToChangeBackgroundWhenTextChanged();
        }
    }

    /**
     * Checks that the text in the cell has been changed and that it is not the same as the cached values.
     */
    private void tryToChangeBackgroundWhenTextChanged() {
        if (!recordedValue.equals(recordedCell.getText())) {
            ifCellValueDifferentFromCachedValueThenChangeLabel(recordedCell, recordedRowIndexCoefficient);
        }
    }
    
    
    /**
     * Will set a label denoting a change that has not been applied to the instrument and notifies the dae view model.
     * 
     * @param cell
     *              The cell on which to make the change.
     * @param rowIndexCoefficient
     *              The index for the cell.
     */
    private void ifCellValueDifferentFromCachedValueThenChangeLabel(ViewerCell cell, int rowIndexCoefficient) {
        String cachedValue = cachedValues.get(cell.getVisualIndex() + rowIndexCoefficient);
        
        if (cachedValue.equals(cell.getText())) {
            cell.setBackground(panelViewModel.getColour("white"));
            panelViewModel.setIsChanged(name + " cell " + (cell.getVisualIndex() + rowIndexCoefficient), false);
        } else {
            cell.setBackground(panelViewModel.getColour("changedColour"));
            panelViewModel.setIsChanged(name + " cell " + (cell.getVisualIndex() + rowIndexCoefficient), true);
        }
    }

    /**
     * Sets a cell to be remembered.
     * 
     * @param recordedCell
     *                      The cell to be remembered
     */
    public void setRecordedCell(ViewerCell recordedCell) {
        this.recordedCell = recordedCell;
    }
    
    /**
     * Sets a cell value to be remembered.
     * 
     * @param recordedValue
     *                      The cell value to be remembered
     */
    public void setRecordedCellValue(String recordedValue) {
        this.recordedValue = recordedValue;
    }
    
    /**
     * Sets a row index to be remembered.
     * 
     * @param recordedRowIndexCoefficient
     *                      The row index to be remembered
     */
    public void setRecordedRowIndexCoefficient(int recordedRowIndexCoefficient) {
        this.recordedRowIndexCoefficient = recordedRowIndexCoefficient;
    }
    
    /**
     * Allows to get a cell at the point defined by x and y.
     * @return
     *              The cell at x and y.
     */
    private ViewerCell getCellFromPoint() {
        Point cellPoint = new Point(x, y);
        return getCell(cellPoint);
    }
    
    /**
     * Finds the next cell in a table, depending on whether the next cell is in the same row or not.
     * 
     * @param cell
     *              The cell from which the next cell is found.
     * @param inSameRow
     *              True if the next cell is in the same row.
     * @return
     *              The next cell.
     */
    private ViewerCell findNextCell(ViewerCell cell, boolean inSameRow) {
        ViewerCell secondCell;
        if (inSameRow) {
            cell = getCellFromPoint();
            secondCell = cell;
            while (secondCell.equals(cell)) {
                x += width;
                secondCell = getCellFromPoint();
                if (secondCell == null) {
                    x += smallXIncrement;
                    secondCell = getCellFromPoint();
                }
            }
        } else {
            x = xStart;
            cell = getCellFromPoint();
            secondCell = cell;
            while (secondCell.equals(cell)) {
                y += height;
                secondCell = getCellFromPoint();
                if (secondCell == null) {
                    break;
                }
            }
        }
        return secondCell;
    }
    
    /**
     * Checks for each cell if the value in the cell is equal to the cached value for the cell. If so, then changes the colored label.
     */
    public void ifTableValuesDifferentFromCachedValuesThenChangeLabels() {
        if (table != null) {
            x = xStart;
            y = yStart;
            ViewerCell cell = getCellFromPoint();
            int rowIndex = (int) Math.floor((y - headerHeight) / height);
            while (cell != null) {
                ifCellValueDifferentFromCachedValueThenChangeLabel(cell, rowIndex * table.getColumnCount());
                for (int i = 0; i < table.getColumnCount() - 1; i++) {
                    cell = findNextCell(cell, true);
                    ifCellValueDifferentFromCachedValueThenChangeLabel(cell, rowIndex * table.getColumnCount());
                }
                cell = findNextCell(cell, false);
                rowIndex++;
            }
        }
    }
    
    /**
     * Creates a cache of the applied values for a table widget.
     * @param cellValues
     *              The array containing the cached table values.
     * @return
     *              The cached values.
     */
    public ArrayList<String> createArrayOfTableValues(ArrayList<String> cellValues) {
        if (!cellValues.isEmpty()) {
            cellValues.clear();
        }
        if (table != null) {
            x = xStart;
            y = yStart;
            ViewerCell cell = getCellFromPoint();
            while (cell != null) {
                cellValues.add(cell.getText());
                for (int i = 0; i < table.getColumnCount() - 1; i++) {
                    cell = findNextCell(cell, true);
                    cellValues.add(cell.getText());
                }
                cell = findNextCell(cell, false);
            }
        }
        return cellValues;
    }
    
    /**
     * Creates an array of cached cell values.
     */
    public void resetCachedValue() {
        cachedValues = createArrayOfTableValues(cachedValues);
        experimentSetupViewModel.addtoTableCachedValues(name, cachedValues);
    }
    
    /** 
     * Creates a cached value for the first time, used after the panels are created when they have first been initialised.
     */
    public void createInitialCachedValue() {
        if (experimentSetupViewModel.getItemFromTableCachedValues(name).isEmpty()) {
            resetCachedValue();
        }  else {
            cachedValues = experimentSetupViewModel.getItemFromTableCachedValues(name);
            ifTableValuesDifferentFromCachedValuesThenChangeLabels();
        }
    }
    
    /**
     * Allows to set the name of the widget.
     * 
     * @param name
     *              The new name for the widget.
     */
    public void setName(String name) {
        this.name = name;
    }
    

    /**
     * Allows to set the experimentSetupViewModel of the widget.
     * 
     * @param experimentSetupViewModel
     *                                  The new experimentSetupViewModel.
     */
    public void setExperimentSetupViewModel(ExperimentSetupViewModel experimentSetupViewModel) {
        this.experimentSetupViewModel = experimentSetupViewModel;
    }
    
    /**
     * Allows to set the panelViewModel of the widget.
     * 
     * @param panelViewModel
     *                                  The new panelViewModel.
     */
    public void setPanelViewModel(PanelViewModel panelViewModel) {
        this.panelViewModel = panelViewModel;
    }
    
}
