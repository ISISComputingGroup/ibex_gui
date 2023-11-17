package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.apache.logging.log4j.Logger;
import static java.lang.Math.min;

import uk.ac.stfc.isis.ibex.scriptgenerator.Activator;
import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptDefinitionNotMatched;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs.SaveScriptGeneratorFileMessageDialog;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The ViewModel for the ScriptGeneratorView.
 */
public class ScriptGeneratorViewModel extends ModelObject {
    protected static final Logger LOG = IsisLog.getLogger(ScriptGeneratorViewModel.class);
    
    private Set<Integer> nicosScriptIds = new HashSet<>();
    
    /**
     * Script IDs that are to be generated to the current file path, if it exists.
     */
    private Set<Integer> scriptsToGenerateToCurrentFilepath = new HashSet<>();

    /**
     * Path to the current parameters save file.
     */
    private String currentParametersFilePath;
    
    /**
     * Time and date of when the script was generated.
     */
    private String scriptGenerationTime;
    
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
    private String parametersFileDisplayString = "Current Script: <new file>";
    
    /**
     * Default string to display for time estimation.
     */
    private String displayString = "Total estimated run time: 00:00:00";

    /**
     * Class to handle updating the expected finish time.
     */
    private ScriptGeneratorExpectedFinishTimer finishTimer;
    
    /**
     * The Scheduler for finish timer.
     */
    private ScheduledExecutorService scheduler;
    
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
     * The current Generate Script As button in the view.
     */
    private Button btnGenerateScriptAs;
    
    /**
     * The currently selected rows
     */
    private boolean hasSelection;
    
    private Clipboard clipboard;
    private static final String TAB = "\t";
    private static final String CRLF = "\r\n";   
    
    
    /**
     * A constructor that sets up the script generator model and 
     *   begins listening to property changes in the model.
     */
    public ScriptGeneratorViewModel() {
	    // Set up the model
	    scriptGeneratorModel = Activator.getModel();
	    scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.PYTHON_READINESS_PROPERTY, evt -> {
	        firePropertyChange(ScriptGeneratorProperties.PYTHON_READINESS_PROPERTY, evt.getOldValue(), evt.getNewValue());
	    });
    }

    /**
     * Set up the model. Allows us to attach listeners for the view first.
     * 
     * @return the model.
     */
    public ScriptGeneratorSingleton setUpModel() {
	    clipboard = new Clipboard(Display.getDefault());
	    scriptGeneratorModel.createScriptDefinitionLoader();
	    scriptGeneratorModel.setUp();
	    // Listen to whether the language support is changed
	    // notify the user if the language is not supported
	    scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.LANGUAGE_SUPPORT_PROPERTY, evt -> {
	        if (Objects.equals(evt.getOldValue(), true) && Objects.equals(evt.getNewValue(), false)) {
	        displayLanguageSupportError();
	        }
	    });
	    // Listen for model threading errors and display to the user if there is one
	    // Model is responsible for logging it
	    scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.THREAD_ERROR_PROPERTY, evt -> {
	        displayThreadingError();
	    });
	    scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.SCRIPT_GENERATION_ERROR_PROPERTY, evt -> {
	        LOG.info("Generation error");
	        displayGenerationError();
	    });
	    // Listen for generated script refreshes
	    scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.GENERATED_SCRIPT_FILENAME_PROPERTY, evt -> {
	        String scriptFilename = (String) evt.getNewValue();
	        Constants.DISPLAY.asyncExec(() -> {
	        scriptGeneratorModel.getLastGeneratedScriptId().ifPresentOrElse(
	            generatedScriptId -> {
	            	scriptGeneratorModel.getScriptFromId(generatedScriptId).ifPresentOrElse(generatedScript -> {
	            		if (!scriptGeneratorModel.isScriptDynamic(generatedScriptId)) {
	            			if (scriptsToGenerateToCurrentFilepath.contains(generatedScriptId)) {
	            				saveScriptToCurrentFilepath(generatedScript);
	            			} else {
	            				saveScriptToNewFilepath(generatedScript, scriptFilename);
	            			}
	            		}
	            	}, () -> {
	            		MessageDialog.openWarning(Constants.DISPLAY.getActiveShell(), "Error", "Failed to generate the script");
	            	});
	            },
	            () -> {
	                MessageDialog.openWarning(Constants.DISPLAY.getActiveShell(), "Error", "Failed to generate the script");
	            }
	            );
	        });
	    });
	    return scriptGeneratorModel;
    }
    
    private void saveScriptToCurrentFilepath(String generatedScript) {
		try {
	    	// Get the script file name using the current parameters file. If no file is loaded, generate name.
			String scriptFilePath = currentParametersFilePath != null
									? currentParametersFilePath
									: scriptGeneratorModel.getDefaultScriptDirectory() + scriptGeneratorModel.generateScriptFileName();
			scriptFilePath = scriptGeneratorModel.getScriptFileNameFromFilepath(scriptFilePath);
			scriptGeneratorModel.getFileHandler().generate(scriptFilePath, generatedScript);
			scriptGeneratorModel.saveParameters(scriptFilePath);
			this.updateParametersFilePath(scriptGeneratorModel.getParametersFileNameFromFilepath(scriptFilePath));
			this.updateGenerationTime();
			MessageDialog.openInformation(Constants.DISPLAY.getActiveShell(), "Script Generated", 
					String.format("Generated script saved to %s.", scriptFilePath));
			
		} catch (IOException e) {
			LOG.error(e);
			MessageDialog.openError(Constants.DISPLAY.getActiveShell(), "Error", "Failed to write generated script to file");
		} catch (NoScriptDefinitionSelectedException e) {
	        MessageDialog.openWarning(Constants.DISPLAY.getActiveShell(), "No script definition selection", 
	                "Cannot generate script. No script definition has been selected");
		}
    }
    
    private void saveScriptToNewFilepath(String generatedScript, String scriptFilename) {
    	SaveScriptGeneratorFileMessageDialog saveAs = (new SaveScriptGeneratorFileMessageDialog(Display.getDefault().getActiveShell(), "Script Generated", scriptFilename, 
				   scriptGeneratorModel.getDefaultScriptDirectory(), generatedScript, scriptGeneratorModel));
    	String genFilePath = (new SaveScriptGeneratorFileMessageDialog(Display.getDefault().getActiveShell(), "Script Generated", scriptFilename, 
				  scriptGeneratorModel.getDefaultScriptDirectory(), generatedScript, scriptGeneratorModel)).open();
		if (genFilePath != null) {
			String paramsFilePath = scriptGeneratorModel.getParametersFileNameFromFilepath(genFilePath);
			this.updateParametersFilePath(paramsFilePath);
			this.updateGenerationTime();
			saveAs.askIfOpenInEditor(genFilePath);
		}
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
	    MessageDialog.openError(Constants.DISPLAY.getActiveShell(), 
	        "Language support issue",
	        "You are attempting to use an unsupported language, "
	            + "parameter validity checking and script generation are disabled at this time");
    }

    /**
     * Display a message dialog box that there was a threading issue when generating or checking parameter validity.
     */
    private void displayGenerationError() {
	    Constants.DISPLAY.asyncExec(() -> {
	        MessageDialog.openError(Constants.DISPLAY.getActiveShell(), 
	            "Error",
	            "Error when generating a script, are your parameters valid?");
	    });
    }

    /**
     * Display a message dialog box that there was an issue when generating a script
     */
    private void displayThreadingError() {
	    MessageDialog.openError(Constants.DISPLAY.getActiveShell(), 
	        "Error",
	        "Generating or parameter validity checking error. Threading issue.");
    }

    /**
     * Adds a new action (row) to the ActionsTable, with default parameter values.
     */
    protected void addEmptyAction() {
	    scriptGeneratorModel.addEmptyAction();
	    // Make sure the table is updated with the new action before selecting it
	    if (!scriptGeneratorModel.getActionParameters().get(ActionsViewTable.NON_EDITABLE_COLUMNS_ON_LEFT).getIsEnum()) {
	    	  Constants.DISPLAY.asyncExec(() -> {
	  	    	viewTable.setCellFocus(scriptGeneratorModel.getActions().size() - 1, ActionsViewTable.NON_EDITABLE_COLUMNS_ON_LEFT);
	  	    });
	    }
	  
    }
    
    /**
	 * Adds a new action with default parameters to the list of actions to a specified location in the table.
	 * 
	 * @param insertionLocation The index to add the specified 
	 */
    protected void insertEmptyAction(Integer insertionLocation) {
	    scriptGeneratorModel.insertEmptyAction(insertionLocation);
	    // Make sure the table is updated with the new action before selecting it
	    if (!scriptGeneratorModel.getActionParameters().get(ActionsViewTable.NON_EDITABLE_COLUMNS_ON_LEFT).getIsEnum()) {
	    	  Constants.DISPLAY.asyncExec(() -> {
	  	    	viewTable.setCellFocus(scriptGeneratorModel.getActions().size() - 1, ActionsViewTable.NON_EDITABLE_COLUMNS_ON_LEFT);
	  	    });
	    }
    }
    
    /**
     * Removes action at position index from ActionsTable.
     * 
     * @param actionsToDelete
     *             the actions to delete.
     */
    protected void deleteAction(List<ScriptGeneratorAction> actionsToDelete) {
	    int toSelect = getFocusRowIndexAfterDelete(actionsToDelete);
	    
	    scriptGeneratorModel.deleteAction(actionsToDelete);
	    if (!scriptGeneratorModel.getActionParameters().get(ActionsViewTable.NON_EDITABLE_COLUMNS_ON_LEFT).getIsEnum()) {
	    	  Constants.DISPLAY.asyncExec(() -> {
	  	    	viewTable.setCellFocus(toSelect, ActionsViewTable.NON_EDITABLE_COLUMNS_ON_LEFT);
	  	    });
	    }
    }

    private int getFocusRowIndexAfterDelete(List<ScriptGeneratorAction> actionsToDelete) {
	    ScriptGeneratorAction lastActionToDelete = actionsToDelete.get(actionsToDelete.size() - 1);
	    int lastActionToDeleteIndex = scriptGeneratorModel.getActions().indexOf(lastActionToDelete);
	    
	    // If last action is selected, select new last action, otherwise select next action
	 	int focusRowIndex = lastActionToDeleteIndex == scriptGeneratorModel.getActions().size() - 1
	 						? lastActionToDeleteIndex - actionsToDelete.size() : (lastActionToDeleteIndex - actionsToDelete.size()) + 1;
	    
	    return focusRowIndex;
    
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
	    if (!scriptGeneratorModel.getActionParameters().get(ActionsViewTable.NON_EDITABLE_COLUMNS_ON_LEFT).getIsEnum()) {
	    	  Constants.DISPLAY.asyncExec(() -> {
	  	    	viewTable.setCellFocus(insertionLocation, ActionsViewTable.NON_EDITABLE_COLUMNS_ON_LEFT);
	  	    });
	    }
    }

    /**
     * Clears all actions from the ActionsTable.
     */
    protected void clearAction() {
	    Constants.DISPLAY.asyncExec(() -> {
	        boolean userConfirmation = MessageDialog.openConfirm(Constants.DISPLAY.getActiveShell(),
	            "Warning",
	            "This will delete all actions, are you sure you want to continue?");
	        if (userConfirmation) {
	        scriptGeneratorModel.clearActions();
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
	    Constants.DISPLAY.asyncExec(() -> {
	    	viewTable.setSelected(actionsToMove, true); 
	    });
    }

    /**
     * Moves actions one row down in table.
     * 
     * @param actionsToMove
     *             the actions to move.
     */
    protected void moveActionDown(List<ScriptGeneratorAction> actionsToMove) {
	    scriptGeneratorModel.moveActionDown(actionsToMove);
	    Constants.DISPLAY.asyncExec(() -> {
	    	viewTable.setSelected(actionsToMove, true);
	    });
    }

    /**
     * Clean up resources when the plug-in is destroyed.
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
	         * Use getName method on Python ScriptGeneratorWrapper class to get labels.
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
    	actionChangeHandler(viewTable, btnGenerateScript, btnGenerateScriptAs, false);
    };

    /**
     * Listen to changes on the actions and action properties of the scriptGenerator table and
     *  update the view table.
     * 
     * @param viewTable The view table to update.
     * @param btnGenerateScript The generate script button to style change.
     * @param btnGenerateScriptAs The generate script as button to style change.
     */
    protected void bindActionProperties(ActionsViewTable viewTable, Button btnGenerateScript, Button btnGenerateScriptAs) {
	    this.viewTable = viewTable;
	    this.btnGenerateScript = btnGenerateScript;
	    this.btnGenerateScriptAs = btnGenerateScriptAs;
	    // Remove listeners so as not to bind them twice
	    this.scriptGeneratorModel.getScriptGeneratorTable().removePropertyChangeListener(ScriptGeneratorProperties.ACTIONS_PROPERTY, actionChangeListener);
	    this.scriptGeneratorModel.getScriptGeneratorTable().addPropertyChangeListener(ScriptGeneratorProperties.ACTIONS_PROPERTY, actionChangeListener);
	    this.scriptGeneratorModel.getScriptGeneratorTable().addPropertyChangeListener(ScriptGeneratorProperties.ACTIONS_PROPERTY, evt -> {
	    	updateParametersFileModifiedStatus();
		});
	    this.scriptGeneratorModel.removePropertyChangeListener(ScriptGeneratorProperties.VALIDITY_ERROR_MESSAGE_PROPERTY, actionChangeListener);
	    this.scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.VALIDITY_ERROR_MESSAGE_PROPERTY, actionChangeListener);
	    this.scriptGeneratorModel.removePropertyChangeListener(ScriptGeneratorProperties.TIME_ESTIMATE_PROPERTY, actionChangeListener);
	    this.scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.TIME_ESTIMATE_PROPERTY, actionChangeListener);
	    this.scriptGeneratorModel.removePropertyChangeListener(ScriptGeneratorProperties.CUSTOM_ESTIMATE_PROPERTY, actionChangeListener);
	    this.scriptGeneratorModel.addPropertyChangeListener(ScriptGeneratorProperties.CUSTOM_ESTIMATE_PROPERTY, actionChangeListener);
	}
	
	private void updateParametersFilePath(String parametersFilePath) {
		this.clearGenerationTime();
		String displayFile = "Current Script: " + parametersFilePath;
		currentParametersFilePath = parametersFilePath;		// Update the current parameter file path for Save.
		unsavedChangesMarkerDisplayed = false;				// Reset unsaved changes marker.
		
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
        		String displayString = parametersFileDisplayString + Constants.UNSAVED_CHANGES_MARKER;
            	firePropertyChange("parametersFile", parametersFileDisplayString, parametersFileDisplayString = displayString);
            	unsavedChangesMarkerDisplayed = true;
        	}
    	} else {
    		if (unsavedChangesMarkerDisplayed) {
    			// If table matches file, and unsaved changes marker is displayed, hide it
        		String displayString = parametersFileDisplayString.replace(Constants.UNSAVED_CHANGES_MARKER, "");
	    		firePropertyChange("parametersFile", parametersFileDisplayString, parametersFileDisplayString = displayString);
	    		unsavedChangesMarkerDisplayed = false;
    		}
    	}
    }
    
    private void updateGenerationTime() {
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");  
	    String now = dtf.format(LocalDateTime.now());
    	String displayString = "Last Script Generated at: " + now;

    	firePropertyChange("scriptGenerationTime", scriptGenerationTime, scriptGenerationTime = displayString);
    }
    
    private void clearGenerationTime() {
    	firePropertyChange("scriptGenerationTime", scriptGenerationTime, scriptGenerationTime = "");
    }
    
    private void updateTotalEstimatedTime() {

	    long totalSeconds = scriptGeneratorModel.getTotalEstimatedTime().isPresent() ? scriptGeneratorModel.getTotalEstimatedTime().get() : 0;
	    String displayTotal = "Total estimated run time: " + changeSecondsToTimeFormat(totalSeconds);
	    finishTimer.setTimeEstimateVal(totalSeconds);
	    firePropertyChange("timeEstimate", displayString, displayString = displayTotal);
    }


    /**
     * Take the total seconds and format into an understandable time format (HH:MM:SS) for display.
     * 
     * @param totalSeconds The number of seconds to format.
     * @return The formatted string of the time.
     */
    public static String changeSecondsToTimeFormat(long totalSeconds) {
	    Duration duration = Duration.ofSeconds(totalSeconds);
	    return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    /**
     * Get the name and location of the parameters save file for the script.
     * 
     * @return The parameters save file name and location.
     */
    public String getParametersFile() {
    	return parametersFileDisplayString;
    }
    
    /**
     * Get the time and date of last script generation.
     * @return The time and date when the last script was generated.
     */
    public String getScriptGenerationTime() {
    	return scriptGenerationTime;
    }
    
    /**
     * Get the estimated time for the script, formatted into something human readable.
     * @return The formatted script time estimate.
     */
    public String getTimeEstimate() {
    	return displayString;
    }

    /**
     * Get the Finish timer.
     * @return The finish timer object of the view model.
     */
    public ScriptGeneratorExpectedFinishTimer getFinishTimer() {
    	return this.finishTimer;
    }

    /**
     * Handle a change in the actions or their properties.
     * Set the UI table's actions from the model and update validity checking.
     * 
     * @param viewTable The view table to update.
     * @param btnGenerateScript Generate Script button's visibility to manipulate
     */
    private void actionChangeHandler(ActionsViewTable viewTable, Button btnGenerateScript, Button btnGenerateScriptAs, boolean rowsChanged) {
	    Constants.DISPLAY.asyncExec(() -> {
	        if (!viewTable.isDisposed()) {
	        	viewTable.updateActions(scriptGeneratorModel.getActions());
	        	updateValidityChecks(viewTable);
	        }
	        if (!btnGenerateScript.isDisposed()) {
	        setButtonGenerateStyle(btnGenerateScript);
	        }
	        if (!btnGenerateScriptAs.isDisposed()) {
	        setButtonGenerateStyle(btnGenerateScriptAs);
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
	    	scriptGeneratorModel.clearActions();
	        for (Label label : globalLabel) {
	        	if (!label.isDisposed()) {
	        	label.dispose();
	        	}
	        }
	        globalLabel.clear();
	        for (Text text: globalParamText) {
	        	if (!text.isDisposed()) {
	        	text.dispose();
	        	}
	        }
	        scriptGeneratorModel.clearGlobalParams();
	        currentGlobals.clear();
	        globalParamText.clear();
	        createGlobalParamsWidgets();
	        if (!globalParamsComposite.isDisposed()) {
		        globalParamsComposite.layout();
	        }
		    mainParent.layout();
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
	        scriptGeneratorModel.clearActions();
	    }
    };
    
    /**
     * Create widgets for the global parameters.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public void createGlobalParamsWidgets() {
		List<ActionParameter> temp;
		String param = "No Global Paramaters";
    	String paramVal = "";
        if (getScriptDefinition().get().getGlobalParameters() != null) {
      	  temp = getScriptDefinition().get().getGlobalParameters();
      	  
      	  // Hide global parameters row if there is nothing to display
      	  ((GridData) globalParamsComposite.getLayoutData()).exclude = temp.isEmpty();
      	  globalParamsComposite.setVisible(!temp.isEmpty());
      	  
      	  if (!temp.isEmpty()) {
      		  for (ActionParameter global : temp) {
      			  param = global.getName();
      			  currentGlobals.add(global.getName());
      			  paramVal = global.getDefaultValue();
      			  if (!globalParamsComposite.isDisposed()) {
	            	  Label globalLabelCurrent = new Label(globalParamsComposite, SWT.NONE);
	            	  globalLabelCurrent.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
	            	  globalLabelCurrent.setText(param);
	            	  globalLabel.add(globalLabelCurrent);
	            	  Text globalParamTextCurrent = new Text(globalParamsComposite, SWT.NONE);
	            	  globalParamTextCurrent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
	            	  globalParamTextCurrent.setEnabled(true);
	            	  globalParamTextCurrent.addListener(SWT.Modify, e -> {
	            		  updateGlobalParams(globalParamTextCurrent.getText(), globalLabelCurrent.getText());
	            	  
	            	  	});
	            	  globalParamTextCurrent.setText(paramVal);
	            	  globalParamText.add(globalParamTextCurrent);
      			  }
      		  }
      	  }
      }
	}

    /**
     * The view's current helpText element.
     */
    private Text helpText;
    
    private List<Label> globalLabel;
    
    private List<Text> globalParamText;
    
    private List<String> currentGlobals;
    
    private Composite globalParamsComposite;
    
    private Composite mainParent;

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
     * @param scriptDefinitionSelector The script definition selector UI element to bind.
     * @param helpText The UI element to display help string text in.
     * @param globalLabel The label widgets of the global parameters.
     * @param globalParamText The text widgets of the global parameters.
     * @param scriptDefintionComposite Composite containing the script definition selector.
     * @param mainParent A composite containing the script generator as a whole.
     */
    protected void bindScriptDefinitionLoader(ComboViewer scriptDefinitionSelector, Text helpText, List<Label> globalLabel, List<Text> globalParamText, Composite scriptDefintionComposite, Composite mainParent) {
	    // Switch the composite value when script definition switched
	    scriptDefinitionSelector.removeSelectionChangedListener(scriptDefinitionSwitchListener);
	    scriptDefinitionSelector.addSelectionChangedListener(scriptDefinitionSwitchListener);
	    // Display new help when script definition switch or make invisible if not help available
	    this.helpText = helpText;
	    this.globalLabel = globalLabel;
	    this.globalParamText = globalParamText;
	    this.globalParamsComposite = scriptDefintionComposite;
	    this.mainParent = mainParent;
	    this.currentGlobals = new ArrayList<String>();
	    this.finishTimer = new ScriptGeneratorExpectedFinishTimer();
	    this.scheduler = Executors.newScheduledThreadPool(1, 
				new ThreadFactoryBuilder().setNameFormat("ScriptGeneratorViewModel-threadpool-%d").build());
	    this.scheduler.scheduleWithFixedDelay(finishTimer, 0, 1, TimeUnit.SECONDS);
	    scriptGeneratorModel.getScriptDefinitionLoader().addPropertyChangeListener(ScriptGeneratorProperties.SCRIPT_DEFINITION_SWITCH_PROPERTY, scriptDefinitionSwitchHelpListener);
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
	    Map<Integer, String> globals = scriptGeneratorModel.getGlobalParamErrors();
	    for (int i = 0; i < this.globalParamText.size(); i++) {
	    	if (globals.containsKey(i)) {
	    		globalParamText.get(i).setBackground(Constants.INVALID_LIGHT_COLOR);
	    		globalParamText.get(i).setBackground(Constants.INVALID_DARK_COLOR);
	    		globalParamText.get(i).setToolTipText(globals.get(i));
	    	} else {
	    		globalParamText.get(i).setBackground(Constants.CLEAR_COLOR);
	    		globalParamText.get(i).setToolTipText(null);
	    	}
	    }
    
    }
    
    private void addLineNumberColumn(ActionsViewTable viewTable) {
    	TableViewerColumn lineNumberColumn = viewTable.createColumn(Constants.ACTION_NUMBER_COLUMN_HEADER, 0, 
	        new CellLabelProvider() {
	            @Override
	            public void update(ViewerCell cell) {
	            	ScriptGeneratorAction action = (ScriptGeneratorAction) cell.getElement();
	            	if (action.isValid()) {
	            		cell.setBackground(Constants.CLEAR_COLOR);
	                } else {
	                	cell.setBackground(Constants.INVALID_LIGHT_COLOR);
	                }
	                for (int i = 0; i < viewTable.table().getItemCount(); i++) {
	                    if (action.equals(viewTable.viewer().getElementAt(i))) {
	                    	String lineNumber = String.valueOf(i + 1);
	                        cell.setText(lineNumber);
	                        Image image = ExecutingStatusDisplay.getImage(action.getDynamicScriptingStatus());
	                        cell.setImage(image);
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
    }
    
    private void addActionParameterColumns(ActionsViewTable viewTable) {
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
	                
	                @Override
	            	public void update(ViewerCell cell) {
	                	ScriptGeneratorAction row = getRow(cell);
	            		cell.setText(stringFromRow(row));
	                    cell.setImage(imageFromRow(row));
	                    if (row.isValid()) {
	                    	cell.setBackground(Constants.CLEAR_COLOR);
	                    } else {
	                    	cell.setBackground(Constants.INVALID_LIGHT_COLOR);
	                    }
	            	}
                });
            
            
            
            if (actionParameter.getIsEnum()) {
            	var editingSupport = new ScriptGeneratorEditingSupportEnum<ScriptGeneratorAction>(viewTable.viewer(), ScriptGeneratorAction.class, actionParameter);

            	viewTable.addEnumEditingSupport(editingSupport);
                 column.setEditingSupport(editingSupport);
            } else {
            	var editingSupport = new ScriptGeneratorEditingSupport(viewTable.viewer(), ScriptGeneratorAction.class, actionParameter);
            	 viewTable.addEditingSupport(editingSupport);
                 column.setEditingSupport(editingSupport);
            }
            
        }
    }
    
    private void addValidityColumn(ActionsViewTable viewTable) {
    	TableViewerColumn validityColumn = viewTable.createColumn(Constants.VALIDITY_COLUMN_HEADER, 
            1, 
            new DataboundCellLabelProvider<ScriptGeneratorAction>(viewTable.observeProperty("validity")) {
	            @Override
	            protected String stringFromRow(ScriptGeneratorAction row) {
	            if (!scriptGeneratorModel.languageSupported) {
	                return ValidityDisplay.UNCERTAIN.getText();
	            }
	            if (row.isValid()) {
	                return ValidityDisplay.VALID.getText();
	            }
	            return ValidityDisplay.INVALID.getText();
	            }
	    
	            @Override
	            public String getToolTipText(Object element) {
	            return getScriptGenActionToolTipText(element);
	            }
	            
	            @Override
	        	public void update(ViewerCell cell) {
	            	ScriptGeneratorAction row = getRow(cell);
	        		cell.setText(stringFromRow(row));
	                cell.setImage(imageFromRow(row));
	                if (row.isValid()) {
	                	cell.setBackground(Constants.VALID_COLOR);
	                } else {
	                	cell.setBackground(Constants.INVALID_DARK_COLOR);
	                }
	        	}
        	});
    	validityColumn.getColumn().setAlignment(SWT.CENTER);
    }
    
    private void addTimeEstimateColumn(ActionsViewTable viewTable) {
    	TableViewerColumn timeEstimateColumn = viewTable.createColumn(Constants.ESTIMATED_RUN_TIME_COLUMN_HEADER, 
            1, 
            new DataboundCellLabelProvider<ScriptGeneratorAction>(viewTable.observeProperty(ScriptGeneratorProperties.TIME_ESTIMATE_PROPERTY)) {
	            @Override
	            protected String stringFromRow(ScriptGeneratorAction row) {
		            if (!scriptGeneratorModel.languageSupported) {
		                return "\u003F"; // A question mark to say we cannot be certain
		            }
		    
		            Optional<Number> estimatedTime = row.getEstimatedTime();
		            if (estimatedTime.isEmpty()) {
		                return Constants.UNKNOWN_TEXT;
		            }
		            return changeSecondsToTimeFormat(estimatedTime.get().longValue());
	            }
	    
	            @Override
	            public String getToolTipText(Object element) {
	            return getScriptGenActionToolTipText(element);
	            }
	            
	            @Override
	        	public void update(ViewerCell cell) {
	            	ScriptGeneratorAction row = getRow(cell);
	        		cell.setText(stringFromRow(row));
	                cell.setImage(imageFromRow(row));
	                if (row.isValid()) {
	                	cell.setBackground(Constants.CLEAR_COLOR);
	                } else {
	                	cell.setBackground(Constants.INVALID_LIGHT_COLOR);
	                }
	        	}
        	});
    	timeEstimateColumn.getColumn().setAlignment(SWT.CENTER);
    }
    
    private void addCustomEstimateColumns(ActionsViewTable viewTable) {
    	Optional<ScriptDefinitionWrapper> scriptDefinition = scriptGeneratorModel.getScriptDefinition();
        if (scriptDefinition.isPresent()) {
        	List<String> customOutputs = scriptDefinition.get().getCustomOutputNames();
        	viewTable.setDynamicNonEditableColumnsOnRight(customOutputs.size());
        	
        	for (String param : customOutputs) {
        		TableViewerColumn column = viewTable.createColumn(
    				param,
    				1,
    				new DataboundCellLabelProvider<ScriptGeneratorAction>(viewTable.observeProperty(ScriptGeneratorProperties.CUSTOM_ESTIMATE_PROPERTY)) {
    					@Override
    	                protected String stringFromRow(ScriptGeneratorAction row) {
    						if (!scriptGeneratorModel.languageSupported) {
    			                return "\u003F"; // A question mark to say we cannot be certain
    			            }
    						
    						Optional<Map<String, String>> estimatedCustom = row.getEstimatedCustom();
    			            if (estimatedCustom.isEmpty() || estimatedCustom.get().isEmpty()) {
    			                return Constants.UNKNOWN_TEXT;
    			            }
    						
    	                    return estimatedCustom.get().getOrDefault(param, Constants.UNKNOWN_TEXT);
    	                }
    					
    					@Override
    		            public String getToolTipText(Object element) {
    						return getScriptGenActionToolTipText(element);
    		            }
    					
    					@Override
    		        	public void update(ViewerCell cell) {
    		            	ScriptGeneratorAction row = getRow(cell);
    		        		cell.setText(stringFromRow(row));
    		                cell.setImage(imageFromRow(row));
    		                if (row.isValid()) {
    		                	cell.setBackground(Constants.CLEAR_COLOR);
    		                } else {
    		                	cell.setBackground(Constants.INVALID_LIGHT_COLOR);
    		                }
    		        	}
    				});
        		column.getColumn().setAlignment(SWT.CENTER);
        	}
        }
    }

    /**
     * Adds a parameter to this actions table.
     * 
     * @param viewTable The table view to add columns to.
     */
    protected void addColumns(ActionsViewTable viewTable) {  
    	addLineNumberColumn(viewTable);
    	addActionParameterColumns(viewTable);
    	addValidityColumn(viewTable);
    	addTimeEstimateColumn(viewTable);
    	addCustomEstimateColumns(viewTable);

        ColumnViewerToolTipSupport.enableFor(viewTable.viewer());
        
        // Add selection listener to table headers, to ensure actions remain visible
		for (int i = 0; i <  viewTable.table().getColumns().length; i++) {
			viewTable.table().getColumn(i).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					scriptGeneratorModel.reloadActions();
				}
			});
		}
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
	        e -> Constants.DISPLAY.asyncExec(() -> {
	            if (!viewTable.isDisposed()) {
	            viewTable.updateTableColumns();
	            }
	        })
        );
    }

    /**
     * Display the first few validity errors or that there are none in a popup box.
     */
    public void displayValidityErrors() {
	    if (scriptGeneratorModel.languageSupported) {
	        String body = getFirstNLinesOfInvalidityErrors(Constants.MAX_ERRORS_TO_DISPLAY_IN_DIALOG);
	        if (!body.isEmpty()) {
	        String heading = "Validity errors:\n\n";
	        String message = heading + body;
	        MessageDialog.openWarning(Constants.DISPLAY.getActiveShell(), "Validity Errors", message);
	        } else {
	        MessageDialog.openInformation(Constants.DISPLAY.getActiveShell(), "Validity Errors", "No validity errors");
	        }
	    } else {
	        displayLanguageSupportError();
	    }
    }
    
    /**
     * Take the params and and the params that we need to update and instruct the model to update them.
     * 
     * @param params All the params.
     * @param toUpdate The params to update.
     */
    public void updateGlobalParams(String params, String toUpdate) {
    	int i = 0;
    	for (String paramName: this.currentGlobals) {
    		if (paramName.equals(toUpdate)) {
    			scriptGeneratorModel.updateGlobalParams(params, i);
    		}
    		i++;
    	}
    }

    /**
     * Generate a script and save it to the current script file. If fail display warnings.
     * @throws UnsupportedLanguageException 
     * @return The ID of the generated script or an empty optional if the generation didn't happen.
     */
    public Optional<Integer> generateScriptToCurrentFilepath() {
    	Optional<Integer> scriptId = generateScript();
    	scriptId.ifPresent(id -> scriptsToGenerateToCurrentFilepath.add(id));
    	return scriptId;
    }
    
    /**
     * Generate a script and display the file it was generated to. If fail display warnings.
     * 
     * @throws UnsupportedLanguageException 
     * @return The ID of the generated script or an empty optional if the generation didn't happen.
     */
    public Optional<Integer> generateScript() {
	    try {
	        return scriptGeneratorModel.refreshGeneratedScript();
	    } catch (InvalidParamsException e) {
	        MessageDialog.openWarning(Constants.DISPLAY.getActiveShell(), "Params Invalid", 
	            "Cannot generate script. Parameters are invalid.");
	    } catch (UnsupportedLanguageException e) {
	        LOG.error(e);
	        MessageDialog.openWarning(Constants.DISPLAY.getActiveShell(), "Unsupported language", 
	            "Cannot generate script. Language to generate in is unsupported.");
	    } catch (NoScriptDefinitionSelectedException e) {
	        LOG.error(e);
	        MessageDialog.openWarning(Constants.DISPLAY.getActiveShell(), "No script definition selection", 
	            "Cannot generate script. No script definition has been selected");
	    }
	    return Optional.empty();
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
	 * @return the location of the repository containing script definitions.
	 */
	public Path getScriptDefinitionsRepoPath() {
		return scriptGeneratorModel.getRepoPath();
	}

    /**
     * Reload the available script definition.
     */
    public void reloadScriptDefinitions() {
    	scriptGeneratorModel.reloadScriptDefinitions();
    }

	/**
	 * Creates prompt message for git updates available.
	 * @return The prompt message
	 */
	public String getUpdatesPromptMessage() { 
		String message = "";
		if (scriptGeneratorModel.updatesAvailable()) {
			message = "Updates Available: Updates to the script definitions are available. Please notify your local contact to update the script definitions at their earliest convenience. ";
		}
		return message;
	}
    /**
     * Creates a dirty local repo prompt message string. 
     * @return The prompt message
     */
	public String getDirtyPromptMessage() {
		String message = "";
		if (scriptGeneratorModel.isDirty()) {
			message = "Local Repo is Dirty: There are uncommitted changes to the script definitions. These will be lost if you update. ";
		}
		return message;
	}
	
	/**
	 * Creates a git connection error prompt message.
	 * @return the prompt message
	 */
	public String getGitErrorPromptMessage() {
		String message = "";
		if (!scriptGeneratorModel.remoteAvailable()) {
			// Warn user git could not be found
			message = "Git error, remote repo unavailable: Could not update script definitions, because the remote git repository could not be reached. "
					+ "You can still continue to use the existing script definitions, but they may be out of date. ";
		}
		return message;
	}

    /**
     * Reload the actions table actions.
     */
    public void reloadActions() {
    	scriptGeneratorModel.reloadActions();
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
	        MessageDialog.openWarning(Constants.DISPLAY.getActiveShell(), "No script definition selection", 
	            "Cannot generate script. No script definition has been selected");
	        return;
	        } catch (ScriptDefinitionNotMatched | UnsupportedOperationException e) {
	        LOG.error(e);
	        MessageDialog.openError(Constants.DISPLAY.getActiveShell(), "Error", e.getMessage());
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
	        MessageDialog dialog = new MessageDialog(Constants.DISPLAY.getActiveShell(), "Replace or Append", null,
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
		        updateParametersFilePath(selectedFile.get());
	        }
	
	    }
    };

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
	 * Checks if the clipboard has contents.
	 * @return True if the clipboard has content, false otherwise.
	 */
	public boolean checkClipboard() {
		String copiedActions = (String) clipboard.getContents(TextTransfer.getInstance());
		if (copiedActions == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Clears the clipboard has contents.
	 */
	public void clearClipboard() {
		clipboard.clearContents();
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

    /**
     * Set that the given script ID is for nicos.
     * 
     * @param scriptId The ID of the script to set
     */
	public void setNicosScript(Integer scriptId) {
		nicosScriptIds.add(scriptId);
	}
		
}

