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

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.StringUtils;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorColours;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorStateObserver;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorViewStateConverter;

/**
 * Dae simulation mode model.
 */
public class CurrentConfigModel extends Closer implements IndicatorModel {

    private IndicatorStateObserver<DisplayConfiguration> observer;
    private static final String CONFIG_PREFIX = "Config: ";

	/**
     * Constructor for the dae simulation mode banner model.
     */
    public CurrentConfigModel() {
    	
    	ForwardingObservable<DisplayConfiguration> currConfig = Configurations.getInstance().display().displayCurrentConfig();
    	
    	IndicatorViewStateConverter<DisplayConfiguration> converter = new IndicatorViewStateConverter<DisplayConfiguration>() {

    		private DisplayConfiguration state;
    		
			@Override
			public void setState(DisplayConfiguration state) {
				this.state = state;
			}

			@Override
			public String getMessage() {
				try {
				    return StringUtils.truncateWithEllipsis(String.format("%s%s", CONFIG_PREFIX, state.name()), 50);
				} catch (NullPointerException e) {
					return "Config: unknown";
				}
			}

			@Override
			public Color color() {
				return IndicatorColours.BLACK;
			}

			@Override
			public Boolean toBool() {
				return availability();
			}

			@Override
			public Boolean availability() {
				try {
				    return state.isConnected();
				} catch (NullPointerException e) {
					return false;
				}
			}
		};
    	
        observer = registerForClose(new IndicatorStateObserver<DisplayConfiguration>(currConfig, converter));
        
        
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