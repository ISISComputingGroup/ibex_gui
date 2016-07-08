
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.devicescreens.desc.Component;
import uk.ac.stfc.isis.ibex.devicescreens.desc.ComponentType;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.synoptic.internal.ObservableComponent;
import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.RecordType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;
import uk.ac.stfc.isis.ibex.targets.Target;

/**
 * This class is responsible for testing ObservableComponent 
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname", "unchecked" })
public class ObservableComponentTest {

	// Items needed between the set up and the tests
	String compDesc = "Component Description";
	ObservableComponent obsComp;
	
	/**
	 * Prep method to generate the appropriate mocks
	 */
	@Before
	public void set_up_observable_component() {
		// Arrange - used as before for use in each test
		String addressSuffix = "Address Suffix";
		String channelValue = "Channel";
		String synopticDescription = "Synoptic Description";
		String pvDisplayName = "PV Display Name";
		String targetValue = "Target Value";
		String compName = "Component Name";
		String targetName = "Target Name";
		ComponentType compType = ComponentType.UNKNOWN;
		RecordType recordType = new RecordType();
		recordType.setIO(IO.READ);
		
		PV mockPV = mock(PV.class);
		when(mockPV.recordType()).thenReturn(recordType);
		when(mockPV.displayName()).thenReturn(pvDisplayName);
		when(mockPV.address()).thenReturn(addressSuffix);
		
		List<PV> mockPVs = new ArrayList<>();
		mockPVs.add(mockPV);
		
		TargetType mockTargetType = TargetType.OPI;
		
		TargetDescription mockTargetDesc = mock(TargetDescription.class);
		when(mockTargetDesc.name()).thenReturn(targetValue);
		when(mockTargetDesc.type()).thenReturn(mockTargetType);
		
		Target mockTarget = mock(Target.class);
		when(mockTarget.name()).thenReturn(targetName);
		
		ComponentDescription mockComp = mock(ComponentDescription.class);
		when(mockComp.target()).thenReturn(mockTargetDesc);
		when(mockComp.type()).thenReturn(compType);
		when(mockComp.name()).thenReturn(compName);
		
		List<ComponentDescription> mockComps = new ArrayList<>();
		mockComps.add(mockComp);
		
		ComponentDescription mockCompDesc = mock(ComponentDescription.class);
		when(mockCompDesc.pvs()).thenReturn(mockPVs);
		when(mockCompDesc.target()).thenReturn(mockTargetDesc);
		when(mockCompDesc.components()).thenReturn(mockComps);
		when(mockCompDesc.name()).thenReturn(compDesc);
		when(mockCompDesc.type()).thenReturn(compType);
		
		Subscription mockSub = mock(Subscription.class);
	
        Observer<String> mockObserver = mock(Observer.class);
		
		ForwardingObservable<String> mockObservable = mock(ForwardingObservable.class);
		when(mockObservable.addObserver(any(Observer.class))).thenReturn(mockSub);
		when(mockObservable.addObserver(mockObserver)).thenReturn(mockSub);
		when(mockObservable.getValue()).thenReturn(synopticDescription);
		
		Variables mockVariables = mock(Variables.class);
        when(mockVariables.defaultReaderRemote(addressSuffix)).thenReturn(mockObservable);
		
		obsComp = new ObservableComponent(mockCompDesc, mockVariables);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.ObservableComponent#target()}.
	 */
	@Test
	public final void check_the_target() {
		// Act
		String actual = obsComp.target().toString();
		// Assert
		assertEquals(compDesc, actual);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.ObservableComponent#setTarget(uk.ac.stfc.isis.ibex.targets.Target)}.
	 */
	@Test
	public final void set_the_target() {
		// Act
		Target mockTarget = mock(Target.class);
		obsComp.setTarget(mockTarget);
		Target actual = obsComp.target();
		// Assert
		assertEquals(mockTarget, actual);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.ObservableComponent#copy()}.
	 */
	@Test
	public final void copy_the_component() {
		// Act
		Component newComp = obsComp.copy();
		// Assert
		assertEquals(obsComp.name(), newComp.name());
	}

}
