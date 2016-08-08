
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

package uk.ac.stfc.isis.ibex.synoptic.tests.internal;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.internal.BaseComponent;
import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.targets.Target;

/**
 * This class is responsible for testing the Base Component 
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname", "unchecked" })
public class BaseComponentTest {

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.BaseComponent#BaseComponent(uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription)}
	 * and {@link uk.ac.stfc.isis.ibex.synoptic.internal.BaseComponent#name()}.
	 */
	@Test
	public final void base_component_name_with_description() {
		// Arrange
		String compName = "TestName";
		ComponentDescription compDesc = new ComponentDescription();
		compDesc.setName(compName);
		BaseComponent baseComp = new BaseComponent(compDesc) {
			@Override
			public Target target() {
				return null;
			}
			@Override
			public void setTarget(Target target) {
			}
			@Override
			public Component copy() {
				return null;
			}
		};
		// Assert
		assertEquals(compName, baseComp.name());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.BaseComponent#BaseComponent(uk.ac.stfc.isis.ibex.synoptic.internal.BaseComponent)}
	 * and {@link uk.ac.stfc.isis.ibex.synoptic.internal.BaseComponent#name()}.
	 */
	@Test
	public final void base_component_name_with_base_component() {
		// Arrange
		String compName = "TestName";
		ComponentDescription compDesc = new ComponentDescription();
		compDesc.setName(compName);
		BaseComponent baseComp = new BaseComponent(compDesc) {
			@Override
			public Target target() {
				return null;
			}
			@Override
			public void setTarget(Target target) {
			}
			@Override
			public Component copy() {
				return null;
			}
		};
		BaseComponent testComp = new BaseComponent(baseComp) {
			@Override
			public Target target() {
				return null;
			}
			@Override
			public void setTarget(Target target) {
			}
			@Override
			public Component copy() {
				return null;
			}
		};
		// Assert
		assertEquals(compName, testComp.name());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.BaseComponent#type()}.
	 */
	@Test
	public final void base_component_with_type() {
		// Arrange
		ComponentDescription compDesc = new ComponentDescription();
		ComponentType compType = ComponentType.ANALYSER;
		compDesc.setType(compType);
		BaseComponent baseComp = new BaseComponent(compDesc) {
			@Override
			public Target target() {
				return null;
			}
			@Override
			public void setTarget(Target target) {
			}
			@Override
			public Component copy() {
				return null;
			}
		};
		// Assert
		assertEquals(compType, baseComp.type());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.BaseComponent#properties()}.
	 */
	@Test
	public final void base_component_with_properties() {
		// Arrange
		LinkedHashSet<ComponentProperty> expected = new LinkedHashSet<>();
		ComponentDescription compDesc = new ComponentDescription();
		BaseComponent baseComp = new BaseComponent(compDesc) {
			@Override
			public Target target() {
				return null;
			}
			@Override
			public void setTarget(Target target) {
			}
			@Override
			public Component copy() {
				return null;
			}
		};
		// Assert
		assertEquals(expected, baseComp.properties());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.BaseComponent#components()}.
	 */
	@Test
	public final void base_component_with_components() {
		// Arrange
		List<Component> expected = new ArrayList<>();
		ComponentDescription compDesc = new ComponentDescription();
		BaseComponent baseComp = new BaseComponent(compDesc) {
			@Override
			public Target target() {
				return null;
			}
			@Override
			public void setTarget(Target target) {
			}
			@Override
			public Component copy() {
				return null;
			}
		};
		// Assert
		assertEquals(expected, baseComp.components());
	}
}