package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.net.URL;
import java.time.Duration;
import java.nio.file.Paths;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.apache.logging.log4j.Logger;
import static java.lang.Math.min;

import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.Activator;
import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptDefinitionNotMatched;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs.SaveScriptGeneratorFileMessageDialog;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.ui.help.AboutHandler;

import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The ViewModel for the ScriptGeneratorView.
 * 
 * @author James King
 *
 */
public class ScriptGeneratorViewModel extends ModelObject {
    private static final Display DISPLAY = Display.getDefault();

    /**
     * A dark red for use in the validity column when a row is invalid.
     */
    private static final Color INVALID_DARK_COLOR = DISPLAY.getSystemColor(SWT.COLOR_RED);

    /**
     * A light read for use in the other script generator table columns when a row is invalid.
     */
    private static final Color INVALID_LIGHT_COLOR = new Color(DISPLAY, 255, 204, 203);

    /**
     * A green for use in the validity column when a row is valid.
     */
    private static final Color VALID_COLOR = DISPLAY.getSystemColor(SWT.COLOR_GREEN);

    /**
     * A clear colour for use in other script generator table columns when a row is valid.
     */
    private static final Color CLEAR_COLOR = DISPLAY.getSystemColor(SWT.COLOR_WHITE);

    /**
     * The colour of the "get validity errors" button when it is grayed out.
     */
    private static final Color GREY_COLOR = DISPLAY.getSystemColor(SWT.COLOR_GRAY);

    /**
     * A light orange to use when validity checks may be incorrect e.g. for when using an unsupported language.
     */
    private static final Color LIGHT_VALIDITY_CHECK_ERROR_COLOR = new Color(DISPLAY, 255, 201, 102);

    /**
     * A dark orange to use when validity checks may be incorrect e.g. for when using an unsupported language.
     */
    private static final Color DARK_VALIDITY_CHECK_ERROR_COLOR = new Color(DISPLAY, 255, 165, 0);

    /**
     * The maximum number of lines to display in the "Get Validity Errors" dialog box before suppressing others.
     */
    private static final int MAX_ERRORS_TO_DISPLAY_IN_DIALOG = 10;

    /**
     * A property that denotes whether the language to generate and check validity errors in is supported,
     */
    private static final String LANGUAGE_SUPPORT_PROPERTY = "language_supported";

    /**
     * A property that denotes whether there has been a threading error in generation or validity checking.
     */
    private static final String THREAD_ERROR_PROPERTY = "thread error";

    /**
     * A property that carries the validity error messages to listen for in order to update table rows.
     */
    private static final String VALIDITY_ERROR_MESSAGE_PROPERTY = "validity error messages";

    /**
     * A property that carries the time estimation to listen for in order to update table rows.
     */
    private static final String TIME_ESTIMATE_PROPERTY = "time estimate";

    /**
     * The property to listen for changes in a Generator containing the generated script (String).
     */
    private static final String GENERATED_SCRIPT_FILENAME_PROPERTY = "generated script filename";

    /**
     * A property to listen to for when actions change in the model.
     */
    private static final String ACTIONS_PROPERTY = "actions";

    /**
     * A property to fire a change of when there is an error generating a script.
     */
    private static final String SCRIPT_GENERATION_ERROR_PROPERTY = "script generation error";

    /**
     * A property that is changed when script definitions are switched.
     */
    private static final String SCRIPT_DEFINITION_SWITCH_PROPERTY = "scriptDefinition";

    /**
     * A property to notify listeners when python becomes ready or not ready.
     */
    private static final String PYTHON_READINESS_PROPERTY = "python ready";

    private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorViewModel.class);
    
    private static final String UNSAVED_CHANGES_MARKER = " (*)";

    /**
     * Path to the current parameters save file.
     */
    private String currentParametersFile;
    
    /**
     * The actions of the currently loaded parameters file.
     */
    private List<Map<JavaActionParameter, String>> currentFileActions = new ArrayList<Map<JavaActionParameter, String>>();
    
    /**
     * Whether the marker for unsaved changes "(*)" in the current parameters file is currently being displayed or not.
     */
    private boolean unsavedChangesMarkerDisplayed = false;
    
    /**
     * Default string to display the current parameters save file name and location.
     */
    private String parametersFileDisplayString = "Current parameters file: <new file>";
    
    /**
     * Default string to display for time estimation.
     */
    private String displayString = "Total estimated run time: 00:00:00";

    /**
     * The reference to the singleton model that the ViewModel is to use.
     */
    private ScriptGeneratorSingleton scriptGeneratorModel;

    /**
     * The current viewTable in the actions.
     */
    private ActionsViewTable viewTable;

    /**
     * The current generate script button in the view.
     */
    private Button btnGenerateScript;

    /**
     * The current Save Parameters button in the view
     */
    private Button btnSaveParam;

    /**
     * The current Save Parameters As button in the view
     */
    private Button btnSaveParamAs;
    
    /**
     * The currently selected rows
     */
    private boolean hasSelection;
    
    private Clipboard clipboard;
    private static String TAB = "\t";
    private static String CRLF = "\r\n";    
    
    
    /**
     * A constructor that sets up the script generator model and 
     *   begins listening to property changes in the model.
     */
    public ScriptGeneratorViewModel() {
    // Set up the model
    scriptGeneratorModel = Activator.getModel();
    scriptGeneratorModel.addPropertyChangeListener(PYTHON_READINESS_PROPERTY, evt -> {
        firePropertyChange(PYTHON_READINESS_PROPERTY, evt.getOldValue(), evt.getNewValue());
    });
    }

    /**
     * Set up the model. Allows us to attach listeners for the view first.
     */
    public void setUpModel() {
    clipboard = new Clipboard(Display.getDefault());
    scriptGeneratorModel.createScriptDefinitionLoader();
    scriptGeneratorModel.setUp();
    // Listen to whether the language support is changed
    // notify the user if the language is not supported
    scriptGeneratorModel.addPropertyChangeListener(LANGUAGE_SUPPORT_PROPERTY, evt -> {
        if (Objects.equals(evt.getOldValue(), true) && Objects.equals(evt.getNewValue(), false)) {
        displayLanguageSupportError();
        }
    });
    // Listen for model threading errors and display to the user if there is one
    // Model is responsible for logging it
    scriptGeneratorModel.addPropertyChangeListener(THREAD_ERROR_PROPERTY, evt -> {
        displayThreadingError();
    });
    scriptGeneratorModel.addPropertyChangeListener(SCRIPT_GENERATION_ERROR_PROPERTY, evt -> {
        LOG.info("Generation error");
        displayGenerationError();
    });
    // Listen for generated script refreshes
    scriptGeneratorModel.addPropertyChangeListener(GENERATED_SCRIPT_FILENAME_PROPERTY, evt -> {
        String scriptFilename = (String) evt.getNewValue();
        DISPLAY.asyncExec(() -> {
        scriptGeneratorModel.getLastGeneratedScript().ifPresentOrElse(
            generatedScript -> {
                (new SaveScriptGeneratorFileMessageDialog(Display.getDefault().getActiveShell(), "Script Generated", scriptFilename, 
                    scriptGeneratorModel.getDefaultScriptDirectory(), generatedScript, scriptGeneratorModel)).open();
            },
            () -> {
                MessageDialog.openWarning(DISPLAY.getActiveShell(), "Error", "Failed to generate the script");
            }
            );
        });
    });

    }

    /**
     * Check if there are any script definitions loaded.
     * 
     * @return true if there is at least one script definition loaded, false if not.
     */
    public boolean scriptDefinitionsAvailable() {
    return this.scriptGeneratorModel.getScriptDefinitionLoader().scriptDefinitionAvailable();
    }


    /**
     * Display a message dialog box that the language that is being used is unsupported.
     */
    private void displayLanguageSupportError() {
    MessageDialog.openError(DISPLAY.getActiveShell(), 
        "Language support issue",
        "You are attempting to use an unsupported language, "
            + "parameter validity checking and script generation are disabled at this time");
    }

    /**
     * Display a message dialog box that there was a threading issue when generating or checking parameter validity.
     */
    private void displayGenerationError() {
    DISPLAY.asyncExec(() -> {
        MessageDialog.openError(DISPLAY.getActiveShell(), 
            "Error",
            "Error when generating a script, are your parameters valid?");
    });
    }

    /**
     * Display a message dialog box that there was an issue when generating a script
     */
    private void displayThreadingError() {
    MessageDialog.openError(DISPLAY.getActiveShell(), 
        "Error",
        "Generating or parameter validity checking error. Threading issue.");
    }

    /**
     * Adds a new action (row) to the ActionsTable, with default parameter values.
     */
    protected void addEmptyAction() {
    scriptGeneratorModel.addEmptyAction();
    // Make sure the table is updated with the new action before selecting it
    actionChangeHandler(viewTable, btnGenerateScript, btnSaveParam, btnSaveParamAs);
    DISPLAY.asyncExec(() -> {
    	viewTable.setCellFocus(scriptGeneratorModel.getActions().size() - 1, ActionsViewTable.NON_EDITABLE_COLUMNS_ON_LEFT);
    });
    }
    
    /**
	 * Adds a new action with default parameters to the list of actions to a specified location in the table.
	 * 
	 * @param insertionLocation The index to add the specified 
	 */
    protected void insertEmptyAction(Integer insertionLocation) {
    scriptGeneratorModel.insertEmptyAction(insertionLocation);
    // Make sure the table is updated with the new action before selecting it
    actionChangeHandler(viewTable, btnGenerateScript, btnSaveParam, btnSaveParamAs);
    DISPLAY.asyncExec(() -> {
        viewTable.setCellFocus(insertionLocation, 0);
    });
    }
    
    /**
     * Removes action at position index from ActionsTable.
     * 
     * @param actionsToDelete
     *             the actions to delete.
     */
    protected void deleteAction(List<ScriptGeneratorAction> actionsToDelete) {
    scriptGeneratorModel.deleteAction(actionsToDelete);
    }

    /**
     * Duplicates action at position indices in ActionsTable.
     * 
     * @param actionsToDuplicate
     *             The actions to duplicate.
     * @param insertionLocation
     *          The index in the list to do the insertion.
     */
    protected void duplicateAction(List<ScriptGeneratorAction> actionsToDuplicate, Integer insertionLocation) {
    scriptGeneratorModel.duplicateAction(actionsToDuplicate, insertionLocation);
    // Make sure the table is updated with the new action before selecting it
    actionChangeHandler(viewTable, btnGenerateScript, btnSaveParam, btnSaveParamAs);
    DISPLAY.asyncExec(() -> {
    	viewTable.setCellFocus(insertionLocation, ActionsViewTable.NON_EDITABLE_COLUMNS_ON_LEFT);
    });
    }

    /**
     * Clears all actions from the ActionsTable.
     */
    protected void clearAction() {
    DISPLAY.asyncExec(() -> {
        boolean userConfirmation = MessageDialog.openConfirm(DISPLAY.getActiveShell(),
            "Warning",
            "This will delete all actions, are you sure you want to continue?");
        if (userConfirmation) {
        scriptGeneratorModel.clearAction();
        }
    });
    }

    /**
     * Moves actions one row up in table.
     * 
     * @param actionsToMove
     *             the actions to move.
     */
    protected void moveActionUp(List<ScriptGeneratorAction> actionsToMove) {
    scriptGeneratorModel.moveActionUp(actionsToMove);
    }

    /**
     * Moves actions one row down in table.
     * 
     * @param actionsToMove
     *             the actions to move.
     */
    protected void moveActionDown(List<ScriptGeneratorAction> actionsToMove) {
    scriptGeneratorModel.moveActionDown(actionsToMove);
    }

    /**
     * Clean up resources when the plugin is destroyed.
     */
    protected void cleanUp() {
    scriptGeneratorModel.cleanUp();
    }

    /**
     * Get a list of available script definitions.
     * 
     * @return A list of available script definitions.
     */
    protected List<String> getAvailableScriptDefinitionsNames() {
    return scriptGeneratorModel.getAvailableScriptDefinitions()
        .stream()
        .map(scriptDefinition -> scriptDefinition.getName())
        .collect(Collectors.toList());
    }

    /**
     * Gets all script definitions that could not be loaded and the reason.
     * 
     * @return A map of script definition load errors with keys as the name of the scriptDefinition.
     *  and the value as the reason it could not be loaded.
     */
    protected Map<String, String> getScriptDefinitionLoadErrors() {
    return scriptGeneratorModel.getScriptDefinitionLoadErrors();
    }

    /**
     * If git errors have been raised, concatenate them to a string to be displayed in a dialog box.
     * @return Optional containing error messages raised.
     */
    protected Optional<String> getGitLoadErrors() {
    	List<String> gitLoadErrors = scriptGeneratorModel.getGitLoadErrors();
    	String loadErrorMessage = null;

    	if (gitLoadErrors.size() > 0) {
    		loadErrorMessage = "The following error(s) occurred when updating the script definitions. It should be OK to continue, but the definitions may be out of date:\n";
    		for (String error: scriptGeneratorModel.getGitLoadErrors()) {
    			loadErrorMessage += error + "\n";
    		}
    	}

    	return Optional.ofNullable(loadErrorMessage);
    }

    
    /**
     * Create and get the label provider for the scriptDefinition selector.
     * 
     * @return The label provider.
     */
    protected LabelProvider getScriptDefinitionSelectorLabelProvider() {
    return new LabelProvider() {
        /**
         * Use getName method on python ScriptGeneratorWrapper class to get labels.
         */
        @Override
        public String getText(Object element) {
        if (element instanceof ScriptDefinitionWrapper) {
            ScriptDefinitionWrapper scriptDefinitionWrapper = (ScriptDefinitionWrapper) element;
            return scriptDefinitionWrapper.getName();
        }
        return super.getText(element);
        }
    };
    }

    /**
     * Listen for changes in actions and activate the handler.
     */
    private PropertyChangeListener actionChangeListener = evt -> {
    actionChangeHandler(viewTable, btnGenerateScript, btnSaveParam, btnSaveParamAs);
    };

    /**
     * Listen to changes on the actions and action properties of the scriptGenerator table and
     *  update the view table.
     * 
     * @param viewTable The view table to update.
     * @param btnGenerateScript The generate script button to style change.
     * @param btnSaveParam The save parameters button.
     * @param btnSaveParamAs The save parameters as button.
     */
    protected void bindActionProperties(ActionsViewTable viewTable, Button btnGenerateScript, Button btnSaveParam, Button btnSaveParamAs) {
    this.viewTable = viewTable;
    this.btnGenerateScript = btnGenerateScript;
    this.btnSaveParam = btnSaveParam;
    this.btnSaveParamAs = btnSaveParamAs;
    // Remove listeners so as not to bind them twice
    this.scriptGeneratorModel.getScriptGeneratorTable().removePropertyChangeListener(ACTIONS_PROPERTY, actionChangeListener);
    this.scriptGeneratorModel.getScriptGeneratorTable().addPropertyChangeListener(ACTIONS_PROPERTY, actionChangeListener);
    this.scriptGeneratorModel.getScriptGeneratorTable().addPropertyChangeListener(ACTIONS_PROPERTY, evt -> {
    	updateParametersFileModifiedStatus();
	});
    this.scriptGeneratorModel.removePropertyChangeListener(VALIDITY_ERROR_MESSAGE_PROPERTY, actionChangeListener);
    this.scriptGeneratorModel.addPropertyChangeListener(VALIDITY_ERROR_MESSAGE_PROPERTY, actionChangeListener);
    this.scriptGeneratorModel.removePropertyChangeListener(TIME_ESTIMATE_PROPERTY, actionChangeListener);
    this.scriptGeneratorModel.addPropertyChangeListener(TIME_ESTIMATE_PROPERTY, actionChangeListener);
    }

    private void updateParametersFile(String parametersFilePath) {
	String displayFile = "Current parameters file: " + parametersFilePath;
	currentParametersFile = parametersFilePath;		// Update the current parameter file path for Save.
	unsavedChangesMarkerDisplayed = false;			// Reset unsaved changes
	
	// Save the loaded actions to check if modified.
	try {
		currentFileActions = scriptGeneratorModel.loadParameterValues(Paths.get(parametersFilePath));
	} catch (UnsupportedOperationException | NoScriptDefinitionSelectedException | ScriptDefinitionNotMatched e) {
		// pass
	}

	firePropertyChange("parametersFile", parametersFileDisplayString, parametersFileDisplayString = displayFile);
    }
    
    private void updateParametersFileModifiedStatus() {
    	
    	// Get the action parameters currently in the table
    	List<Map<JavaActionParameter, String>> tableActionParameters = new ArrayList<Map<JavaActionParameter, String>>();
    	for (ScriptGeneratorAction action : scriptGeneratorModel.getActions()) {
    		tableActionParameters.add(action.getActionParameterValueMap());
    	}
    	
    	// Compare the table parameters with those of the loaded file
    	if (!tableActionParameters.equals(currentFileActions)) {
        	if (!unsavedChangesMarkerDisplayed) {
        		// If table does not match file, and no unsaved changes marker is displayed, display it
        		String displayString = parametersFileDisplayString + UNSAVED_CHANGES_MARKER;
            	firePropertyChange("parametersFile", parametersFileDisplayString, parametersFileDisplayString = displayString);
            	unsavedChangesMarkerDisplayed = true;
        	}
    	} else {
    		if (unsavedChangesMarkerDisplayed) {
    			// If table matches file, and unsaved changes marker is displayed, hide it
        		String displayString = parametersFileDisplayString.replace(UNSAVED_CHANGES_MARKER, "");
	    		firePropertyChange("parametersFile", parametersFileDisplayString, parametersFileDisplayString = displayString);
	    		unsavedChangesMarkerDisplayed = false;
    		}
    	}
    }
    
    private void updateTotalEstimatedTime() {

    long totalSeconds = scriptGeneratorModel.getTotalEstimatedTime().isPresent() ? scriptGeneratorModel.getTotalEstimatedTime().get() : 0;
    String displayTotal = "Total estimated run time: " + changeSecondsToTimeFormat(totalSeconds);

    firePropertyChange("timeEstimate", displayString, displayString = displayTotal);
    }


    private String changeSecondsToTimeFormat(long totalSeconds) {
    Duration duration = Duration.ofSeconds(totalSeconds);
    return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    /**
     * Get the name and location of the parameters save file for the script.
     * @return The parameters save file name and location.
     */
    public String getParametersFile() {
    return parametersFileDisplayString;
    }
    
    /**
     * Get the estimated time for the script, formatted into something human readable.
     * @return The formatted script time estimate.
     */
    public String getTimeEstimate() {
    return displayString;
    }

    /**
     * Handle a change in the actions or their properties.
     * Set the UI table's actions from the model and update validity checking.
     * 
     * @param viewTable The view table to update.
     * @param btnGenerateScript Generate Script button's visibility to manipulate
     * @param btnSaveParam Save Parameter button's visibility to manipulate
     * @param btnSaveParamAs Save Parameter As button's visibility to manipulate
     */
    private void actionChangeHandler(ActionsViewTable viewTable, Button btnGenerateScript, Button btnSaveParam, Button btnSaveParamAs) {
    DISPLAY.asyncExec(() -> {
        if (!viewTable.isDisposed()) {
        viewTable.setRows(scriptGeneratorModel.getActions());
        updateValidityChecks(viewTable);
        }
        if (!btnGenerateScript.isDisposed()) {
        setButtonGenerateStyle(btnGenerateScript);
        setButtonGenerateStyle(btnSaveParam);
        setButtonGenerateStyle(btnSaveParamAs);
        }
        updateTotalEstimatedTime();
    });
    }

    private void setButtonGenerateStyle(Button btnGenerateScript) {
    if (scriptGeneratorModel.languageSupported) {
        // Grey the button out if parameters are valid
        btnGenerateScript.setEnabled(scriptGeneratorModel.areParamsValid());
    } else {
        // Grey the button out when language is not supported
        btnGenerateScript.setEnabled(false);
    }
    }

    private PropertyChangeListener scriptDefinitionSwitchHelpListener = new PropertyChangeListener() {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Display the new script definition help string
        if (!helpText.isDisposed()) {
        Optional<ScriptDefinitionWrapper> optionalScriptDefinition = getScriptDefinition();
        optionalScriptDefinition.ifPresentOrElse(
            realScriptDefinition -> {
                displayHelpString(realScriptDefinition, helpText);
            },
            () -> {
                helpText.setText("");
            });
        }
    }
    };

    /**
     * The view's current helpText element.
     */
    private Text helpText;

    private ISelectionChangedListener scriptDefinitionSwitchListener = new ISelectionChangedListener() {

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        String selectedScriptDefinitionName;
        if (!event.getSelection().isEmpty()) {
        selectedScriptDefinitionName = (String) event.getStructuredSelection().getFirstElement();
        scriptGeneratorModel.getScriptDefinitionLoader().getLastSelectedScriptDefinitionName()
        .ifPresentOrElse(lastSelectedScriptDefinitionName -> {
            if (!selectedScriptDefinitionName.equals(lastSelectedScriptDefinitionName)) {
            scriptGeneratorModel.getScriptDefinitionLoader().setScriptDefinition(selectedScriptDefinitionName);
            }
        }, () -> scriptGeneratorModel.getScriptDefinitionLoader().setScriptDefinition(selectedScriptDefinitionName));
        }
    }
    };

    /**
     * Bind the script definition loader to the context.
     * 
     * @param scriptDefinitionSelector The script definition selector ui element to bind.
     * @param helpText The UI element to display help string text in.
     */
    protected void bindScriptDefinitionLoader(ComboViewer scriptDefinitionSelector, Text helpText) {
    // Switch the composite value when script definition switched
    scriptDefinitionSelector.removeSelectionChangedListener(scriptDefinitionSwitchListener);
    scriptDefinitionSelector.addSelectionChangedListener(scriptDefinitionSwitchListener);
    // Display new help when script definition switch or make invisible if not help available
    this.helpText = helpText;
    scriptGeneratorModel.getScriptDefinitionLoader().addPropertyChangeListener(SCRIPT_DEFINITION_SWITCH_PROPERTY, scriptDefinitionSwitchHelpListener);
    }

    /**
     * Display help string to the user if present, else clear the help string UI display.
     * 
     * @param scriptDefinition The script definition to get the help string from
     * @param helpText The text UI element to display the help string in.
     */
    private void displayHelpString(ScriptDefinitionWrapper scriptDefinition, Text helpText) {
    Optional.ofNullable(scriptDefinition.getHelp()).ifPresentOrElse(
        helpString -> {
            helpText.setText(helpString);
        },
        () -> {
            helpText.setText("");
        });
    }
    /**
     * Asynchronously get the URL of the user manual and bind it to the Open Manual button.
     * 
     * @param manualButton The button that should open the manual
     */
    public void bindManualButton(Button manualButton) {
    CompletableFuture.supplyAsync(() -> getUserManualUrl())
    .thenAccept(url -> {
        DISPLAY.asyncExec(() -> {
        if (!manualButton.isDisposed()) {
            setupLinkButton(manualButton, url);
        }
        });
    });
    }

    /**
     * Sets up the button that links to the manual of the script generator.
     * 
     * @param linkButton The button to set up.
     * @param target The url to point the button towards (an empty optional, if nowhere to point the button to).
     */
    protected void setupLinkButton(Button linkButton, Optional<URL> target) {
    target.ifPresent(url -> {
        linkButton.setEnabled(true);
        linkButton.setToolTipText(url.toString());
        linkButton.addListener(SWT.Selection, e -> {
        try {
            IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
            browser.openURL(url);
        } catch (PartInitException ex) {
            LoggerUtils.logErrorWithStackTrace(LOG, "Failed to open URL in browser: " + url, ex);
        }
        });
    });
    }

    /**
     * Get the first i lines of invalidity errors for the current script definition and parameters.
     * 
     * @param i The number of lines to receive.
     * @return Invalidity errors.
     */
    protected String getFirstNLinesOfInvalidityErrors(int i) {
    return scriptGeneratorModel.getFirstNLinesOfInvalidityErrors(i);
    }

    /**
     * Get the actions currently held in the script generator table.
     * 
     * @return A list of script generator actions
     */
    protected List<ScriptGeneratorAction> getActions() {
    return scriptGeneratorModel.getActions();
    }

    /**
     * Update the tables action rows to be coloured if invalid.
     * 
     * @param viewTable The table in the view to update.
     */
    protected void updateValidityChecks(ActionsViewTable viewTable) {
    List<ScriptGeneratorAction> actions = scriptGeneratorModel.getActions();
    TableItem[] items = viewTable.table().getItems();
    int validityColumnIndex = viewTable.table().getColumnCount() - 2;
    for (int i = 0; i < actions.size(); i++) {
        if (i < items.length) {
        if (!scriptGeneratorModel.languageSupported) {
            items[i].setBackground(LIGHT_VALIDITY_CHECK_ERROR_COLOR);
            items[i].setBackground(validityColumnIndex, DARK_VALIDITY_CHECK_ERROR_COLOR);
        } else if (actions.get(i).isValid()) {
            items[i].setBackground(CLEAR_COLOR);
            items[i].setBackground(validityColumnIndex, VALID_COLOR);
        } else {
            items[i].setBackground(INVALID_LIGHT_COLOR);
            items[i].setBackground(validityColumnIndex, INVALID_DARK_COLOR);
        }
        } else {
        LOG.warn("ScriptGeneratorViewModel - ActionsTable and UI Table mismatch");
        }
    }
    }

    /**
     * Adds a parameter to this actions table.
     * 
     * @param viewTable The table view to add columns to.
     */
    protected void addColumns(ActionsViewTable viewTable) {  
        // Add line numbers
        TableViewerColumn lineNumberColumn = viewTable.createColumn("Action", 0, 
                new CellLabelProvider() {
                    @Override
                    public void update(ViewerCell cell) {
                        for (int i = 0; i < viewTable.table().getItemCount(); i++) {
                            if (cell.getElement().equals(viewTable.viewer().getElementAt(i))) {
                                cell.setText(String.valueOf(i + 1));
                                break;
                            }
                        }
                    }
        
                    @Override
                    public String getToolTipText(Object element) {
                        return getScriptGenActionToolTipText(element);
                    }
        });
    	lineNumberColumn.getColumn().setAlignment(SWT.CENTER);
        // Add action parameter columns
        for (JavaActionParameter actionParameter: scriptGeneratorModel.getActionParameters()) {
            String columnName = actionParameter.getName();
            TableViewerColumn column = viewTable.createColumn(
                columnName, 
                2,
                new DataboundCellLabelProvider<ScriptGeneratorAction>(viewTable.observeProperty(columnName)) {
                @Override
                protected String stringFromRow(ScriptGeneratorAction row) {
                    return row.getActionParameterValue(actionParameter);
                }
    
                @Override
                public String getToolTipText(Object element) {
                    return getScriptGenActionToolTipText(element);
                }
                });
    
            var editingSupport = new StringEditingSupport<ScriptGeneratorAction>(viewTable.viewer(), ScriptGeneratorAction.class) {          
                @Override
                protected String valueFromRow(ScriptGeneratorAction row) {
                    return row.getActionParameterValue(actionParameter);
                }
        
                @Override
                protected void setValueForRow(ScriptGeneratorAction row, String value) {
                    row.setActionParameterValue(actionParameter, value);
                }
            };
            viewTable.addEditingSupport(editingSupport);
            column.setEditingSupport(editingSupport);
            
        }
        // Add validity notifier column
        TableViewerColumn validityColumn = viewTable.createColumn("Validity", 
            1, 
            new DataboundCellLabelProvider<ScriptGeneratorAction>(viewTable.observeProperty("validity")) {
            @Override
            protected String stringFromRow(ScriptGeneratorAction row) {
            if (!scriptGeneratorModel.languageSupported) {
                return "\u003F"; // A question mark to say we cannot be certain
            }
            if (row.isValid()) {
                return "\u2714"; // A tick for valid
            }
            return "\u2718"; // Unicode cross for invalidity
            }
    
            @Override
            public String getToolTipText(Object element) {
            return getScriptGenActionToolTipText(element);
            }
    
        });
        validityColumn.getColumn().setAlignment(SWT.CENTER);
    
        // Add estimated time column
        TableViewerColumn timeEstimateColumn = viewTable.createColumn("Estimated run time", 
            1, 
            new DataboundCellLabelProvider<ScriptGeneratorAction>(viewTable.observeProperty(TIME_ESTIMATE_PROPERTY)) {
            @Override
            protected String stringFromRow(ScriptGeneratorAction row) {
            if (!scriptGeneratorModel.languageSupported) {
                return "\u003F"; // A question mark to say we cannot be certain
            }
    
            Optional<Number> estimatedTime = row.getEstimatedTime();
            if (estimatedTime.isEmpty()) {
                return "Unknown";
            }
            return changeSecondsToTimeFormat(estimatedTime.get().longValue());
            }
    
            @Override
            public String getToolTipText(Object element) {
            return getScriptGenActionToolTipText(element);
            }
    
        });
        timeEstimateColumn.getColumn().setAlignment(SWT.CENTER);
    
        ColumnViewerToolTipSupport.enableFor(viewTable.viewer());
    }

    /**
     * Get the tooltip text for a script generator action.
     * We don't want to show a tooltip if the action is valid, 
     *   SWT shows no tooltip if we return null.
     * 
     * @param action The action to get the tooltip text for.
     * @return The text for the action tooltip.
     */
    private String getScriptGenActionToolTipText(Object action) {
    if (action == null) {
        return null;
    }
    ScriptGeneratorAction actionImpl = (ScriptGeneratorAction) action;
    if (actionImpl.isValid()) {
        return null; // Do not show a tooltip
    }
    return "The reason this row is invalid is:\n"
    + actionImpl.getInvalidityReason().get() + "\n"; // Show reason on next line as a tooltip
    }

    /**
     * Attach a property listener on action parameters to update the table columns when changed.
     * 
     * @param viewTable The table to update the columns of.
     */
    protected void addActionParamPropertyListener(ActionsViewTable viewTable) {
    scriptGeneratorModel.getScriptGeneratorTable().addPropertyChangeListener("actionParameters", 
        e -> DISPLAY.asyncExec(() -> {
            if (!viewTable.isDisposed()) {
            viewTable.updateTableColumns();
            }
        })
        );
    }

    /**
     * Display a new dialog with information about the script generator.
     */
    public void displayAbout() {
    	var shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    	var handler = new AboutHandler();
    	handler.execute(shell);
    }

    /**
     * Generate a script and display the file it was generated to. If fail display warnings.
     * @throws UnsupportedLanguageException 
     */
    public void generate() {
    try {
        scriptGeneratorModel.refreshGeneratedScript();
    } catch (InvalidParamsException e) {
        MessageDialog.openWarning(DISPLAY.getActiveShell(), "Params Invalid", 
            "Cannot generate script. Parameters are invalid.");
    } catch (UnsupportedLanguageException e) {
        LOG.error(e);
        MessageDialog.openWarning(DISPLAY.getActiveShell(), "Unsupported language", 
            "Cannot generate script. Language to generate in is unsupported.");
    } catch (NoScriptDefinitionSelectedException e) {
        LOG.error(e);
        MessageDialog.openWarning(DISPLAY.getActiveShell(), "No script definition selection", 
            "Cannot generate script. No script definition has been selected");
    }
    }

    /**
     * Get the current selected script definition.
     * 
     * @return The selected script definition.
     */
    public Optional<ScriptDefinitionWrapper> getScriptDefinition() {
    return scriptGeneratorModel.getScriptDefinition();
    }

    /**
     * Reload the available script definition.
     */
    public void reloadScriptDefinitions() {
    scriptGeneratorModel.reloadScriptDefinitions();
    }

	public String getPromptMessage() {
        String message = "Updates to the script definitions are available. Updating your definitions may erase current scripts.";
		if (scriptGeneratorModel.isDirty()) {
			message += "\n WARNING: There are uncommitted changes to the script definitions. These will be lost if you update.";
		}
		return message;
	}

	/**
	 * Gets whether the remote git repo URL is accessible.
	 * @return true if the remote repo URL can be accessed.
	 */
	public boolean remoteAvailable() {
		return scriptGeneratorModel.remoteAvailable();
	}
	
	/**
	 * Get whether there are updates available for the git repository.
	 * @return true if there are updates available.
	 */
	public boolean updatesAvailable() {
		return scriptGeneratorModel.updatesAvailable();
	}


	/**
	 * Merges git repository from upstream.
	 */
	public void mergeOrigin() {
		scriptGeneratorModel.mergeOrigin();
		
	}
    /**
     * Reload the actions table actions.
     */
    public void reloadActions() {
    scriptGeneratorModel.reloadActions();
    }

    /**
     * Save Parameter Values to the current file.
     */
    public void saveParameterValuesToCurrentFile() {
    	// If there is no current file, do Save As to new file.
    	if (currentParametersFile == null || currentParametersFile.isEmpty()) {
    		saveParameterValues();
    	} else {
    		try {
    		scriptGeneratorModel.saveParameters(currentParametersFile);
    		updateParametersFile(currentParametersFile);
            MessageDialog.openInformation(DISPLAY.getActiveShell(), "Saved", "File saved!");  
    		} catch (NoScriptDefinitionSelectedException e) {
            LOG.error(e);
            MessageDialog.openWarning(DISPLAY.getActiveShell(), "No script definition selection", 
                "Cannot generate script. No script definition has been selected");
    		}
    	}
    }
    
    /**
     * Save Parameter Values to a file. Checks if file already exists and asks if user wants to overwrite it.
     */
    public void saveParameterValues() {        
    Optional<String> fileName  = openFileDialog(SWT.SAVE);
    // filename will be null if user has clicked cancel button
    if (fileName.isPresent()) {
        try {
        scriptGeneratorModel.saveParameters(fileName.get());
        updateParametersFile(fileName.get());
        MessageDialog.openInformation(DISPLAY.getActiveShell(), "Saved", "File saved!");  
        } catch (NoScriptDefinitionSelectedException e) {
        LOG.error(e);
        MessageDialog.openWarning(DISPLAY.getActiveShell(), "No script definition selection", 
            "Cannot generate script. No script definition has been selected");
        } 
    }
    }

    /**
     * Open file dialog which opens file browser for saving and loading parameter values.
     * @param action save or load
     * @return filename to save or load
     */
    private Optional<String> openFileDialog(int action) {
    FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), action);
    dialog.setFilterPath("C:/scripts");
    dialog.setOverwrite(true);
    if (action == SWT.SAVE) {
        dialog.setText("Save as");
        dialog.setFilterExtensions(new String[] {"*.sgp"});

    } else {
        dialog.setText("Load");
        // Keep JSON extension when loading (for older param. files)
        dialog.setFilterExtensions(new String[] {"*.sgp", "*.json"});
    }
    return Optional.ofNullable(dialog.open());
    }

    /**
     * Load parameter values.
     */
    public void loadParameterValues() {
    Optional<String> selectedFile = openFileDialog(SWT.OPEN);
    if (selectedFile.isPresent()) {
        List<Map<JavaActionParameter, String>> newActions = new ArrayList<Map<JavaActionParameter, String>>();
        try {
        newActions = scriptGeneratorModel.loadParameterValues(Paths.get(selectedFile.get()));
        } catch (NoScriptDefinitionSelectedException e) {
        LOG.error(e);
        MessageDialog.openWarning(DISPLAY.getActiveShell(), "No script definition selection", 
            "Cannot generate script. No script definition has been selected");
        return;
        } catch (ScriptDefinitionNotMatched | UnsupportedOperationException e) {
        LOG.error(e);
        MessageDialog.openError(DISPLAY.getActiveShell(), "Error", e.getMessage());
        return;
        }
        
        Integer dialogResponse; //-1 for cancel, 0 for append, 1 for replace
        Boolean emptyModel;
        if (scriptGeneratorModel.getActions().isEmpty()) {
        emptyModel = true;
        dialogResponse = 0;
        } else {
        emptyModel = false;
        String[] replaceOrAppend = new String[] {"Append", "Replace"};
        MessageDialog dialog = new MessageDialog(DISPLAY.getActiveShell(), "Replace or Append", null,
            "Would you like to replace the current parameters or append the new parameters?", 
            MessageDialog.QUESTION, replaceOrAppend, -1);
        dialogResponse = dialog.open();
        }

        if (dialogResponse != -1) {
        Boolean replace = dialogResponse == 1;
        scriptGeneratorModel.addActionsToTable(newActions, replace);
        }
        
        if (emptyModel || dialogResponse == 1) {
	        // Get the save file path from Optional value and update the current file path label.
	        updateParametersFile(selectedFile.get());
        }

    }
    };


    /** Get the user manual URL for the script generator from the model.
     * 
     * @return An optional with a value containing the URL if there is a user manual to load.
     */
    public Optional<URL> getUserManualUrl() {
    return scriptGeneratorModel.getUserManualUrl();
    }

    /**
     * Set the selected actions.
     * @param selectedRows The selected actions.
     */
    public void setSelected(List<ScriptGeneratorAction> selectedRows) {
    setHasSelection(selectedRows.size() > 0);
    }

    private void setHasSelection(boolean hasSelection) {
    firePropertyChange("hasSelection", this.hasSelection, this.hasSelection = hasSelection);
    }

    /**
     * Get whether a selection has been made.
     * @return True if an action has been selected, false otherwise.
     */
    public boolean getHasSelection() {
    return hasSelection;
    }

	/**
	 * Gets the singleton to update its path to the repo from the python.
	 */
	public void setRepoPath() {
		scriptGeneratorModel.setRepoPath();
	}
	
	/**
	 * Copies actions to clipboard.
	 * @param actions copied actions
	 */
	public void copyActions(String actions) {
		clipboard.setContents(new Object[] {actions}, new Transfer[] {TextTransfer.getInstance()});
	}
	
	/**
	 * Paste copied actions to the script generator. If the user wants to paste the copied actions
	 * to another script definition, it will decide how many values(per row) to paste depending on the number
	 * column label in new script definition. If there are 10 copied values and the script definition to
	 * which user has switched to only contains 3 param, it will paste only first 3 values however,
	 * all 10 copied values will still be in clipboard.
	 * @param pasteLocation row where user wants to paste.
	 */
	public void pasteActions(int pasteLocation) {
		String copiedActions = (String) clipboard.getContents(TextTransfer.getInstance());
		List<JavaActionParameter> parameters = scriptGeneratorModel.getActionParameters();
		// Convert string data to a List
		ArrayList<String> actions = new ArrayList<String>(Arrays.asList(copiedActions.split(CRLF)));
		// find how many values per row
		int numerOfValuesPerRow = copiedActions.split(CRLF)[0].split(TAB).length;
		// calculate how many values per row to actually paste. It could be different if user has switched script definition.
		int numberOfValuesPerRowToPaste = min(numerOfValuesPerRow, parameters.size());
		ArrayList<Map<JavaActionParameter, String>> listOfActions = new ArrayList<Map<JavaActionParameter, String>>();
		for (String action: actions) {
			Map<JavaActionParameter, String> map = IntStream.range(0, numberOfValuesPerRowToPaste)
		            .boxed()
		            .collect(Collectors.toMap(idx -> parameters.get(idx), idx -> Arrays.asList(action.split(TAB)).get(idx)));
			listOfActions.add(map);
		}
		scriptGeneratorModel.pasteActions(listOfActions, pasteLocation);
	}

	/**
	 * Cleans up resources being used by the view model.
	 */
    public void dispose() {
        clipboard.dispose();
    }
		
}
