
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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

package uk.ac.stfc.isis.ibex.configserver.configuration;

import java.awt.Color;

import org.eclipse.swt.graphics.RGB;

import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.LongChannel;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Instrument specific button displayed in the banner.
 */
public class BannerButton extends ModelObject {
    private String name;
    private String pv;
    private Boolean local = true;
    private Long pvValue;
    private String textColour;
    private String buttonColour;
    private int width;
    private int height;
    
    private Writable<Long> pvWritable;
    
    /**
     * Creates a writable for the PV that this button writes to.
     */
    public void createPVWritable() {
        String fullPv = pv;
        if (local) {
            fullPv = InstrumentUtils.addPrefix(pv);
        }
        pvWritable = new WritableFactory(OnInstrumentSwitch.CLOSE).getSwitchableWritable(new LongChannel(), fullPv);
    }
    
    /**
     * @return the writable for the button's PV
     */
    public Writable<Long> writable() {
        return pvWritable;
    }
    
    /**
     * @return the name of the button
     */
    public String name() {
        return name;
    }
    
    /**
     * @return the full address of the PV that this button writes to
     */
    public String pv() {
        if (local) {
            return InstrumentUtils.addPrefix(pv);
        } else {
            return pv;
        }
    }
    
    /**
     * @return the Long value which this button writes to the PV
     */
    public Long valueToWrite() {
        return pvValue;
    }
    
    /**
     * @return the RGB colour of the text in the button
     */
    public RGB textColour() {
        Color c = Color.decode(textColour);
        return new RGB(c.getRed(), c.getGreen(), c.getBlue());
    }
    
    /**
     * @return the RGB colour of the button
     */
    public RGB buttonColour() {
        Color c = Color.decode(buttonColour);
        return new RGB(c.getRed(), c.getGreen(), c.getBlue());
    }
    
    /**
     * @return the width of the button
     */
    public int width() {
        return width;
    }
    
    /**
     * @return the height of the button
     */
    public int height() {
        return height;
    }
}
