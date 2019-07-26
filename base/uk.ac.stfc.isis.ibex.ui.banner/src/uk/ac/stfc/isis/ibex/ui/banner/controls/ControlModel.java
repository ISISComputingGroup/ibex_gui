
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

package uk.ac.stfc.isis.ibex.ui.banner.controls;

import org.eclipse.swt.graphics.RGB;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * Interface for Control Models.
 */
public interface ControlModel {
    /**
     * Action when the button is clicked.
     */
	void click();
	/**
     * @return the index which indicates the order that elements on the banner should be displayed
     */
    int index();
	/**
	 * @return the button's text
	 */
	String text();
	/**
     * @return an UpdatedValue Boolean of whether the button is enabled
     */
	UpdatedValue<Boolean> enabled();
	/**
     * @return the RGB colour of the text
     */
	RGB textColour();
	/**
     * @return the RGB colour of the button
     */
    RGB buttonColour();
    /**
     * @return the font size
     */
    int fontSize();
    /**
     * @return the width of the button
     */
    int width();
    /**
     * @return the height of the button
     */
    int height();
}
