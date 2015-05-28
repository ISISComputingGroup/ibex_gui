package uk.ac.stfc.isis.ibex.ui.configserver.editing.groups;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableGroup;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DataboundCombo;

public class GroupNameComboViewer extends DataboundCombo<EditableGroup> {
	
	public GroupNameComboViewer(Composite parent, int style) {
		super(parent, style, "name");
	}
}
