package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import org.eclipse.jface.viewers.ColumnViewer;
import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionDynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

/**
 * Editing support for the scriptgenerator.
 *
 */
public class ScriptGeneratorEditingSupport extends StringEditingSupport<ScriptGeneratorAction> {
	private JavaActionParameter actionParameter;
	
	/**
	 * Create this editing support.
	 * @param viewer the column viewer
	 * @param rowType the row type
	 * @param actionParameter the associated action parameter for the column
	 */
	public ScriptGeneratorEditingSupport(ColumnViewer viewer, Class<ScriptGeneratorAction> rowType, JavaActionParameter actionParameter) {
		super(viewer, rowType);
		this.actionParameter = actionParameter;
	}
		
	/**
	 * Returns whether the current cell can be edited.
	 * @param element the action of the cell to edit
	 */
	@Override
	protected boolean canEdit(Object element) {
		ScriptGeneratorAction action = (ScriptGeneratorAction) element;
		if (ExecutingStatusDisplay.equalsAction(ActionDynamicScriptingStatus.EXECUTING, action)
				|| ExecutingStatusDisplay.equalsAction(ActionDynamicScriptingStatus.PAUSED_DURING_EXECUTION, action)) {
			return false;
		}
		return canEdit;
	}

	@Override
    protected String valueFromRow(ScriptGeneratorAction row) {
        return row.getActionParameterValue(actionParameter);
    }

    @Override
    protected void setValueForRow(ScriptGeneratorAction row, String value) {
        row.setActionParameterValue(actionParameter, value);
    }

}
