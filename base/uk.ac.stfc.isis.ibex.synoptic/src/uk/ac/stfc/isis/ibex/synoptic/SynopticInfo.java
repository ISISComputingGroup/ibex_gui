
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

package uk.ac.stfc.isis.ibex.synoptic;

import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;

@SuppressWarnings({ "checkstyle:membername" })
public class SynopticInfo {

	private final String name;
	private final String pv;
    private final boolean is_default;
	
    public SynopticInfo() {
        this.name = Variables.NONE_SYNOPTIC_NAME;
        this.pv = Variables.NONE_SYNOPTIC_PV;
        this.is_default = false;
    }

    public SynopticInfo(String name, String pv, boolean isDefault) {
		this.name = name;
		this.pv = pv;
        this.is_default = isDefault;
	}
	
	public String name() {
		return name;
	}
	
	public String pv() {
		return pv;
	}
	
    public boolean isDefault() {
        return is_default;
    }

	public static Collection<String> names(Collection<SynopticInfo> infos) {
		if (infos == null) {
			return Collections.emptyList();
		}
		
		return Lists.newArrayList(Iterables.transform(infos, new Function<SynopticInfo, String>() {
			@Override
			public String apply(SynopticInfo info) {
				return info.name();
			}	
		}));		
	}
	
	public static SynopticInfo search(Collection<SynopticInfo> available, String name) {
		for (SynopticInfo synoptic : available) {
			if (synoptic.name().equals(name)) {
				return synoptic;
			}
		}
		return null;
	}
}
