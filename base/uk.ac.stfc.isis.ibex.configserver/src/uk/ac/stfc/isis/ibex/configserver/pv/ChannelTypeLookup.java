
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

package uk.ac.stfc.isis.ibex.configserver.pv;

import java.util.Locale;

import uk.ac.stfc.isis.ibex.instrument.channels.ChannelType;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.NumberWithUnitsChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;

public final class ChannelTypeLookup {

    private ChannelTypeLookup() {
    }

    public static ChannelType<String> get(String type) {
		switch (type.toUpperCase(Locale.ENGLISH)) {
		case "AI":
			return new NumberWithUnitsChannel();
		case "STRINGIN":
		case "STRINGOUT":
			return new StringChannel();
		default:
			return new DefaultChannel();
		}
	}
}
