package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import org.eclipse.jface.viewers.ColumnViewer;
import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.ui.widgets.FakeEnumEditingSupport;

/**
 * Editing support for the scriptgenerator.
 * @param <TRow> the type of row which is edited 
 */
public class ScriptGeneratorEditingSupportEnum<TRow> extends FakeEnumEditingSupport<ScriptGeneratorAction> {
	private JavaActionParameter actionParameter;
	
	/**
	 * Create this editing support.
	 * @param viewer the column viewer
	 * @param rowType the row type
	 * @param actionParameter the associated action parameter for the column
	 */
	public ScriptGeneratorEditingSupportEnum(ColumnViewer viewer, Class<ScriptGeneratorAction> rowType, JavaActionParameter actionParameter) {
		super(viewer, rowType, actionParameter.getEnumValues());
		this.actionParameter = actionParameter;
	}

	@Override
	protected String getEnumValueForRow(ScriptGeneratorAction row) {
		return row.getActionParameterValue(actionParameter);
	}

	@Override
	protected void setEnumForRow(ScriptGeneratorAction row, String value) {
		row.setActionParameterValue(actionParameter, value);
		
	}


	


}
