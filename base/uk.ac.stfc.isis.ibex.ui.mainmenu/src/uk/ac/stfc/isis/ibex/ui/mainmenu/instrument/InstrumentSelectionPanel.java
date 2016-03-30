
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

package uk.ac.stfc.isis.ibex.ui.mainmenu.instrument;

import java.util.Collection;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

public class InstrumentSelectionPanel extends Composite {

    private Text txtInstrument;
    private InstrumentTable instrumentTable;
    private Collection<InstrumentInfo> instruments;

    public InstrumentSelectionPanel(Composite parent, int style, Collection<InstrumentInfo> instruments) {
        super(parent, style);

        this.instruments = instruments;

        setLayout(new FillLayout(SWT.HORIZONTAL));

        Group grpInstrument = new Group(this, SWT.NONE);
        grpInstrument.setText("Instrument Selector");
        grpInstrument.setLayout(new GridLayout(3, false));

        createInstrumentLabel(grpInstrument);
        createInstrumentTextBox(grpInstrument);
        createClearButton(grpInstrument);
        createTable(grpInstrument);
    }

    public InstrumentInfo getSelected() {
        String selectedInstrumentName = txtInstrument.getText();

        if (selectedInstrumentName.isEmpty()) {
            return null;
        }

        for (InstrumentInfo instrument : instruments) {
            if (selectedInstrumentName.toUpperCase().equals(instrument.name().toUpperCase())) {
                return instrument;
            }
        }
            
        return new InstrumentInfo(selectedInstrumentName);
    }

    private void createInstrumentLabel(Composite parent) {
        Label lblInstrument = new Label(parent, SWT.NONE);
        lblInstrument.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblInstrument.setText("Instrument:");
    }

    private void createInstrumentTextBox(Composite parent) {
        txtInstrument = new Text(parent, SWT.BORDER);
        GridData gdInstrument = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdInstrument.widthHint = 130;
        txtInstrument.setLayoutData(gdInstrument);
        txtInstrument.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent arg0) {
                instrumentTable.setSearch(txtInstrument.getText());
            }
        });
    }

    private void createClearButton(Composite parent) {
        final Button btnClear = new Button(parent, SWT.NONE);
        btnClear.setText("Clear");
        Listener clearListener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.widget == btnClear) {
                    txtInstrument.setText("");
                }
            }
        };

        btnClear.addListener(SWT.Selection, clearListener);
    }

    private void createTable(Composite parent) {
        instrumentTable = new InstrumentTable(parent, SWT.NONE, SWT.V_SCROLL | SWT.NO_SCROLL | SWT.FULL_SELECTION,
                this.instruments);
        GridData gdInstrumentTable = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        gdInstrumentTable.heightHint = 300;
        instrumentTable.setLayoutData(gdInstrumentTable);
        instrumentTable.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (selection.size() > 0) {
                    InstrumentInfo instrument = (InstrumentInfo) selection.getFirstElement();
                    txtInstrument.setText(instrument.name());
                }
            }
        });
    }
}
