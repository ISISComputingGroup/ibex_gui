package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.net.URL;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.apache.logging.log4j.Logger;

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
	
	/**
	 * The reference to the singleton model that the ViewModel is to use.
	 */
	private ScriptGeneratorSingleton scriptGeneratorModel;
	
	/**
	 * The current viewTable in the actions.
	 */
	private ActionsViewTable viewTable;


	/**
	 * The current get validity errors button in the view.
	 */
	private Button btnGetValidityErrors;

	/**
	 * The current generate script button in the view.
	 */
	private Button btnGenerateScript;
	
	/**
	 * The current Save Parameters button in the view
	 */
	private Button btnSaveParam;
	
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
							SaveScriptGeneratorFileMessageDialog.openInformation(Display.getDefault().getActiveShell(),
									"Script Generated", "",
									scriptGeneratorModel.getScriptFilepathPrefix(),
									scriptFilename, generatedScript, scriptGeneratorModel);
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
	}
	
	/**
	 * Removes action at position index from ActionsTable.
	 * 
	 * @param index
	 * 			the index to delete.
	 */
	protected void deleteAction(int index) {
		scriptGeneratorModel.deleteAction(index);
	}
	
	/**
	 * Duplicates action at position index in ActionsTable.
	 * 
	 * @param index
	 * 			the index to duplicate.
	 */
	protected void duplicateAction(int index) {
		scriptGeneratorModel.duplicateAction(index);
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
	 * Moves action one row up in table.
	 * 
	 * @param index
	 * 			the index to move.
	 */
	protected void moveActionUp(int index) {
		scriptGeneratorModel.moveActionUp(index);
	}

	/**
	 * Moves action one row down in table.
	 * 
	 * @param index
	 * 			the index to move.
	 */
	protected void moveActionDown(int index) {
		scriptGeneratorModel.moveActionDown(index);
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
	 * @return A map of script definition load errors with keys as the name of the scriptDefinition
	 *  and the value as the reason it could not be loaded
	 */
	protected Map<String, String> getScriptDefinitionLoadErrors() {
		return scriptGeneratorModel.getScriptDefinitionLoadErrors();
	}
	
	protected Optional<String> getGitLoadErrors() {
		List<String> gitLoadErrors = scriptGeneratorModel.getGitLoadErrors();
		String loadErrorMessage = null;

		if (gitLoadErrors.size() > 0) {
			loadErrorMessage = "The following error(s) occurred when trying to update script definitions: \n";
			for (String error: scriptGeneratorModel.getGitLoadErrors()) {
				loadErrorMessage += error +"\n";
			}
		};

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
			actionChangeHandler(viewTable, btnGetValidityErrors, btnGenerateScript, btnSaveParam);
		};

	/**
	 * Listen to changes on the actions and action validity property of the scriptGenerator table and
	 *  update the view table.
	 * 
	 * @param viewTable The view table to update.
	 * @param btnGetValidityErrors The validity check button to style change.
	 * @param btnGenerateScript The generate script button to style change.
	 */
	protected void bindValidityChecks(ActionsViewTable viewTable, Button btnGetValidityErrors, Button btnGenerateScript, Button btnSaveParam) {
		this.viewTable = viewTable;
		this.btnGetValidityErrors = btnGetValidityErrors;
		this.btnGenerateScript = btnGenerateScript;
		this.btnSaveParam = btnSaveParam;
		// Remove listeners so as not to bind them twice
		this.scriptGeneratorModel.getScriptGeneratorTable().removePropertyChangeListener(ACTIONS_PROPERTY, actionChangeListener);
		this.scriptGeneratorModel.getScriptGeneratorTable().addPropertyChangeListener(ACTIONS_PROPERTY, actionChangeListener);
		this.scriptGeneratorModel.removePropertyChangeListener(VALIDITY_ERROR_MESSAGE_PROPERTY, actionChangeListener);
		this.scriptGeneratorModel.addPropertyChangeListener(VALIDITY_ERROR_MESSAGE_PROPERTY, actionChangeListener);
	}
	
	/**
	 * Handle a change in the actions or their validity.
	 * Set the UI table's actions from the model and update validity checking.
	 * 
	 * @param viewTable The view table to update.
	 * @param btnGetValidityErrors The button to manipulate.
	 * @param btnGenerateScript Generate Script button's visibility to manipulate
	 * @param btnSaveParam Save Parameter button's visibility to manipulate
	 */
	private void actionChangeHandler(ActionsViewTable viewTable, Button btnGetValidityErrors, Button btnGenerateScript, Button btnSaveParam) {
		DISPLAY.asyncExec(() -> {
			if (!viewTable.isDisposed()) {
	            viewTable.setRows(scriptGeneratorModel.getActions());
	            updateValidityChecks(viewTable);
			}
			if (!btnGetValidityErrors.isDisposed()) {
				setButtonValidityStyle(btnGetValidityErrors);
			}
			if (!btnGenerateScript.isDisposed()) {
				setButtonGenerateStyle(btnGenerateScript);
				setButtonGenerateStyle(btnSaveParam);
			}
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
	
	private void setButtonValidityStyle(Button btnGetValidityErrors) {
		if (scriptGeneratorModel.languageSupported) {
			// Grey the button out if parameters are valid, if not make it red
			btnGetValidityErrors.setEnabled(!scriptGeneratorModel.areParamsValid());
			if (scriptGeneratorModel.areParamsValid()) {
				btnGetValidityErrors.setBackground(GREY_COLOR);
			} else {
				btnGetValidityErrors.setBackground(INVALID_LIGHT_COLOR);
			}
		} else {
			// Alert the user with an orange colour that the validity checks may be incorrect
			btnGetValidityErrors.setBackground(LIGHT_VALIDITY_CHECK_ERROR_COLOR);
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
		int validityColumnIndex = viewTable.table().getColumnCount() - 1;
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
			        		return getScriptGenActionToolTipText((ScriptGeneratorAction) element);
			        	}
					});
			
	        column.setEditingSupport(new StringEditingSupport<ScriptGeneratorAction>(viewTable.viewer(), ScriptGeneratorAction.class) {
	
	            @Override
	            protected String valueFromRow(ScriptGeneratorAction row) {
	                return row.getActionParameterValue(actionParameter);
	            }
	
	            @Override
	            protected void setValueForRow(ScriptGeneratorAction row, String value) {
	                row.setActionParameterValue(actionParameter, value);
	            }
	        });	
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
        		return getScriptGenActionToolTipText((ScriptGeneratorAction) element);
        	}
        	
        });
        validityColumn.getColumn().setAlignment(SWT.CENTER);
        
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
    private String getScriptGenActionToolTipText(ScriptGeneratorAction action) {
    	if (action.isValid()) {
			return null; // Do not show a tooltip
		}
		return "The reason this row is invalid is:\n"
			+ action.getInvalidityReason().get() + "\n"; // Show reason on next line as a tooltip
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
	 * Display the first few validity errors or that there are none in a popup box.
	 */
	public void displayValidityErrors() {
		if (scriptGeneratorModel.languageSupported) {
			String body = getFirstNLinesOfInvalidityErrors(MAX_ERRORS_TO_DISPLAY_IN_DIALOG);
			if (!body.isEmpty()) {
				String heading = "Validity errors:\n\n";
				String message = heading + body;
				MessageDialog.openWarning(DISPLAY.getActiveShell(), "Validity Errors", message);
			} else {
				MessageDialog.openInformation(DISPLAY.getActiveShell(), "Validity Errors", "No validity errors");
			}
		} else {
			displayLanguageSupportError();
		}
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

	/**
	 * Reload the actions table actions.
	 */
	public void reloadActions() {
		scriptGeneratorModel.reloadActions();
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
		dialog.setFilterExtensions(new String[] {"*.json"});
		dialog.setOverwrite(true);
		if (action == SWT.SAVE) {
			dialog.setText("Save as");

		} else {
			dialog.setText("Load");
		}
		return Optional.ofNullable(dialog.open());
	}
	
	/**
	 * Load parameter values.
	 */
	public void loadParameterValues() {
		Optional<String> selectedFile = openFileDialog(SWT.OPEN);
		// filename will be null if user has clicked cancel button
		if (selectedFile.isPresent()) {
			try {
				scriptGeneratorModel.loadParameterValues(selectedFile.get());
			} catch (NoScriptDefinitionSelectedException e) {
				LOG.error(e);
				MessageDialog.openWarning(DISPLAY.getActiveShell(), "No script definition selection", 
						"Cannot generate script. No script definition has been selected");
			} catch (ScriptDefinitionNotMatched | UnsupportedOperationException e) {
				LOG.error(e);
				MessageDialog.openError(DISPLAY.getActiveShell(), "Error", e.getMessage());
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

	public String getPromptMessage() {
        String message = "Updates to the script definitions are available. Updating your definitions may erase current scripts.";
		//if (scriptGeneratorModel.isDirty()) {
		//	message += "\n WARNING: There are uncommitted changes to the script defintions. These will be lost if you update.";
		//}
		return message;
	}

	public boolean remoteAvailable() {
		return scriptGeneratorModel.remoteAvailable();
	}
	
	public boolean updatesAvailable() {
		return scriptGeneratorModel.updatesAvailable();
	}


	public void mergeOrigin() {
		scriptGeneratorModel.mergeOrigin();
		
	}
}
