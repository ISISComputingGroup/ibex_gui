
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.AlarmState;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.StringUtils;
import uk.ac.stfc.isis.ibex.ui.Utils;

/**
 * Model containing banner item status as updated values used for display in
 * GUI.
 */
public class BannerItemModel extends ModelObject {
    
    private static final Color RED = SWTResourceManager.getColor(255, 0, 0); 
    private static final Color BLACK = SWTResourceManager.getColor(0, 0, 0);
    private static final Color PURPLE = SWTResourceManager.getColor(255, 0, 255);
    private static final Color ORANGE = SWTResourceManager.getColor(255, 120, 50);

    private final int index;
    private final SettableUpdatedValue<String> text = new SettableUpdatedValue<>();
    private final SettableUpdatedValue<Boolean> availability = new SettableUpdatedValue<>(true);
    private final SettableUpdatedValue<Color> colour = new SettableUpdatedValue<>(BLACK);
    
    private final BannerItem item;
    private final int width;
    private static final int MIN_WIDTH = 10;
    private static final int MAX_WIDTH = 500;
    
    private static final int MAX_TEXT_LENGTH = 30;
    
    /**
     * Instantiates model and converter.
     * 
     * @param item the banner item being observed
     */
    public BannerItemModel(final BannerItem item) {
    	this.item = item;
    	index = item.index();
    	width = item.width();
                
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
    	
    	StringUtils.truncateWithEllipsis(setText, MAX_TEXT_LENGTH);
    	
    	text.setValue(setText);
    }
    
    private synchronized void updateColour() {
    	Color colour;
    	AlarmState alarm = item.alarm();
    	
    	if (alarm == AlarmState.MAJOR) {
    		colour = RED;
    	} else if (alarm == AlarmState.MINOR) {
    		colour = ORANGE;
    	} else if (alarm == AlarmState.UNDEFINED || alarm == AlarmState.INVALID) {
    		colour = PURPLE;
    	} else {
    		colour = BLACK;
    	}
    	
    	this.colour.setValue(colour);
    }

    /**
     * @return the index which indicates the order that elements on the banner should be displayed
     */
    public int index() {
        return index;
    }
    
    /**
     * @return the width of the banner item
     */
    public int width() {
    	return Utils.constrainIntToRange(width, MIN_WIDTH, MAX_WIDTH);
    }
    
    /**
     * @return the text of this banner item
     */
	public UpdatedValue<String> text() {
		return text;
	}

	/**
     * @return the colour of this banner item.
     */
	public UpdatedValue<Color> color() {
		return colour;
	}

	/**
     * @return the availability of this banner item.
     */
	public UpdatedValue<Boolean> availability() {
		return availability;
	}

}
