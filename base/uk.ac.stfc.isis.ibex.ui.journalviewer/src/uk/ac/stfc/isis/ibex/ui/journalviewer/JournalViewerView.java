
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2018 Science & Technology Facilities Council.
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.journalviewer.models.JournalViewModel;

/**
 * Journal viewer main view.
 */
public class JournalViewerView extends ViewPart {

    /**
     * The view ID.
     */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.journalviewer.JournalViewerView"; //$NON-NLS-1$
	
	private static final int LABEL_FONT_SIZE = 11;
	private static final int HEADER_FONT_SIZE = 16;
	
    private Label lblError;
    private Label lblLastUpdate;
    private Button btnRefresh;

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent - Parent UI element
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		Label lblTitle = new Label(parent, SWT.NONE);
		lblTitle.setFont(SWTResourceManager.getFont("Segoe UI", HEADER_FONT_SIZE, SWT.BOLD));
		lblTitle.setText("Journal Viewer");
		
        new Label(parent, SWT.NONE);

        btnRefresh = new Button(parent, SWT.NONE);
        btnRefresh.setText("Refresh");

        lblError = new Label(parent, SWT.NONE);
        lblError.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true));
        lblError.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        lblError.setText("placeholder");
        
        lblLastUpdate = new Label(parent, SWT.NONE);
        lblLastUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        lblLastUpdate.setText("placeholder");

        bind(JournalViewerUI.getDefault().getModel());
	}
	
    private void bind(final JournalViewModel model) {
        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.text().observe(lblError),
                BeanProperties.value("message").observe(model));
        bindingContext.bindValue(WidgetProperties.text().observe(lblLastUpdate),
                BeanProperties.value("lastUpdate").observe(model));
        
        btnRefresh.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.refresh();
            }
        });
    }

	@Override
	public void dispose() {
	}

	@Override
	public void setFocus() {		
	}
}
