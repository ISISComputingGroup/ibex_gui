
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

package uk.ac.stfc.isis.ibex.ui.alarm;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;
import uk.ac.stfc.isis.ibex.ui.PerspectiveReopener;

/**
 * The class that deals with the instrument being switched.
 * 
 */
public class AlarmsInstrumentInfoReceiver implements InstrumentInfoReceiver {
    
    /**
     * Allow the alarm perspective to be closed and reopened.
     */
    private PerspectiveReopener alarmPerspectiveReopener;
		
    
    /**
     * Instantiates a new alarms; call getInstance instead in most cases.
     */
    public AlarmsInstrumentInfoReceiver() {
        alarmPerspectiveReopener = new PerspectiveReopener(AlarmPerspective.ID);
    }

    /**
     * On change of instrument there is nothing to do for this perspective. The
     * settings for the alarm server are changed by the alarm model.
     * 
     * @param instrument to set
     */
    @Override
    public void setInstrument(InstrumentInfo instrument) {
        // nothing to do on set instrument
    }


    /**
     * Before the instrument changes close the alarm perspective so that it
     * releases its alarm model. Remember that it was closed so it can be
     * reopened after the instrument has been switched.
     * 
     * @param instrument instrument to switch to
     */
    @Override
    public void preSetInstrument(InstrumentInfo instrument) {
        alarmPerspectiveReopener.closePerspective();
    }


    /**
     * After the instrument has switched reopen the alarm perspective if it was
     * open before switching.
     * 
     * @param instrument instrument to switch to
     */
    @Override
    public void postSetInstrument(InstrumentInfo instrument) {
        alarmPerspectiveReopener.reopenPerspective();
    }
}
