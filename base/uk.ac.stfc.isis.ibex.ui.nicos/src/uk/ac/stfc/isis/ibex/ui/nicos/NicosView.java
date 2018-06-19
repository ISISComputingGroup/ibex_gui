
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.ExistingScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.QueueScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ConnectionStatusConverter;
import uk.ac.stfc.isis.ibex.ui.nicos.models.OutputLogViewModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptSendStatusConverter;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptStatusViewModel;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * The main view for the NICOS scripting perspective.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosView extends ViewPart {
	/**
     * The public ID of this class.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.nicos.nicosview";
    
    private final Shell shell;
    private DataBindingContext bindingContext = new DataBindingContext();
    
    private NicosModel model;
    private QueueScriptViewModel queueScriptViewModel;

    private Label lblCurrentScriptStatus;

	private ScriptStatusViewModel scriptStatusViewModel;
    private OutputLogViewModel outputLogViewModel;

	private Label lblCurrentError;

    /**
     * The default constructor for the view.
     */
    public NicosView() {
        model = Nicos.getDefault().getModel();
        queueScriptViewModel = new QueueScriptViewModel(model);
        scriptStatusViewModel = new ScriptStatusViewModel(model);
        outputLogViewModel = new OutputLogViewModel(model);

        shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void createPartControl(Composite parent) {
        GridLayout glParent = new GridLayout(2, true);
        glParent.marginRight = 10;
        glParent.marginHeight = 10;
        glParent.marginWidth = 10;
        parent.setLayout(glParent);
        
        createOutputContainer(parent);
        createCurrentScriptContainer(parent);
        createStatusContainer(parent);
        createQueueContainer(parent);
    }

	private void createQueueContainer(Composite parent) {
		Composite queueContainer = new Composite(parent, SWT.BORDER);
		GridData gdQueueContainer = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gdQueueContainer.heightHint = 300;
        queueContainer.setLayoutData(gdQueueContainer);
        queueContainer.setLayout(new GridLayout(2, false));
        
        Label lblQueuedScripts = new Label(queueContainer, SWT.NONE);
        lblQueuedScripts.setText("Queued scripts (double click name to see contents):");
        lblQueuedScripts.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
        
        final ListViewer queuedScriptsViewer = new ListViewer(queueContainer, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
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
        
        queuedScriptsViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) queuedScriptsViewer.getSelection();
                queueScriptViewModel.setSelectedScript((QueuedScript) selection.getFirstElement());
			}
		});

        Composite moveComposite = new Composite(queueContainer, SWT.NONE);
	    moveComposite.setLayout(new GridLayout(1, false));
	    moveComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        
        Button btnScriptUp =  new Button(moveComposite, SWT.NONE);
		GridData gdBtnScriptUp = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gdBtnScriptUp.widthHint = 25;
		btnScriptUp.setLayoutData(gdBtnScriptUp);
		btnScriptUp.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_up.png"));
        btnScriptUp.setToolTipText("Move selected script UP");
		
		Button btnScriptDown =  new Button(moveComposite, SWT.NONE);
		GridData gdBtnScriptDown = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gdBtnScriptDown.widthHint = 25;
		btnScriptDown.setLayoutData(gdBtnScriptDown);
		btnScriptDown.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_down.png"));
		btnScriptDown.setToolTipText("Move selected script DOWN");
		
        Composite scriptSendGrp = new Composite(queueContainer, SWT.NONE);
        scriptSendGrp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout ssgLayout = new GridLayout(2, false);
        ssgLayout.marginHeight = 10;
        ssgLayout.marginWidth = 10;
        scriptSendGrp.setLayout(ssgLayout);
		        
        final Button btnCreateScript = new Button(scriptSendGrp, SWT.NONE);
        btnCreateScript.setText("Create Script and Add to Queue");
        btnCreateScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        final Button btnDequeueScript = new Button(scriptSendGrp, SWT.NONE);
        btnDequeueScript.setText("Dequeue Selected Script");
        btnDequeueScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        btnCreateScript.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
        	QueueScriptDialog dialog = new QueueScriptDialog(shell, queueScriptViewModel);
        	dialog.open();
        }
        });
                
        btnDequeueScript.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                queueScriptViewModel.dequeueScript();
            }
        });
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnDequeueScript), 
        		BeanProperties.value("dequeueButtonEnabled").observe(queueScriptViewModel));

        btnScriptUp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                queueScriptViewModel.moveScript(true);
            }
        });
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnScriptUp), 
        		BeanProperties.value("upButtonEnabled").observe(queueScriptViewModel));

        btnScriptDown.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                queueScriptViewModel.moveScript(false);
            }
        });
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(btnScriptDown), 
        		BeanProperties.value("downButtonEnabled").observe(queueScriptViewModel));

        
		
	}

	private void createOutputContainer(Composite parent) {
    	Composite outputContainer = new Composite(parent, SWT.BORDER);
        outputContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        outputContainer.setLayout(new GridLayout(1, false));
        
        Label lblOutput = new Label(outputContainer, SWT.NONE);
        lblOutput.setText("Output");
        

        final StyledText txtOutput = new StyledText(outputContainer, SWT.V_SCROLL | SWT.BORDER);
        txtOutput.setEditable(false);
        txtOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));

        bindingContext.bindValue(WidgetProperties.text().observe(txtOutput),
                BeanProperties.value("log").observe(outputLogViewModel));
        
        txtOutput.addListener(SWT.Modify, new Listener() {
            @Override
            public void handleEvent(Event e) {
                txtOutput.setTopIndex(txtOutput.getLineCount() - 1);
            }
        });
    }
	
    private void createCurrentScriptContainer(Composite parent) {
    	Composite currentScriptContainer = new Composite(parent, SWT.BORDER);
        currentScriptContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        currentScriptContainer.setLayout(new GridLayout(1, false));
        
        Composite currentScriptInfoContainer = new Composite(currentScriptContainer, SWT.NONE);
        currentScriptInfoContainer.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        currentScriptInfoContainer.setLayout(new GridLayout(2, false));
        
        lblCurrentScriptStatus = new Label(currentScriptInfoContainer, SWT.NONE);
        lblCurrentScriptStatus.setText("Current script status: ");
        
        Label lineNumberIndicator = new Label(currentScriptInfoContainer, SWT.NONE);
        lineNumberIndicator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        bindingContext.bindValue(WidgetProperties.text().observe(lineNumberIndicator),
                BeanProperties.value("lineNumber").observe(scriptStatusViewModel));
        
        Label lblStatusReadback = new Label(currentScriptInfoContainer, SWT.NONE);
        lblStatusReadback.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        
        bindingContext.bindValue(WidgetProperties.text().observe(lblStatusReadback),
                BeanProperties.value("statusReadback").observe(scriptStatusViewModel));
        
        NicosControlButtonPanel controlPanel =
                new NicosControlButtonPanel(currentScriptContainer, SWT.NONE, scriptStatusViewModel);
        controlPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        StyledText txtCurrentScript = new StyledText(currentScriptContainer, SWT.BORDER);
        txtCurrentScript.setEditable(false);
        txtCurrentScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        bindingContext.bindValue(WidgetProperties.text().observe(txtCurrentScript),
                BeanProperties.value("currentlyExecutingScript").observe(model));
	}
    
    private void createStatusContainer(Composite parent) {
    	Composite statusContainer = new Composite(parent, SWT.BORDER);
        statusContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        statusContainer.setLayout(new GridLayout(1, false));
        
        Composite nicosStatus = new Composite(statusContainer, SWT.NONE);
        nicosStatus.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        nicosStatus.setLayout(new GridLayout(2, false));
        
        lblCurrentError = new Label(nicosStatus, SWT.NONE);
        lblCurrentError.setText("NICOS status: ");
        
        Label errorIndicator = new Label(nicosStatus, SWT.NONE);
        errorIndicator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        bindingContext.bindValue(WidgetProperties.text().observe(errorIndicator),
                BeanProperties.value("error").observe(model));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus() {
        lblCurrentScriptStatus.setFocus();
    }

}
