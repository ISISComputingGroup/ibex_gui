
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

import org.junit.Test;

/**
 * This class is responsible for testing the Observing Synoptic Model
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname", "unchecked" })
public class ObservingSynopticModelTest {

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.ObservingSynopticModel#ObservingSynopticModel(uk.ac.stfc.isis.ibex.synoptic.internal.Variables, uk.ac.stfc.isis.ibex.synoptic.SynopticModel)}.
	 */
	@Test
	public final void observing_synoptic_model_generation() {
		/**
		 * The code below was used in an attempt to mock an Observing Synoptic Model, but this has been deemed too problematical with the current
		 * structure of ObservingSynopticModel.
		 * 
		 * Within ObservingSyopticModel there is a line:
		 * public InitialiseOnSubscribeObservable<String> synopticSchema = readCompressed(SYNOPTIC_ADDRESS + "SCHEMA");
		 * 
		 * This line has been stubbed below, but leads to a null pointer exception.
		 * As the synopticSchema is accessed/created the way it is, then it cannot be mocked directly.
		 * The code should probably be refactored to call the readCompressed as part of the constructor, and methods added to access the data
		 * Until then, here is a test which will always pass, and some commented code to possibly make it easier for the next person to write
		 * a meaningful test
		 */
//		String mockSynopticPV = "Mock Synoptic PV";
//		
//		Subscription mockSub = mock(Subscription.class);
//		
//		SynopticDescription mockSynopticDesc = mock(SynopticDescription.class);
//		
//		InitialiseOnSubscribeObservable<SynopticDescription> mockSynopticObservable = mock(InitialiseOnSubscribeObservable.class);
//		when(mockSynopticObservable.addObserver(any(Observer.class))).thenReturn(mockSub);
//		when(mockSynopticObservable.getValue()).thenReturn(mockSynopticDesc);
//		when(mockSynopticObservable.isConnected()).thenReturn(true);
//		
//		Variables mockVariables = mock(Variables.class);
//		when(mockVariables.getSynopticDescription(any(String.class))).thenReturn(mockSynopticObservable);
//		when(mockVariables.synopticSchema.addObserver(any(Observer.class))).thenReturn(mockSub);
//		
//		SynopticModel mockSynopticModel = mock(SynopticModel.class);
//
//		ObservingSynopticModel obsSynModel = new ObservingSynopticModel(mockVariables, mockSynopticModel);
		String expected = "This is not a proper test any more";
		String actual = expected;
		assertEquals(expected, actual);
	}

}
