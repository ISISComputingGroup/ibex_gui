
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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

package uk.ac.stfc.isis.ibex.ui.journalviewer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * The Class Search Control to allow searching within the log.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class FilterControl extends Composite {

    private ArrayList<Composite> cmpFilters = new ArrayList<Composite>();
    private Text txtSearchRBNumber;
    private Composite cmpSearch;
    private Composite cmpTitle;
    private Composite cmpRBNumber;
    private Composite cmpUsers;
    private static StackLayout stackSearch = new StackLayout();

    private static final int SEARCH_BOX_WIDTH = 410;
    private static final int RUN_NUMBER_MAX_VALUE = 99999999;

    private static final Display DISPLAY = Display.getCurrent();
    private Text txtSearchTitle;
    private Text txtSearchUsers;

    private Spinner spinnerFromNumber, spinnerToNumber;
    private Button chkNumberFrom, chkNumberTo, chkTimeFrom, chkTimeTo;
    private DateTime dtFromDate, dtFromTime, dtToDate, dtToTime;

    private Combo cmbFilterType;

    /**
     * Instantiates a new filter control.
     *
     * @param parent
     *            the parent in which this control resides
     */
    public FilterControl(Composite parent) {
        super(parent, SWT.NONE);
        Composite grpFilter = new Composite(parent, SWT.NONE);
        grpFilter.setLayout(null);

        Label lblSearch = new Label(grpFilter, SWT.NONE);
        lblSearch.setBounds(5, 14, 64, 15);
        lblSearch.setText("Search type:");

        cmbFilterType = new Combo(grpFilter, SWT.READ_ONLY);
        cmbFilterType.setBounds(74, 10, 97, 23);

        cmbFilterType.setItems(new String[] {"Run number", "Title", "Start time", "RB number", "Users"});
        cmbFilterType.select(0);

        cmpSearch = new Composite(grpFilter, SWT.NONE);
        cmpSearch.setBounds(176, 5, 435, 34);
        cmpSearch.setLayout(stackSearch);

        Composite cmpRunNumber = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpRunNumber);
        GridLayout glCmpRunNumber = new GridLayout(5, false);
        glCmpRunNumber.marginLeft = 90;
        cmpRunNumber.setLayout(glCmpRunNumber);

        chkNumberFrom = new Button(cmpRunNumber, SWT.CHECK);
        chkNumberFrom.setText("From");

        spinnerFromNumber = new Spinner(cmpRunNumber, SWT.BORDER);
        spinnerFromNumber.setMaximum(RUN_NUMBER_MAX_VALUE);
        GridData gdSpinnerFromNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdSpinnerFromNumber.widthHint = 30;
        spinnerFromNumber.setLayoutData(gdSpinnerFromNumber);
        spinnerFromNumber.setEnabled(false);

        Label lblSpacing = new Label(cmpRunNumber, SWT.NONE);
        lblSpacing.setText("     ");

        chkNumberTo = new Button(cmpRunNumber, SWT.CHECK);
        chkNumberTo.setText("To");

        spinnerToNumber = new Spinner(cmpRunNumber, SWT.BORDER);
        spinnerToNumber.setMaximum(RUN_NUMBER_MAX_VALUE);
        GridData gdSpinnerToNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdSpinnerToNumber.widthHint = 30;
        spinnerToNumber.setLayoutData(gdSpinnerToNumber);
        spinnerToNumber.setEnabled(false);

        cmpTitle = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpTitle);
        cmpTitle.setLayout(new GridLayout(1, false));

        txtSearchTitle = new Text(cmpTitle, SWT.BORDER);
        GridData gdTxtSearchTitle = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdTxtSearchTitle.widthHint = SEARCH_BOX_WIDTH;
        txtSearchTitle.setLayoutData(gdTxtSearchTitle);

        Composite cmpTimePicker = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpTimePicker);
        cmpTimePicker.setLayout(new GridLayout(7, false));

        chkTimeFrom = new Button(cmpTimePicker, SWT.CHECK);
        chkTimeFrom.setText("From");

        dtFromDate = new DateTime(cmpTimePicker, SWT.BORDER);
        dtFromDate.setEnabled(false);

        dtFromTime = new DateTime(cmpTimePicker, SWT.BORDER | SWT.TIME);
        dtFromTime.setEnabled(false);

        Label lblSpacing1 = new Label(cmpTimePicker, SWT.NONE);
        lblSpacing1.setText("        ");

        chkTimeTo = new Button(cmpTimePicker, SWT.CHECK);
        chkTimeTo.setText("To");

        dtToDate = new DateTime(cmpTimePicker, SWT.BORDER);
        dtToDate.setEnabled(false);

        dtToTime = new DateTime(cmpTimePicker, SWT.BORDER | SWT.TIME);
        dtToTime.setEnabled(false);

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

        cmpRBNumber = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpRBNumber);
        cmpRBNumber.setLayout(new GridLayout(1, false));

        txtSearchRBNumber = new Text(cmpRBNumber, SWT.BORDER);
        GridData gdTxtSearchRBNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdTxtSearchRBNumber.widthHint = SEARCH_BOX_WIDTH;
        txtSearchRBNumber.setLayoutData(gdTxtSearchRBNumber);

        stackSearch.topControl = cmpRunNumber;

        cmpUsers = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpUsers);
        cmpUsers.setLayout(new GridLayout(1, false));

        txtSearchUsers = new Text(cmpUsers, SWT.BORDER);
        GridData gdTxtSearchUsers = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdTxtSearchUsers.widthHint = SEARCH_BOX_WIDTH;
        txtSearchUsers.setLayoutData(gdTxtSearchUsers);

        cmpSearch.setTabList(new Control[] {cmpRunNumber, cmpTitle, cmpTimePicker, cmpRBNumber, cmpUsers});

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

    /**
     * @return The current selection index of the filter type drop-down menu.
     */
    public int getCmbFilterTypeIndex() {
        return cmbFilterType.getSelectionIndex();
    }

    /**
     * @return The text in the search box of the currently active filter type.
     */
    public String getActiveSearchText() {
        if (stackSearch.topControl == cmpTitle) {
            return txtSearchTitle.getText();
        } else if (stackSearch.topControl == cmpRBNumber) {
            return txtSearchRBNumber.getText();
        } else if (stackSearch.topControl == cmpUsers) {
            return txtSearchUsers.getText();
        } else {
            return null;
        }
    }

    /**
     * @return The number in the from run number spinner, (null if inactive).
     */
    public Integer getRunNumberFrom() {
        return chkNumberFrom.getSelection() ? spinnerFromNumber.getSelection() : null;
    }

    /**
     * @return The number in the to run number spinner, (null if inactive).
     */
    public Integer getRunNumberTo() {
        return chkNumberTo.getSelection() ? spinnerToNumber.getSelection() : null;
    }

    /**
     * @return A Calendar representing the start time to search from, (null if inactive).
     */
    public Calendar getStartTimeFrom() {
        return chkTimeFrom.getSelection() ? new GregorianCalendar(dtFromDate.getYear(), dtFromDate.getMonth(),
                dtFromDate.getDay(), dtFromTime.getHours(), dtFromTime.getMinutes(), dtFromTime.getSeconds()) : null;
    }

    /**
     * @return A Calendar representing the start time to search to, (null if inactive).
     */
    public Calendar getStartTimeTo() {
        return chkTimeTo.getSelection() ? new GregorianCalendar(dtToDate.getYear(), dtToDate.getMonth(),
                dtToDate.getDay(), dtToTime.getHours(), dtToTime.getMinutes(), dtToTime.getSeconds()) : null;
    }
}
