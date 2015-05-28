package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;

public class PropertyContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {		
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		List<Property> propertyList = (List<Property>) inputElement;		
		return propertyList.toArray();
	}

}
