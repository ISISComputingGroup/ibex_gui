
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.GroupedComponentTarget;
import uk.ac.stfc.isis.ibex.targets.Target;

public class InstrumentNavigationGraph {

	private final Map<Component, TargetNode> targetNodes = new HashMap<>();
	private final Map<String, TargetNode> targets = new LinkedHashMap<>();
	
    private TargetNode head;
	
	public InstrumentNavigationGraph(Synoptic instrument) {
		createTargetNodes(instrument.components());
        buildGraph(instrument);
	}
	
	private TargetNode buildGraph(Synoptic instrument) {
		Target top = topNode(instrument);
        head = addTargetNode(top);
		
		if (instrument.components().isEmpty()) {
			return head;
		}
		
		head.setNext(nodeOfFirstChild(instrument.components()));
		
		connectHorizontally(instrument.components(), head);
		connectVertically(instrument.components());
		
		return head;
	}

	public TargetNode head() {
        return head;
	}
	
	public Map<String, TargetNode> targets() {
		return new LinkedHashMap<>(targets);
	}
	
	private static Target topNode(Synoptic instrument) {
		String name = Strings.isNullOrEmpty(instrument.name()) ? "Instrument" : instrument.name();
		Target top = new GroupedComponentTarget(name, instrument.components());
		return top;
	}
	
	private TargetNode addTargetNode(Target target) {
		TargetNode node = new TargetNode(target);
		
		if (target != null) {
			targets.put(target.name(), node);
		}
		
		if (target instanceof GroupedComponentTarget) {
			GroupedComponentTarget group = (GroupedComponentTarget) target;
			if (!group.components().isEmpty()) {
				Target extraTarget = group.components().get(0).target();
				TargetNode extraNode = addTargetNode(extraTarget);
				extraNode.setUp(node);
			}
		}
		
		return node;		
	}
	
	private void connectVertically(List<? extends Component> components) {
		for (Component component : components) {
			TargetNode current = targetNodes.get(component);
			if (hasChildren(component)) {
				current.setDown(nodeOfFirstChild(component.components()));
				connectHorizontally(component.components(), current);
				connectVertically(component.components());
			}
		}
	}

	private void connectHorizontally(List<? extends Component> components, TargetNode up) {
		TargetNode previous = null;
		for (Component component : componentsWithTargets(components)) {
			TargetNode current = targetNodes.get(component);
			current.setUp(up);
						
			if (previous != null) { 
				current.setPrevious(previous);
				previous.setNext(current);
			}
			
			previous = current;
		}
	}
	
	private static List<Component> componentsWithTargets(List<? extends Component> components) {
		List<Component> withTargets = new ArrayList<>();
		for (Component component : components) {
			if (component.target() != null) {
				withTargets.add(component);
			}
		}
		
		return withTargets;
	}

	private static boolean hasChildren(Component component) {
		return !component.components().isEmpty();
	}

	private TargetNode nodeOfFirstChild(List<? extends Component> components) {
		Component firstChild = components.get(0);
		return targetNodes.get(firstChild);
	}
	
	private void createTargetNodes(List<? extends Component> components) {
		for (Component component : components) {
			targetNodes.put(component, addTargetNode(component.target()));
			createTargetNodes(component.components());
		}
	}
}
