
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

package uk.ac.stfc.isis.ibex.ui.nicos.dialogs;

import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;

/**
 * The dialog for queueing a new script to send to the script server.
 */
public class QueueScriptDialog extends Dialog {

	private static final Point INITIAL_SIZE = new Point(950, 800);
	
	private QueueScriptPanel creator;
    private Button queueBtn;

    private QueueScriptViewModel model;

    private static final String DEFAULT_SCRIPT_DIRECTORY = "c:\\scripts";
    private static final String[] ALLOWED_SCRIPT_NAMES = {"Python Scripts (*.py)"};
    private static final String[] ALLOWED_SCRIPT_EXTENSIONS = {"*.py"};

	/**
     * The constructor for this class.
     * 
     * @param parentShell
     *            The shell that this dialog is created from.
     * @param model
     *            the model for queueing a script
     */
    public QueueScriptDialog(Shell parentShell, QueueScriptViewModel model) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM);
        this.model = model;
	}
	
    private String getLoadScriptPath() {
		FileDialog dialog = new FileDialog(getShell());
		dialog.setFilterExtensions(ALLOWED_SCRIPT_EXTENSIONS);
		dialog.setFilterNames(ALLOWED_SCRIPT_NAMES);
		dialog.setFilterPath(DEFAULT_SCRIPT_DIRECTORY);
		return dialog.open();
    }
    
    private void loadScript() {
    	String scriptPath = getLoadScriptPath();
		if (!Strings.isNullOrEmpty(scriptPath)) {
			try {
				model.loadContentsFromFile(scriptPath);
			} catch (IOException e) {
				MessageBox dialog = new MessageBox(getShell(), SWT.ICON_ERROR);
				dialog.setMessage("Opening " + scriptPath + " failed.");
				dialog.setText("Load Script Failed");
				dialog.open();
			}
		}
    }
    
	@Override
	protected Control createDialogArea(Composite parent) {
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT | SWT.WRAP | SWT.LEFT);
	    ToolItem loadScript = new ToolItem(toolbar, SWT.PUSH);
	    loadScript.setToolTipText("Load Script");
	    loadScript.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.nicos", "icons/open_folder.png"));
	    loadScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadScript();
			}
		});
	    
        creator = new QueueScriptPanel(parent, SWT.NONE, model);
        creator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return creator;
	}

	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
        queueBtn = createButton(parent, IDialogConstants.OK_ID, "Queue", false);
        queueBtn.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/play.png"));

        queueBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                model.queueScript();
            }
        });
		
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", true);
	}	
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
        shell.setText("Queue Script");
	}
	
	@Override
	protected Point getInitialSize() {
		return INITIAL_SIZE;
	}
}
