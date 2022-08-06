
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
import java.util.Optional;
import java.util.OptionalInt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.model.UIThreadUtils;
import uk.ac.stfc.isis.ibex.ui.journalviewer.models.JournalViewModel;
import uk.ac.stfc.isis.ibex.ui.widgets.DateTimeWithCalendar;


/**
 * The Class Search Control to allow searching within the log.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SearchInput extends Composite {

    private final int runNumberIndex = 0;
    private final int startTimeIndex = 2;

    private ArrayList<Composite> cmpFilters = new ArrayList<Composite>();
    private Composite cmpSearch;
    private Text textSearchBox;
    private static StackLayout stackSearch = new StackLayout();

    private static final int SEARCH_BOX_WIDTH = 410;
    
    // Run numbers can be up to 8 digits
    private static final int RUN_NUMBER_MAX_VALUE = 99999999;
    private static final int NUMBER_SPINNER_WIDTH = 45;

    private static final Display DISPLAY = Display.getCurrent();

    private Spinner spinnerFromNumber;
    private Spinner spinnerToNumber;
    private Button chkNumberFrom; 
    private Button chkNumberTo; 
    private Button chkTimeFrom; 
    private Button chkTimeTo;
    
    private DateTimeWithCalendar dtFromDate;
    private DateTimeWithCalendar dtToTime;
    private DateTimeWithCalendar dtToDate;
    private DateTimeWithCalendar dtFromTime;  
    
    private JournalViewModel viewModel;
    private Combo cmbFilterType;

    /**
     * Instantiates a new filter control.
     *
     * @param parent
     *            the parent in which this control resides
     * @param viewModel
     *            the viewmodel for this view
     */
    public SearchInput(Composite parent, JournalViewModel viewModel) {
        super(parent, SWT.NONE);
        this.viewModel = viewModel;
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
        glCmpRunNumber.marginLeft = 75;
        cmpRunNumber.setLayout(glCmpRunNumber);

        chkNumberFrom = new Button(cmpRunNumber, SWT.CHECK);
        chkNumberFrom.setText("From");

        spinnerFromNumber = new Spinner(cmpRunNumber, SWT.BORDER);
        spinnerFromNumber.setMaximum(RUN_NUMBER_MAX_VALUE);
        GridData gdSpinnerFromNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdSpinnerFromNumber.widthHint = NUMBER_SPINNER_WIDTH;
        spinnerFromNumber.setLayoutData(gdSpinnerFromNumber);
        spinnerFromNumber.setEnabled(false);

        Label lblSpacing = new Label(cmpRunNumber, SWT.NONE);
        lblSpacing.setText("     ");

        chkNumberTo = new Button(cmpRunNumber, SWT.CHECK);
        chkNumberTo.setText("To");

        spinnerToNumber = new Spinner(cmpRunNumber, SWT.BORDER);
        spinnerToNumber.setMaximum(RUN_NUMBER_MAX_VALUE);
        GridData gdSpinnerToNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdSpinnerToNumber.widthHint = NUMBER_SPINNER_WIDTH;
        spinnerToNumber.setLayoutData(gdSpinnerToNumber);
        spinnerToNumber.setEnabled(false);

        Composite cmpTextSearch = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpTextSearch);
        cmpTextSearch.setLayout(new GridLayout(1, false));

        textSearchBox = new Text(cmpTextSearch, SWT.BORDER);
        GridData gdTxtSearchTitle = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdTxtSearchTitle.widthHint = SEARCH_BOX_WIDTH;
        textSearchBox.setLayoutData(gdTxtSearchTitle);

        Composite cmpTimePicker = new Composite(cmpSearch, SWT.NONE);
        cmpFilters.add(cmpTimePicker);
        cmpTimePicker.setLayout(new GridLayout(7, false));

        chkTimeFrom = new Button(cmpTimePicker, SWT.CHECK);
        chkTimeFrom.setText("From");

        dtFromDate = new DateTimeWithCalendar(cmpTimePicker, SWT.BORDER);
        dtFromDate.setEnabled(false);

        dtFromTime = new DateTimeWithCalendar(cmpTimePicker, SWT.BORDER | SWT.TIME);
        dtFromTime.setEnabled(false);

        Label lblSpacing1 = new Label(cmpTimePicker, SWT.NONE);
        lblSpacing1.setText("        ");

        chkTimeTo = new Button(cmpTimePicker, SWT.CHECK);
        chkTimeTo.setText("To");

        dtToDate = new DateTimeWithCalendar(cmpTimePicker, SWT.BORDER);
        dtToDate.setEnabled(false);

        dtToTime = new DateTimeWithCalendar(cmpTimePicker, SWT.BORDER | SWT.TIME);
        dtToTime.setEnabled(false);

        stackSearch.topControl = cmpRunNumber;
        cmpFilters.add(cmpTextSearch);
        cmpFilters.add(cmpTextSearch);

        cmpSearch
                .setTabList(new Control[] {cmpRunNumber, cmpTextSearch, cmpTimePicker, cmpTextSearch, cmpTextSearch});

        addListeners();
        bind();

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
        return textSearchBox.getText();
    }

    /**
     * @return The number in the from run number spinner.
     */
    public OptionalInt getRunNumberFrom() {
        return chkNumberFrom.getSelection() ? OptionalInt.of(spinnerFromNumber.getSelection()) : OptionalInt.empty();
    }

    /**
     * @return The number in the to run number spinner.
     */
    public OptionalInt getRunNumberTo() {
        return chkNumberTo.getSelection() ? OptionalInt.of(spinnerToNumber.getSelection()) : OptionalInt.empty();
    }

    /**
     * @return A Calendar representing the start time to search from.
     */
    public Optional<Calendar> getStartTimeFrom() {
        return chkTimeFrom.getSelection()
                ? Optional.of(new GregorianCalendar(dtFromDate.getYear(), dtFromDate.getMonth(), dtFromDate.getDay(),
                        dtFromTime.getHours(), dtFromTime.getMinutes(), dtFromTime.getSeconds()))
                : Optional.empty();
    }

    /**
     * @return A Calendar representing the start time to search to.
     */
    public Optional<Calendar> getStartTimeTo() {
        return chkTimeTo.getSelection() ? Optional.of(new GregorianCalendar(dtToDate.getYear(), dtToDate.getMonth(),
                dtToDate.getDay(), dtToTime.getHours(), dtToTime.getMinutes(), dtToTime.getSeconds()))
                : Optional.empty();
    }

    /**
     * Clears all of the user input and resets the buttons.
     */
    public void clearInput() {
        UIThreadUtils.asyncExec(new Runnable() {
            @Override
            public void run() {
                chkNumberFrom.setSelection(false);
                chkNumberTo.setSelection(false);
                chkTimeFrom.setSelection(false);
                chkTimeTo.setSelection(false);

                textSearchBox.setText("");

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


                // get new values from the fields once it is is cleared and let the model know
                viewModel.setFromNumber(spinnerFromNumber.getSelection());
                viewModel.setToNumber(spinnerToNumber.getSelection());

                viewModel.setToTime(dtToTime.getDateTime());
                viewModel.setFromTime(dtFromTime.getDateTime());
                viewModel.setToDate(dtToDate.getDateTime());
                viewModel.setFromDate(dtFromDate.getDateTime());



            }
        });
    }

    /**
     * bind all the Search type fields to the JournalViewModel for which warning message needs 
     * to be displayed on the GUI i.e. run number and start time
     */
    private void bind() {
        spinnerFromNumber.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                viewModel.setFromNumber(spinnerFromNumber.getSelection());
            }
        });
        spinnerToNumber.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                viewModel.setToNumber(spinnerToNumber.getSelection());
            }
        });

        dtFromDate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.setFromDate(dtFromDate.getDateTime());
            }
        });

        dtFromTime.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.setFromTime(dtFromTime.getDateTime());
            }
        });

        dtToDate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.setToDate(dtToDate.getDateTime());
            }
        });

        dtToTime.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.setToTime(dtToTime.getDateTime());
            }

        });

    }

    private void addListeners() {
        chkTimeFrom.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dtFromDate.setEnabled(chkTimeFrom.getSelection());
                dtFromTime.setEnabled(chkTimeFrom.getSelection());
                
                viewModel.setInitialFromDateTime(dtFromTime.getDateTime(), dtFromDate.getDateTime());
                viewModel.setInitialToDateTime(dtToTime.getDateTime(), dtToDate.getDateTime());
            }
        });


        chkTimeTo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dtToDate.setEnabled(chkTimeTo.getSelection());
                dtToTime.setEnabled(chkTimeTo.getSelection());

                viewModel.setInitialFromDateTime(dtFromTime.getDateTime(), dtFromDate.getDateTime());
                viewModel.setInitialToDateTime(dtToTime.getDateTime(), dtToDate.getDateTime());
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
                UIThreadUtils.asyncExec(() -> {
                    stackSearch.topControl = cmpFilters.get(cmbFilterType.getSelectionIndex());
                    cmpSearch.layout();

                    viewModel.clearMessage();

                    int index = cmbFilterType.getSelectionIndex();
                    if (index == runNumberIndex) {
                        // when run number is selected from the drop down, send
                        // the run number that is already present in the field
                        // to the viewModel
                        viewModel.setFromNumber(spinnerFromNumber.getSelection());
                        viewModel.setToNumber(spinnerToNumber.getSelection());
                    } else if ((index == startTimeIndex) && (dtToTime != null)) {
                        // when start is selected from the drop down, send the
                        // date that is already present in the field to the view
                        // viewModel
                        viewModel.setToTime(dtToTime.getDateTime());
                        viewModel.setFromTime(dtFromTime.getDateTime());
                        viewModel.setToDate(dtToDate.getDateTime());
                        viewModel.setFromDate(dtFromDate.getDateTime());

                    }

                });
            }
        });
    }

    /**
     * Adds a KeyListener to the elements where search parameters are input.
     * 
     * @param listener
     *            the KeyListener to add
     */
    public void addSearchListeners(KeyListener listener) {
        textSearchBox.addKeyListener(listener);
        spinnerFromNumber.addKeyListener(listener);
        spinnerToNumber.addKeyListener(listener);
        dtFromDate.addKeyListener(listener);
        dtFromTime.addKeyListener(listener);
        dtToDate.addKeyListener(listener);
        dtToTime.addKeyListener(listener);
    }
}
