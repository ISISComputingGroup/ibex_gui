
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

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Generic selection dialog class.
 */
public abstract class SelectionDialog extends BasicSelectionDialog {

    /**
     * @param parentShell
     *            The shell to open the dialog from.
     * @param title
     *            The title of the dialog box.
     **/
    protected SelectionDialog(Shell parentShell, String title) {
        super(parentShell, title);
    }
    
    /**
     * Creates and returns a table pre-configured with a two columned layout.
     * 
     * @param parent
     *            The parent composite
     * @param style
     *            The style settings
     * @return The table object
     */
    @Override
    protected Table createTable(Composite parent, int style) {
	    Composite tableComposite = new Composite(parent, SWT.NONE);
	    tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    Table table = new Table(tableComposite, style);
	    table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	    TableColumnLayout tableColumnLayout = new TableColumnLayout();

	    TableColumn firstColumn = new TableColumn(table, SWT.NONE);
        tableColumnLayout.setColumnData(firstColumn, new ColumnWeightData(1));
        firstColumn.setText("Name");
        firstColumn.setResizable(true);
        firstColumn.setMoveable(true);
        firstColumn.setToolTipText("Name of the configuration");

        TableColumn secondColumn = new TableColumn(table, SWT.NONE);
        tableColumnLayout.setColumnData(secondColumn, new ColumnWeightData(1));
        secondColumn.setText("Description");
        secondColumn.setResizable(true);
        secondColumn.setMoveable(true);
        secondColumn.setToolTipText("Description of the configuration");
       
        tableComposite.setLayout(tableColumnLayout);

        table.setHeaderVisible(true);
        Display display = table.getDisplay();
        table.setHeaderBackground(display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));

        return table;

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
   protected boolean isResizable() {
    	return true;
   }
}
