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

public abstract class SpectrumObserver<T> implements Observer<T> {
    
    public abstract void onNonNullValue(T value);
    
    @Override
    public void onValue(T value) {
        if (value != null) {
            onNonNullValue(value);
        } 
    }
    
    @Override
    public void onError(Exception e) {
        System.out.println("error!");
        e.printStackTrace();
    }

    @Override
    public void onConnectionStatus(boolean isConnected) {
    }

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