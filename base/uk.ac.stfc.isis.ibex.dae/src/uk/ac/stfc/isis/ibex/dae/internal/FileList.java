
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

package uk.ac.stfc.isis.ibex.dae.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;

import com.google.common.base.Strings;
import com.google.gson.Gson;

public class FileList extends TransformingObservable<String, Collection<String>> {

	private final Gson gson = new Gson();
	
	public FileList(ForwardingObservable<String> files) {
		super(files);
	}

	@Override
	protected Collection<String> transform(String value) {
		if (Strings.isNullOrEmpty(value) || Strings.isNullOrEmpty(value.trim())) {
			return Collections.emptyList();
		}
		
		return Arrays.asList(gson.fromJson(value, String[].class));
	}
}
