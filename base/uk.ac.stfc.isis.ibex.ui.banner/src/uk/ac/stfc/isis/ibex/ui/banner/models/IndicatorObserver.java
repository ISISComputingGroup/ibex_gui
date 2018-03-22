
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

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * This is an observer to support indicators.
 * @param <T> The data type being observed.
 */
public abstract class IndicatorObserver<T> implements Closable {
    private Subscription subscription;

    private final Observer<T> observer = new BaseObserver<T>() {
        @Override
        public void onValue(T value) {
            setSimMode(value);
        }

        @Override
        public void onError(Exception e) {
            setUnknown();
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                setUnknown();
            }
        }
    };

    protected final SettableUpdatedValue<String> text;
    protected final SettableUpdatedValue<Color> color;
    protected final SettableUpdatedValue<Boolean> availability;

    /**
     * Constructor for a dae simulation mode observer.
     * @param observable the observable to use
     */
    public IndicatorObserver(ForwardingObservable<T> observable) {
        text = new SettableUpdatedValue<>();
        color = new SettableUpdatedValue<>();
        availability = new SettableUpdatedValue<>();
        
        subscription = observable.addObserver(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        subscription.removeObserver();
    }

    /**
     * Called when the dae simulation mode PV updates.
     * @param value the new status of dae simulation mode
     */
    protected abstract void setSimMode(T value);

    /**
     * Called when the dae simulation mode PV becomes disconnected.
     */
    protected abstract void setUnknown();

    /** 
     * The updated value for the text.
     * @return the updated value for the text
     */
    public UpdatedValue<String> text() {
        return text;
    }

    /**
     * The updated value for the color.
     * @return the updated value for the color
     */
    public UpdatedValue<Color> color() {
        return color;
    }

    /**
     * The updated value for the availability.
     * @return the updated value for the availability
     */
    public UpdatedValue<Boolean> availability() {
        return availability;
    }

}
