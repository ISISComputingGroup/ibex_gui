package uk.ac.stfc.isis.ibex.ui.configserver.editing.groups;

import org.eclipse.jface.viewers.LabelProvider;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;

public class GroupNameLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		return ((Block) element).getName();
	}
}
