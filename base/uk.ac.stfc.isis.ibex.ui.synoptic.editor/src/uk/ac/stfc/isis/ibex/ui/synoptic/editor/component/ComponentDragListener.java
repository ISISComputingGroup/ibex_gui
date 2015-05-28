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

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument.InstrumentTreeView;

/**
 * Provides event notifications for drag events in the component tree when the
 * user is rearranging the tree elements
 * 
 */
public class ComponentDragListener implements DragSourceListener {
	private final InstrumentTreeView viewer;

	public ComponentDragListener(InstrumentTreeView viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		ComponentDescription selection = viewer.getSelected();
		viewer.setCurrentDragSource(selection);
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		// do nothing; data is set in dragStart
		event.data = "component";
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		viewer.setCurrentDragSource(null);
	}

}
