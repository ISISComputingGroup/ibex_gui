
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.banner.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.configserver.AlarmState;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorColours;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;

/**
 * Model containing banner item status as updated values used for display in
 * GUI.
 */
public class BannerItemModel extends ModelObject implements IndicatorModel {

    private final SettableUpdatedValue<String> text = new SettableUpdatedValue<>();
    private final SettableUpdatedValue<Boolean> availability = new SettableUpdatedValue<>(true);
    private final SettableUpdatedValue<Color> colour = new SettableUpdatedValue<>(IndicatorColours.BLACK);
    
    private final BannerItem item;
    
    private static final int MAX_TEXT_LENGTH = 30;
    private static final String ELIPSES = "...";
    
    /**
     * Instantiates model and converter.
     * 
     * @param item the banner item being observed
     */
    public BannerItemModel(final BannerItem item) {
    	this.item = item;
                
        item.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
            	update();
            }
        });
        
        update();
    }
    
    private synchronized void update() {
    	updateText();
    	updateColour();
    }
    
    private synchronized void updateText() {
    	String setText;
    	
    	if (item.value() == null) {
    		setText = item.name() + " (disconnected)";
    	} else {
    		setText = item.name() + ": " + item.value();
    	}
    	
    	if (setText.length() > MAX_TEXT_LENGTH) {
    		setText = setText.substring(0, MAX_TEXT_LENGTH - ELIPSES.length()) + ELIPSES;
    	}
    	text.setValue(setText);
    }
    
    private synchronized void updateColour() {
    	Color colour;
    	AlarmState alarm = item.alarm();
    	
    	if (alarm == AlarmState.MAJOR) {
    		colour = IndicatorColours.RED;
    	} else if (alarm == AlarmState.MINOR) {
    		colour = IndicatorColours.ORANGE;
    	} else if (alarm == AlarmState.UNDEFINED || alarm == AlarmState.INVALID) {
    		colour = IndicatorColours.PURPLE;
    	} else {
    		colour = IndicatorColours.BLACK;
    	}
    	
    	this.colour.setValue(colour);
    }

    /**
     * @return the text of this banner item.
     */
	@Override
	public UpdatedValue<String> text() {
		return text;
	}

	/**
	 * @return the colour of this banner item.
	 */
	@Override
	public UpdatedValue<Color> color() {
		return colour;
	}

	/**
	 * @return the availability of this banner item.
	 */
	@Override
	public UpdatedValue<Boolean> availability() {
		return availability;
	}

}
