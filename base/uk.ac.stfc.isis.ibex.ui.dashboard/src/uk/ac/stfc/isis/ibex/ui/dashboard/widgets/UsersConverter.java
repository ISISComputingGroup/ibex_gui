
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

package uk.ac.stfc.isis.ibex.ui.dashboard.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.conversion.Converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * Takes the JSON list string and converts it into a nicely formatted list of
 * names.
 */
public class UsersConverter extends Converter {
	
    public UsersConverter(Object fromType, Object toType) {
		super(fromType, toType);
	}

	@Override
	public Object convert(Object arg0) {
		String raw = "";
		
		List<String> names = new ArrayList<String>();
		
		try {
            // CHECKSTYLE:OFF The declaration format for GSON's TypeToken upsets
            // CheckStyle.
			names = new Gson().fromJson(arg0.toString(), new TypeToken<List<String>>() { }.getType());
            // CHECKSTYLE:ON
		} catch (Exception err) {
			//It was not valid json, so just set users to nothing
            return "";
		}
		
		if (names != null && names.size() > 0) {
			for (int i = 0; i < names.size(); ++i) {
				if (i == 0) {
					raw += names.get(i);
				} else if (i == names.size() - 1) {
					raw += " and " + names.get(i);
				} else {
					raw += ", " + names.get(i);
				}	
			}
		}
		
		return raw.trim();
	}

}
