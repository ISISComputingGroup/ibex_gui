
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.journal.JournalSearch;
import uk.ac.stfc.isis.ibex.journal.JournalSearchCalendar;
import uk.ac.stfc.isis.ibex.journal.JournalSearchWidgetType;
import uk.ac.stfc.isis.ibex.ui.journalviewer.models.JournalViewModel;

/**
 * The Class Search Control to allow searching within the log.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SearchInput extends Composite {

    private Composite cmpSearch;
    private Composite cmpTxtSearch;
    private static StackLayout stackSearch = new StackLayout();

    private static final int SEARCH_BOX_WIDTH = 410;
    private static final int RUN_NUMBER_MAX_VALUE = 99999999;

    private static final Display DISPLAY = Display.getCurrent();
    private Text txtSearch;

    private Spinner spinnerFromNumber, spinnerToNumber;
    private Button chkNumberFrom, chkNumberTo, chkTimeFrom, chkTimeTo;
    private DateTime dtFromDate, dtFromTime, dtToDate, dtToTime;

    private Combo cmbFilterType;
    private JournalViewModel model;

    /**
     * Instantiates a new filter control.
     *
     * @param parent
     *            the parent in which this control resides
     * @param model
     *            the view model
     */
    public SearchInput(Composite parent, JournalViewModel model) {
        super(parent, SWT.NONE);
        Composite grpFilter = new Composite(parent, SWT.NONE);
        grpFilter.setLayout(null);
        this.model = model;
        
        Map<JournalSearchWidgetType, Composite> map = new HashMap<>();

        Label lblSearch = new Label(grpFilter, SWT.NONE);
        lblSearch.setBounds(5, 14, 64, 15);
        lblSearch.setText("Search type:");

        cmbFilterType = new Combo(grpFilter, SWT.READ_ONLY);
        cmbFilterType.setBounds(74, 10, 97, 23);

        cmbFilterType.setItems(model.getSearches().stream().map(JournalSearch::getField).map(JournalField::getFriendlyName).toArray(String[]::new));
        cmbFilterType.select(0);

        cmpSearch = new Composite(grpFilter, SWT.NONE);
        cmpSearch.setBounds(176, 5, 435, 34);
        cmpSearch.setLayout(stackSearch);

        Composite cmpIntSearch = new Composite(cmpSearch, SWT.NONE);
        map.put(JournalSearchWidgetType.INTEGER_SEARCH, cmpIntSearch);
        GridLayout glCmpRunNumber = new GridLayout(5, false);
        glCmpRunNumber.marginLeft = 90;
        cmpIntSearch.setLayout(glCmpRunNumber);

        chkNumberFrom = new Button(cmpIntSearch, SWT.CHECK);
        chkNumberFrom.setText("From");

        spinnerFromNumber = new Spinner(cmpIntSearch, SWT.BORDER);
        spinnerFromNumber.setMaximum(RUN_NUMBER_MAX_VALUE);
        GridData gdSpinnerFromNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdSpinnerFromNumber.widthHint = 30;
        spinnerFromNumber.setLayoutData(gdSpinnerFromNumber);
        spinnerFromNumber.setEnabled(false);

        Label lblSpacing = new Label(cmpIntSearch, SWT.NONE);
        lblSpacing.setText("     ");

        chkNumberTo = new Button(cmpIntSearch, SWT.CHECK);
        chkNumberTo.setText("To");

        spinnerToNumber = new Spinner(cmpIntSearch, SWT.BORDER);
        spinnerToNumber.setMaximum(RUN_NUMBER_MAX_VALUE);
        GridData gdSpinnerToNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdSpinnerToNumber.widthHint = 30;
        spinnerToNumber.setLayoutData(gdSpinnerToNumber);
        spinnerToNumber.setEnabled(false);

        cmpTxtSearch = new Composite(cmpSearch, SWT.NONE);
        map.put(JournalSearchWidgetType.TEXT_SEARCH, cmpTxtSearch);
        cmpTxtSearch.setLayout(new GridLayout(1, false));

        txtSearch = new Text(cmpTxtSearch, SWT.BORDER);
        GridData gdTxtSearchTitle = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdTxtSearchTitle.widthHint = SEARCH_BOX_WIDTH;
        txtSearch.setLayoutData(gdTxtSearchTitle);

        Composite cmpTimePicker = new Composite(cmpSearch, SWT.NONE);
        map.put(JournalSearchWidgetType.CALENDAR_SEARCH, cmpTimePicker);
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

        stackSearch.topControl = cmpIntSearch;
        cmpSearch.setTabList(new Control[]{cmpIntSearch, cmpTxtSearch, cmpTimePicker});

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
                        stackSearch.topControl = map.get(getJournalSearch().getWidgetType());
                        // stackSearch.topControl = cmpFilters.get(cmbFilterType.getSelectionIndex());
                        cmpSearch.layout();
                    }
                });
            }
        });
    }

    /**
     * @return The currently selected journal search
     */
    public JournalSearch getJournalSearch() {
        JournalSearch journalSearch = model.getSearches().get(cmbFilterType.getSelectionIndex());
        JournalSearchWidgetType widgetType = journalSearch.getWidgetType();
        
        if (widgetType == JournalSearchWidgetType.CALENDAR_SEARCH) {
            journalSearch.setFrom(getTimeFrom());
            
        }
        return journalSearch;
    }

    /**
     * @return The text in the search box of the currently active filter type.
     */
    private Optional<String> getSearchText() {
        return Optional.of(txtSearch.getText());
    }

    /**
     * @return The number in the from run number spinner.
     */
    private Optional<Integer> getNumberFrom() {
        return chkNumberFrom.getSelection() ? Optional.of(spinnerFromNumber.getSelection()) : Optional.empty();
    }

    /**
     * @return The number in the to run number spinner.
     */
    private Optional<Integer> getNumberTo() {
        return chkNumberTo.getSelection() ? Optional.of(spinnerToNumber.getSelection()) : Optional.empty();
    }

    /**
     * @return A Calendar representing the start time to search from.
     */
    private Optional<Calendar> getTimeFrom() {
        return chkTimeFrom.getSelection() ? Optional.of(new GregorianCalendar(dtFromDate.getYear(), dtFromDate.getMonth(),
                dtFromDate.getDay(), dtFromTime.getHours(), dtFromTime.getMinutes(), dtFromTime.getSeconds())) : Optional.empty();
    }

    /**
     * @return A Calendar representing the start time to search to.
     */
    private Optional<Calendar> getTimeTo() {
        return chkTimeTo.getSelection() ? Optional.of(new GregorianCalendar(dtToDate.getYear(), dtToDate.getMonth(),
                dtToDate.getDay(), dtToTime.getHours(), dtToTime.getMinutes(), dtToTime.getSeconds())) : Optional.empty();
    }
    
    /**
     * Clears all of the user input and resets the buttons.
     */
    public void clearInput() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                chkNumberFrom.setSelection(false);
                chkNumberTo.setSelection(false);
                chkTimeFrom.setSelection(false);
                chkTimeTo.setSelection(false);
                
                txtSearch.setText("");
                
                spinnerFromNumber.setSelection(0);
                spinnerToNumber.setSelection(0);
                spinnerFromNumber.setEnabled(false);
                spinnerToNumber.setEnabled(false);
                
                Calendar cal = Calendar.getInstance();
                dtFromDate.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dtToDate.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dtFromTime.setTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                dtToTime.setTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                dtFromDate.setEnabled(false);
                dtToDate.setEnabled(false);
                dtFromTime.setEnabled(false);
                dtToTime.setEnabled(false);
            }
        });
    }
}
