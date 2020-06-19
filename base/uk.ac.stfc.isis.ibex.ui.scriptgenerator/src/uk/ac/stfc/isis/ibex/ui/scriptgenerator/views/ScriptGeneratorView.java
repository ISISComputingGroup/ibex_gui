
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


import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

/**
 * Provides the UI to control the script generator.
 * 
 * Uses code from http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/SWTTableSimpleDemo.htm
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ScriptGeneratorView {
	
	private static PreferenceSupplier preferences = new PreferenceSupplier();
	
	
	private static final Display DISPLAY = Display.getDefault();

	/**
	 * A clear colour for use in other script generator table columns when a row is valid.
	 */
	private static final Color CLEAR_COLOUR = DISPLAY.getSystemColor(SWT.COLOR_WHITE);
	
	/**
	 * The ViewModel the View is updated by.
	 */
	private ScriptGeneratorViewModel scriptGeneratorViewModel;

	/**
	 * The main parent for UI elements
	 */
	private Composite mainParent;
	
	/**
	 * The string to display if there is no script defininitions to select.
	 */
	private static final String NO_SCRIPT_DEFINITIONS_MESSAGE = String.format("\u26A0 Warning: Could not load any script definitions from %s"
			+ System.getProperty("line.separator")
			+ "Have they been located in the correct place or is this not your preferred location?", 
			preferences.scriptGeneratorScriptDefinitionFolders());
	
	/**
	 * The string to display if python is loading.
	 */
	private static final String LOADING_MESSAGE = "Loading...";
	
	/**
	 * The string to display if python is loading.
	 */
	private static final String RELOADING_MESSAGE = "Reloading...";
	
	/**
	 * Denotes whether script definitions have been loaded once.
	 */
	private boolean scriptDefinitionsLoadedOnce = false;
	
	/**
	 * A property to listen for when python becomes ready or not ready.
	 */
	private static final String PYTHON_READINESS_PROPERTY = "python ready";
	
	/**
	 * Create a button to manipulate the rows of the script generator table and
	 *  move them up and down.
	 * 
	 * @param parent The composite the button will live in.
	 * @param icon The icon to display on the button.
	 * @param direction The direction of the button "up" or "down".
	 * @return The created button.
	 */
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
		
		mainParent = parent;
		
		scriptGeneratorViewModel.addPropertyChangeListener(PYTHON_READINESS_PROPERTY, evt -> {
			boolean ready = (boolean) evt.getNewValue();
			if (ready) {
				scriptGeneratorViewModel.reloadScriptDefinitions();
				displayLoaded();
			} else {
				displayLoading();
			}
		});
		
		scriptGeneratorViewModel.setUpModel();
	}
	
	/**
	 * Destroy all child elements of the mainParent.
	 */
	private void destroyUIContents() {
		for (Control child : mainParent.getChildren()) {
			child.dispose();
		}
	}
	
	/**
	 * Display loading.
	 */
	private void displayLoading() {
		DISPLAY.asyncExec(() -> {
			destroyUIContents();
			Label loadingMessage = new Label(mainParent, SWT.NONE);
			loadingMessage.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
			// Make the warning label bigger from: https://stackoverflow.com/questions/1449968/change-just-the-font-size-in-swt
			FontData[] fD = loadingMessage.getFont().getFontData();
			fD[0].setHeight(16);
			loadingMessage.setFont(new Font(Display.getDefault(), fD[0]));
			if (scriptDefinitionsLoadedOnce) {
				loadingMessage.setText(RELOADING_MESSAGE);
			} else {
				loadingMessage.setText(LOADING_MESSAGE);
			}
			
			mainParent.layout();
		});
	}
	
	/**
	 * Display when loaded.
	 */
	private void displayLoaded() {
		DISPLAY.asyncExec(() -> {
			scriptDefinitionsLoadedOnce = true;
			destroyUIContents();
			if (scriptGeneratorViewModel.scriptDefinitionsAvailable()) {
				
				// A composite to contain the elements at the top of the script generator
				Composite topBarComposite = new Composite(mainParent, SWT.NONE);
				topBarComposite.setLayout(new GridLayout(6, false));
				topBarComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
				// Composite to contain help strings from script definitions
		        Composite scriptDefinitionComposite = new Composite(topBarComposite, SWT.NONE);
		        scriptDefinitionComposite.setLayout(new GridLayout(5, false));
		        scriptDefinitionComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		        
		        // The label for the script definition selector drop down
	 			Label scriptDefinitionSelectorLabel = new Label(scriptDefinitionComposite, SWT.NONE);
	 			scriptDefinitionSelectorLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
	 			scriptDefinitionSelectorLabel.setText("Script Definition:");
	 	
	 			// Drop-down box to select between script definitions.
	 			ComboViewer scriptDefinitionSelector = setUpScriptDefinitionSelector(scriptDefinitionComposite);
	 			
	 			// Separate help and selector
	 			new Label(scriptDefinitionComposite, SWT.SEPARATOR | SWT.VERTICAL);
		        
	 			// Label for script definition help
		        Label helpLabel = new Label(scriptDefinitionComposite, SWT.NONE);
				helpLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				helpLabel.setText("Help: ");
				
				// Display help for the script definition
				Text helpText = new Text(scriptDefinitionComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
				var helpTextDataLayout = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false, 1, 1);
				helpTextDataLayout.widthHint = 400;
				helpTextDataLayout.heightHint = 100;
				helpText.setLayoutData(helpTextDataLayout);
				helpText.setBackground(CLEAR_COLOUR);
				// Display the correct starting text
				scriptGeneratorViewModel.getScriptDefinition().ifPresentOrElse(
							scriptDefinition -> {
								Optional.ofNullable(scriptDefinition.getHelp()).ifPresentOrElse(
									helpString -> helpText.setText(helpString),
									() -> helpText.setText("")
								);
							},
							() -> helpText.setText("")
						);
				
	            // Composite for the Open Manual button
	            Composite manualButtonComposite = new Composite(topBarComposite, SWT.NONE);
	            manualButtonComposite.setLayout(new GridLayout(1, false));
	            manualButtonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

	            // Button to open the user manual in a button
	            final Button manualButton = new Button(manualButtonComposite, SWT.NONE);
	            manualButton.setEnabled(false);
	            manualButton.setText("Open Manual");
	            manualButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	            
				// The composite to contain the button to check validity
				Composite validityComposite = new Composite(topBarComposite, SWT.NONE);
				validityComposite.setLayout(new GridLayout(1, false));
				validityComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				
				// Button to check validity errors
				final Button btnGetValidityErrors = new Button(validityComposite, SWT.NONE);
		        btnGetValidityErrors.setText("Get Validity Errors");
		        btnGetValidityErrors.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		        btnGetValidityErrors.addListener(SWT.Selection, e -> {
		        	scriptGeneratorViewModel.displayValidityErrors();
		        });
		        
		        Map<String, String> scriptDefinitionLoadErrors = scriptGeneratorViewModel.getScriptDefinitionLoadErrors();
		        
		        if (!scriptDefinitionLoadErrors.isEmpty()) {
			        setUpScriptDefinitionLoadErrorTable(mainParent, scriptDefinitionLoadErrors); 			    
		        }
		        
		        // The composite to contain the UI table
		        Composite tableContainerComposite = new Composite(mainParent, SWT.NONE);
		        tableContainerComposite.setLayout(new GridLayout(2, false));
		        tableContainerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				
		        // The UI table
				ActionsViewTable table = 
						new ActionsViewTable(tableContainerComposite,
								SWT.NONE, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION,
								scriptGeneratorViewModel);
				table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				scriptGeneratorViewModel.reloadActions();
		        
				// Composite for move action up/down buttons
				Composite moveComposite = new Composite(tableContainerComposite, SWT.NONE);
			    moveComposite.setLayout(new GridLayout(1, false));
			    moveComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		        
			    // Make buttons to move an action up and down the list
		        Button btnMoveActionUp = createMoveRowButton(moveComposite, "move_up.png", "up");
		        btnMoveActionUp.addListener(SWT.Selection, e ->	scriptGeneratorViewModel.moveActionUp(table.selectedRows()));
		        
		        Button btnMoveActionDown = createMoveRowButton(moveComposite, "move_down.png", "down");
		        btnMoveActionDown.addListener(SWT.Selection, e -> scriptGeneratorViewModel.moveActionDown(table.selectedRows()));
		        
                // Composite for the row containing the total estimated run time
                Composite estimateGrp = new Composite(mainParent, SWT.NONE);
                estimateGrp.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
                GridLayout estimateLayout = new GridLayout(1, true);
                estimateLayout.marginRight = 40;
                estimateGrp.setLayout(estimateLayout);
                
                // Label for the total estimated run time
                Label estimateText = new Label(estimateGrp, SWT.RIGHT);
                estimateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                String currentFont = estimateText.getFont().getFontData()[0].getName();
                Font font = new Font(estimateText.getDisplay(), new FontData(currentFont, 11, SWT.BOLD));
                estimateText.setFont(font);
                estimateText.setText("Total estimated run time: 0 seconds");
		        
		        // Composite for laying out new/delete/duplicate action buttons
		        Composite actionsControlsGrp = new Composite(mainParent, SWT.NONE);
		        actionsControlsGrp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		        GridLayout ssgLayout = new GridLayout(4, true);
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
		        btnDeleteAction.addListener(SWT.Selection, e -> scriptGeneratorViewModel.deleteAction(table.selectedRows()));
		
		        final Button btnDuplicateAction = new Button(actionsControlsGrp, SWT.NONE);
		        btnDuplicateAction.setText("Duplicate Action");
		        btnDuplicateAction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		        btnDuplicateAction.addListener(SWT.Selection, e -> scriptGeneratorViewModel.duplicateAction(table.selectedRows(), table.getSelectionIndex()));
		        
                final Button btnClearAction = new Button(actionsControlsGrp, SWT.NONE);
                btnClearAction.setText("Clear All Actions");
                btnClearAction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
                btnClearAction.addListener(SWT.Selection, e -> scriptGeneratorViewModel.clearAction());
                
		        // Composite for generate buttons
		        Composite generateButtonsGrp = new Composite(mainParent, SWT.NONE);
		        generateButtonsGrp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		        GridLayout gbgLayout = new GridLayout(3, true);
		        gbgLayout.marginHeight = 10;
		        gbgLayout.marginWidth = 10;
		        generateButtonsGrp.setLayout(gbgLayout);
		        
		        // Button to generate a script
		        final Button generateScriptButton = new Button(generateButtonsGrp, SWT.NONE);
		        generateScriptButton.setText("Generate Script");
		        generateScriptButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		        generateScriptButton.addListener(SWT.Selection, e -> scriptGeneratorViewModel.generate());
		        
		       	
		       	final Button saveExperimentalParametersButton = new Button(generateButtonsGrp, SWT.NONE);
		       	saveExperimentalParametersButton.setText("Save Parameters");
		       	saveExperimentalParametersButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		       	saveExperimentalParametersButton.addListener(SWT.Selection, e -> scriptGeneratorViewModel.saveParameterValues());
		       	
		       	final Button loadExperimentalParametersButton = new Button(generateButtonsGrp, SWT.NONE);
		       	loadExperimentalParametersButton.setText("Load Parameters");
		       	loadExperimentalParametersButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		       	loadExperimentalParametersButton.addListener(SWT.Selection, e -> scriptGeneratorViewModel.loadParameterValues());
		        // Bind the context and the validity checking listeners
		        bind(scriptDefinitionSelector,
		                table,
		                btnGetValidityErrors,
		                generateScriptButton,
		                estimateText,
		                helpText,
		                saveExperimentalParametersButton,
		                manualButton);
				
			} else {
				
				Label warningMessage = new Label(mainParent, SWT.NONE);
				warningMessage.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
				// Make the warning label bigger from: https://stackoverflow.com/questions/1449968/change-just-the-font-size-in-swt
				FontData[] fD = warningMessage.getFont().getFontData();
				fD[0].setHeight(16);
				warningMessage.setFont(new Font(Display.getDefault(), fD[0]));
				warningMessage.setText(NO_SCRIPT_DEFINITIONS_MESSAGE);
				warningMessage.pack();
				
				Map<String, String> scriptDefinitionLoadErrors = scriptGeneratorViewModel.getScriptDefinitionLoadErrors();
		        
		        if (!scriptDefinitionLoadErrors.isEmpty()) {
			        setUpScriptDefinitionLoadErrorTable(mainParent, scriptDefinitionLoadErrors); 			    
		        }
				
			}
			mainParent.layout();
		});
	}
	
	
	/**
	 * Set up a composite and table to display script definition load errors.
	 */
	private void setUpScriptDefinitionLoadErrorTable(Composite parent, Map<String, String> scriptDefinitionLoadErrors) {
		if (!preferences.hideScriptGenScriptDefinitionErrorTable()) {
			// A composite to contain the script definition load errors
			Composite scriptDefinitionErrorComposite = new Composite(parent, SWT.NONE);
			scriptDefinitionErrorComposite.setLayout(new GridLayout(1, false));
			scriptDefinitionErrorComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			// A table to display the script definition load errors
			// From http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/SWTTableSimpleDemo.htm
			Table table = new Table(scriptDefinitionErrorComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		    table.setHeaderVisible(true);
		    String[] titles = {"Script Definition", "Error"};
		    
		    for (int i = 0; i < titles.length; i++) {
		    	TableColumn column = new TableColumn(table, SWT.NULL);
		    	column.setText(titles[i]);
		    }
		    
		    for (Map.Entry<String, String> loadError : scriptDefinitionLoadErrors.entrySet()) {
		    	TableItem item = new TableItem(table, SWT.NULL);
		    	item.setText(0, loadError.getKey());
		    	item.setText(1, loadError.getValue());
		    }
		    
		    for (int i = 0; i < titles.length; i++) {
		    	table.getColumn(i).pack();
		    }
		}
	}
	
	/**
	 * Creates a new combo box and configures sets its input to the script definition loader.
	 * @param globalSettingsComposite
	 * 			The composite to draw the box in
	 * @return Combo box with available script definitions
	 */
	private ComboViewer setUpScriptDefinitionSelector(Composite globalSettingsComposite) {
		ComboViewer scriptDefinitionSelector = new ComboViewer(globalSettingsComposite, SWT.READ_ONLY);

		scriptDefinitionSelector.setContentProvider(ArrayContentProvider.getInstance());
		scriptDefinitionSelector.setLabelProvider(scriptGeneratorViewModel.getScriptDefinitionSelectorLabelProvider());
		scriptDefinitionSelector.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		scriptDefinitionSelector.setInput(scriptGeneratorViewModel.getAvailableScriptDefinitionsNames());
		scriptDefinitionSelector.setSelection(new StructuredSelection(scriptGeneratorViewModel.getScriptDefinition().get().getName()));
		
		return scriptDefinitionSelector;
	}
	
	/**
	 * Binds the Script Generator Table, script definition selector and validity check models to their views.
	 */

	private void bind(ComboViewer scriptDefinitionSelector,
	        ActionsViewTable table,
	        Button btnGetValidityErrors,
	        Button btnGenerateScript,
	        Label totalEstimatedTimeLabel,
	        Text helpText,
	        Button btnSaveParameters,
	        Button manualButton) {
		scriptGeneratorViewModel.bindScriptDefinitionLoader(scriptDefinitionSelector, helpText);

		scriptGeneratorViewModel.bindActionProperties(table, btnGetValidityErrors, btnGenerateScript, btnSaveParameters);
		
		scriptGeneratorViewModel.bindManualButton(manualButton);
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(totalEstimatedTimeLabel),
                BeanProperties.value("timeEstimate").observe(scriptGeneratorViewModel));

	}

}
