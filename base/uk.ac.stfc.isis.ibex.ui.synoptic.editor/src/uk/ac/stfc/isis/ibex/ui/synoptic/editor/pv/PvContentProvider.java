package uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;

public class PvContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {		
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		List<PV> pvList = (List<PV>) inputElement;		
		return pvList.toArray();
	}

}
