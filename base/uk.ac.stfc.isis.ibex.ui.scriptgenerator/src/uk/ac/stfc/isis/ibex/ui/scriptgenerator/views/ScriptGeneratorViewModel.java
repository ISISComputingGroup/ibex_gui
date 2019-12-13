package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.Activator;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;


public class ScriptGeneratorViewModel {
	
	private static final Color invalidDarkColor = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	private static final Color invalidLightColor = new Color(Display.getCurrent(), 255, 204, 203);
	private static final Color validColor = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
	private static final Color clearColor = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	
	private ScriptGeneratorSingleton scriptGeneratorModel;
	
	public ScriptGeneratorViewModel() {
		scriptGeneratorModel = Activator.getModel();
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
		    // Use getName method on python Config class to get labels.
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
	 * Listen to changes on the actions property of the scriptGenerator table and update the view table.
	 * 
	 * @param table The view table to update.
	 * @param display The display to change.
	 */
	protected void bindValidityChecks(ActionsViewTable viewTable) {
		this.scriptGeneratorModel.getScriptGeneratorTable().addPropertyChangeListener("actions", e -> 
			Display.getCurrent().asyncExec(() -> {
                viewTable.setRows(scriptGeneratorModel.getActions());
                updateValidityChecks(viewTable);
			}
		));
	}

	/**
	 * Bind the config loader to the context.
	 * 
	 * @param bindingContext The context.
	 * @param configSelector The config selector ui element to bind.
	 */
	protected void bindConfigLoader(DataBindingContext bindingContext, ComboViewer configSelector) {
		bindingContext.bindValue(ViewersObservables.observeSingleSelection(configSelector), 
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
			if (actions.get(i).isValid()) {
				items[i].setBackground(clearColor);
				items[i].setBackground(validityColumnIndex, validColor);
			} else {
				items[i].setBackground(invalidLightColor);
				items[i].setBackground(validityColumnIndex, invalidDarkColor);
			}
		}
	}

	/**
	 * Carry out actions in model on the view table changing. 
	 */
	protected void onTableChange() {
		scriptGeneratorModel.onTableChange();
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
	                onTableChange();
	                updateValidityChecks(viewTable);
	            }
	        });	
        }
        // Add validity notifier column
        TableViewerColumn validityColumn = viewTable.createColumn("Validity", 
        		1, 
        		new DataboundCellLabelProvider<ScriptGeneratorAction>(viewTable.observeProperty("validity")) {
        	@Override
			protected String stringFromRow(ScriptGeneratorAction row) {
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
     * 
     * @param action The action to get the tooltip text for.
     * @return The text for the action tooltip.
     */
    private String getScriptGenActionToolTipText(ScriptGeneratorAction action) {
    	if (action.isValid()) {
			return null; 
		}
		return "The reason this row is invalid is:\n" +
			action.getInvalidityReason() + "\n"; // Show reason on next line as a tooltip
    }
    
    /**
     * Attach a property listener on aciton parameters to update the table columns when changed.
     * 
     * @param viewTable The table to update the columns of.
     */
    protected void addActionParamPropertyListener(ActionsViewTable viewTable) {
    	scriptGeneratorModel.getScriptGeneratorTable().addPropertyChangeListener("actionParameters", 
    			e -> Display.getCurrent().asyncExec(() -> viewTable.updateTableColumns())
    	);
    }
	
}
