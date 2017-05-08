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
package uk.ac.stfc.isis.ibex.dae.detectordiagnostics;

import uk.ac.stfc.isis.ibex.epics.observing.Observer;

/**
 * An implementation of an observer that ignores nulls and prints stack traces on error.
 * 
 * @param <T> the type to observe
 */
public abstract class SpectrumObserver<T> implements Observer<T> {
    
    /**
     * This method is called when a non-null value is available.
     *
     * @param value the value
     */
    public abstract void onNonNullValue(T value);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onValue(T value) {
        if (value != null) {
            onNonNullValue(value);
        } 
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onError(Exception e) {
        System.out.println("error!");
        e.printStackTrace();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConnectionStatus(boolean isConnected) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(T value, Exception error, boolean isConnected) {
        if (value != null) {
            onNonNullValue(value);
        }
        
        if (error != null) {
            onError(error);
        }
        
        onConnectionStatus(isConnected);
    }  
}