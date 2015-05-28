package uk.ac.stfc.isis.ibex.ui.synoptic.editor.model;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;

public interface IPropertySelectionListener {
	void selectionChanged(Property oldProperty, Property newProperty);
}
