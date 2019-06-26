
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

package uk.ac.stfc.isis.ibex.ui.journalviewer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.journal.JournalRow;
import uk.ac.stfc.isis.ibex.ui.journalviewer.models.JournalViewModel;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import org.eclipse.swt.layout.RowLayout;


/**
 * The Class Search Control to allow searching within the log.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class FilterControl extends Composite {

    private Composite parent;

    private ArrayList<Composite> cmpFilters = new ArrayList<Composite>();
    private Text txtSearchRBNumber;
    private Composite cmpSearch;
    private static StackLayout stackSearch = new StackLayout();
    
    private static final int SEARCH_BOX_WIDTH = 410;
    
    private static final Display DISPLAY = Display.getCurrent();
    private Text txtSearchTitle;
    private Text txtSearchUsers;
    
    /**
     * Instantiates a new filter control.
     *
     * @param parent the parent in which this control resides
     */
    public FilterControl(Composite parent) {
        super(parent, SWT.NONE);        
        this.parent = parent;
        
        Composite grpFilter = new Composite(parent, SWT.NONE);
        grpFilter.setLayout(null);
        
        Label lblSearch = new Label(grpFilter, SWT.NONE);
        lblSearch.setBounds(5, 14, 64, 15);
        lblSearch.setText("Search type:");
        
        Combo cmbFilterType = new Combo(grpFilter, SWT.READ_ONLY);
        cmbFilterType.setBounds(74, 10, 97, 23);
        
        cmbFilterType.setItems(new String[] {"Run Number", "RB Number", "Title", "Users", "Start Time"});
        cmbFilterType.select(0);
        
        cmpSearch = new Composite(grpFilter, SWT.NONE);
        cmpSearch.setBounds(176, 5, 435, 34);
        cmpSearch.setLayout(stackSearch);
        
        Composite cmpRunNumber = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpRunNumber);
        GridLayout gl_cmpRunNumber = new GridLayout(5, false);
        gl_cmpRunNumber.marginLeft = 90;
        cmpRunNumber.setLayout(gl_cmpRunNumber);
        
        Button chkNumberFrom = new Button(cmpRunNumber, SWT.CHECK);
        chkNumberFrom.setText("From");
        
        Spinner spinnerFromNumber = new Spinner(cmpRunNumber, SWT.BORDER);
        GridData gd_spinnerFromNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_spinnerFromNumber.widthHint = 30;
        spinnerFromNumber.setLayoutData(gd_spinnerFromNumber);
        spinnerFromNumber.setEnabled(false);
        
        Label lblSpacing = new Label(cmpRunNumber, SWT.NONE);
        lblSpacing.setText("     ");
        
        Button chkNumberTo = new Button(cmpRunNumber, SWT.CHECK);
        chkNumberTo.setText("To");
        
        Spinner spinnerToNumber = new Spinner(cmpRunNumber, SWT.BORDER);
        GridData gd_spinnerToNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_spinnerToNumber.widthHint = 30;
        spinnerToNumber.setLayoutData(gd_spinnerToNumber);
        spinnerToNumber.setEnabled(false);
        
        Composite cmpRBNumber = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpRBNumber);
        cmpRBNumber.setLayout(new GridLayout(1, false));
        
        txtSearchRBNumber = new Text(cmpRBNumber, SWT.BORDER);
        GridData gdTxtSearchRBNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdTxtSearchRBNumber.widthHint = SEARCH_BOX_WIDTH;
        txtSearchRBNumber.setLayoutData(gdTxtSearchRBNumber);
        
        stackSearch.topControl = cmpRunNumber;
        
        Composite cmpTitle = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpTitle);
        cmpTitle.setLayout(new GridLayout(1, false));
        
        txtSearchTitle = new Text(cmpTitle, SWT.BORDER);
        GridData gdTxtSearchTitle = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdTxtSearchTitle.widthHint = SEARCH_BOX_WIDTH;
        txtSearchTitle.setLayoutData(gdTxtSearchTitle);
        
        Composite cmpUsers = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpUsers);
        cmpUsers.setLayout(new GridLayout(1, false));
        
        txtSearchUsers = new Text(cmpUsers, SWT.BORDER);
        GridData gdTxtSearchUsers = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdTxtSearchUsers.widthHint = SEARCH_BOX_WIDTH;
        txtSearchUsers.setLayoutData(gdTxtSearchUsers);
        
        Composite cmpTimePicker = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpTimePicker);
        cmpTimePicker.setLayout(new GridLayout(7, false));
        
        Button chkTimeFrom = new Button(cmpTimePicker, SWT.CHECK);
        chkTimeFrom.setText("From");
        
        DateTime dtFromDate = new DateTime(cmpTimePicker, SWT.BORDER);
        dtFromDate.setEnabled(false);
        
        DateTime dtFromTime = new DateTime(cmpTimePicker, SWT.BORDER | SWT.TIME);
        dtFromTime.setEnabled(false);
        
        Label lblSpacing1 = new Label(cmpTimePicker, SWT.NONE);
        lblSpacing1.setText("        ");
        
        Button chkTimeTo = new Button(cmpTimePicker, SWT.CHECK);
        chkTimeTo.setText("To");
        
        DateTime dtToDate = new DateTime(cmpTimePicker, SWT.BORDER);
        dtToDate.setEnabled(false);
        
        DateTime dtToTime = new DateTime(cmpTimePicker, SWT.BORDER | SWT.TIME);
        dtToTime.setEnabled(false);
        cmpSearch.setTabList(new Control[]{cmpRunNumber, cmpRBNumber, cmpTitle, cmpUsers, cmpTimePicker});
        
        chkTimeFrom.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dtFromDate.setEnabled(chkTimeFrom.getSelection());
                dtFromTime.setEnabled(chkTimeFrom.getSelection());
            }
        });
        
        chkTimeTo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dtToDate.setEnabled(chkTimeTo.getSelection());
                dtToTime.setEnabled(chkTimeTo.getSelection());
            }
        });
        
        chkNumberFrom.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                spinnerFromNumber.setEnabled(chkNumberFrom.getSelection());
            }
        });
        
        chkNumberTo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                spinnerToNumber.setEnabled(chkNumberTo.getSelection());
            }
        });
        
        cmbFilterType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                DISPLAY.asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        stackSearch.topControl = cmpFilters.get(cmbFilterType.getSelectionIndex());
                        cmpSearch.layout();
                    }
                });
            }
        });
    }
}