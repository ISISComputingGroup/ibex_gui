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
package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.dae.Dae;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorColours;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;

/**
 * Dae simulation mode model.
 */
public class DaeSimulationModeModel extends Closer implements IndicatorModel {

    private IndicatorObserver<Boolean> observer;

    /**
     * Constructor for the dae simulation mode banner model.
     */
    public DaeSimulationModeModel() {
        observer = registerForClose(new IndicatorObserver<Boolean>(Dae.getInstance().model().simulationMode()) {
			
			@Override
			protected void setUnknown() {
				text.setValue("DAE Simulation mode: unknown");
                color.setValue(IndicatorColours.RED);
			}
			
			@Override
			protected void setSimMode(Boolean value) {
				if (value) {
                    text.setValue("DAE Simulation mode: active");
                    color.setValue(IndicatorColours.RED);
                } else {
                	// Display no message if not in simulation mode.
                    text.setValue("");
                    color.setValue(IndicatorColours.BLACK);
                }	
			}
		});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatedValue<String> text() {
        return observer.text();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatedValue<Color> color() {
        return observer.color();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatedValue<Boolean> availability() {
        return observer.availability();
    }

}
