package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import java.awt.Composite;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

public interface IIocDependentPanel extends Composite {
	public void setIoc(EditableIoc ioc);
}
