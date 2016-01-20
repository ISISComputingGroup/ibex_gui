
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

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;

/**
 * This is class allows the source observable in ForwardingObservable to be
 * switched. The old observable is closed when it does so.
 */
public class SwitchableObservable<T> extends ForwardingObservable<T> implements Switchable {

    private Switcher switcher;
    private ClosableObservable<T> source;

    public SwitchableObservable(ClosableObservable<T> source) {
        super(source);
        this.source = source;
    }

    @Override
    public Switcher getSwitcher() {
        return this.switcher;
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

    /**
     * Currently only used for testing.
     * 
     * @return The source observable.
     */
    public ClosableObservable<T> getSource() {
        return source;
    }

    /**
     * Set a new source to observe.
     * 
     * There is a type conversion issue here. If the type is wrong then the new
     * source will not be set.
     * 
     * @param newSource
     *            The source to observe
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setSource(Closable newSource) {
        
        ClosableObservable<T> castNewSource;

        try {
            castNewSource = (ClosableObservable<T>) newSource;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return;
        }
        
        super.setSource(castNewSource);
        source.close();
        this.source = castNewSource;
    }


}
