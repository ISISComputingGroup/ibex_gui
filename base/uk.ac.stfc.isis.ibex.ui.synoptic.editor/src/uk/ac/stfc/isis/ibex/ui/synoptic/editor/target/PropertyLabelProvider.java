package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;

public class PropertyLabelProvider implements ILabelProvider {
	
	@Override
	public String getText(Object element) {
		if (element instanceof Property) {
			Property property = (Property) element;
			String display = property.key() + " | " + property.value();
			return display;
		} else {
			return "#UNKNOWN OBJECT TYPE";
		}
	}

	
	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {	
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {	
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}
}
