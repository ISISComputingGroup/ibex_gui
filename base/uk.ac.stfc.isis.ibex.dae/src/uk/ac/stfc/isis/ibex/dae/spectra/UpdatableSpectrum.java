
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

import org.osgi.service.prefs.Preferences;

/**
 * An updatable spectrum.
 */
public class UpdatableSpectrum extends Spectrum {

	private boolean requiresUpdate;
	
	/**
	 * Constructor.
	 * @param preferenceStore the preference store to use.
	 */
	public UpdatableSpectrum(Preferences preferenceStore) {
		super(preferenceStore);
	}

	/**
	 * Whether the spectrum requires an update.
	 * @return true if it requires an update, false otherwise
	 */
	public boolean getRequiresUpdate() {
		return requiresUpdate;
	}
	
	/**
	 * Trigger an update of the spectrum.
	 */
	public void update() {
		firePropertyChange("requiresUpdate", requiresUpdate, requiresUpdate = false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNumber(int value) {
		super.setNumber(value);
		firePropertyChange("requiresUpdate", requiresUpdate, requiresUpdate = true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPeriod(int value) {
		super.setPeriod(value);
		firePropertyChange("requiresUpdate", requiresUpdate, requiresUpdate = true);
	}
}
