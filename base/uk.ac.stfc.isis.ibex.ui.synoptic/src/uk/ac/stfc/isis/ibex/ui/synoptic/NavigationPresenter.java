
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

package uk.ac.stfc.isis.ibex.ui.synoptic;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.navigation.TargetNode;
import uk.ac.stfc.isis.ibex.targets.Target;

/**
 * The model for the navigation of the synoptic via the arrow buttons.
 */
public class NavigationPresenter extends ModelObject {
	
	private TargetNode current;
	
    /**
     * Constructor for the model.
     * 
     * @param node
     *            The node to start on.
     */
	public NavigationPresenter(TargetNode node) {
		current = node;
	}
	
    /**
     * @return The current target that we are looking at.
     */
	public Target currentTarget() {
		return current.item();
	}
	
    /**
     * Set the node to look at.
     * 
     * @param target
     *            The node to look at.
     */
	public void setCurrentTarget(TargetNode target) {
		current = target;
		fireAllWithNewValues();
	}

    /**
     * @return The current target that we are looking at.
     */
	public TargetNode currentNode() {
		return current;
	}
	
    /**
     * @return True if the current node has a previous node.
     */
	public boolean getHasPrevious() {
		return current != null && current.previous() != null;
	}

    /**
     * @return True if the current node has a parent node.
     */
	public boolean getHasUp() {
		return current != null && current.up() != null;
	}
	
    /**
     * @return True if the current node has a next node.
     */
	public boolean getHasNext() {
		return current != null && current.next() != null;
	}
	
    /**
     * @return True if the current node has a child node.
     */
	public boolean getHasDown() {
		return current != null && current.down() != null;
	}
	
    /**
     * Moves to the previous node.
     */
	public void previous() {
		if (getHasPrevious()) {
			setCurrent(current.previous());
		}
	}
	
    /**
     * Get the name of the previous node.
     * 
     * @return The name of the previous node.
     */
	public String nameOfPrevious() {
		return getHasPrevious() ? displayName(current.previous().item().name()) : "";
	}

    /**
     * Moves to the parent node.
     */
	public void up() {
		if (getHasUp()) {
			setCurrent(current.up());
		}
	}
	
    /**
     * Get the name of the parent node.
     * 
     * @return The name of the parent node.
     */
	public String nameOfUp() {
		return getHasUp() ? displayName(current.up().item().name()) : "";
	}
	
    /**
     * Moves to the next node.
     */
	public void next() {
		if (getHasNext()) {
			setCurrent(current.next());
		}
	}
	
    /**
     * Get the name of the next node.
     * 
     * @return The name of the next node.
     */
	public String nameOfNext() {
		String returnString = "";
		
		if (getHasNext() && (current.next().item() != null)) {
			returnString = current.next().item().name();
		}

		return displayName(returnString);
	}
	
	private String displayName(String name) {
		return name.trim();
	}
	
	private void setCurrent(TargetNode node) {
		current = node;
		fireAllWithNewValues();
	}
	
	private void fireAllWithNewValues() {
		// Sending an previous value of null forces an update of databound controls
		firePropertyChange("hasPrevious", null, getHasPrevious());
		firePropertyChange("hasUp", null, getHasUp());
		firePropertyChange("hasNext", null, getHasNext());
		firePropertyChange("hasDown", null, getHasDown());
		
		firePropertyChange("currentTarget", null, currentTarget());
		firePropertyChange("currentNode", null, currentNode());
	}
}
