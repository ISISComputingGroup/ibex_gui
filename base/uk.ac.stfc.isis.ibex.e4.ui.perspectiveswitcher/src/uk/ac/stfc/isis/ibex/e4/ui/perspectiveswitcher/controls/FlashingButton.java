
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

package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;

/**
 * Where attention is needed, a flashing button should help to draw attention.
 */
public class FlashingButton implements Runnable, DisposeListener {

    private static final int SLEEP_TIME = 800;

    private final ButtonViewModel model;
    private boolean flashOn;
    // private final Display display;
    private Thread flashThread;

    private Color off;
    private Color on;

    /**
     * Set up the flashing button.
     * 
     * @param model
     *            the model for the button that will flash
     * @param color
     * 			  the color for the button to show when flashing
     */
    public FlashingButton(final ButtonViewModel model, Color color) {
        this.model = model;
        // this.display = display;
        flashOn = true;

        on = color;
    }

    /**
     * Start the button flashing.
     */
    public void start() {
        if (null == flashThread) {
            flashThread = new Thread(this, "FlashingButton thread");
            flashThread.start();
        }
    }

    /**
     * Stop the button flashing.
     */
    public void stop() {
        flashThread = null;
        model.setColor(off);
    }

    /**
     * Set the background colour.
     * 
     * @param background
     *            the colour to use by default.
     */
    public void setDefaultColour(Color background) {
        this.off = background;
    }

    /**
     * Overwrite the on colour.
     * 
     * @param foreground
     *            the alternate 'on' colour
     */
    public void setFlashedColour(Color foreground) {
        this.on = foreground;
    }

    /**
     * @return if the flash is on
     */
    public boolean isFlashOn() {
        return flashOn;
    }

    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        while (flashThread == thisThread) {
            if (flashOn) {
                changeColour(on);
            } else {
                changeColour(off);
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                flashThread = null;
            }

            flashOn = !flashOn;
        }
    }

    /**
     * @param c
     *            the colour to set
     */
    private void changeColour(final Color c) {
        // display.asyncExec(new Runnable() {
        // @Override
        // public void run() {
        if (!model.inFocus) {
            model.setColor(c);
        }
        // }
        // });
    }

    /**
     * Widget disposal listener so button can stop flashing.
     * 
     * @param event
     */
    @Override
    public void widgetDisposed(DisposeEvent event) {
        stop();
    }
}
