/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.nicos.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

/**
 * The view for the dialog that queues a new script to send to the script
 * server.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class QueueScriptPanel extends Composite {

    private DataBindingContext bindingContext = new DataBindingContext();
	
    /**
     * The constructor for this class.
     * 
     * @param parent
     *            The composite that this panel belongs to.
     * @param style
     *            The SWT style of this panel.
     * @param model
     *            the view model for queueing a script
     */
    public QueueScriptPanel(Composite parent, int style, QueueScriptViewModel model) {
		super(parent, style);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.horizontalSpacing = 1;
        setLayout(gridLayout);
        
        StyledText styledText = new StyledText(this, SWT.BORDER);
        styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
        
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(styledText),
                BeanProperties.value("code").observe(model.getScript()));
        
        Label lblScriptName = new Label(this, SWT.NONE);
        lblScriptName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblScriptName.setText("Script Name:");
        
        Text scriptName = new Text(this, SWT.BORDER);
        GridData textGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        textGridData.horizontalIndent = 5;
        scriptName.setLayoutData(textGridData);
                bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(scriptName),
        		BeanProperties.value("name").observe(model.getScript()));
	}
}
