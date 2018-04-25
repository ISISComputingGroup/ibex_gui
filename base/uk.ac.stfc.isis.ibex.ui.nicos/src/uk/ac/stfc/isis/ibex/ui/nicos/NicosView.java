
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

package uk.ac.stfc.isis.ibex.ui.nicos;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
<<<<<<< HEAD
=======
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
>>>>>>> master
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.ExistingScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.QueueScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ConnectionStatusConverter;
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptSendStatusConverter;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;

/**
 * The main view for the NICOS current Script view.
 */
@SuppressWarnings("checkstyle:magicnumber")
<<<<<<< HEAD
public class NicosView {
	
	/**
	 * The public ID of this class.
	 */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.nicos.NicosView";

    private static final String INITIAL_SCRIPT = "# Script\nprint(\"My Script\")";
=======
public class NicosView extends ViewPart {
    
    /**
     * The public ID of this class.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.nicos.nicosview";
>>>>>>> master
    
    private static final int FIXED_WIDTH = 750;
    private static final int FIXED_HEIGHT = 225;
	
	private final Shell shell;
    private DataBindingContext bindingContext = new DataBindingContext();
    StyledText txtCurrentScript;
    
    private NicosModel model;
    private QueueScriptViewModel queueScriptViewModel;

    /**
     * The default constructor for the view.
     */
    public NicosView() {
        model = Nicos.getDefault().getModel();
<<<<<<< HEAD
        queueScriptViewModel = new QueueScriptViewModel(model, INITIAL_SCRIPT);
=======
        queueScriptViewModel = new QueueScriptViewModel(model);
        scriptStatusViewModel = new ScriptStatusViewModel(model);
        outputLogViewModel = new OutputLogViewModel(model);
>>>>>>> master

        shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }

	@PostConstruct
	public void createPartControl(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite nicosComposite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(nicosComposite);
        scrolledComposite.setMinSize(new Point(FIXED_WIDTH, FIXED_HEIGHT));
		
		GridLayout glParent = new GridLayout(1, false);
		glParent.marginHeight = 10;
		glParent.marginWidth = 10;
		nicosComposite.setLayout(glParent);

        // Connection info
        Composite connectionGrp = new Composite(nicosComposite, SWT.NONE);
        connectionGrp.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1));
        GridLayout connLayout = new GridLayout(2, false);
        connLayout.marginHeight = 10;
        connLayout.marginWidth = 10;
        connectionGrp.setLayout(connLayout);

        Label lblConnectionStatus = new Label(connectionGrp, SWT.NONE);
        GridData connStatusLayoutData = new GridData(SWT.BEGINNING, SWT.FILL, false, true, 1, 1);
        connStatusLayoutData.widthHint = 100;
        lblConnectionStatus.setLayoutData(connStatusLayoutData);
        bindingContext.bindValue(WidgetProperties.text().observe(lblConnectionStatus),
                BeanProperties.value("connectionStatus").observe(model), null, new ConnectionStatusConverter());

        Label lblConnectionError = new Label(connectionGrp, SWT.NONE);
        lblConnectionError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        bindingContext.bindValue(WidgetProperties.text().observe(lblConnectionError),
                BeanProperties.value("connectionErrorMessage").observe(model));

        txtCurrentScript = new StyledText(nicosComposite, SWT.BORDER);
        txtCurrentScript.setEditable(false);
        txtCurrentScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

<<<<<<< HEAD
=======
        final StyledText txtOutput = new StyledText(parent, SWT.V_SCROLL | SWT.BORDER);
        txtOutput.setEditable(false);
        txtOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
        
>>>>>>> master
        bindingContext.bindValue(WidgetProperties.text().observe(txtCurrentScript),
                BeanProperties.value("currentlyExecutingScript").observe(model));
        
<<<<<<< HEAD
        Composite scriptSendGrp = new Composite(nicosComposite, SWT.NONE);
=======
        txtOutput.addListener(SWT.Modify, new Listener() {
            @Override
            public void handleEvent(Event e) {
                txtOutput.setTopIndex(txtOutput.getLineCount() - 1);
            }
        });
        
        Label lblQueuedScripts = new Label(parent, SWT.NONE);
        lblQueuedScripts.setText("Queued scripts (double click to see contents):");
        
        ListViewer queuedScriptsViewer = new ListViewer(parent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        List list = queuedScriptsViewer.getList();
        list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        queuedScriptsViewer.setContentProvider(new ObservableListContentProvider());
        queuedScriptsViewer.setLabelProvider(new LabelProvider() {
        	public String getText(Object element) {
        		QueuedScript code = (QueuedScript) element;
        		return code.getName();
        	}
        });
        
        queuedScriptsViewer.setInput(BeanProperties.list("queuedScripts").observe(model));
        
        queuedScriptsViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				QueuedScript selection = (QueuedScript) ((IStructuredSelection) event.getSelection()).getFirstElement();
				ExistingScriptDialog dialog = new ExistingScriptDialog(shell, selection);
				dialog.open();
			}
		});
        
        Composite scriptSendGrp = new Composite(parent, SWT.NONE);
>>>>>>> master
        scriptSendGrp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout ssgLayout = new GridLayout(3, false);
        ssgLayout.marginHeight = 10;
        ssgLayout.marginWidth = 10;
        scriptSendGrp.setLayout(ssgLayout);

        Button btnCreateScript = new Button(scriptSendGrp, SWT.NONE);
        btnCreateScript.setText("Create Script");
        btnCreateScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        
        Label lblQueueScriptStatus = new Label(scriptSendGrp, SWT.NONE);
        GridData layoutData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
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

}
