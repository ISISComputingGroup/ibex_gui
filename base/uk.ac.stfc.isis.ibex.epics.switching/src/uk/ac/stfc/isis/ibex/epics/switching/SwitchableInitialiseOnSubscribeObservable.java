
/**
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

package uk.ac.stfc.isis.ibex.epics.switching;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;

/**
 * This is not a permanent new class, just a temporary one while refactoring!
 * This should be merged with InitialiseOnSubscribeObservable once changes are
 * made in the code base to use the new switching method.
 */
public class SwitchableInitialiseOnSubscribeObservable<T> extends InitialiseOnSubscribeObservable<T> implements Switchable {

    private Switcher switcher;
    private ClosableCachingObservable<T> source;

    public SwitchableInitialiseOnSubscribeObservable(ClosableCachingObservable<T> source) {
        super(source);
        this.source = source;
    }

    @Override
    public void setSwitcher(Switcher switcher) {
        this.switcher = switcher;
    }

    @Override
    public void close() {
        if (switcher != null) {
            switcher.unregsiterSwitchable(this);
        }
        source.close();
        super.close();
    }

    public ClosableCachingObservable<T> getSource() {
        return source;
    }

    // TODO: What can we do about the type conversion issue here?
    @SuppressWarnings("unchecked")
    @Override
    public void setSource(Closable newSource) {
        
        ClosableCachingObservable<T> sourceySauce;

        try {
            sourceySauce = (ClosableCachingObservable<T>) newSource;
        } catch (ClassCastException e) {
            // Return - something has gone wrong!
            return;
        }
        
        super.setSource(sourceySauce);
        source.close();
        this.source = sourceySauce;
    }

    @Override
    public Switcher getSwitcher() {
        return this.switcher;
    }

}
