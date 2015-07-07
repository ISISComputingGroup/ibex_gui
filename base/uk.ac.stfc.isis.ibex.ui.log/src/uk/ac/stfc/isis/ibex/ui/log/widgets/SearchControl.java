
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
import java.util.GregorianCalendar;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.ui.log.filter.LogMessageFilter;

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

	private Text txtValue;
	private Combo cmboFields;

	private Button chkFrom;
	private DateTime dtFromDate;
	private DateTime dtFromTime;
	private Button chkTo;
	private DateTime dtToDate;
	private DateTime dtToTime;
	
	private Button chkInfo;

	private ISearchModel searcher;
	private LogDisplay parent;
	
	private final LogMessageFilter infoFilter = new LogMessageFilter(LogMessageFields.SEVERITY, "INFO", true); 
	
	public SearchControl(LogDisplay parent, final ISearchModel searcher) {
		super(parent, SWT.NONE);
		
		this.parent = parent;
		this.searcher = searcher;

		setLayout(new GridLayout(5, false));

		// Create search text box
		GridData valueLayout = new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1);
		valueLayout.widthHint = 300;

		txtValue = new Text(this, SWT.BORDER);
		txtValue.setLayoutData(valueLayout);
		new Label(this, SWT.NONE);
		cmboFields = new Combo(this, SWT.READ_ONLY);
		cmboFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		cmboFields.setItems(FIELD_NAMES);
		cmboFields.select(0);

		// Create search button
		Button btnSearch = new Button(this, SWT.NONE);
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
		Button btnClear = new Button(this, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearSearchResults();
			}
		});
		btnClear.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnClear.setImage(ResourceManager.getPluginImage(
				"uk.ac.stfc.isis.ibex.ui.log", "icons/delete.png"));
		btnClear.setToolTipText("Clear search results and return to recent message view");
		// timeLayout.widthHint = 600;

		// Date-time picker
		Composite timePicker = new Composite(this, SWT.NONE);
		timePicker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		timePicker.setLayout(new GridLayout(7, false));

		// 'From' control
		chkFrom = new Button(timePicker, SWT.CHECK);
		chkFrom.setText("From");

		dtFromDate = new DateTime(timePicker, SWT.BORDER | SWT.DROP_DOWN);
		dtFromTime = new DateTime(timePicker, SWT.BORDER | SWT.DROP_DOWN
				| SWT.TIME);
		dtFromDate.setEnabled(false);
		dtFromTime.setEnabled(false);

		chkFrom.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dtFromDate.setEnabled(chkFrom.getSelection());
				dtFromTime.setEnabled(chkFrom.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Label lblNewLabel = new Label(timePicker, SWT.NONE);
		lblNewLabel.setText("        ");

		// 'To' Control
		chkTo = new Button(timePicker, SWT.CHECK);
		chkTo.setText("To");

		dtToDate = new DateTime(timePicker, SWT.BORDER | SWT.DROP_DOWN);
		dtToTime = new DateTime(timePicker, SWT.BORDER | SWT.DROP_DOWN
				| SWT.TIME);
		dtToDate.setEnabled(false);
		dtToTime.setEnabled(false);

		chkTo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dtToDate.setEnabled(chkTo.getSelection());
				dtToTime.setEnabled(chkTo.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		new Label(this, SWT.NONE);
		
		// Add the info filter check
		chkInfo = new Button(this, SWT.CHECK);
		chkInfo.setText("Show info messages");
		chkInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setInfoFilter(!chkInfo.getSelection());
			}
		});
		setInfoFilter(!chkInfo.getSelection());
		
	}

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
				LogMessageFields field = FIELDS[fieldIndex];
				String value = txtValue.getText();

				Calendar from = null, to = null;
				if (chkFrom.getSelection()) {
					from = new GregorianCalendar(dtFromDate.getYear(),
							dtFromDate.getMonth(), dtFromDate.getDay(),
							dtFromTime.getHours(), dtFromTime.getMinutes(),
							dtFromTime.getSeconds());
				}

				if (chkTo.getSelection()) {
					to = new GregorianCalendar(dtToDate.getYear(),
							dtToDate.getMonth(), dtToDate.getDay(),
							dtToTime.getHours(), dtToTime.getMinutes(),
							dtToTime.getSeconds());
				}

				searcher.search(field, value, from, to);
			}
		}
	}
	
	private void clearSearchResults() {
		txtValue.setText("");

		if (searcher != null) {
			searcher.clearSearch();
		}
	}
	
	private void setInfoFilter(boolean set) {
		if (set) {
			parent.addMessageFilter(infoFilter);
		} else {
			parent.removeMessageFilter(infoFilter);
		}
	}
}