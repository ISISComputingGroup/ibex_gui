
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.tableofmotors;

import org.csstudio.opibuilder.util.MacrosInput;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.ui.targets.OpiTargetView;

public class TableOfMotorsOpiTargetView extends OpiTargetView {
    /**
     * Class ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.tableofmotors.TableOfMotorsOpiTargetView";

    /**
     * File name of the web links OPI.
     */
    private static final String TABLE_OF_MOTORS_OPI_PATH = "table_of_motors.opi";

    /**
     * {@inheritDoc}
     */
	@Override
	protected Path opi() throws OPIViewCreationException {
		return Opi.getDefault().opiProvider().pathFromName(TABLE_OF_MOTORS_OPI_PATH);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		try {
			initialiseOPI();
		} catch (OPIViewCreationException e) {
			throw new PartInitException(e.getMessage(), e);
		}
	}

	@Override
	public MacrosInput macros() {
		MacrosInput macros = emptyMacrosInput();
		macros.put("P", Instrument.getInstance().getPvPrefix());
		return macros;
	}

}
