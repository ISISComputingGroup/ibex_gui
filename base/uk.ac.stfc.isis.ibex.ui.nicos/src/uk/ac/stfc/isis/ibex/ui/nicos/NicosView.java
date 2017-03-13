
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

package uk.ac.stfc.isis.ibex.ui.nicos;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.QueueScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptSendStatusConverter;

/**
 * The main view for the NICOS scripting perspective.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosView extends ViewPart {
	
	/**
	 * The public ID of this class.
	 */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.nicos.nicosview";

    private static final String INITIAL_SCRIPT = "# Script\nprint(\"My Script\")";
	
	private final Shell shell;
    private DataBindingContext bindingContext = new DataBindingContext();
	
    private NicosModel model;
    private QueueScriptViewModel queueScriptViewModel;

    private Label lblCurrentScript;

	/**
	 * The default constructor for the view.
	 */
	public NicosView() {
        model = Nicos.getDefault().getModel();
        queueScriptViewModel = new QueueScriptViewModel(model, INITIAL_SCRIPT);

		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout glParent = new GridLayout(2, false);
		glParent.marginRight = 10;
		glParent.marginHeight = 10;
		glParent.marginWidth = 10;
		parent.setLayout(glParent);
		
		lblCurrentScript = new Label(parent, SWT.NONE);
		lblCurrentScript.setText("Current Script");
		
		Label lblOutput = new Label(parent, SWT.NONE);
		lblOutput.setText("Output");
		
		StyledText txtCurrentScript = new StyledText(parent, SWT.BORDER);
		txtCurrentScript.setEditable(false);
		txtCurrentScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		StyledText txtOutput = new StyledText(parent, SWT.BORDER);
		txtOutput.setEditable(false);
		txtOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
        Composite scriptSendGrp = new Composite(parent, SWT.NONE);
        scriptSendGrp.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1));
        GridLayout ssgLayout = new GridLayout(3, false);
        ssgLayout.marginRight = 10;
        ssgLayout.marginHeight = 10;
        ssgLayout.marginWidth = 10;
        scriptSendGrp.setLayout(ssgLayout);

        Button btnCreateScript = new Button(scriptSendGrp, SWT.NONE);
		btnCreateScript.setText("Create Script");
        btnCreateScript.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false, true, 1, 1));
		
        Label lblQueueScriptStatus = new Label(scriptSendGrp, SWT.NONE);
        GridData layoutData = new GridData(SWT.BEGINNING, SWT.FILL, false, true, 1, 1);
        layoutData.widthHint = 80;
        lblQueueScriptStatus.setLayoutData(layoutData);
        bindingContext.bindValue(WidgetProperties.text().observe(lblQueueScriptStatus),
                BeanProperties.value("scriptSendStatus").observe(queueScriptViewModel), null, new ScriptSendStatusConverter());


        Label lblQueueScriptError = new Label(scriptSendGrp, SWT.NONE);
        lblQueueScriptError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        bindingContext.bindValue(WidgetProperties.text().observe(lblQueueScriptError),
                BeanProperties.value("scriptSendErrorMessage").observe(queueScriptViewModel));

		btnCreateScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                QueueScriptDialog dialog = new QueueScriptDialog(shell, queueScriptViewModel);
				dialog.open();
			}
		});
	}

    /**
     * 
     */
    @Override
    public void setFocus() {
        lblCurrentScript.setFocus();
    }

}
