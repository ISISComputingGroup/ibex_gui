
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

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.model.Target;
import uk.ac.stfc.isis.ibex.synoptic.navigation.TargetNode;

public class NavigationPresenter extends ModelObject {
	
	private static final Logger LOG = IsisLog.getLogger("NavigationPresenter");
	
	private TargetNode current;
	
	public NavigationPresenter(TargetNode node) {
		current = node;
	}
	
	public Target currentTarget() {
		return current.item();
	}
	
	public void setCurrentTarget(TargetNode target) {
		current = target;
		fireAllWithNewValues();
	}

	public TargetNode currentNode() {
		return current;
	}
	
	public boolean getHasPrevious() {
		return current != null && current.previous() != null;
	}

	public boolean getHasUp() {
		return current != null && current.up() != null;
	}
	
	public boolean getHasNext() {
		return current != null && current.next() != null;
	}
	
	public boolean getHasDown() {
		return current != null && current.down() != null;
	}
	
	public void previous() {
		if (getHasPrevious()) {
			setCurrent(current.previous());
		}
	}
	
	public String nameOfPrevious() {
		return getHasPrevious() ? displayName(current.previous().item().name()) : "";
	}

	public void up() {
		if (getHasUp()) {
			setCurrent(current.up());
		}
	}
	
	public String nameOfUp() {
		return getHasUp() ? displayName(current.up().item().name()) : "";
	}
	
	public void next() {
		if (getHasNext()) {
			setCurrent(current.next());
		}
	}
	
	public String nameOfNext() {
		String returnString = "";
		
		if (getHasNext() && (current.next().item() != null)) {
			returnString = current.next().item().name();
		}

		return displayName(returnString);
	}
	
	public void down() {
		if (getHasDown()) {
			setCurrent(current.down());
		}
	}
	
	public String nameOfDown() {
		return getHasDown() ? displayName(current.down().item().name()) : "";
	}
	
	private String displayName(String name) {
		return name.trim();
	}
	
	private void setCurrent(TargetNode node) {
		LOG.info(node.item());
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
