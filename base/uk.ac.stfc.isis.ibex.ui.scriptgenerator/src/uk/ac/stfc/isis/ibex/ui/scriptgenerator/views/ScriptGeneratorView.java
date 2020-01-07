
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;


import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;

/**
 * Provides settings to control the script generator.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ScriptGeneratorView {
	private DataBindingContext bindingContext;
	private ActionsViewTable table;
	private ComboViewer configSelector;
	private static final Display DISPLAY = Display.getCurrent();
	private ScriptGeneratorViewModel scriptGeneratorViewModel;
	
	private Button createMoveRowButton(Composite parent, String icon, String direction) {
        Button moveButton =  new Button(parent, SWT.NONE);
		GridData gdBtnMoveRow = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gdBtnMoveRow.widthHint = 25;
		moveButton.setLayoutData(gdBtnMoveRow);
		moveButton.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/" + icon));
        moveButton.setToolTipText("Move selected row " + direction);
        return moveButton;
	}
	
	/**
	 * Container for the UI objects.
	 * 
	 * @param parent the parent composite.
	 */
	@PostConstruct
	public void createPartControl(Composite parent) {
		
		scriptGeneratorViewModel = new ScriptGeneratorViewModel();

		GridData gdQueueContainer = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gdQueueContainer.heightHint = 300;
		parent.setLayoutData(gdQueueContainer);
		parent.setLayout(new GridLayout(1, false));
		
		Composite topBarComposite = new Composite(parent, SWT.NONE);
		topBarComposite.setLayout(new GridLayout(2, false));
		topBarComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite globalSettingsComposite = new Composite(topBarComposite, SWT.NONE);
		globalSettingsComposite.setLayout(new GridLayout(2, false));
		globalSettingsComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		
		Label configSelectorLabel = new Label(globalSettingsComposite, SWT.NONE);
		configSelectorLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		configSelectorLabel.setText("Config:");

		// Drop-down box to select between configs.
		configSelector = setUpConfigSelector(globalSettingsComposite);
		
		Composite validityComposite = new Composite(topBarComposite, SWT.NONE);
		validityComposite.setLayout(new GridLayout(1, false));
		validityComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		// Button to check validity errors
		final Button btnGetValidityErrors = new Button(validityComposite, SWT.NONE);
        btnGetValidityErrors.setText("Get Validity Errors");
        btnGetValidityErrors.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        btnGetValidityErrors.addListener(SWT.Selection, e -> displayValidityErrors());
        btnGetValidityErrors.setBackground(new Color(DISPLAY, 255, 204, 203));
        
        Composite tableContainerComposite = new Composite(parent, SWT.NONE);
        tableContainerComposite.setLayout(new GridLayout(2, false));
        tableContainerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table = new ActionsViewTable(tableContainerComposite, SWT.NONE, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION, scriptGeneratorViewModel);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
		// Composite for move action up/down buttons
		Composite moveComposite = new Composite(tableContainerComposite, SWT.NONE);
	    moveComposite.setLayout(new GridLayout(1, false));
	    moveComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        
	    // Make buttons to move an action up and down the list
        Button btnMoveActionUp = createMoveRowButton(moveComposite, "move_up.png", "up");
        btnMoveActionUp.addListener(SWT.Selection, e -> scriptGeneratorViewModel.moveActionUp(table.getSelectionIndex()));
        
        Button btnMoveActionDown = createMoveRowButton(moveComposite, "move_down.png", "down");
        btnMoveActionDown.addListener(SWT.Selection, e -> scriptGeneratorViewModel.moveActionDown(table.getSelectionIndex()));
        
        // Composite for laying out new/delete/duplicate action buttons
        Composite actionsControlsGrp = new Composite(parent, SWT.NONE);
        actionsControlsGrp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout ssgLayout = new GridLayout(3, false);
        ssgLayout.marginHeight = 10;
        ssgLayout.marginWidth = 10;
        actionsControlsGrp.setLayout(ssgLayout);
		        
        // Make buttons for insert new/delete/duplicate actions
        final Button btnInsertAction = new Button(actionsControlsGrp, SWT.NONE);
        btnInsertAction.setText("Add Action");
        btnInsertAction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        btnInsertAction.addListener(SWT.Selection, e -> scriptGeneratorViewModel.addEmptyAction());
        
        final Button btnDeleteAction = new Button(actionsControlsGrp, SWT.NONE);
        btnDeleteAction.setText("Delete Action");
        btnDeleteAction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        btnDeleteAction.addListener(SWT.Selection, e -> scriptGeneratorViewModel.deleteAction(table.getSelectionIndex()));

        final Button btnDuplicateAction = new Button(actionsControlsGrp, SWT.NONE);
        btnDuplicateAction.setText("Duplicate Action");
        btnDuplicateAction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        btnDuplicateAction.addListener(SWT.Selection, e -> scriptGeneratorViewModel.duplicateAction(table.getSelectionIndex()));
        		
        bind();
		
        scriptGeneratorViewModel.addEmptyAction();
	}
	
	/**
	 * Creates a new combo box and configures sets its input to the config loader.
	 * @param globalSettingsComposite
	 * 			The composite to draw the box in
	 * @return configSelector
	 * 			Combo box with available configurations
	 */
	private ComboViewer setUpConfigSelector(Composite globalSettingsComposite) {
		configSelector = new ComboViewer(globalSettingsComposite, SWT.READ_ONLY);

		configSelector.setContentProvider(ArrayContentProvider.getInstance());
		configSelector.setLabelProvider(scriptGeneratorViewModel.getConfigSelectorLabelProvider());
		configSelector.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		configSelector.setInput(scriptGeneratorViewModel.getAvailableConfigs());
		configSelector.setSelection(new StructuredSelection(scriptGeneratorViewModel.getConfig()));
		
		return configSelector;
	}
	
	/**
	 * Binds the Script Generator Table and config selector models to their views.
	 */
	private void bind() {
		bindingContext = new DataBindingContext();
		
		scriptGeneratorViewModel.bindConfigLoader(bindingContext, configSelector);

		scriptGeneratorViewModel.bindValidityChecks(table);
	}
	
	public void displayValidityErrors() {
		String heading = "Validity errors:\n\n";
		String body = heading + scriptGeneratorViewModel.getFirstNLinesOfInvalidityErrors(10);
		MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Validity Errors", body);
	}
	

}
