package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class DataboundCombo<T> extends ComboViewer {
	
	private final IViewerObservableValue selected;
	
	public DataboundCombo(Composite parent, int style, String observedProperty) {
		super(parent, style | SWT.DROP_DOWN);
		
		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		setContentProvider(contentProvider);
		setLabelProvider(
				new ObservableMapLabelProvider(BeansObservables.observeMaps(contentProvider.getKnownElements(), new String[] { observedProperty } )));	
		
		selected = ViewerProperties.singleSelection().observe(this);
	}

	public IViewerObservableValue selected() {
		return selected;
	}
}
