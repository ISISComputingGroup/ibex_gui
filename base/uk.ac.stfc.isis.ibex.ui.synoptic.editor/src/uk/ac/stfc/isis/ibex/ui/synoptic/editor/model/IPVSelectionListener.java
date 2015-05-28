package uk.ac.stfc.isis.ibex.ui.synoptic.editor.model;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;

public interface IPVSelectionListener {
	void selectionChanged(PV oldSelection, PV newSelection);
}
