
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.ui.dae.DaeViewModel;


/**
 * A class containing a set of methods used in the DAE panels. 
 *
 */
public class PanelUtilities {

    private Color changed;
    private Color white;
    private Color unchanged;
    private DaeViewModel daeViewModel;
    ViewerCell recordedCell;
    private String recordedValue = "";
    private MouseListener mouseListener;
    private List<ViewerCell> changedCells = new ArrayList<ViewerCell>();
    
    /**
     * The constructor for the class.
     */
    public PanelUtilities(DaeViewModel daeViewModel, Display DISPLAY) {
        this.daeViewModel = daeViewModel;
        changed = DISPLAY.getSystemColor(SWT.COLOR_YELLOW);
        white = DISPLAY.getSystemColor(SWT.COLOR_WHITE);
        unchanged = new Color(DISPLAY, 240, 240, 240);
    }
    
    /**
     * Adds a listener to a spinner to colour it's background upon change to the observed property.
     *  
     * @param property
     *                  The property that is being observed. 
     * @param spinner
     *                  The spinner to which the listener is added.
     * @param viewModel
     *                  The viewModel in which the change is set.
     */
    public void addSpinnerPropertyChangeListenerDataAc(String property, Spinner spinner, ModelObject viewModel) {
        viewModel.addPropertyChangeListener(property, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                spinner.setBackground(changed);
                setIsChanged(true);
            }
        });
    }
    
    /**
     * Adds a listener to a button to colour its background upon selection of the button.
     * 
     * @param btn
     *              The button to which the listener is added.
     */
    public void addBtnSelectionListener(Button btn) {
        btn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                btn.setBackground(changed);
                setIsChanged(true);
            }
        });
    }
    
    /**
     * Adds a listener to a pair of radio buttons to colour their backgrounds upon selection of one of the radio buttons.
     * 
     * @param btn1
     *              The first button to which the listener is added.
     * @param btn2
     *              The second button to which the listener is added.
     */
    public void addRadioBtnSelectionListener(Button btn1, Button btn2) {
        SelectionListener listener = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (btn1.getSelection()) {
                    btn2.setBackground(unchanged);
                    btn1.setBackground(changed);
                    setIsChanged(true);
                } else {
                    btn1.setBackground(unchanged);
                    btn2.setBackground(changed);
                    setIsChanged(true);
                }
            }
        };
        btn1.addSelectionListener(listener);
        btn2.addSelectionListener(listener);
    }
    
    /**
     * Adds a listener to a text input widget to colour it's background upon change to the observed property.
     * 
     * @param property
     *                  The property being observed.
     * @param textInput
     *                  The text input to which the listener is added.
     * @param viewModel
     *                  The viewModel in which the change is made.
     */
    public void addTextInputPropertyChangeListener(String property, Text textInput, ModelObject viewModel) {
        viewModel.addPropertyChangeListener(property, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                textInput.setBackground(changed);
                setIsChanged(true);
            }
        });
    }
    
    /**
     * Adds a listener to a combo box to colour the background of the associated label upon change to the boxes' selection.
     * The associated label's background is coloured as colouring the combo box colours the selection inside it.
     * The label RB contains the item previously selected by the combo box and allows to check.
     * 
     * @param Selector
     *                  The combo box to which the listener is added.
     * @param label
     *                  The label to colour.
     * @param RB
     *                  The previous value selected in the combo box.
     */
    public void addSelectionListenersWithCurrent(Combo Selector, Composite panel, Label RB) {
        Selector.addSelectionListener(new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                if ((Selector.getText()).equals(RB.getText())) {
                    panel.setBackground(unchanged);
                    setIsChanged(false);
                } else {
                    panel.setBackground(changed);
                    setIsChanged(true);
                }
            }
        });
    }
    
    /**
     * Adds a listener to a combo box to colour the background of the associated label upon change to the boxes' selection.
     * The associated label's background is coloured as colouring the combo box colours the selection inside it.
     * 
     * @param Selector
     *                  The combo box to which the listener is added.
     * @param label
     *                  The label to colour.
     */
    public void addSelectionListenersWithoutCurrent(Combo Selector, Composite panel) {
        Selector.addSelectionListener(new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                    panel.setBackground(changed);
                    setIsChanged(true);
            }
        });
    }
    
    public void addTableListener(Table table, TableViewer viewer) {
       mouseListener = new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent event) {
                if (!table.isDisposed()) {
                    Point pt = new Point(event.x, event.y);
                    ViewerCell cell = viewer.getCell(pt);
                    
                    tryToChangeBackgroundOfCell();
                    if (cell != null) {
                        setRecordedCell(cell);
                        setRecordedCellValue(cell.getText());
                    }
                }
            }

            @Override
            public void mouseUp(MouseEvent e) {
            }
        };

        if(table != null && mouseListener != null) {
            table.addMouseListener(mouseListener);
        }
    }
    
    /**
     * A method that tries to change the background of a recorded cell. If the change happens it also notifies the daeViewModel.
     * 
     */
    public void tryToChangeBackgroundOfCell() {
        checkThatRecordedValueIsNotEmpty();
    }

    private void checkThatRecordedValueIsNotEmpty() {
        if (!recordedValue.isEmpty()) {
            checkThatRecordedCellIsNotNull();
        }
    }

    private void checkThatRecordedCellIsNotNull() {
        if (recordedCell != null) {
            checkThatTextHasChanged();
        }
    }

    private void checkThatTextHasChanged() {
        if (!recordedValue.equals(recordedCell.getText())) {
            recordedCell.setBackground(changed);
            setIsChanged(true);
            addToChangedCells(recordedCell);
        }
    }

    /**
     * Forgets about the recorded cell and its value.
     * This is used once the changes have been applied.
     */
    public void unlabelCells() {
        for (ViewerCell cell : changedCells) {
            cell.setBackground(white);
        }
        changedCells.clear();
        setRecordedCell(null);
        setRecordedCellValue("");
    }

    public void setRecordedCell(ViewerCell recordedCell) {
        this.recordedCell = recordedCell;
    }

    public void setRecordedCellValue(String recordedValue) {
        this.recordedValue = recordedValue;
    }

    /**
     * Returns a colour depending on which one is asked for.
     * 
     * @param isChanged
     *                  True to get the "changed" colour, false to get the "unchanged" colour.
     * @return
     *                  The "changed" or the "unchanged" colour.
     */
    public Color getColour(boolean isChanged) {
        if (isChanged) {
            return changed;
        } else {
            return unchanged;
        }
    }
    
    /**
     * Returns the colour white.
     * 
     * @return
     *          The colour white.
     */
    public Color getWhite() {
        return white;
    }

    /**
     * Allows to change the value that denotes whether or not changes have been made but not applied.
     * 
     * @param isChanged
     *                  True is changes have been made but not applied.
     */
    public void setIsChanged(boolean isChanged) {
        daeViewModel.setIsChanged(isChanged);
    }
    
    private void addToChangedCells(ViewerCell cell) {
        changedCells.add(cell);
    }

}
