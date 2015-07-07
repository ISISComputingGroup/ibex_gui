
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

package uk.ac.stfc.isis.ibex.product.model.tests;

import static org.junit.Assert.*;

import uk.ac.stfc.isis.ibex.product.model.blockserver.PVSummaryRepository;
import uk.ac.stfc.isis.ibex.pv.PVSummary;
import uk.ac.stfc.isis.ibex.pv.events.PVEvent;
import uk.ac.stfc.isis.ibex.pv.format.Format;
import uk.ac.stfc.isis.ibex.pv.format.FormatException;
import org.junit.Before;
import org.junit.Test;

public class PVSummaries {

	private PVSummaryRepository repository;
	private PVEvent<String> emptyPvsEvent;
	private PVEvent<String> pvsEvent;
	
	@Before
	public void setup() throws FormatException {
		repository = new PVSummaryRepository();
		
		String raw = "789cb3b1afc8cd51284b2d2acecccfb35532d4335052b0b7e3b229282bd6b7e3020093e808b6";
		String xml = Format.asBytes().apply(Format.fromZippedHex()).format(raw);
		emptyPvsEvent = new PVEvent<String>(true, "<pvs/>");
		
		pvsEvent = new PVEvent<String>(true, "<pvs><pv><name>DAE:ABORTRUN</name><type>bo</type><description>Abort the run</description><ioc>ISISDAE_01</ioc></pv></pvs>");
	}
	
	@Test
	public void no_summaries_are_added_if_no_pvs_are_provided() {	
		repository.updater().pvChanged(emptyPvsEvent);
		assertTrue(repository.summaries().isEmpty()); 
	}
	
	@Test
	public void summaries_are_added_when_pv_changes() {	
		repository.updater().pvChanged(pvsEvent);
		assertFalse(repository.summaries().isEmpty()); 
	}

	@Test
	public void pv_summaries_are_correct() {	
		repository.updater().pvChanged(pvsEvent);
		PVSummary summary = repository.summaries().get(0);
		assertEquals("DAE:ABORTRUN", summary.name);
		assertEquals("bo", summary.recordType);
		assertEquals("Abort the run", summary.description);
		assertEquals("ISISDAE_01", summary.ioc);	
	}
}
