
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

package uk.ac.stfc.isis.ibex.synoptic.navigation;

import uk.ac.stfc.isis.ibex.targets.Target;

/**
 * A node representing a target (e.g. OPI) in the synoptic view.
 */
public class TargetNode  {

	private final Target item;
	private TargetNode previous = null;
	private TargetNode up = null;
	private TargetNode next = null;
	private TargetNode down = null;
	
	/**
	 * Create a new target node pointing at the specified target.
	 * @param item the target
	 */
	public TargetNode(Target item) {
		this.item = item;
	}
	
	/**
	 * Copy another target node.
	 * @param other the other node
	 */
	public TargetNode(TargetNode other) {
		this(other.item);
		previous = other.previous;
		up = other.up;
		next = other.next;
		down = other.down;
	}
	
	/**
	 * Gets the target.
	 * @return the target
	 */
	public Target item() {
		return item;
	}
	
	/**
	 * Gets the previous target.
	 * @return the previous target
	 */
	public TargetNode previous() {
		return previous;
	}

	/**
	 * Sets the previous target.
	 * @param node the previous target
	 */
	public void setPrevious(TargetNode node) {
		previous = node;
	}
	
	/**
	 * Gets the target "up" from this one.
	 * @return the target
	 */
	public TargetNode up() {
		return up;
	}
	
	/**
	 * Sets the target "up" from this one.
	 * @param node the target
	 */
	public void setUp(TargetNode node) {
		up = node;
	}
	
	/**
	 * Gets the next target.
	 * @return the next target
	 */
	public TargetNode next() {
		return next;
	}
	
	/**
	 * Sets the next target.
	 * @param node the next target
	 */
	public void setNext(TargetNode node) {
		next = node;
	}
	
	/**
	 * Gets the target "down" from this one.
	 * @return the target
	 */
	public TargetNode down() {
		return down;
	}
	
	/**
	 * Sets the target "down" from this one.
	 * @param node the target
	 */
	public void setDown(TargetNode node) {
		down = node;
	}
	
}
