
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
package uk.ac.stfc.isis.ibex.devicescreens.tests;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;

/**
 * Observable to be used for testing, allows access to setValue, setError and
 * setConnectionChanged methods.
 * 
 * This is final, so no mocking this, or using it outside testing!
 * 
 * @param <T> the type of the source observable
 */
public class TestableSwitchableObservable<T> extends SwitchableObservable<T> {

    /**
     * Creates an instance of the observable.
     * 
     * @param source the source observable.
     */
    public TestableSwitchableObservable(ClosableObservable<T> source) {
        super(source);
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
    }

    @Override
    public void setError(Exception e) {
        super.setError(e);
    }

    @Override
    public void setConnectionStatus(boolean isConnected) {
        super.setConnectionStatus(isConnected);
    }
}
