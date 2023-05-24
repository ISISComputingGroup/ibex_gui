package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import org.eclipse.jface.viewers.ColumnViewer;
import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionDynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

public class ScriptGeneratorEditingSupport extends StringEditingSupport<ScriptGeneratorAction> {
	private JavaActionParameter actionParameter;
	public ScriptGeneratorEditingSupport(ColumnViewer viewer, Class<ScriptGeneratorAction> rowType, JavaActionParameter actionParameter) {
		super(viewer, rowType);
		this.actionParameter = actionParameter;
	}
	
	@Override
	protected boolean canEdit(Object element) {
		if(ExecutingStatusDisplay.equalsAction(ActionDynamicScriptingStatus.EXECUTING, (ScriptGeneratorAction) element)||
				ExecutingStatusDisplay.equalsAction(ActionDynamicScriptingStatus.PAUSED_DURING_EXECUTION, (ScriptGeneratorAction) element)) {
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
