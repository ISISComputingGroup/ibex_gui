
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
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.ui.nicos.dialogs.QueueScriptDialog;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ConnectionStatusConverter;
import uk.ac.stfc.isis.ibex.ui.nicos.models.OutputLogViewModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptSendStatusConverter;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptStatusViewModel;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;

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

    private Label lblCurrentScriptStatus;

	private ScriptStatusViewModel scriptStatusViewModel;
    private OutputLogViewModel outputLogViewModel;

    /**
     * The default constructor for the view.
     */
    public NicosView() {
        model = Nicos.getDefault().getModel();
        queueScriptViewModel = new QueueScriptViewModel(model, INITIAL_SCRIPT);
        scriptStatusViewModel = new ScriptStatusViewModel(model);
        outputLogViewModel = new OutputLogViewModel(model);

        shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }

    @Override
    public void createPartControl(Composite parent) {
        GridLayout glParent = new GridLayout(2, true);
        glParent.marginRight = 10;
        glParent.marginHeight = 10;
        glParent.marginWidth = 10;
        parent.setLayout(glParent);

        // Connection info
        Composite connectionGrp = new Composite(parent, SWT.NONE);
        connectionGrp.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1));
        GridLayout connLayout = new GridLayout(2, false);
        connLayout.marginRight = 10;
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
        
        Composite currentScriptInfoContainer = new Composite(parent, SWT.NONE);
        currentScriptInfoContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        currentScriptInfoContainer.setLayout(new GridLayout(2, false));
        
        lblCurrentScriptStatus = new Label(currentScriptInfoContainer, SWT.NONE);
        lblCurrentScriptStatus.setText("Current script status: ");
        
        Label lineNumberIndicator = new Label(currentScriptInfoContainer, SWT.NONE);
        lineNumberIndicator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        bindingContext.bindValue(WidgetProperties.text().observe(lineNumberIndicator),
                BeanProperties.value("lineNumber").observe(scriptStatusViewModel));
        
        Label lblOutput = new Label(parent, SWT.NONE);
        lblOutput.setText("Output");
        
        StyledText txtCurrentScript = new StyledText(parent, SWT.BORDER);
        txtCurrentScript.setEditable(false);
        txtCurrentScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        final StyledText txtOutput = new StyledText(parent, SWT.V_SCROLL | SWT.BORDER);
        txtOutput.setEditable(false);
        txtOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
        
        bindingContext.bindValue(WidgetProperties.text().observe(txtCurrentScript),
                BeanProperties.value("currentlyExecutingScript").observe(model));

        bindingContext.bindValue(WidgetProperties.text().observe(txtOutput),
                BeanProperties.value("log").observe(outputLogViewModel));
        
        txtOutput.addListener(SWT.Modify, new Listener() {
            @Override
            public void handleEvent(Event e) {
                txtOutput.setTopIndex(txtOutput.getLineCount() - 1);
            }
        });
        
        Label lblQueuedScripts = new Label(parent, SWT.NONE);
        lblQueuedScripts.setText("Queued scripts:");
            
        Composite tableComposite = new Composite(parent, SWT.NONE);
        tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        TableColumnLayout tableColumnLayout = new TableColumnLayout();
        tableComposite.setLayout(tableColumnLayout);
        
        final TableViewer tableViewer = new TableViewer(tableComposite, SWT.HIDE_SELECTION | SWT.BORDER | SWT.NO_SCROLL | SWT.V_SCROLL);        
       
        tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        tableViewer.getTable().setToolTipText("");
        
        TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.CENTER);
        tableColumnLayout.setColumnData(tableViewerColumn.getColumn(), new ColumnWeightData(100, 0, false)); 
        tableViewerColumn.setLabelProvider(new OwnerDrawLabelProvider() {

        	private String getText(Object element) {
        		QueuedScript script = (QueuedScript) element;
        		return script.script;
        	}
        	
			@Override
			protected void measure(Event event, Object element) {
				event.width = tableViewer.getTable().getColumn(event.index).getWidth();
				if (event.width == 0) {
					return;
				}
				Point size = event.gc.textExtent(getText(element));
				int lines = size.x / event.width + 1;
				event.height = size.y * lines;
			}

			@Override
			protected void paint(Event event, Object element) {
				event.gc.drawText(getText(element), event.x, event.y, true);
			}
		});
        
        tableViewer.setContentProvider(new ObservableListContentProvider());
        tableViewer.setInput(BeanProperties.list("queuedScripts").observe(model));
        
        Composite scriptSendGrp = new Composite(parent, SWT.NONE);
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
        
        NicosControlButtonPanel controlPanel =
                new NicosControlButtonPanel(parent, SWT.NONE, scriptStatusViewModel);
        controlPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    }

    /**
     * 
     */
    @Override
    public void setFocus() {
        lblCurrentScriptStatus.setFocus();
    }

}
