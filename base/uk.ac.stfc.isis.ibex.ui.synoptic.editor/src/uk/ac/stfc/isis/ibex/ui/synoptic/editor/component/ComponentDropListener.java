
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

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

import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument.InstrumentTreeView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.InstrumentViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

/**
 * 
 * User click drag - startDrag User release drag - setDragData drop - drop drop
 * - performDrop drag - finishedDrag
 */
public class ComponentDropListener extends ViewerDropAdapter {
	private final InstrumentTreeView viewer;
	private final InstrumentViewModel instrument;

	public ComponentDropListener(InstrumentTreeView viewer,
			InstrumentViewModel instrument) {
		super(viewer.getTreeViewer());
		this.viewer = viewer;
		this.instrument = instrument;
	}

	@Override
	public void drop(DropTargetEvent event) {
		// If drop is not valid, do nothing
		if (!validateDrop(event)) {
			return;
		}

		ComponentDescription sourceComponent = viewer.getCurrentDragSource();
		ComponentDescription targetComponent = (ComponentDescription) determineTarget(event);
		ComponentDescription sourceParent = sourceComponent.getParent();
		int location = this.determineLocation(event);
		
		// remove component from parent's list of components
		if (location == LOCATION_BEFORE || location == LOCATION_AFTER
				|| location == LOCATION_ON) {
			if (sourceParent == null) {
				// sourceParent is the instrument
				InstrumentDescription parent = instrument.getInstrument();
				parent.removeComponent(sourceComponent);
			} else {
				sourceParent.removeComponent(sourceComponent);
			}
		}

		if (location == LOCATION_BEFORE || location == LOCATION_AFTER) {
			// Get the parent of the target component - fail if null
			ComponentDescription targetParent = targetComponent.getParent();

			// parent == instrument
			if (targetParent == null) {
				InstrumentDescription parent = instrument.getInstrument();
				int position = parent.components().indexOf(targetComponent);
				if (location == LOCATION_AFTER) {
					++position;
				}

				parent.addComponent(sourceComponent, position);
			} else {
				int position = targetParent.components().indexOf(
						targetComponent);
				if (location == LOCATION_AFTER) {
					++position;
				}

				targetParent.addComponent(sourceComponent, position);
			}

			sourceComponent.setParent(targetParent);
		} else if (location == LOCATION_ON) {
			// Move the dragged component to its new parent
			targetComponent.addComponent(sourceComponent);
			sourceComponent.setParent(targetComponent);
		}

		super.drop(event);
	}

	/**
	 * Called from super.drop()
	 */
	@Override
	public boolean performDrop(Object data) {
		instrument.broadcastInstrumentUpdate(UpdateTypes.MOVE_COMPONENT);
		return true;
	}

	/**
	 * Returns a value indicating whether a drop at the current position is
	 * allowed.
	 */
	public boolean validateDrop(DropTargetEvent event) {
		// Fail if the component being dragged is null
		ComponentDescription sourceComponent = viewer.getCurrentDragSource();
		if (sourceComponent == null) {
			return false;
		}

		// Fail if the target is null or not a component
		Object targetObject = determineTarget(event);
		if (targetObject == null
				|| !(targetObject instanceof ComponentDescription)) {
			return false;
		}

		// Fail if trying to drop onto self
		int location = this.determineLocation(event);
		if (sourceComponent == targetObject) {
			return false;
		}

		// Fail if no target
		if (location == LOCATION_NONE) {
			return false;
		}
		
		//Fail if trying to drop a parent on to a child
		if (sourceComponent.hasChild((ComponentDescription) targetObject)) {
			return false;
		}

		return true;
	}

	/**
	 * Called whenever the user moves the mouse over a new element while
	 * dragging
	 */
	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		DropTargetEvent event = getCurrentEvent();
		return validateDrop(event);
	}
}
