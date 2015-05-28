/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.component;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

/**
 * Presents the Instrument and its descendant components in a form that can be
 * used by the ComponentTreeView
 * 
 */
public class ComponentContentProvider implements ITreeContentProvider {
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ComponentDescription) {
			return ((ComponentDescription) parentElement).components()
					.toArray();
		} else if (parentElement instanceof InstrumentDescription) {
			return ((InstrumentDescription) parentElement).components()
					.toArray();
		} else {
			return null;
		}
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof ComponentDescription) {
			return ((ComponentDescription) element).getParent();
		} else {
			return null;
		}
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ComponentDescription) {
			ComponentDescription container = (ComponentDescription) element;
			return container.components() != null
					&& !container.components().isEmpty();
		} else if (element instanceof InstrumentDescription) {
			InstrumentDescription container = (InstrumentDescription) element;
			return container.components() != null
					&& !container.components().isEmpty();
		} else {
			return false;
		}
	}
}
