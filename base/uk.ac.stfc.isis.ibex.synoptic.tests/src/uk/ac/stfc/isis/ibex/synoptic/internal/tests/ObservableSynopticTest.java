
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

package uk.ac.stfc.isis.ibex.synoptic.internal.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.synoptic.internal.ObservableSynoptic;
import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;
import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

/**
 * This class is responsible for testing the observable synoptic 
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname", "unchecked" })
public class ObservableSynopticTest {

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.ObservableSynoptic#name()}.
	 */
	@Test
	public final void check_synoptic_name() {
		// Arrange
		String expected = "Synoptic Name";
		
		SynopticDescription mockInstrument = mock(SynopticDescription.class);
		when(mockInstrument.name()).thenReturn(expected);
		
		Variables mockVariables = mock(Variables.class);
		
		ObservableSynoptic obsSynoptic = new ObservableSynoptic(mockInstrument, mockVariables);
		// Act
		String actual = obsSynoptic.name();
		// Assert
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.ObservableSynoptic#components()}.
	 */
	@Test
	public final void check_components_by_list_size() {
		// Arrange
		String compDescName = "Component Description";
		ComponentType compType = ComponentType.UNKNOWN;
		
		ComponentDescription mockCompDesc = mock(ComponentDescription.class);
		when(mockCompDesc.type()).thenReturn(compType);
		when(mockCompDesc.name()).thenReturn(compDescName);
		
		List<ComponentDescription> mockCompDescs = new ArrayList<>();
		mockCompDescs.add(mockCompDesc);
		mockCompDescs.add(mockCompDesc);
		mockCompDescs.add(mockCompDesc);
		
		SynopticDescription mockInstrument = mock(SynopticDescription.class);
		when(mockInstrument.components()).thenReturn(mockCompDescs);
		
		Variables mockVariables = mock(Variables.class);
		
		ObservableSynoptic obsSynoptic = new ObservableSynoptic(mockInstrument, mockVariables);
		
		// Act
		List<? extends Component> actual = obsSynoptic.components();
		
		// Assert
		assertEquals(mockCompDescs.size(), actual.size());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.ObservableSynoptic#getDescription()}.
	 */
	@Test
	public final void check_description() {
		// Arrange
		String expected = "Synoptic Name";
		
		SynopticDescription mockInstrument = mock(SynopticDescription.class);
		when(mockInstrument.name()).thenReturn(expected);
		
		Variables mockVariables = mock(Variables.class);
		
		ObservableSynoptic obsSynoptic = new ObservableSynoptic(mockInstrument, mockVariables);
		// Act
		SynopticDescription actual = obsSynoptic.getDescription();
		// Assert
		assertEquals(expected, actual.name());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.ObservableSynoptic#showBeam()}.
	 */
	@Test
	public final void show_beam_true() {
		// Arrange
		Boolean expected = true;
		
		SynopticDescription mockInstrument = mock(SynopticDescription.class);
		when(mockInstrument.showBeam()).thenReturn(expected);
		
		Variables mockVariables = mock(Variables.class);
		
		ObservableSynoptic obsSynoptic = new ObservableSynoptic(mockInstrument, mockVariables);
		// Act
		Boolean actual = obsSynoptic.showBeam();
		// Assert
		assertEquals(expected, actual);
	}
	
	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.ObservableSynoptic#showBeam()}.
	 */
	@Test
	public final void show_beam_false() {
		// Arrange
		Boolean expected = false;
		
		SynopticDescription mockInstrument = mock(SynopticDescription.class);
		when(mockInstrument.showBeam()).thenReturn(expected);
		
		Variables mockVariables = mock(Variables.class);
		
		ObservableSynoptic obsSynoptic = new ObservableSynoptic(mockInstrument, mockVariables);
		// Act
		Boolean actual = obsSynoptic.showBeam();
		// Assert
		assertEquals(expected, actual);
	}

}
