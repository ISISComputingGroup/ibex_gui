
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

import java.util.Objects;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItemState;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorColours;

/**
 * Converts banner item state models for display in GUI.
 */
public class BannerItemViewState {
    private BannerItem item;
    private BannerItemState state;

    public BannerItemViewState(BannerItem item) {
        this.item = Objects.requireNonNull(item);
        setState(item.getCurrentState());
    }

    /**
     * Sets the state.
     * 
     * @param state
     *            The state to set the banner item to.
     */
    public void setState(BannerItemState state) {
    	System.out.println("Setting state");
        this.state = Objects.requireNonNull(state);
        System.out.println(state.colour() + " " + state.message());
    }

    /**
     * Gets the message displayed in the GUI consisting of name and state of the
     * property.
     * 
     * @return the message
     */
    public String getMessage() {
        return item.name() + ": " + state.message();
    }

    /**
     * Gets the text colour for the message displayed in the GUI.
     * 
     * @return the colour
     */
    public Color color() {
        switch (state.colour()) {
            case "BLACK":
                return IndicatorColours.BLACK;
            case "GREEN":
                return IndicatorColours.GREEN;
            case "RED":
                return IndicatorColours.RED;
            default:
                return IndicatorColours.DEFAULT;
        }
    }
}
