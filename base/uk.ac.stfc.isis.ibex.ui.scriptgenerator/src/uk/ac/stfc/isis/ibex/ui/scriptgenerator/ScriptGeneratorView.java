
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

package uk.ac.stfc.isis.ibex.ui.scriptgenerator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.scriptgenerator.PythonBuilder;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorRow;
import uk.ac.stfc.isis.ibex.ui.scriptgenerator.ScriptGeneratorTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Text;

/**
 * Holds all UI elements of the Script Generator.
 */
public class ScriptGeneratorView extends ViewPart {
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.scriptgenerator.scriptgeneratorview";
	
	private Button btnPreview;
	private Button btnWrite;
	private Button btnClearTable;
	private TablePanel table;
	
	private Collection<ScriptGeneratorRow> rows;
	private PythonBuilder builder;
	private String script;
	 
	public ScriptGeneratorView() {
		super();
		
		builder = new PythonBuilder();
	}

	@SuppressWarnings("unused")
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, true));
		
		Composite topPanel = new Composite(parent, SWT.NONE);
		topPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		topPanel.setLayout(new GridLayout(5, false));
		
		Label lblSettingsTitle = new Label(topPanel, SWT.LEFT);
		lblSettingsTitle.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblSettingsTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblSettingsTitle.setText(" Settings");
		
		Label lblSeparator = new Label(topPanel, SWT.NONE);
		GridData gdSeparator = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gdSeparator.widthHint = 20;
		lblSeparator.setLayoutData(gdSeparator);
		
		Label lblEstimateTitle = new Label(topPanel, SWT.LEFT);
		lblEstimateTitle.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblEstimateTitle.setText(" Estimated Script Time");
		
		Label lblSeparator2 = new Label(topPanel, SWT.NONE);
		GridData gdSeparator2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2);
		gdSeparator2.widthHint = 20;
		lblSeparator2.setLayoutData(gdSeparator2);
		
		Label lblSaveLoad = new Label(topPanel, SWT.LEFT);
		lblSaveLoad.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblSaveLoad.setText("  Save / Load");
		
		SettingsPanel settingsPanel = new SettingsPanel(topPanel, SWT.BORDER_SOLID);
		GridLayout glSettingsPanel = (GridLayout) settingsPanel.getLayout();
		glSettingsPanel.makeColumnsEqualWidth = true;
		settingsPanel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2));
		
		new Label(topPanel, SWT.NONE);
		
		EstimatePanel estimatePanel = new EstimatePanel(topPanel, SWT.NONE);
		estimatePanel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		SaveLoadPanel saveLoadPanel = new SaveLoadPanel(topPanel, SWT.NONE);
		GridLayout gridLayout = (GridLayout) saveLoadPanel.getLayout();
		gridLayout.marginWidth = 0;
		saveLoadPanel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		new Label(topPanel, SWT.NONE);
		
		Composite buttonsComposite = new Composite(topPanel, SWT.NONE);
		buttonsComposite.setLayout(new GridLayout(2, true));
		buttonsComposite.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 1, 1));
		
		btnPreview = new Button(buttonsComposite, SWT.NONE);
		btnPreview.setText("Preview Script");
		GridData gdButtonPreview = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gdButtonPreview.minimumWidth = 80;
		btnPreview.setLayoutData(gdButtonPreview);
		
		btnWrite = new Button(buttonsComposite, SWT.NONE);
		btnWrite.setText("Write Script");
		GridData gdButtonWrite = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gdButtonWrite.minimumWidth = 80;
		btnWrite.setLayoutData(gdButtonWrite);
		
		new Label(topPanel, SWT.NONE);
		
		btnClearTable = new Button(topPanel, SWT.NONE);
		btnClearTable.setText("Clear Table");
		GridData gdButtonClearTable = new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1);
		gdButtonClearTable.minimumWidth = 80;
		btnClearTable.setLayoutData(gdButtonClearTable);
		
		table = new TablePanel(parent, SWT.NONE);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
//		rows = tablePanel.getRows(); 
//		buildPython(rows);
		
//		temporary colours
//		buttonsComposite.setBackground(settingsPanel.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
//		settingsPanel.setBackground(settingsPanel.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
//		estimatePanel.setBackground(estimatePanel.getDisplay().getSystemColor(SWT.COLOR_BLUE)); 
//		buttonsPanel.setBackground(buttonsPanel.getDisplay().getSystemColor(SWT.COLOR_GREEN));  
		
		bind();
	}
	
	public void bind() {
		btnPreview.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                builder.setRows(table.getRows());
                script = builder.getScript();

                Shell shell = new Shell();
                MessageDialog.openInformation(shell, "Script Preview", script);
            }
        });
		
		btnWrite.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                builder.setRows(table.getRows());
                script = builder.getScript();
            }
        });
		
		// btnClearTable etc
	}
	
	public void buildPython(Collection<ScriptGeneratorRow> rows) {
		// build Python
	}
	
	
	public void sendToNicos() {
		// send to NICOS
	}
	
	public void saveToCsv() {
		// save to csv
	}
	
	@Override
	public void setFocus() {
		
	}

}