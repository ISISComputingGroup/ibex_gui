
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.mainmenu.instrument;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

/**
 * 
 */
public class InstrumentTable extends Composite {
    private static final int TABLE_HEIGHT = 100;

    private Table table;
    private Collection<InstrumentInfo> instruments;

    public InstrumentTable(Composite parent, int style, int tableStyle, Collection<InstrumentInfo> instruments) {
        super(parent, SWT.NONE);

        this.instruments = instruments;

        GridLayout compositeLayout = new GridLayout(1, false);
        compositeLayout.marginHeight = 0;
        compositeLayout.marginWidth = 0;
        setLayout(compositeLayout);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        createControls(this, tableStyle);
        setRows(this.instruments);
    }

    public void addSelectionListener(SelectionListener listener) {
        table.addSelectionListener(listener);
    }

    public void removeSelectionListener(SelectionListener listener) {
        table.removeSelectionListener(listener);
    }

    public InstrumentInfo getSelectedInstrument() {
        String selectedInstrumentName = table.getItem(table.getSelectionIndex()).getText(0);
        return getInstrumentFromName(selectedInstrumentName);
    }

    private void createControls(Composite parent, int tableStyle) {
        Composite controlComposite = new Composite(parent, SWT.NONE);
        GridLayout glControlComposite = new GridLayout(1, false);
        glControlComposite.marginHeight = 0;
        glControlComposite.marginWidth = 0;
        controlComposite.setLayout(glControlComposite);
        controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        table = new Table(controlComposite, tableStyle);// , SWT.BORDER |
                                                   // SWT.FULL_SELECTION);
        GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gdTable.minimumHeight = TABLE_HEIGHT;
        table.setLayoutData(gdTable);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn nameColumn = new TableColumn(table, SWT.NULL);
        nameColumn.setText("Name");
    }

    private void setRows(Collection<InstrumentInfo> instruments) {
        table.removeAll();

        for (InstrumentInfo instrument : instruments) {
            TableItem item = new TableItem(table, SWT.NULL);
            item.setText(0, instrument.name());
        }

        table.setEnabled(instruments.size() > 0);
        table.getColumn(0).pack();
    }

    private InstrumentInfo getInstrumentFromName(String name) {
        for (InstrumentInfo instrument : instruments) {
            if (name.equals(instrument.name())) {
                return instrument;
            }
        }

        return new InstrumentInfo("");
    }
}
