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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontDescriptor;
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

import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;
import uk.ac.stfc.isis.ibex.ui.widgets.IBEXButtonFactory;

/**
 * Provides the UI to control the script generator.
 * 
 * Uses code from http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/SWTTableSimpleDemo.htm
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ScriptGeneratorView {

	
    private static PreferenceSupplier preferences = new PreferenceSupplier();

    private static final Display DISPLAY = Display.getDefault();

    private final DataBindingContext bindingContext = new DataBindingContext();

    /**
     * A clear colour for use in other script generator table columns when a row is valid.
     */
    private static final Color CLEAR_COLOUR = DISPLAY.getSystemColor(SWT.COLOR_WHITE);
    
    /**
     * The ViewModel the View is updated by.
     */
    private ScriptGeneratorViewModel scriptGeneratorViewModel;
    
    /**
     * The ViewModel to control nicos interactions.
     */
    private ScriptGeneratorNicosViewModel nicosViewModel;

    /**
     * The main parent for UI elements
     */
    private Composite mainParent;

    /**
     * The string to display if there are no script definitions to select.
     */
    private static final String NO_SCRIPT_DEFINITIONS_MESSAGE = String.format("\u26A0 Warning: Could not load any script definitions from %s"
        + System.getProperty("line.separator")
        + "Have they been located in the correct place or is this not your preferred location?", 
        preferences.scriptGeneratorScriptDefinitionFolder());

    /**
     * Denotes whether script definitions have been loaded once.
     */
    private boolean scriptDefinitionsLoadedOnce = false;

    private ActionsViewTable table;
    private Button btnMoveActionUp;
    private Button btnMoveActionDown;
    @SuppressWarnings("unused")
	private Button btnAddAction;
    @SuppressWarnings("unused")
    private Button btnInsertAction;
    private Label parametersFileText;
    private Label scriptGenerationTimeText;
    private Label estimateText;
    private Label expectedFinishText;
    private Button runButton;
    private Button stopButton;
    private Button pauseButton;

    private Button generateScriptButton;
    private Button generateScriptAsButton;
    private ScriptGeneratorHelpMenu helpMenu;

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
    parent.setLayout(new GridLayout());

    mainParent = parent;

    scriptGeneratorViewModel.addPropertyChangeListener(ScriptGeneratorProperties.PYTHON_READINESS_PROPERTY, evt -> {
        boolean ready = (boolean) evt.getNewValue();
        if (ready) {
	        doGitActions();
	        displayLoaded();
	        scriptGeneratorViewModel.reloadScriptDefinitions();
        } else {
        	displayLoading();
        }
    });
    
//    scriptGeneratorViewModel.addPropertyChangeListener(DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, evt -> {
//    	nicosModel.queueScript("Script generator", (String) evt.getNewValue() + "\nrunscript()"); 
//    });

    var scriptGeneratorModel = scriptGeneratorViewModel.setUpModel();
    nicosViewModel = new ScriptGeneratorNicosViewModel(scriptGeneratorModel);
    }

    /**
     * Clean up resources being used by the view.
     */
    @PreDestroy
    public void dispose() {
        scriptGeneratorViewModel.dispose();
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
	 * Create dialog boxes asking informing the user if there are changes to the git repository.
	 */
	private void doGitActions() {
		DISPLAY.asyncExec(() -> {
			StringBuilder messageBuilder = new StringBuilder();
			
			// Display prompt if remote git is not available
			messageBuilder.append(promptBuilder(scriptGeneratorViewModel.getGitErrorPromptMessage()));
			
			// Display prompt if new commits are available
			messageBuilder.append(promptBuilder(scriptGeneratorViewModel.getUpdatesPromptMessage()));
			
			// Display prompt if local repo is dirty
			messageBuilder.append(scriptGeneratorViewModel.getDirtyPromptMessage());
			
			Optional<String> gitErrors = scriptGeneratorViewModel.getGitLoadErrors();
			if (gitErrors.isPresent()) {
				messageBuilder.append(promptBuilder(gitErrors.get()));
			}
			
			if (messageBuilder.length() != 0) {
				MessageDialog.openInformation(DISPLAY.getActiveShell(), "Git errors occurred", messageBuilder.toString());
			}
		});

		scriptGeneratorViewModel.setRepoPath();
	}
	
	/**
	 * Helper function to add New lines between each error of the prompt error message.
	 * @param errorMessage 
	 * @return the message, with another new line at the end of it. 
	 */
	public String promptBuilder(String errorMessage) {
		if (errorMessage.length() > 0) {
			errorMessage += "\n\n";
		}
		
		return errorMessage;
	}

    /**
     * Display loading.
     */
    private void displayLoading() {
	    DISPLAY.asyncExec(() -> {
	        destroyUIContents();
	        Label loadingMessage = new Label(mainParent, SWT.NONE);
	        loadingMessage.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
	        
	        // Make the warning label bigger
	        final FontDescriptor largeDescriptor = FontDescriptor.createFrom(loadingMessage.getFont()).setHeight(16);
	        final Font largeFont = largeDescriptor.createFont(Display.getDefault());
	        loadingMessage.setFont(largeFont);
	        loadingMessage.addDisposeListener(e -> largeFont.dispose()); // Need to dispose of new font's resources
	        
	        if (scriptDefinitionsLoadedOnce) {
	        	loadingMessage.setText(Constants.RELOADING_MESSAGE);
	        } else {
	        	loadingMessage.setText(Constants.LOADING_MESSAGE);
	        }
	
	        mainParent.layout();
	    });
    }
    
    /**
     * Creates a new Composite used to group buttons primarily on this page.
     * 
     * @param parent a composite control which will be the parent of the new instance (cannot be null)
     * @return the new COmposite instance
     */
    private Composite makeGroupingComposite(Composite parent) {
    	Composite group = new Composite(parent, SWT.NONE);
        group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        GridLayout layout = new GridLayout(1, true);
        layout.marginHeight = 10;
        layout.marginWidth = 10;
        group.setLayout(layout);
        return group;
    }
    
    private void drawActionsModifierButtons(Composite parent) {
    	// Composite for laying out new/delete/duplicate action buttons
        Composite actionsControlsGrp = makeGroupingComposite(parent);

        // Make buttons for insert new/delete/duplicate actions
        btnAddAction = IBEXButtonFactory.expanding(actionsControlsGrp, Constants.BUTTON_TITLE_ADD_ROW_TO_END, Constants.BUTTON_TOOLTIP_ADD_ROW_TO_END, null, e -> scriptGeneratorViewModel.addEmptyAction());
        btnInsertAction = IBEXButtonFactory.expanding(actionsControlsGrp, Constants.BUTTON_TITLE_INSERT_ROW_BELOW, Constants.BUTTON_TOOLTIP_INSERT_ROW_BELOW, null, e -> scriptGeneratorViewModel.insertEmptyAction(table.getSelectionIndex() + 1));
        IBEXButtonFactory.expanding(actionsControlsGrp, Constants.BUTTON_TITLE_DELETE_ROWS, Constants.BUTTON_TOOLTIP_DELETE_ROWS, null, e -> scriptGeneratorViewModel.clearAction());
    }
    
    private void drawScriptSavingAndLoadingButtons(Composite parent) {
    	// Composite for generate buttons
        Composite generateButtonsGrp = makeGroupingComposite(parent);
        
    	// Buttons to generate a script
        generateScriptButton = IBEXButtonFactory.expanding(generateButtonsGrp, Constants.BUTTON_TITLE_SAVE, null, null, e -> scriptGeneratorViewModel.generateScriptToCurrentFilepath());
        generateScriptAsButton = IBEXButtonFactory.expanding(generateButtonsGrp, Constants.BUTTON_TITLE_SAVE_AS, null, null, e -> scriptGeneratorViewModel.generateScript());
        IBEXButtonFactory.expanding(generateButtonsGrp, Constants.BUTTON_TITLE_LOAD, null, null, e -> scriptGeneratorViewModel.loadParameterValues());
    }
    
    /**
     * Draw Run, Pause, Stop buttons for dynamic scripting.
     * 
     * @param parent a composite control which will be the parent of the new instance (cannot be null)
     */
    private void drawDynamicScriptingControlButtons(Composite parent) {
    	// Composite for generate buttons
        Composite dynamicScriptingButtonsGrp = new Composite(parent, SWT.NONE);
        dynamicScriptingButtonsGrp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
        GridLayout layout = new GridLayout(3, true);
        layout.marginHeight = 10;
        layout.marginWidth = 10;
        dynamicScriptingButtonsGrp.setLayout(layout);
        
        // Button to run/pause/stop script in nicos
        runButton = IBEXButtonFactory.expanding(dynamicScriptingButtonsGrp, null, "Run", Constants.IMAGE_RUN, null);
        pauseButton = IBEXButtonFactory.expanding(dynamicScriptingButtonsGrp, null, "Pause", Constants.IMAGE_PAUSE, null);
        stopButton = IBEXButtonFactory.expanding(dynamicScriptingButtonsGrp, null, "Stop", Constants.IMAGE_STOP, null);        
        nicosViewModel.bindControls(runButton, pauseButton, stopButton);
    }

    private void drawRunAndFinishTime(Composite parent) {
    	// Composite for the row containing  total estimated run time
        Composite scriptTimeGrp = makeGroupingComposite(parent);
        scriptTimeGrp.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, true, false));
        
    	// Label for the total estimated run time
        estimateText = new Label(scriptTimeGrp, SWT.TOP);
        estimateText.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        String currentFont = estimateText.getFont().getFontData()[0].getName();
        Font font = new Font(estimateText.getDisplay(), new FontData(currentFont, 11, SWT.BOLD));
        estimateText.setFont(font);
        estimateText.setText("Total estimated run time: 0 seconds");

        // Label for the expected finish time
        expectedFinishText = new Label(scriptTimeGrp, SWT.BOTTOM);
        expectedFinishText.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        currentFont = expectedFinishText.getFont().getFontData()[0].getName();
        font = new Font(expectedFinishText.getDisplay(), new FontData(currentFont, 11, SWT.BOLD));
        expectedFinishText.setFont(font);
        expectedFinishText.setText("Expected Finish Time: 00:00:00");
    }
    
    /**
     * Creates the help text box as well as a label in front of it
     * 
     * @param parent the container to draw the text help into
     * @return the text box instance
     */
    private Text makeHelpTextBox(Composite parent) {
    	// Label for script definition help
        Label helpLabel = new Label(parent, SWT.NONE);
        helpLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1));
        helpLabel.setText("Help: ");

        // Display help for the script definition
        Text helpText = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
        var helpTextDataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        helpTextDataLayout.heightHint = 50;
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
        
        return helpText;
    }
    
    /**
     * Draws left side of the panel containing the table and the move up/down buttons
     */
    private void drawTable(Composite parent) {
    	// The composite to contain the UI table
        Composite tableContainerComposite = new Composite(parent, SWT.NONE);
        tableContainerComposite.setLayout(new GridLayout(2, false));
        tableContainerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // The UI table
        table = new ActionsViewTable(tableContainerComposite, SWT.NONE, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION, scriptGeneratorViewModel);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        scriptGeneratorViewModel.reloadActions();

        // Composite for move action up/down buttons
        Composite moveComposite = new Composite(tableContainerComposite, SWT.NONE);
        moveComposite.setLayout(new GridLayout());
        moveComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

        // Make buttons to move an action up and down the list
        btnMoveActionUp = IBEXButtonFactory.compact(moveComposite, null, "Move selected row up.", Constants.IMAGE_UP_ARROW, e -> scriptGeneratorViewModel.moveActionUp(table.selectedRows()));
        btnMoveActionDown = IBEXButtonFactory.compact(moveComposite, null, "Move selected row down.", Constants.IMAGE_DOWN_ARROW, e -> scriptGeneratorViewModel.moveActionDown(table.selectedRows()));
    }
    
    /**
     * Draws right side of the panel containing the buttons
     */
    private void drawButtons(Composite parent) {
    	drawScriptSavingAndLoadingButtons(parent);
        drawActionsModifierButtons(parent);
        drawDynamicScriptingControlButtons(parent);
        drawRunAndFinishTime(parent);
    }
    
    private void drawMiddle() {
    	// Composite to split the middle into bigger left and smaller right section
        Composite middleComposite = new Composite(mainParent, SWT.NONE);
        middleComposite.setLayout(new GridLayout(2, false));
        middleComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        drawTable(middleComposite);
        
        // The composite to contain the buttons on the right to the table
        Composite buttonContainerComposite = new Composite(middleComposite, SWT.NONE);
        buttonContainerComposite.setLayout(new GridLayout());
        GridData gridData = new GridData();
        gridData.verticalAlignment = SWT.FILL;
        buttonContainerComposite.setLayoutData(gridData);
        
        drawButtons(buttonContainerComposite);
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
	        topBarComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	
	        // Composite to contain help strings from script definitions
	        Composite scriptDefinitionComposite = new Composite(topBarComposite, SWT.NONE);
	        scriptDefinitionComposite.setLayout(new GridLayout(5, false));
	        scriptDefinitionComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
	
	        // The label for the script definition selector drop down
	        Label scriptDefinitionSelectorLabel = new Label(scriptDefinitionComposite, SWT.NONE);
	        scriptDefinitionSelectorLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, true, 1, 1));
	        scriptDefinitionSelectorLabel.setText("Script Definition:");
	
	        // Drop-down box to select between script definitions.
	        ComboViewer scriptDefinitionSelector = setUpScriptDefinitionSelector(scriptDefinitionComposite);
	
	        // Separate help and selector
	        new Label(scriptDefinitionComposite, SWT.SEPARATOR | SWT.VERTICAL);
	
	        Text helpText = makeHelpTextBox(scriptDefinitionComposite);
	        
	        Composite globalParamComposite = new Composite(mainParent, SWT.NONE);
	        globalParamComposite.setLayout(new GridLayout(24, false));
	        globalParamComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
	        
	        List<Label> globalLabel = new ArrayList<Label>();
	        List<Text> globalParamText = new ArrayList<Text>();
	        Map<String, String> scriptDefinitionLoadErrors = scriptGeneratorViewModel.getScriptDefinitionLoadErrors();
	
	        if (!scriptDefinitionLoadErrors.isEmpty()) {
	            setUpScriptDefinitionLoadErrorTable(mainParent, scriptDefinitionLoadErrors);                 
	        }
	        
	        drawMiddle();
	        
	        // Composite for the row containing the parameter file location and total estimated run time
	        Composite scriptInfoGrp = new Composite(mainParent, SWT.NONE);
	        scriptInfoGrp.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
	        GridLayout scriptInfoLayout = new GridLayout(3, true);
	        scriptInfoLayout.marginRight = 40;
	        scriptInfoGrp.setLayout(scriptInfoLayout);
	        
	        // Label for Location of Saved Parameters File
	        parametersFileText = new Label(scriptInfoGrp, SWT.LEFT);
	        parametersFileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	        parametersFileText.setText("Current Script: <new file>");
	        
	        scriptGenerationTimeText = new Label(scriptInfoGrp, SWT.LEFT);
	        scriptGenerationTimeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	        
	        // Needs to be after nicos model has been set up (in drawDynamicScriptingControls)
	        helpMenu = new ScriptGeneratorHelpMenu(topBarComposite);
			var scriptDefinitionsRepoPath = scriptGeneratorViewModel.getScriptDefinitionsRepoPath();
			helpMenu.setScriptDefinitionsLocation(scriptDefinitionsRepoPath);

	        // Bind the context and the validity checking listeners
	        bind(scriptDefinitionSelector,
	            helpText,
	            globalLabel,
	            globalParamText,
	            globalParamComposite);
	        scriptGeneratorViewModel.createGlobalParamsWidgets();
	        
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
     * @param parent The parent container
     * @param scriptDefinitionLoadErrors The map containing the script definition load errors.
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
     *             The composite to draw the box in
     * @return Combo box with available script definitions
     */
    private ComboViewer setUpScriptDefinitionSelector(Composite globalSettingsComposite) {
	    ComboViewer scriptDefinitionSelector = new ComboViewer(globalSettingsComposite, SWT.READ_ONLY);
	
	    scriptDefinitionSelector.setContentProvider(ArrayContentProvider.getInstance());
	    scriptDefinitionSelector.setLabelProvider(scriptGeneratorViewModel.getScriptDefinitionSelectorLabelProvider());
	    scriptDefinitionSelector.getCombo().setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1));
	    scriptDefinitionSelector.setInput(scriptGeneratorViewModel.getAvailableScriptDefinitionsNames());
	    scriptDefinitionSelector.setSelection(new StructuredSelection(scriptGeneratorViewModel.getScriptDefinition().get().getName()));
	
	    return scriptDefinitionSelector;
    }

    private void bindToHasSelected(Control controlToDisable) {
    bindingContext.bindValue(WidgetProperties.enabled().observe(controlToDisable),
        BeanProperties.value("hasSelection").observe(scriptGeneratorViewModel));        
    }

    /**
     * Binds the Script Generator Table, script definition selector and validity check models to their views.
     * @param scriptDefinitionSelector The selector for script definitions
     * @param helpText The help text
     */
    private void bind(ComboViewer scriptDefinitionSelector, Text helpText, List<Label> globalLabel, List<Text> globalParamText, Composite globalParamsComposite) {
	    scriptGeneratorViewModel.bindScriptDefinitionLoader(scriptDefinitionSelector, helpText, globalLabel, globalParamText, globalParamsComposite, mainParent);
	
	    scriptGeneratorViewModel.bindActionProperties(table, generateScriptButton, generateScriptAsButton);
	
	    table.addSelectionChangedListener(event -> scriptGeneratorViewModel.setSelected(table.selectedRows()));
	
	    bindingContext.bindValue(WidgetProperties.text().observe(parametersFileText),
	    	BeanProperties.value("parametersFile").observe(scriptGeneratorViewModel));
	    
	    bindingContext.bindValue(WidgetProperties.text().observe(scriptGenerationTimeText),
	        	BeanProperties.value("scriptGenerationTime").observe(scriptGeneratorViewModel));
	    
	    bindingContext.bindValue(WidgetProperties.text().observe(estimateText),
	        BeanProperties.value("timeEstimate").observe(scriptGeneratorViewModel));
	    
	    scriptGeneratorViewModel.getFinishTimer().addPropertyChangeListener("finishTimeVal", e -> {
	    	DISPLAY.asyncExec(() -> {
	    		expectedFinishText.setText((String) e.getNewValue());
	    	});
	    });
	
	    bindToHasSelected(btnMoveActionUp);
	    bindToHasSelected(btnMoveActionDown);
    }

}
