package uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;

public class PvLabelProvider implements ILabelProvider {
	
	@Override
	public String getText(Object element) {
		if(element instanceof PV) {
			PV pv = (PV) element;
			String display = pv.displayName();
			display += " (" + pv.recordType().io().name() + ")";
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
