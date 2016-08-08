
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

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItemState;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorColours;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorViewStateConverter;

/**
 * Converts banner item state models for display in GUI.
 */
public class BannerItemViewState implements IndicatorViewStateConverter<BannerItemState> {
    private BannerItem item;
    private BannerItemState state;

    public BannerItemViewState(BannerItem item) {
        this.item = item;
        setState(item.getCurrentState());
    }

    /**
     * Sets the state.
     */
    @Override
    public void setState(BannerItemState state) {
        this.state = state;
    }

    /**
     * Gets the message displayed in the GUI consisting of name and state of the
     * property.
     * 
     * @return the message
     */
    @Override
    public String getName() {
        return item.name().toUpperCase() + " is " + state.message().toUpperCase();
    }

    /**
     * Gets the text colour for the message displayed in the GUI.
     * 
     * @return the colour
     */
    @Override
    public Color color() {
        switch (state.colour()) {
            case "BLACK":
                return IndicatorColours.BLACK;
            case "GREEN":
                return IndicatorColours.GREEN;
            case "RED":
            default:
                return IndicatorColours.RED;
        }
    }

    /**
     * Not used.
     * 
     * @return
     */
    @Override
    public Boolean toBool() {
        return true;
    }

    /**
     * Not used.
     * 
     * @return
     */
    @Override
    public Boolean availability() {
        return true;
    }

}
