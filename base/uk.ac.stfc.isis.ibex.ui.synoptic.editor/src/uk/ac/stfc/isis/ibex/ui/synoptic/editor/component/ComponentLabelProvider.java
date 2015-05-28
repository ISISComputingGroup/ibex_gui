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

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;

public class ComponentLabelProvider implements ILabelProvider {
	@Override
	public Image getImage(Object element) {
		if (element instanceof ComponentDescription) {
			return ComponentIcons
					.thumbnailForComponent((ComponentDescription) element);
		} else {
			return null;
		}
	}

	@Override
	public String getText(Object element) {
		if (element instanceof ComponentDescription) {
			return ((ComponentDescription) element).name();
		} else {
			return null;
		}
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}
}
