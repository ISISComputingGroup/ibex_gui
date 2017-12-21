
/*
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

package uk.ac.stfc.isis.ibex.instrument;

/**
 * This interface allows plugins to respond to switches of instrument. 
 * It is broken into three parts the preSet - called on
 * all instruments first, Set - called on all instruments and postSet - called
 * on all instruments last. This is the interface for the instrument switching
 * extension point.
 */
public interface InstrumentInfoReceiver {

    /**
     * Sets the instrument.
     *
     * @param instrument the new instrument that is being switched to
     */
    void setInstrument(InstrumentInfo instrument);

    /**
     * Called before an instrument set is called. This can be used for closing
     * resources used by the current instrument.
     *
     * @param instrument the instrument that will be changed to
     */
    void preSetInstrument(InstrumentInfo instrument);

    /**
     * Called after an instrument set is called. This can be used for tidying up
     * after an instrument change.
     *
     * @param instrument the instrument swicthed to
     */
    void postSetInstrument(InstrumentInfo instrument);

}
