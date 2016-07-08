
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

public class TargetNode  {

	private final Target item;
	private TargetNode previous = null;
	private TargetNode up = null;
	private TargetNode next = null;
	private TargetNode down = null;
	
	public TargetNode(Target item) {
		this.item = item;
	}
	
	public TargetNode(TargetNode other) {
		this(other.item);
		previous = other.previous;
		up = other.up;
		next = other.next;
		down = other.down;
	}
	
	public Target item() {
		return item;
	}
	
	public TargetNode previous() {
		return previous;
	}

	public void setPrevious(TargetNode node) {
		previous = node;
	}
	
	public TargetNode up() {
		return up;
	}
	
	public void setUp(TargetNode node) {
		up = node;
	}
	
	public TargetNode next() {
		return next;
	}
	
	public void setNext(TargetNode node) {
		next = node;
	}
	
	public TargetNode down() {
		return down;
	}
	
	public void setDown(TargetNode node) {
		down = node;
	}
	
}
