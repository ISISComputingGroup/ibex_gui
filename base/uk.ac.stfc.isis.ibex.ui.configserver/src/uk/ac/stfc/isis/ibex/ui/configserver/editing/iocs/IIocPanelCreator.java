package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import org.eclipse.swt.widgets.Composite;

public interface IIocPanelCreator {
	public IIocDependentPanel factory(Composite parent);
}
