package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.Map;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;

/**
 * Implement this interface on the view to update the UI accordingly.
 * 
 */
public interface ScriptGeneratorViewModelDelegate {

	/**
	 * Called when save and save as is enabled or disabled.
	 * 
	 * @param viewModel
	 * @param enabled
	 */
	public void onSaveEnabledChange(ScriptGeneratorViewModel viewModel, boolean enabled);

	/**
	 * Called when actions validity changes in the table.
	 * 
	 * @param viewModel
	 * @param allActionsValid
	 * @param errors
	 */
	public void onActionsValidityChange(ScriptGeneratorViewModel viewModel, boolean allActionsValid,
			Map<Integer, String> errors);

	/**
	 * Called when selected script definition changes.
	 * 
	 * @param viewModel
	 * @param scriptDefinition
	 */
	public void onScriptDefinitionChange(ScriptGeneratorViewModel viewModel,
			Optional<ScriptDefinitionWrapper> scriptDefinition);

	/**
	 * Called when an error message needs to be displayed to the user.
	 * 
	 * @param viewModel the dispatching view model
	 * @param title     usually the dialog's title
	 * @param message   the main body
	 */
	public void onErrorMessage(ScriptGeneratorViewModel viewModel, String title, String message);

	/**
	 * Called when a warning message needs to be displayed to the user.
	 * 
	 * @param viewModel the dispatching view model
	 * @param title     usually the dialog's title
	 * @param message   the main body
	 */
	public void onWarningMessage(ScriptGeneratorViewModel viewModel, String title, String message);

	/**
	 * Called when an information message needs to be displayed to the user.
	 * 
	 * @param viewModel the dispatching view model
	 * @param title     usually the dialog's title
	 * @param message   the main body
	 */
	public void onInfoMessage(ScriptGeneratorViewModel viewModel, String title, String message);

	/**
	 * Called when a user confirmation message needs to be displayed.
	 * 
	 * @param viewModel the dispatching view model
	 * @param title     usually the dialog's title
	 * @param message   the main body
	 * @return whether the user confirmed the message or denied it
	 */
	public boolean onUserConfirmationRequest(ScriptGeneratorViewModel viewModel, String title, String message);

	/**
	 * Called when the user needs to select from a range of options.
	 * 
	 * @param viewModel    the dispatching view model
	 * @param title        usually the dialog's title
	 * @param message      the main body
	 * @param options      the array of option titles
	 * @param defaultIndex the index of the default option
	 * @return the selected option's index
	 */
	public int onUserSelectOptionRequest(ScriptGeneratorViewModel viewModel, String title, String message, String[] options,
			int defaultIndex);
}
