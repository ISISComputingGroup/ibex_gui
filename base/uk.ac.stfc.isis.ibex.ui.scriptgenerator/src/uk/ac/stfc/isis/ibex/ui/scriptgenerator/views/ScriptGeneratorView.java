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
import java.util.Arrays;
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
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
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
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSettingsSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButton;

/**
 * Provides the UI to control the script generator.
 * 
 * Uses code from
 * http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/SWTTableSimpleDemo.htm
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ScriptGeneratorView implements ScriptGeneratorViewModelDelegate {

	private static PreferenceSupplier preferences = new PreferenceSupplier();

	private static final Display DISPLAY = Display.getDefault();

	private final DataBindingContext bindingContext = new DataBindingContext();

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
	private static final String NO_SCRIPT_DEFINITIONS_MESSAGE = "\u26A0 Warning: Could not load any script definitions from "
			+ preferences.scriptGeneratorScriptDefinitionFolder().orElse("any folders") + System.lineSeparator()
			+ "Have they been located in the correct place or is this not your preferred location?";

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
	private Text helpText;
	private Composite globalParamComposite;
	private List<Text> globalParamTextList = new ArrayList<Text>();
	private Label errorLabel;

	/**
	 * Container for the UI objects.
	 * 
	 * @param parent the parent composite.
	 */
	@PostConstruct
	public void createPartControl(Composite parent) {
		scriptGeneratorViewModel = new ScriptGeneratorViewModel();
		scriptGeneratorViewModel.setScriptGeneratorViewModelDelegate(this);

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
	 * Create dialog boxes asking informing the user if there are changes to the git
	 * repository.
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
				MessageDialog.openInformation(DISPLAY.getActiveShell(), "Git errors occurred",
						messageBuilder.toString());
			}
		});

		scriptGeneratorViewModel.setRepoPath();
	}

	/**
	 * Helper function to add New lines between each error of the prompt error
	 * message.
	 * 
	 * @param errorMessage
	 * @return the message, with another new line at the end of it.
	 */
	public String promptBuilder(String errorMessage) {
		if (errorMessage.length() > 0) {
			errorMessage += System.lineSeparator() + System.lineSeparator();
		}

		return errorMessage;
	}

	/**
	 * Creates a label centred vertically and horizontally within its parent.
	 * 
	 * @param parent  the containing composite
	 * @param message the message to be displayed
	 */
	private void makeCenteredMessage(Composite parent, String message) {
		Label messageLabel = new Label(parent, SWT.NONE);
		messageLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		// Make the label bigger
		final FontDescriptor largeDescriptor = FontDescriptor.createFrom(messageLabel.getFont()).setHeight(16);
		final Font largeFont = largeDescriptor.createFont(Display.getDefault());
		messageLabel.setFont(largeFont);
		messageLabel.addDisposeListener(e -> largeFont.dispose()); // Need to dispose of new font's resources

		messageLabel.setText(message);
		parent.layout();
	}

	/**
	 * Display loading message while python has not been loaded yet.
	 */
	private void displayLoading() {
		DISPLAY.asyncExec(() -> {
			destroyUIContents();
			makeCenteredMessage(mainParent,
					scriptDefinitionsLoadedOnce ? Constants.RELOADING_MESSAGE : Constants.LOADING_MESSAGE);
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
				topBarComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

				ComboViewer scriptDefinitionSelector = makeScriptDefinitionSelector(topBarComposite);

				new Label(topBarComposite, SWT.SEPARATOR | SWT.VERTICAL);

				makeHelpTextBox(topBarComposite);

				globalParamComposite = new Composite(mainParent, SWT.NONE);
				globalParamComposite.setLayout(new GridLayout(24, false));
				globalParamComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));

				// Composite to split the middle into bigger left and smaller right section
				Composite middleComposite = makeGrid(mainParent, 2, false, 0);
				middleComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

				makeTableAndUpDownButtons(middleComposite);

				// The composite to contain the buttons on the right to the table
				Composite buttonContainerComposite = makeGrid(middleComposite, 1, false, 0);
				buttonContainerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

				makeControlButtons(buttonContainerComposite);

				// Composite for the row containing the parameter file location and total
				// estimated run time
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

				// Needs to be after nicos model has been set up
				helpMenu = new ScriptGeneratorHelpMenu(topBarComposite);
				var scriptDefinitionsRepoPath = scriptGeneratorViewModel.getScriptDefinitionsRepoPath();
				helpMenu.setScriptDefinitionsLocation(scriptDefinitionsRepoPath);

				// Bind the context and the validity checking listeners
				bind(scriptDefinitionSelector);

				onScriptDefinitionChange(scriptGeneratorViewModel, scriptGeneratorViewModel.getScriptDefinition());
			} else {
				makeCenteredMessage(mainParent, NO_SCRIPT_DEFINITIONS_MESSAGE);
			}

			Map<String, String> scriptDefinitionLoadErrors = scriptGeneratorViewModel.getScriptDefinitionLoadErrors();

			if (!scriptDefinitionLoadErrors.isEmpty()) {
				setUpScriptDefinitionLoadErrorTable(mainParent, scriptDefinitionLoadErrors);
			}

			mainParent.layout();
		});
	}

	/**
	 * Creates a new Composite used to group buttons vertically on this page.
	 * 
	 * @param parent  a composite control which will be the parent of the new
	 *                instance (cannot be null)
	 * @param columns number of columns
	 * @param equal   whether or not to make the columns equal width
	 * @param margin  margin around all four sides
	 * @return the new COmposite instance
	 */
	private Composite makeGrid(Composite parent, int columns, boolean equal, int margin) {
		Composite group = new Composite(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		GridLayout layout = new GridLayout(columns, equal);
		layout.marginHeight = margin;
		layout.marginWidth = margin;
		group.setLayout(layout);
		return group;
	}

	private void makeToggleParameterTransfer(Composite parent) {
		Composite actionsControlsGrp = makeGrid(parent, 1, true, 10);

		new IBEXButton(actionsControlsGrp, SWT.CHECK, event -> {
			boolean enabled = ((Button) event.widget).getSelection();
			scriptGeneratorViewModel.setParameterTransferEnabled(enabled);
		})
				.text(Constants.CHECKBOX_TITLE_PARAM_TRANSFER)
				.layoutData(IBEXButton.defaultGrid)
				.selected(Constants.PARAM_TRANSFER_DEFAULT);
	}
	
	private void makeToggleInvalidPauseSkip(Composite parent) {
		Composite actionsControlsGrp = makeGrid(parent, 1, true, 10);

		// Pause
		new IBEXButton(actionsControlsGrp, SWT.RADIO, event -> {
			ScriptGeneratorSettingsSingleton.getInstance().setSkipEnabled(false); //sets skipEnabled to false, causing Pause on invalid actions during run of script
		})
				.text(Constants.CHECKBOX_TITLE_INVALID_PAUSE)
				.tooltip(Constants.TOOLTIP_INVALID_PAUSE)
				.selected(!Constants.INVALID_SKIP_DEFAULT);

		// Skip
		new IBEXButton(actionsControlsGrp, SWT.RADIO, event -> {
			ScriptGeneratorSettingsSingleton.getInstance().setSkipEnabled(true); //sets skipEnabled to true, skipping over invalid actions during run of script
		})
				.text(Constants.CHECKBOX_TITLE_INVALID_SKIP)
				.tooltip(Constants.TOOLTIP_INVALID_SKIP)
				.selected(Constants.INVALID_SKIP_DEFAULT);
	}

	/**
	 * Creates a column containing three buttons for table row modifications.
	 * 
	 * @param parent the containing Composite
	 */
	private void makeTableRowControlButtons(Composite parent) {
		// Composite for laying out new/delete/duplicate action buttons
		Composite actionsControlsGrp = makeGrid(parent, 1, true, 10);

		// Make buttons for insert new/delete/duplicate actions
		btnAddAction = new IBEXButton(actionsControlsGrp, SWT.NONE, event -> {
			scriptGeneratorViewModel.addEmptyAction();
		})
				.text(Constants.BUTTON_TITLE_ADD_ROW_TO_END)
				.tooltip(Constants.BUTTON_TOOLTIP_ADD_ROW_TO_END)
				.layoutData(IBEXButton.expandingGrid)
				.get();
		btnInsertAction = new IBEXButton(actionsControlsGrp, SWT.NONE, event -> {
			scriptGeneratorViewModel.insertEmptyAction(table.getSelectionIndex() + 1);
		})
				.text(Constants.BUTTON_TITLE_INSERT_ROW_BELOW)
				.tooltip(Constants.BUTTON_TOOLTIP_INSERT_ROW_BELOW)
				.layoutData(IBEXButton.expandingGrid)
				.get();
		new IBEXButton(actionsControlsGrp, SWT.NONE, event -> {
			scriptGeneratorViewModel.clearAction();
		})
		.text(Constants.BUTTON_TITLE_DELETE_ROWS)
		.tooltip(Constants.BUTTON_TOOLTIP_DELETE_ROWS)
		.layoutData(IBEXButton.expandingGrid);
	}

	/**
	 * Creates a column containing three buttons for save, save as, and load script.
	 * 
	 * @param parent the containing Composite
	 */
	private void makeScriptSaveLoadButtons(Composite parent) {
		// Composite for generate buttons
		Composite generateButtonsGrp = makeGrid(parent, 1, true, 10);

		// Buttons to generate a script
		generateScriptButton = new IBEXButton(generateButtonsGrp, SWT.NONE, event -> {
			scriptGeneratorViewModel.generateScriptToCurrentFilepath();
		})
				.text(Constants.BUTTON_TITLE_SAVE)
				.tooltip(Constants.BUTTON_TITLE_SAVE)
				.layoutData(IBEXButton.expandingGrid)
				.get();
		generateScriptAsButton = new IBEXButton(generateButtonsGrp, SWT.NONE, event -> {
			scriptGeneratorViewModel.generateScript();
		})
				.text(Constants.BUTTON_TITLE_SAVE_AS)
				.tooltip(Constants.BUTTON_TITLE_SAVE_AS)
				.layoutData(IBEXButton.expandingGrid)
				.get();
		new IBEXButton(generateButtonsGrp, SWT.NONE, event -> {
			scriptGeneratorViewModel.loadParameterValues();
		})
		.text(Constants.BUTTON_TITLE_LOAD)
		.tooltip(Constants.BUTTON_TITLE_LOAD)
		.layoutData(IBEXButton.expandingGrid);
	}

	/**
	 * Draw Run, Pause, Stop buttons for dynamic scripting.
	 * 
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 */
	private void makeDynamicScriptingControlButtons(Composite parent) {
		errorLabel = new Label(parent, SWT.NONE);
		errorLabel.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
		errorLabel.setForeground(new Color(255, 0, 0));

		// Composite for generate buttons
		Composite dynamicScriptingButtonsGrp = makeGrid(parent, 3, true, 10);

		// Button to run/pause/stop script in nicos
		runButton = new IBEXButton(dynamicScriptingButtonsGrp, SWT.NONE)
				.image(Constants.IMAGE_RUN)
				.tooltip("Run")
				.layoutData(IBEXButton.expandingGrid)
				.get();
		pauseButton = new IBEXButton(dynamicScriptingButtonsGrp, SWT.NONE)
				.image(Constants.IMAGE_PAUSE)
				.tooltip("Pause")
				.layoutData(IBEXButton.expandingGrid)
				.get();
		stopButton = new IBEXButton(dynamicScriptingButtonsGrp, SWT.NONE)
				.image(Constants.IMAGE_STOP)
				.tooltip("Stop")
				.layoutData(IBEXButton.expandingGrid)
				.get();
		nicosViewModel.bindControls(runButton, pauseButton, stopButton);
	}

	private void makeRunAndFinishTime(Composite parent) {
		// Composite for the row containing total estimated run time
		Composite scriptTimeGrp = makeGrid(parent, 1, true, 10);
		scriptTimeGrp.setLayoutData(new GridData(SWT.END, SWT.NONE, true, false));

		// Label for the total estimated run time
		estimateText = new Label(scriptTimeGrp, SWT.TOP);
		estimateText.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		final FontDescriptor boldDescriptor = FontDescriptor.createFrom(estimateText.getFont()).setHeight(11)
				.setStyle(SWT.BOLD);
		final Font estimateFont = boldDescriptor.createFont(Display.getDefault());
		estimateText.setFont(estimateFont);
		estimateText.setText("Total estimated run time: 0 seconds");
		estimateText.addDisposeListener(e -> estimateFont.dispose()); // Need to dispose of new font's resources

		// Label for the expected finish time
		expectedFinishText = new Label(scriptTimeGrp, SWT.BOTTOM);
		expectedFinishText.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		final Font expectedFinishFont = boldDescriptor.createFont(Display.getDefault());
		expectedFinishText.setFont(expectedFinishFont);
		expectedFinishText.setText("Expected Finish Time: 00:00:00");
		expectedFinishText.addDisposeListener(e -> expectedFinishFont.dispose()); // Need to dispose of new font's
																					// resources
	}

	/**
	 * Draws left side of the panel containing the table and the move up/down
	 * buttons
	 */
	private void makeTableAndUpDownButtons(Composite parent) {
		// The composite to contain the UI table
		Composite tableContainerComposite = new Composite(parent, SWT.NONE);
		tableContainerComposite.setLayout(new GridLayout(2, false));
		tableContainerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// The UI table
		table = new ActionsViewTable(tableContainerComposite, SWT.NONE, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION,
				scriptGeneratorViewModel);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scriptGeneratorViewModel.reloadActions();

		// Composite for move action up/down buttons
		Composite moveComposite = new Composite(tableContainerComposite, SWT.NONE);
		moveComposite.setLayout(new GridLayout());
		moveComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		// Make buttons to move an action up and down the list
		btnMoveActionUp = new IBEXButton(moveComposite, SWT.NONE, event -> {
			scriptGeneratorViewModel.moveActionUp(table.selectedRows());
		})
				.image(Constants.IMAGE_UP_ARROW)
				.tooltip("Move selected row up")
				.layoutData(IBEXButton.compactGrid)
				.get();
		btnMoveActionDown = new IBEXButton(moveComposite, SWT.NONE, event -> {
			scriptGeneratorViewModel.moveActionDown(table.selectedRows());
		})
				.image(Constants.IMAGE_DOWN_ARROW)
				.tooltip("Move selected row down")
				.layoutData(IBEXButton.compactGrid)
				.get();
	}

	/**
	 * Draws right side of the panel containing the buttons
	 */
	private void makeControlButtons(Composite parent) {
		makeToggleParameterTransfer(parent);
		makeToggleInvalidPauseSkip(parent);
		makeScriptSaveLoadButtons(parent);
		makeTableRowControlButtons(parent);
		makeDynamicScriptingControlButtons(parent);
		makeRunAndFinishTime(parent);
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
		helpText = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		var helpTextDataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		helpTextDataLayout.heightHint = 50;
		helpText.setLayoutData(helpTextDataLayout);
		helpText.setBackground(Constants.CLEAR_COLOUR);
		// Display the correct starting text
		scriptGeneratorViewModel.getScriptDefinition().ifPresentOrElse(scriptDefinition -> {
			Optional.ofNullable(scriptDefinition.getHelp()).ifPresentOrElse(helpString -> helpText.setText(helpString),
					() -> helpText.setText(""));
		}, () -> helpText.setText(""));

		return helpText;
	}

	/**
	 * Set up a composite and table to display script definition load errors.
	 * 
	 * @param parent                     The parent container
	 * @param scriptDefinitionLoadErrors The map containing the script definition
	 *                                   load errors.
	 */
	private void setUpScriptDefinitionLoadErrorTable(Composite parent, Map<String, String> scriptDefinitionLoadErrors) {
		if (!preferences.hideScriptGenScriptDefinitionErrorTable()) {
			// A composite to contain the script definition load errors
			Composite scriptDefinitionErrorComposite = makeGrid(parent, 1, false, 5);
			scriptDefinitionErrorComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

			Label errorLabel = new Label(scriptDefinitionErrorComposite, SWT.NONE);
			errorLabel.setText("Errors:");

			// A table to display the script definition load errors
			// From http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/SWTTableSimpleDemo.htm
			Table table = new Table(scriptDefinitionErrorComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			table.setHeaderVisible(true);
			GridData d = new GridData();
			d.heightHint = 80;
			table.setLayoutData(d);
			String[] titles = {"Script Definition", "Error"};

			for (String title : titles) {
				TableColumn column = new TableColumn(table, SWT.NULL);
				column.setText(title);
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
	 * Creates a new label and combo box and configures its input to the script
	 * definition loader.
	 * 
	 * @param parent the composite to draw the box in
	 * @return Combo box with available script definitions
	 */
	private ComboViewer makeScriptDefinitionSelector(Composite parent) {
		// Composite to contain help strings from script definitions
		Composite scriptDefinitionComposite = new Composite(parent, SWT.NONE);
		scriptDefinitionComposite.setLayout(new GridLayout(2, false));

		// The label for the script definition selector drop down
		Label scriptDefinitionSelectorLabel = new Label(scriptDefinitionComposite, SWT.NONE);
		scriptDefinitionSelectorLabel.setText("Script Definition:");

		// Drop-down box to select between script definitions.
		ComboViewer scriptDefinitionSelector = new ComboViewer(scriptDefinitionComposite, SWT.READ_ONLY);

		scriptDefinitionSelector.setContentProvider(ArrayContentProvider.getInstance());
		scriptDefinitionSelector.setLabelProvider(scriptGeneratorViewModel.getScriptDefinitionSelectorLabelProvider());
		scriptDefinitionSelector.getCombo().setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1));
		scriptDefinitionSelector.setInput(scriptGeneratorViewModel.getAvailableScriptDefinitionsNames());
		scriptDefinitionSelector
				.setSelection(new StructuredSelection(scriptGeneratorViewModel.getScriptDefinition().get().getName()));

		scriptDefinitionSelector.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) {
					String selectedScriptDefinitionName = (String) event.getStructuredSelection().getFirstElement();
					scriptGeneratorViewModel.changeScriptDefinition(selectedScriptDefinitionName);
				}
			}
		});

		return scriptDefinitionSelector;
	}

	private void bindToHasSelected(Control controlToDisable) {
		bindingContext.bindValue(WidgetProperties.enabled().observe(controlToDisable),
				BeanProperties.value("hasSelection").observe(scriptGeneratorViewModel));
	}

	/**
	 * Binds the Script Generator Table, script definition selector and validity
	 * check models to their views.
	 * 
	 * @param scriptDefinitionSelector The selector for script definitions
	 */
	private void bind(ComboViewer scriptDefinitionSelector) {
		scriptGeneratorViewModel.bindScriptDefinitionLoader(scriptDefinitionSelector);

		scriptGeneratorViewModel.bindActionProperties(table);

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

	@Override
	public void onSaveEnabledChange(ScriptGeneratorViewModel viewModel, boolean enabled) {
		generateScriptButton.setEnabled(enabled);
		generateScriptAsButton.setEnabled(enabled);
	}

	@Override
	public void onActionsValidityChange(ScriptGeneratorViewModel viewModel, boolean allActionsValid,
			Map<Integer, String> globalErrors) {
		// Highlight global param errors
		Display.getDefault().asyncExec(() -> {
			for (int i = 0; i < this.globalParamTextList.size(); i++) {
				if (globalErrors.containsKey(i)) {
					globalParamTextList.get(i).setBackground(Constants.INVALID_LIGHT_COLOR);
					globalParamTextList.get(i).setBackground(Constants.INVALID_DARK_COLOR);
					globalParamTextList.get(i).setToolTipText(globalErrors.get(i));
				} else {
					globalParamTextList.get(i).setBackground(Constants.CLEAR_COLOR);
					globalParamTextList.get(i).setToolTipText(null);
				}
			}

			runButton.setEnabled(allActionsValid);
			errorLabel.setText(allActionsValid ? "" : "\u26A0 There are invalid actions.");
			errorLabel.setVisible(!allActionsValid);
			errorLabel.getParent().layout();
		});
	}

	@Override
	public void onScriptDefinitionChange(ScriptGeneratorViewModel viewModel,
			Optional<ScriptDefinitionWrapper> scriptDefinition) {

		if (scriptDefinition.isEmpty()) {
			helpText.setText("");
			return;
		}

		ScriptDefinitionWrapper scriptDefinitionWrapper = scriptDefinition.get();

		// Display help text
		helpText.setText(scriptDefinitionWrapper.getHelp());

		// Clear previous global parameter widgets and display new global parameters
		Arrays.stream(globalParamComposite.getChildren()).forEach(Control::dispose);
		globalParamTextList.clear();

		List<ActionParameter> temp;
		String param = "No Global Paramaters";
		String paramVal = "";
		if (scriptDefinitionWrapper.getGlobalParameters() != null) {
			temp = scriptDefinitionWrapper.getGlobalParameters();

			// Hide global parameters row if there is nothing to display
			((GridData) globalParamComposite.getLayoutData()).exclude = temp.isEmpty();
			globalParamComposite.setVisible(!temp.isEmpty());

			if (!temp.isEmpty()) {
				for (int paramIndex = 0; paramIndex < temp.size(); paramIndex++) {
					ActionParameter global = temp.get(paramIndex);
					param = global.getName();
					paramVal = global.getDefaultValue();
					if (!globalParamComposite.isDisposed()) {
						Label globalLabelCurrent = new Label(globalParamComposite, SWT.NONE);
						globalLabelCurrent.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
						globalLabelCurrent.setText(param);
						Text globalParamTextCurrent = new Text(globalParamComposite, SWT.NONE);
						globalParamTextCurrent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
						globalParamTextCurrent.setEnabled(true);
						final int index = paramIndex;
						globalParamTextCurrent.addListener(SWT.Modify, e -> {
							viewModel.updateGlobalParams(index, globalParamTextCurrent.getText());
						});
						globalParamTextCurrent.setText(paramVal);
						globalParamTextList.add(globalParamTextCurrent);
					}
				}
			}
		}

		if (!globalParamComposite.isDisposed()) {
			globalParamComposite.layout();
		}
		mainParent.layout();
	}

	@Override
	public void onErrorMessage(ScriptGeneratorViewModel viewModel, String title, String message) {
		MessageDialog.openError(Constants.DISPLAY.getActiveShell(), title, message);
	}

	@Override
	public void onWarningMessage(ScriptGeneratorViewModel viewModel, String title, String message) {
		MessageDialog.openWarning(Constants.DISPLAY.getActiveShell(), title, message);
	}

	@Override
	public void onInfoMessage(ScriptGeneratorViewModel viewModel, String title, String message) {
		MessageDialog.openInformation(Constants.DISPLAY.getActiveShell(), title, message);
	}

	@Override
	public boolean onUserConfirmationRequest(ScriptGeneratorViewModel viewModel, String title, String message) {
		return MessageDialog.openConfirm(Constants.DISPLAY.getActiveShell(), title, message);
	}

	@Override
	public int onUserSelectOptionRequest(ScriptGeneratorViewModel viewModel, String title, String message,
			String[] options, int defaultIndex) {
		MessageDialog dialog = new MessageDialog(Constants.DISPLAY.getActiveShell(), title, null, message,
				MessageDialog.QUESTION, options, defaultIndex);
		return dialog.open();
	}

}
