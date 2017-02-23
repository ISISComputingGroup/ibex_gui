
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

@SuppressWarnings("checkstyle:magicnumber")
public class SearchControl extends Canvas {

    private SearchControlViewModel viewModel;

	private Text txtValue;
	private Combo cmboFields;

	private Button chkFrom;
	private DateTime dtFromDate;
	private DateTime dtFromTime;
	private Button chkTo;
	private DateTime dtToDate;
	private DateTime dtToTime;
	
    private Combo cmboSeverity;

    private Label lblSearchText;

    private ProgressBar progressBar;
	
	public SearchControl(LogDisplay parent, final ISearchModel searcher) {
		super(parent, SWT.NONE);

        this.viewModel = new SearchControlViewModel();

        GridLayout gl = new GridLayout(2, false);
        gl.marginWidth = 0;
        setLayout(gl);

        Group grpFilter = new Group(this, SWT.NONE);
        grpFilter.setText("Filter Options");
        grpFilter.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

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
		cmboFields.select(0);

		// Create search button
        Button btnSearch = new Button(grpFilter, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                viewModel.search();
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
                viewModel.clearSearchResults();
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

		dtFromDate = new DateTime(timePicker, SWT.BORDER | SWT.DROP_DOWN);
		dtFromTime = new DateTime(timePicker, SWT.BORDER | SWT.DROP_DOWN
				| SWT.TIME);
		dtFromDate.setEnabled(false);
		dtFromTime.setEnabled(false);

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
                viewModel.setInfoFilter(cmboSeverity.getText());
			}
		});
        viewModel.setInfoFilter(cmboSeverity.getText());

        // Progress bar
        progressBar = new ProgressBar(grpFilter, SWT.INDETERMINATE);
        progressBar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		
        bind();
	}

    private void bind() {

        DataBindingContext bindingContext = new DataBindingContext();

        /* Search progress indicators */
        bindingContext.bindValue(WidgetProperties.visible().observe(lblSearchText),
                BeanProperties.value("progressIndicatorsVisible").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.visible().observe(progressBar),
                BeanProperties.value("progressIndicatorsVisible").observe(viewModel));

        /* List of items in cmboFields */
        bindingContext.bindValue(WidgetProperties.selection().observe(cmboFields),
                BeanProperties.value("searchItems").observe(viewModel));

        /* "To" datetime picker */
        bindingContext.bindValue(WidgetProperties.selection().observe(chkTo),
                BeanProperties.value("toCheckboxSelected").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(dtToDate),
                BeanProperties.value("toCheckboxSelected").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(dtToTime),
                BeanProperties.value("toCheckboxSelected").observe(viewModel));

        /* "From" datetime picker */
        bindingContext.bindValue(WidgetProperties.selection().observe(chkFrom),
                BeanProperties.value("fromCheckboxSelected").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(dtFromDate),
                BeanProperties.value("fromCheckboxSelected").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.enabled().observe(dtFromTime),
                BeanProperties.value("fromCheckboxSelected").observe(viewModel));
    }
}