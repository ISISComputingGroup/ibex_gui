
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

package uk.ac.stfc.isis.ibex.configserver.tests.server;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.pv.PVAddress;

public class Addresses {

	@Test
	public void are_colon_separated_by_default() {
		assertThat(PVAddress.startWith("IN").append("LARMOR").toString(), is("IN:LARMOR"));
	}
	
	@Test
	public void allow_fields_to_be_added(){
		assertThat(PVAddress.startWith("IN").append("LARMOR").field("VAL").toString(), is("IN:LARMOR.VAL"));
	}
	
	@Test
	public void are_immutable() {
		PVAddress address = PVAddress.startWith("IN");
		address.append("ALF");
		assertThat(address.append("LARMOR").toString(), is("IN:LARMOR"));
	}
}
