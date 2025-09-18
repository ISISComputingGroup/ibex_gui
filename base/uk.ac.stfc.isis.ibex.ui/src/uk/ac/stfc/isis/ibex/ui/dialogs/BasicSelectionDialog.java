
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * Generic selection dialog class.
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class BasicSelectionDialog extends Dialog {

    private String title;
    
    /**
     * A single or multiple-column table for displaying the list of items to select from.
     */
    public Table items;

    /**
     * @param parentShell
     *            The shell to open the dialog from.
     * @param title
     *            The title of the dialog box.
     **/
    protected BasicSelectionDialog(Shell parentShell, String title) {
        super(parentShell);
        this.title = title;
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(title);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(450, 300); 
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        createSelection(container);
        
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                okPressed();
            }
        };
        
        SelectionAdapter selectionAdapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                getButton(IDialogConstants.OK_ID).setEnabled(items.getSelection().length != 0);
            }
        };
        
        items.addMouseListener(mouseAdapter);

        items.addSelectionListener(selectionAdapter);
        
        items.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                items.removeMouseListener(mouseAdapter);
                items.removeSelectionListener(selectionAdapter);
            }
            
        });

        return container;
    }

    @Override
    protected Control createButtonBar(Composite parent) {
        Control control = super.createButtonBar(parent);

        // Selection starts as null so disable ok button
        getButton(IDialogConstants.OK_ID).setEnabled(false);

        return control;
    }

    /**
     * Converts the items in a table into a list of strings.
     * 
     * @param items
     *            The table items
     * @return The items as a list of strings
     */
    protected Collection<String> asString(TableItem[] items) {
        Collection<String> converted = new ArrayList<String>();
        for (TableItem item : items) {
            converted.add(item.getText());
        }
        return converted;
    }

    /**
     * Abstract method for creating the selection dialog.
     * 
     * @param container
     *            The parent container
     */
    protected abstract void createSelection(Composite container);

    /**
     * Abstract method that creates and returns a table pre-configured with
     * a single or multiple column layout.
     * 
     * @param parent
     *            The parent composite
     * @param style
     *            The style settings
     * @param columnNames
     * 			  The names (and number) of table columns
     * @return The table object
     */
    protected abstract Table createTable(Composite parent, int style, List<String> columnNames);

    /**
     * Sets a list of names as items in the selection table specifically for deleting config dialog.
     * 
     * @param names
     *            The names
     *            
     * @param configNamesWithFlags
     *            Names of the configuration and its protection flag
     */
    protected void setItems(List<String> names, Map<String, Boolean> configNamesWithFlags) {
        this.items.clearAll();
        for (String name : names) {
            TableItem item = new TableItem(this.items, SWT.NONE);
            item.setText(name);
            if (configNamesWithFlags.get(name)) {
                item.setImage(JFaceResources.
                        getImage(DLG_IMG_MESSAGE_WARNING));   
            }
        }
    }
    
    /**
     * Sets a list of names as items in the selection table.
     * 
     * @param names
     *            The names
     */
    protected void setItems(List<String> names) {
        this.items.clearAll();
        for (String name : names) {
            TableItem item = new TableItem(this.items, SWT.NONE);
            item.setText(name);
        }
    }
    
    /**
    * Sets a list of configuration names and their description as items in a
    * selection table with two columns.
    * 
    * @param namesAndDescriptions
    *             A map containing the names and descriptions of the recent configurations in key/value pairs.      
    * @param configNamesWithFlags
    *             Names of the configuration and its protection flag
    */
   protected void setMultipleColumnItems(SortedMap<String, String> namesAndDescriptions, Map<String, Boolean> configNamesWithFlags) {
       this.items.clearAll();
       String[] columns = new String[2];

       for (Entry<String, String> entry : namesAndDescriptions.entrySet()) {
           TableItem item = new TableItem(this.items, SWT.NONE);
           columns[0] = entry.getKey();
           columns[1] = entry.getValue();
           
           item.setText(columns);
           if (configNamesWithFlags.get(entry.getKey())) {
               item.setImage(JFaceResources.
                       getImage(DLG_IMG_MESSAGE_WARNING));   
           }
       }
   }
}
