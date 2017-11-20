
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

package uk.ac.stfc.isis.ibex.dae.spectra;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.Preferences;

import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;

public class Spectra extends Closer {
	
    private static final int MAX_SPECTRUM_NUMBER = 4;

    private final ArrayList<UpdatableSpectrum> spectra = new ArrayList<>();
	private final DaeObservables observables;

	public Spectra(DaeObservables observables) {
		this.observables = observables;
	}

	public List<? extends UpdatableSpectrum> spectra() {
        while (spectra.size() < MAX_SPECTRUM_NUMBER) {
            addSpectrum(spectra.size() + 1, 1);
        }
		return spectra;
	}

	private void addSpectrum(int spectrum, int period) {
		
		Preferences preferenceStore = ConfigurationScope.INSTANCE
				.getNode("uk.ac.stfc.isis.ibex.instrument").node("spectrapreferences" + spectrum);
		
		UpdatableSpectrum spec = registerForClose(new ObservedSpectrum(preferenceStore, observables));
		
		spec.update();
		
		spectra.add(spec);
	}
}
