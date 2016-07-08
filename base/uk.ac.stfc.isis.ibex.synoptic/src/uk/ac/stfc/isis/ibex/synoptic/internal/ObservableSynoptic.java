
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

package uk.ac.stfc.isis.ibex.synoptic.internal;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.devicescreens.desc.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

/**
 * Defines the synoptic layout, observable for changes to synoptic.
 * 
 */
public class ObservableSynoptic implements Synoptic {

	private final SynopticDescription instrument;

	private final List<Component> components = new ArrayList<>();

	public ObservableSynoptic(SynopticDescription instrument, Variables variables) {
		this.instrument = instrument;

		for (ComponentDescription description : instrument.components()) {
			components.add(new ObservableComponent(description, variables));
		}
	}

	@Override
	public String name() {
		return instrument.name();
	}

	@Override
	public List<? extends Component> components() {
		return new ArrayList<>(components);
	}

	@Override
	public SynopticDescription getDescription() {
		return instrument;
	}

	@Override
	public Boolean showBeam() {
		return instrument.showBeam();
	}
}
