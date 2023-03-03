
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

package uk.ac.stfc.isis.ibex.ui.log.widgets;

import java.util.Calendar;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.ui.log.filter.LogMessageFilter;
import uk.ac.stfc.isis.ibex.ui.widgets.DateTimeWithCalendar;

/**
 * The Class Search Control to allow searching within the log.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SearchControl extends Canvas {
	private static final LogMessageFields[] FIELDS = {
			LogMessageFields.CONTENTS, LogMessageFields.CLIENT_NAME,
			LogMessageFields.CLIENT_HOST, LogMessageFields.SEVERITY,
			LogMessageFields.TYPE, LogMessageFields.APPLICATION_ID };

	private static final String[] FIELD_NAMES;

	static {
		FIELD_NAMES = new String[FIELDS.length];
		for (int f = 0; f < FIELDS.length; ++f) {
			FIELD_NAMES[f] = FIELDS[f].getDisplayName();
		}
	}
	private final LogDisplayModel viewModel;
	private final DataBindingContext bindingContext = new DataBindingContext();
	private Text txtValue;
	private Combo cmboFields;

	private Button chkFrom;
	private DateTimeWithCalendar dtFromDate;
	private DateTimeWithCalendar dtFromTime;
	private Button chkTo;
	private DateTimeWithCalendar dtToDate;
	private DateTimeWithCalendar dtToTime;
	
    private Combo cmboSeverity;

	private ISearchModel searcher;
	private LogDisplay parent;

    private final LogMessageFilter infoFilter =
            new LogMessageFilter(LogMessageFields.SEVERITY, LogMessageSeverity.INFO.name(), true);
    private final LogMessageFilter minorFilter =
            new LogMessageFilter(LogMessageFields.SEVERITY, LogMessageSeverity.MINOR.name(), true);

    private Label lblSearchText;

    private ProgressBar progressBar;
	
	private Label error;
	/**
	 * Instantiates a new search control.
	 *
	 * @param parent the parent in which this control resides
	 * @param model a model to allow searching of log messages
	 */
	public SearchControl(LogDisplay parent, final LogDisplayModel model) {
		super(parent, SWT.NONE);
		this.viewModel = model;
		this.parent = parent;
		this.searcher = model;

        GridLayout gl = new GridLayout(1, false);
        setLayout(gl);

        Group grpFilter = new Group(this, SWT.NONE);
        grpFilter.setText("Filter Options");
        grpFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        GridLayout glGrpFilter = new GridLayout(6, false);
        glGrpFilter.verticalSpacing = 0;
        grpFilter.setLayout(glGrpFilter);

		// Create search text box
		GridData valueLayout = new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1);
		valueLayout.widthHint = 300;

        txtValue = new Text(grpFilter, SWT.BORDER);
		txtValue.setLayoutData(valueLayout);
        new Label(grpFilter, SWT.NONE);
        cmboFields = new Combo(grpFilter, SWT.READ_ONLY);
		cmboFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		cmboFields.setItems(FIELD_NAMES);
		cmboFields.select(0);

		// Create search button
        Button btnSearch = new Button(grpFilter, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});

		btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnSearch.setImage(ResourceManager.getPluginImage(
				"uk.ac.stfc.isis.ibex.ui.log", "icons/search.png"));
		btnSearch.setToolTipText("Search");

		// Create clear button
        Button btnClear = new Button(grpFilter, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearSearchResults();
                setProgressIndicatorsVisible(false);
			}
		});
		btnClear.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnClear.setImage(ResourceManager.getPluginImage(
				"uk.ac.stfc.isis.ibex.ui.log", "icons/delete.png"));
		btnClear.setToolTipText("Clear search results and return to recent message view");
		// timeLayout.widthHint = 600;

        // progress bar label
        lblSearchText = new Label(grpFilter, SWT.NONE);
        lblSearchText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        lblSearchText.setText("Searching...");

		// Date-time picker
        Composite timePicker = new Composite(grpFilter, SWT.NONE);
		timePicker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
        GridLayout timePickerGridLayout = new GridLayout(7, false);
        timePickerGridLayout.marginWidth = 0;
        timePicker.setLayout(timePickerGridLayout);

		// 'From' control
		chkFrom = new Button(timePicker, SWT.CHECK);
		chkFrom.setText("From");

		dtFromDate = new DateTimeWithCalendar(timePicker, SWT.BORDER | SWT.DROP_DOWN);
		dtFromTime = new DateTimeWithCalendar(timePicker, SWT.BORDER | SWT.DROP_DOWN
				| SWT.TIME);
		dtFromDate.setEnabled(false);
		dtFromTime.setEnabled(false);
		
        chkFrom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dtFromDate.setEnabled(chkFrom.getSelection());
				dtFromTime.setEnabled(chkFrom.getSelection());
				
				viewModel.setInitialFromDateTime(dtFromDate.getDateTime(), dtFromTime.getDateTime());
				viewModel.setInitialToDateTime(dtToDate.getDateTime(), dtToTime.getDateTime());
 			}
		});

		Label lblNewLabel = new Label(timePicker, SWT.NONE);
		lblNewLabel.setText("        ");

		// 'To' Control
		chkTo = new Button(timePicker, SWT.CHECK);
		chkTo.setText("To");

		dtToDate = new DateTimeWithCalendar(timePicker, SWT.BORDER | SWT.DROP_DOWN);
		dtToTime = new DateTimeWithCalendar(timePicker, SWT.BORDER | SWT.DROP_DOWN
				| SWT.TIME);
		dtToDate.setEnabled(false);
		dtToTime.setEnabled(false);

        chkTo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dtToDate.setEnabled(chkTo.getSelection());
				dtToTime.setEnabled(chkTo.getSelection());
				
				viewModel.setInitialFromDateTime(dtFromDate.getDateTime(), dtFromTime.getDateTime());
				viewModel.setInitialToDateTime(dtToDate.getDateTime(), dtToTime.getDateTime());
			}
		});
        
        addSearchListeners(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
                    search();
                }
            }
        });
		
        new Label(grpFilter, SWT.NONE);
		
		// Add the info filter check
        cmboSeverity = new Combo(grpFilter, SWT.DROP_DOWN | SWT.READ_ONLY);
        cmboSeverity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        cmboSeverity.setItems(LogMessageSeverity.allToString().toArray(new String[0]));
        cmboSeverity.setText(LogMessageSeverity.MAJOR.toString());
        new Label(grpFilter, SWT.NONE);
        new Label(grpFilter, SWT.NONE);
        cmboSeverity.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                setInfoFilter(cmboSeverity.getText());
			}
		});
        setInfoFilter(cmboSeverity.getText());

        error = new Label(grpFilter, SWT.NONE);
        error.setText("Date/Time in wrong order");
        error.setVisible(false);
        error.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        

        // Progress bar
        progressBar = new ProgressBar(grpFilter, SWT.INDETERMINATE);
        lblSearchText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        setProgressIndicatorsVisible(false);
        
        
        startListeners();
        		
	}
	
	/**
	 * Start widget listeners so we can update the ViewModel.
	 */
	
	public void startListeners() {   
        
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
        
		bindingContext.bindValue(WidgetProperties.visible().observe(error),
                BeanProperties.value("errorVisibility").observe(viewModel));
                
	}
	
	/**
	 * Sets the searcher model.
	 *
	 * @param searcher the new searcher for searching the log database
	 */
	public void setSearcher(ISearchModel searcher) {
		this.searcher = searcher;
	}

	/**
	 * Requests that the model perform a search for log messages that match the
	 * request parameters.
	 */
	private void search() {
		if (searcher != null) {
			int fieldIndex = cmboFields.getSelectionIndex();

			if (fieldIndex != -1) {
                final LogMessageFields field = FIELDS[fieldIndex];
                final String value = txtValue.getText();

                final Calendar from = chkFrom.getSelection()
                        ? getSearchDateAndTime(dtFromDate.getDateTime(), dtFromTime.getDateTime()) : null;
                final Calendar to = chkTo.getSelection()
                        ? getSearchDateAndTime(dtToDate.getDateTime(), dtToTime.getDateTime())  : null;

                runSearchJob(field, value, from, to);

            }
        }
    }

    private void runSearchJob(final LogMessageFields field, final String value, final Calendar from,
            final Calendar to) {

        final Runnable searchJob = new Runnable() {
            @Override
            public void run() {
                setProgressIndicatorsVisible(true);
                searcher.search(field, value, from, to);
                setProgressIndicatorsVisible(false);
            }
        };

        Thread searchJobThread = new Thread(searchJob, "Log search thread");

        searchJobThread.start();
	}
    
    /**
     * Get both the date and time search filter as one object.
     * @param The search date filter.
     * @param The search time filter.
     * @return The search calendar date and time filter.
     */
    private Calendar getSearchDateAndTime(Calendar date, Calendar time) {
    	date.set(Calendar.HOUR, time.get(Calendar.HOUR));
    	date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
    	date.set(Calendar.SECOND, time.get(Calendar.SECOND));
    	return date;
    }

    private void setProgressIndicatorsVisible(final boolean visible) {

        getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                lblSearchText.setVisible(visible);
                progressBar.setVisible(visible);
            }
        });

    }

	private void clearSearchResults() {
		txtValue.setText("");

		if (searcher != null) {
			searcher.clearSearch();
		}
	}
	
    private void setInfoFilter(String set) {
        parent.removeMessageFilter(infoFilter);
        parent.removeMessageFilter(minorFilter);
        if (set.equals(LogMessageSeverity.MAJOR.toString())) {
            parent.addMessageFilter(infoFilter);
            parent.addMessageFilter(minorFilter);
        } else if (set.equals(LogMessageSeverity.MINOR.toString())) {
            parent.addMessageFilter(infoFilter);
        }
	}
    
    /**
     * Adds a KeyListener to the elements where search parameters are input.
     * 
     * @param listener
     *            the KeyListener to add
     */
    private void addSearchListeners(KeyListener listener) {
        txtValue.addKeyListener(listener);
        dtFromDate.addKeyListener(listener);
        dtFromTime.addKeyListener(listener);
        dtToDate.addKeyListener(listener);
        dtToTime.addKeyListener(listener);
    }
}
