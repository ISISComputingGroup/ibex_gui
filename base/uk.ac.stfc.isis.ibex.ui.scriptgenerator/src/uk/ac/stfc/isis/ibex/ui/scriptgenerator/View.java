
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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.scriptgenerator.PythonBuilder;
import uk.ac.stfc.isis.ibex.scriptgenerator.estimate.Estimate;
import uk.ac.stfc.isis.ibex.scriptgenerator.row.Row;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.ApertureSans;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.ApertureTrans;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.CollectionMode;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.Order;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.SampleGeometry;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.Settings;

/**
 * Holds all UI elements of the Script Generator.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class View extends ViewPart {
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.scriptgenerator.scriptgeneratorview";
	
	private Button btnPreview;
	private Button btnWrite;
	private Button btnClearTable;
	private TablePanel table;
	
	private Collection<Row> rows;
	private PythonBuilder builder;
	private Estimate estimate;
	private Settings settings;
	private String script;
	 
	/**
	 * The default constructor.
	 */
	public View() {
		super();
	
		builder = new PythonBuilder();
		estimate = new Estimate(40, 120);
		settings = new Settings(1, 1, 7, 7, Order.TRANS, false, ApertureSans.MEDIUM, ApertureTrans.MEDIUM, SampleGeometry.DISC, CollectionMode.HISTOGRAM);
		
		rows = new ArrayList<Row>();
		rows.add(new Row());
        builder.setRows(rows);
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
		
		SettingsPanel settingsPanel = new SettingsPanel(topPanel, SWT.BORDER_SOLID, settings);
		GridLayout glSettingsPanel = (GridLayout) settingsPanel.getLayout();
		glSettingsPanel.makeColumnsEqualWidth = true;
		settingsPanel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2));
		
		new Label(topPanel, SWT.NONE);
		
		EstimatePanel estimatePanel = new EstimatePanel(topPanel, SWT.NONE, estimate);
		estimatePanel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		SaveLoadPanel saveLoadPanel = new SaveLoadPanel(topPanel, SWT.NONE);
		GridLayout gridLayout = (GridLayout) saveLoadPanel.getLayout();
		gridLayout.marginWidth = 0;
		saveLoadPanel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		new Label(topPanel, SWT.NONE);
		
		Composite buttonsComposite = new Composite(topPanel, SWT.NONE);
		Shell buttonsShell = new Shell(buttonsComposite.getShell());
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
		
		table = new TablePanel(parent, SWT.NONE, rows);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		bind(buttonsShell);
	}
	
	/**
	 * Binding for the Write Script and Preview Script buttons.
	 * @param saveShell the shell of the composite used for Write Script
	 */
	public void bind(final Shell saveShell) {
		btnPreview.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                script = builder.getScript();

                Shell shell = new Shell();
                MessageDialog.openInformation(shell, "Script Preview", script);
            }
        });
		
		btnWrite.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String defaultFileName = ".py";

        		FileDialog dialog = new FileDialog(saveShell, SWT.SAVE);

        		dialog.setFilterNames(new String[] {"Python File (*.py)", 
        				"All Files (*.*)" });
        		dialog.setFilterExtensions(new String[] {"*.py"});
        		dialog.setFilterPath("c:\\");
        		dialog.setFileName(defaultFileName);

        		String filename = dialog.open();
        		
        		if (filename != null) {
        			String script = builder.getScript();
        			PrintWriter writer;
					try {
						writer = new PrintWriter(filename);
						writer.print(script);
						writer.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
        		}
            }
        });
		
		btnClearTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
	}
	
	@Override
	public void setFocus() {
		
	}

}