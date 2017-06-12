
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.component;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticParentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument.InstrumentTreeView;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

/**
 * Allow the component to receive a drop event.
 * 
 * User click drag - startDrag User release drag - setDragData drop - drop drop
 * - performDrop drag - finishedDrag
 */
public class ComponentDropListener extends ViewerDropAdapter {
	private final InstrumentTreeView view;
	private final SynopticViewModel instrument;

    /**
     * Instantiates a new component drop listener.
     *
     * @param viewer
     *            the tree viewer that has this listener
     * @param view
     *            the overall view that contains the tree
     * @param instrument
     *            the instrument synoptic
     */
    public ComponentDropListener(TreeViewer viewer, InstrumentTreeView view,
			SynopticViewModel instrument) {
        super(viewer);
        this.view = view;
		this.instrument = instrument;
	}

	@Override
	public void drop(DropTargetEvent event) {
		// If drop is not valid, do nothing
		if (!validateDrop(event)) {
			return;
		}

		Collection<ComponentDescription> sourceComponents = view.getCurrentDragSource();
		ComponentDescription targetComponent = (ComponentDescription) determineTarget(event);
		int location = this.determineLocation(event);
		
        // Drop on to viewer makes items appears at the end of the list
        if (targetComponent == null) {
            List<ComponentDescription> components = instrument.getSynoptic().components();
            targetComponent = components.get(components.size() - 1);
            location = LOCATION_AFTER;
        }

		for (ComponentDescription sourceComponent : sourceComponents) {
			SynopticParentDescription sourceParent = instrument.getParent(sourceComponent);
			
			// remove component from parent's list of components
			if (location == LOCATION_BEFORE || location == LOCATION_AFTER
					|| location == LOCATION_ON) {
				sourceParent.removeComponent(sourceComponent);
			}
	
			if (location == LOCATION_BEFORE || location == LOCATION_AFTER) {
				// Get the parent of the target component - fail if null
				SynopticParentDescription targetParent = instrument.getParent(targetComponent);
				int position = targetParent.components().indexOf(
						targetComponent);
				if (location == LOCATION_AFTER) {
					++position;
				}

				targetParent.addComponent(sourceComponent, position);
	
				sourceComponent.setParent(targetParent);
			} else if (location == LOCATION_ON) {
				// Move the dragged component to its new parent
				targetComponent.addComponent(sourceComponent);
				sourceComponent.setParent(targetComponent);
			}
		}
		
		super.drop(event);
	}

	/**
     * Allows component to be dropped.
     * 
     * Called from super.drop().
     *
     * @param data the data
     * @return true, if component can be moved successful; false otherwise
     */
	@Override
	public boolean performDrop(Object data) {
		instrument.broadcastInstrumentUpdate(UpdateTypes.MOVE_COMPONENT);
		return true;
	}

	/**
     * Validate whether a component can be dropped.
     * 
     * @param event the drop event
     * @return true if it can be dropped; false otherwise
     */
	public boolean validateDrop(DropTargetEvent event) {
		// Fail if the component being dragged is null
		Collection<ComponentDescription> sourceComponents = view.getCurrentDragSource();
		if (sourceComponents == null) {
			return false;
		}

        // If target is null then target is parent and dropped item can appear
        // at the end if it is
        // not already at the end
		Object targetObject = determineTarget(event);
        if (targetObject == null) {
            List<ComponentDescription> components = instrument.getSynoptic().components();
            ComponentDescription lastComponent = components.get(components.size() - 1);
            return !sourceComponents.contains(lastComponent);
        }

        // only drop onto component if there is a target
        if (!(targetObject instanceof ComponentDescription)) {
			return false;
		}

		// Fail if no target
		int location = this.determineLocation(event);
		if (location == LOCATION_NONE) {
			return false;
		}
		
		for (ComponentDescription sourceComponent : sourceComponents) {
			//Fail if trying to drop a parent on to a child
			if (sourceComponent.hasChild((ComponentDescription) targetObject)) {
				return false;
			}
			
			// Fail if trying to drop onto self
			if (sourceComponent == targetObject) {
				return false;
			}
		}
		return true;
	}

	/**
     * Validate that an item can be dropped. Called whenever the user moves the
     * mouse over a new element while dragging.
     *
     * @param target the target of the drop
     * @param operation the operation being performed by the drop
     * @param transferType the transfer type
     * @return true, if drop is valid; false otherwise
     */
	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		DropTargetEvent event = getCurrentEvent();
		return validateDrop(event);
	}
}
