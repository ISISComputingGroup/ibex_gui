
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

package uk.ac.stfc.isis.ibex.ui.widgets.observable;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * A text box for displaying and editing a record.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class WritableObservingTextBox extends Composite {
	
	private DataBindingContext bindingContext;
	
	private final Text textBox;
	private final Button setButton;
	
	public WritableObservingTextBox(Composite parent, int style, 
	        StringWritableObservableAdapter adapter) {
	    
		super(parent, style);
		
		int numCols = (style & SWT.UP) != 0 ? 1 : 2;
		GridLayout gridLayout = new GridLayout(numCols, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		textBox = new Text(this, SWT.BORDER);
		textBox.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		textBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
				
		setButton = new Button(this, SWT.NONE);
		GridData gdSetButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdSetButton.widthHint = 50;
		setButton.setLayoutData(gdSetButton);
		setButton.setText("Set");
		
		if (adapter != null) {
			bind(adapter);
		}
	}
	
	/**
	 * Sets the tool tip message of the text box. By default, the text box has 
	 * no tool tip.
	 * @param toolTipMsg A string that will be the message to appear in the tool
	 * tip of the text box. If the argument is an empty string, no tool tip 
	 * will appear.
	 */
	public void setToolTip(String toolTipMsg) {
	    textBox.setToolTipText("");
	}

	private void bind(final StringWritableObservableAdapter adapter) {
		bindingContext = new DataBindingContext();	
		bindingContext.bindValue(WidgetProperties.enabled().observe(setButton), BeanProperties.value("value").observe(adapter.canSetText()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(textBox), BeanProperties.value("value").observe(adapter.canSetText()));
		bindingContext.bindValue(WidgetProperties.text().observe(textBox), BeanProperties.value("value").observe(adapter.text()));
		
		textBox.addListener(SWT.Traverse, event -> {
	            if (event.detail == SWT.TRAVERSE_RETURN) {
	                uncheckedSetText(adapter);
	            }
	    });
		
		setButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
                uncheckedSetText(adapter);
			}
		});
	}
	
	private void uncheckedSetText(StringWritableObservableAdapter adapter) {
		adapter.uncheckedSetText(textBox.getText());
	}
}
