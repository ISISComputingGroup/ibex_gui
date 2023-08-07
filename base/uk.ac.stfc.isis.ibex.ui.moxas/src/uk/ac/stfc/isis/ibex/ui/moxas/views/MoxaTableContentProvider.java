package uk.ac.stfc.isis.ibex.ui.moxas.views;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * Content provider for the table displaying Moxa port mappings.
 */
public class MoxaTableContentProvider implements ITreeContentProvider {
	@Override
	public Object[] getElements(Object inputElement) {
		return HashMap.class.cast(inputElement).values().toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof MoxaList) {
			return ArrayList.class.cast(parentElement).toArray();
		} else {
			Object[] empty = {};
			return empty;
		}

	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return this.getChildren(element).length > 0;
	}
}
