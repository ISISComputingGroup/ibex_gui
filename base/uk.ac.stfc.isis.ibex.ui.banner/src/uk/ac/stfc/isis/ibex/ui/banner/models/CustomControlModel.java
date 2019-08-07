
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.swt.graphics.RGB;

import uk.ac.stfc.isis.ibex.banner.ObservableCustomControl;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerButton;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * The model for custom buttons in the banner.
 */
public class CustomControlModel extends ModelObject {

    private final int index;
    private final ObservableCustomControl control;
    private final String text;
    private final RGB textColour;
    private final RGB buttonColour;
    private final int fontSize;
    private final int width;
    private final int height;
    
    private static final int MIN_FONT = 0;
    private static final int MAX_FONT = 16;
    private static final int MIN_WIDTH = 10;
    private static final int MAX_WIDTH = 1000;
    private static final int MIN_HEIGHT = 10;
    private static final int MAX_HEIGHT = 35;
    
    /**
     * TThe model for custom buttons in the banner.
     * 
     * @param button
     *                     A BannerButton class containing information about the custom button
     */
    public CustomControlModel(BannerButton button) {
        control = new ObservableCustomControl(button.writable(), button.valueToWrite());
        index = button.index();
        text = button.name();
        textColour = button.textColour();
        buttonColour = button.buttonColour();
        fontSize = button.fontSize();
        width = button.width();
        height = button.height();
    }
    
    /**
     * Action when the button is clicked.
     */
    public void click() {
        control.act();
    }
    
    /**
     * @return the index which indicates the order that elements on the banner should be displayed
     */
    public int index() {
        return index;
    }

    /**
     * @return the button's text
     */
    public String text() {
        return text;
    }

    /**
     * @return an UpdatedValue Boolean of whether the button is enabled
     */
    public UpdatedValue<Boolean> enabled() {
        return control.canWrite();
    }
    
    /**
     * @return the RGB colour of the text
     */
    public RGB textColour() {
        return textColour;
    }

    /**
     * @return the RGB colour of the button
     */
    public RGB buttonColour() {
        return buttonColour;
    }
    
    /**
     * @return the font size
     */
    public int fontSize() {
        if (fontSize < MIN_FONT) {
            return MIN_FONT;
        } else if (fontSize > MAX_FONT) {
            return MAX_FONT;
        } else {
            return fontSize;
        }
    }

    /**
     * @return the width of the button
     */
    public int width() {
        if (width < MIN_WIDTH) {
            return MIN_WIDTH;
        } else if (width > MAX_WIDTH) {
            return MAX_WIDTH;
        } else {
            return width;
        }
    }

    /**
     * @return the height of the button
     */
    public int height() {
        if (height < MIN_HEIGHT) {
            return MIN_HEIGHT;
        } else if (height > MAX_HEIGHT) {
            return MAX_HEIGHT;
        } else {
            return height;
        }
    }

}
