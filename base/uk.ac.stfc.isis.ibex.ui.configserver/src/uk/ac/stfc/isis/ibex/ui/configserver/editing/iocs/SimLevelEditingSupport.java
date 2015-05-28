package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.widgets.EnumEditingSupport;

public class SimLevelEditingSupport extends EnumEditingSupport<EditableIoc, SimLevel> {

	public SimLevelEditingSupport(ColumnViewer viewer) {
		super(viewer, EditableIoc.class, SimLevel.class);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {		
		CellEditor editor = super.getCellEditor(element);
		editor.setStyle(SWT.READ_ONLY);
		
		return editor;
	}
	
	@Override
	protected boolean canEdit(Object element) {
		EditableIoc row = EditableIoc.class.cast(element);
		return row.isEditable();
	}
	
	@Override
	protected SimLevel getEnumValueForRow(EditableIoc row) {
		return row.getSimLevel();
	}

	@Override
	protected void setEnumForRow(EditableIoc row, SimLevel value) {
		row.setSimLevel(value);
	}
}
