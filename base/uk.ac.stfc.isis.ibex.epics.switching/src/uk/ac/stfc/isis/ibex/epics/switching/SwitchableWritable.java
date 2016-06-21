
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

import uk.ac.stfc.isis.ibex.epics.conversion.DoNothingConverter;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.ForwardingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * This class provides a writable with a source that can be change. Primarily
 * this would be used for writing to a PV that changes on instrument switch.
 * 
 * @param <T>
 */
public class SwitchableWritable<T> extends ForwardingWritable<T, T> implements Switchable {

    private Switcher switcher;
    private Writable<T> source;

    public SwitchableWritable(Writable<T> source) {
        super(source, new DoNothingConverter<T>());
        this.source = source;
    }

    @Override
    public void setSwitcher(Switcher switcher) {
        this.switcher = switcher;
    }

    @Override
    public Switcher getSwitcher() {
        return switcher;
    }

    @Override
    public void close() {
        if (switcher != null) {
            switcher.unregsiterSwitchable(this);
        }
        source.close();
        super.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setSource(Closable newSource) {

        Writable<T> castNewSource;

        try {
            castNewSource = (Writable<T>) newSource;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return;
        }

        super.setWritable(castNewSource);
        this.source = castNewSource;
    }

    /**
     * Just used for testing.
     * 
     * @return The source writable.
     */
    public Writable<T> getSource() {
        return source;
    }

}
