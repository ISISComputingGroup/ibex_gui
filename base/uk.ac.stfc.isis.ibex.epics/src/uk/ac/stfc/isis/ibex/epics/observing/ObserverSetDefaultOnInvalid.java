
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2020
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

package uk.ac.stfc.isis.ibex.epics.observing;

import java.util.function.Consumer;

/**
 * Creates an observer that will call the provided lambda with it's new value if it gets a valid value.
 * 
 * Otherwise will call the lambda with the provided default value.
 * 
 * @param <T> the generic type that the observer is observing
 */
public class ObserverSetDefaultOnInvalid<T> extends BaseObserver<T> {

    Consumer<T> consumer;
    T defaultArg;
    
    /**
     * Create the observer.
     * @param consumer A lambda to call when the observable changes.
     * @param defaultArg The argument to send to the lambda when an invalid state occurs.
     */
    public ObserverSetDefaultOnInvalid(Consumer<T> consumer, T defaultArg) {
        this.consumer = consumer;
        this.defaultArg = defaultArg;
    }

    @Override
    public void onValue(T value) {
        consumer.accept(value);
    }

    @Override
    public void onError(Exception e) {
        consumer.accept(defaultArg);
    }

    @Override
    public void onConnectionStatus(boolean isConnected) {
        if (!isConnected) {
            consumer.accept(defaultArg);
        }
    }
}
