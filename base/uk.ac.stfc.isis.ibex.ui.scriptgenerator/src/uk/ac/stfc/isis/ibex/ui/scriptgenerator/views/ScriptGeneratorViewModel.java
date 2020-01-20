package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.viewers.typed.ViewerProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.Activator;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.widgets.FileMessageDialog;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * The ViewModel for the ScriptGeneratorView.
 * 
 * @author James King
 *
 */
public class ScriptGeneratorViewModel {
	
	private static final Display DISPLAY = Display.getDefault();
	

	/**
	 * A dark red for use in the validity column when a row is invalid.
	 */
	private static final Color invalidDarkColor = DISPLAY.getSystemColor(SWT.COLOR_RED);
	
	/**
	 * A light read for use in the other script generator table columns when a row is invalid.
	 */
	private static final Color invalidLightColor = new Color(DISPLAY, 255, 204, 203);
	
	/**
	 * A green for use in the validity column when a row is valid.
	 */
	private static final Color validColor = DISPLAY.getSystemColor(SWT.COLOR_GREEN);
	
	/**
	 * A clear colour for use in other script generator table columns when a row is valid.
	 */
	private static final Color clearColor = DISPLAY.getSystemColor(SWT.COLOR_WHITE);
	
	/**
	 * The colour of the "get validity errors" button when it is grayed out.
	 */
	private static final Color greyColor = DISPLAY.getSystemColor(SWT.COLOR_GRAY);
	
	/**
	 * A light orange to use when validity checks may be incorrect e.g. for when using an unsupported language.
	 */
	private static final Color lightValidityCheckErrorColor = new Color(DISPLAY, 255, 201, 102);
	
	/**
	 * A dark orange to use when validity checks may be incorrect e.g. for when using an unsupported language.
	 */
	private static final Color darkValidityCheckErrorColor = new Color(DISPLAY, 255, 165, 0);
	
	/**
	 * The maximum number of lines to display in the "Get Validity Errors" dialog box before suppressing others.
	 */
	private int MAX_ERRORS_TO_DISPLAY_IN_DIALOG = 10;
	
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
	private static final String GENERATED_SCRIPT_FILEPATH_PROPERTY = "generated script filepath";
	
	/**
	 * A property to listen to for when actions change in the model.
	 */
	private static final String ACTIONS_PROPERTY = "actions";
	
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorViewModel.class);
	
	/**
	 * The reference to the singleton model that the ViewModel is to use.
	 */
	private ScriptGeneratorSingleton scriptGeneratorModel;
	
	/**
	 * A constructor that sets up the script generator model and 
	 *   begins listening to property changes in the model.
	 */
	public ScriptGeneratorViewModel() {
		// Set up the model
		scriptGeneratorModel = Activator.getModel();
		scriptGeneratorModel.createConfigLoader();
		scriptGeneratorModel.setUp();
		// Listen to whether the language support is changed
		// notify the user if the language is not supported
		scriptGeneratorModel.addPropertyChangeListener(LANGUAGE_SUPPORT_PROPERTY, evt -> {
			if(Objects.equals(evt.getOldValue(), true) && Objects.equals(evt.getNewValue(), false)) {
				displayLanguageSupportError();
			}
		});
		// Listen for model threading errors and display to the user if there is one
		// Model is responsible for logging it
		scriptGeneratorModel.addPropertyChangeListener(THREAD_ERROR_PROPERTY, evt -> {
			displayThreadingError();
		});
	}
	
	/**
	 * Display a message dialog box that the language that is being used is unsupported.
	 */
	private void displayLanguageSupportError() {
		MessageDialog.openError(DISPLAY.getActiveShell(), 
				"Language support issue",
				"You are attempting to use an unsupported language, " + 
				"parameter validity checking and script generation are disabled at this time");
	}
	
	/**
	 * Display a message dialog box that there was a threading issue when generating or checking parameter validity.
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
	 * Get a list of available configs.
	 * 
	 * @return A list of available configs.
	 */
	protected List<Config> getAvailableConfigs() {
		return scriptGeneratorModel.getAvailableConfigs();
	}
	
	/**
	 * Get the currently loaded configuration.
	 * 
	 * @return The currently loaded configuration.
	 */
	protected Config getConfig() {
		return scriptGeneratorModel.getConfig();
	}
	
	/**
	 * Create and get the label provider for the config selector.
	 * 
	 * @return The label provider.
	 */
	protected LabelProvider getConfigSelectorLabelProvider() {
		return new LabelProvider() {
		    /**
		     * Use getName method on python Config class to get labels.
		     */
			@Override
		    public String getText(Object element) {
		        if (element instanceof Config) {
		        	Config actionWrapper = (Config) element;
		            return actionWrapper.getName();
		        }
		        return super.getText(element);
		    }
		};
	}

	/**
	 * Listen to changes on the actions and action validity property of the scriptGenerator table and
	 *  update the view table.
	 * 
	 * @param table The view table to update.
	 * @param btnGetValidityErrors The validity check button to style change.
	 * @param btnGenerateScript The generate script button to style change.
	 */
	protected void bindValidityChecks(ActionsViewTable viewTable, Button btnGetValidityErrors, Button btnGenerateScript) {
		this.scriptGeneratorModel.getScriptGeneratorTable().addPropertyChangeListener(ACTIONS_PROPERTY, e -> {
			actionChangeHandler(viewTable, btnGetValidityErrors, btnGenerateScript);
		});
		this.scriptGeneratorModel.addPropertyChangeListener(VALIDITY_ERROR_MESSAGE_PROPERTY, e -> {
			actionChangeHandler(viewTable, btnGetValidityErrors, btnGenerateScript);
		});
		// Listen for generated script refreshes
		scriptGeneratorModel.addPropertyChangeListener(GENERATED_SCRIPT_FILEPATH_PROPERTY, evt -> {
			@SuppressWarnings("unchecked")
			Optional<String> optionalScriptFilePath = Optional.class.cast(evt.getNewValue());
			DISPLAY.asyncExec(() -> {
				optionalScriptFilePath.ifPresentOrElse(
						scriptFilePath -> {
							openFileDialog(scriptFilePath);
						},
						() -> {
							MessageDialog.openWarning(DISPLAY.getActiveShell(), "Error", "Failed to generate the script");
						}
				);
			});
		});
	}
	
	private void openFileDialog(String filePath) {
		FileMessageDialog.openInformation(Display.getDefault().getActiveShell(),
				"Script Generated", "Script generated at: ", filePath);
	}
	
	/**
	 * Handle a change in the actions or their validity.
	 * Set the UI table's actions from the model and update validity checking.
	 * 
	 * @param viewTable The view table to update.
	 * @param btnGetValidityErrors The button to manipulate.
	 */
	private void actionChangeHandler(ActionsViewTable viewTable, Button btnGetValidityErrors, Button btnGenerateScript) {
		DISPLAY.asyncExec(() -> {
            viewTable.setRows(scriptGeneratorModel.getActions());
            updateValidityChecks(viewTable);
            setButtonValidityStyle(btnGetValidityErrors);
            setButtonGenerateStyle(btnGenerateScript);
		});
	}
	
	private void setButtonGenerateStyle(Button btnGenerateScript) {
		if(scriptGeneratorModel.languageSupported) {
			// Grey the button out if parameters are valid
			btnGenerateScript.setEnabled(scriptGeneratorModel.areParamsValid());
		} else {
			// Grey the button out when language is not supported
			btnGenerateScript.setEnabled(false);
		}
	}
	
	private void setButtonValidityStyle(Button btnGetValidityErrors) {
		if(scriptGeneratorModel.languageSupported) {
			// Grey the button out if parameters are valid, if not make it red
			btnGetValidityErrors.setEnabled(!scriptGeneratorModel.areParamsValid());
			if(scriptGeneratorModel.areParamsValid()) {
				btnGetValidityErrors.setBackground(greyColor);
			} else {
				btnGetValidityErrors.setBackground(invalidLightColor);
			}
		} else {
			// Alert the user with an orange colour that the validity checks may be incorrect
			btnGetValidityErrors.setBackground(lightValidityCheckErrorColor);
		}
	}

	/**
	 * Bind the config loader to the context.
	 * 
	 * @param bindingContext The context.
	 * @param configSelector The config selector ui element to bind.
	 */
	protected void bindConfigLoader(DataBindingContext bindingContext, ComboViewer configSelector) {
		bindingContext.bindValue(ViewerProperties.singleSelection().observe(configSelector), 
				BeanProperties.value("config").observe(scriptGeneratorModel.getConfigLoader()));
	}

	/**
	 * Get the first i lines of invalidity errors for the current configuration and parameters.
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
		int validityColumnIndex = viewTable.table().getColumnCount()-1;
		for (int i = 0; i < actions.size(); i++) {
			if(i < items.length) {
				if (!scriptGeneratorModel.languageSupported) {
					items[i].setBackground(lightValidityCheckErrorColor);
					items[i].setBackground(validityColumnIndex, darkValidityCheckErrorColor);
				} else if (actions.get(i).isValid()) {
					items[i].setBackground(clearColor);
					items[i].setBackground(validityColumnIndex, validColor);
				} else {
					items[i].setBackground(invalidLightColor);
					items[i].setBackground(validityColumnIndex, invalidDarkColor);
				}
			} else {
				LOG.warn("ScriptGeneratorViewModel - ActionsTable and UI Table mismatch");
			}
		}
	}
	
	/**
     * Adds a parameter to this actions table.
     * 
     * @param viewTabl The table view to add columns to.
     */
    protected void addColumns(ActionsViewTable viewTable) {    	
    	// Add action parameter columns
        for (ActionParameter actionParameter: scriptGeneratorModel.getActionParameters()) {
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
        		if(!scriptGeneratorModel.languageSupported) {
        			return "\u003F";
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
		return "The reason this row is invalid is:\n" +
			action.getInvalidityReason().get() + "\n"; // Show reason on next line as a tooltip
    }
    
    /**
     * Attach a property listener on aciton parameters to update the table columns when changed.
     * 
     * @param viewTable The table to update the columns of.
     */
    protected void addActionParamPropertyListener(ActionsViewTable viewTable) {
    	scriptGeneratorModel.getScriptGeneratorTable().addPropertyChangeListener("actionParameters", 
    			e -> DISPLAY.asyncExec(() -> viewTable.updateTableColumns())
    	);
    }
    
    /**
	 * Display the first few validity errors or that there are none in a popup box.
	 */
	public void displayValidityErrors() {
		if(scriptGeneratorModel.languageSupported) {
			String body = getFirstNLinesOfInvalidityErrors(MAX_ERRORS_TO_DISPLAY_IN_DIALOG);
			if(!body.isEmpty()) {
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
     */
    public void generate() {
    	try {
    		LOG.info("Refreshing script");
    		scriptGeneratorModel.refreshGeneratedScript();
		} catch (InvalidParamsException e) {
			MessageDialog.openWarning(DISPLAY.getActiveShell(), "Params Invalid", 
					"Cannot generate script. Parameters are invalid.");
		} catch (UnsupportedLanguageException e) {
			LOG.error(e);
			MessageDialog.openWarning(DISPLAY.getActiveShell(), "Unsupported language", 
					"Cannot generate script. Language to generate in is unsupported.");
		}
    }
	
}