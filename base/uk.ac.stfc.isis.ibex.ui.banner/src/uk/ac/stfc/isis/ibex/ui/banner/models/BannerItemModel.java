
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

import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItemState;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;

/**
 * Model containing banner item status as updated values used for display in
 * GUI.
 */
public class BannerItemModel extends ModelObject implements IndicatorModel {

    private SettableUpdatedValue<String> text;
    private SettableUpdatedValue<Color> color;
    private SettableUpdatedValue<Boolean> availability;
    private BannerItemViewState converter;

    /**
     * Instantiates model and converter.
     * 
     * @param item the banner item being observed
     */
    public BannerItemModel(BannerItem item) {
        converter = new BannerItemViewState(item);
        text = new SettableUpdatedValue<>();
        color = new SettableUpdatedValue<>();
        availability = new SettableUpdatedValue<>();
        item.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("currentState")) {
                    BannerItemState newstate = (BannerItemState) evt.getNewValue();
                    converter.setState(newstate);
                    update();
                }
            }
        });

        update();
    }

    /**
     * Updates the display parameters of the status message in the GUI upon
     * state change of the banner item.
     */
    public void update() {
        text.setValue(converter.getName());
        color.setValue(converter.color());
        availability.setValue(converter.toBool());
    }

    @Override
    public UpdatedValue<String> text() {
        return text;
    }

    @Override
    public UpdatedValue<Color> color() {
        return color;
    }

    @Override
    public UpdatedValue<Boolean> availability() {
        return availability;
    }
}
