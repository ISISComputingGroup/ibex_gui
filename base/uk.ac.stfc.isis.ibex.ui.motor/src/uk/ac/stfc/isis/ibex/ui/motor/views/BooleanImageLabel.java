
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

package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

/**
 * A label that toggles it's image between two possible states.
 */
public class BooleanImageLabel {

    private final Label label;
	private final Image enabledImage;
	private final Image disabledImage;
	
	private final String enabledTooltip;
	private final String disabledTooltip;
	
    /**
     * Creates a label that holds an image that can be greyed out.
     * 
     * @param parent
     *            The parent composite for the label.
     * @param enabledImage
     *            The image to show when enabled
     * @param disabledImage
     *            The image to show when disabled
     * @param enabledTooltip
     *            The tooltip to display when enabled
     * @param disabledTooltip
     * 			  The tooptip to display when disabled
     */
    public BooleanImageLabel(final Composite parent, Image enabledImage, Image disabledImage, String enabledTooltip, String disabledTooltip) {
        label = new Label(parent, SWT.NONE);
        label.setAlignment(SWT.CENTER);
        label.setImage(enabledImage);
		this.enabledImage = enabledImage;
		this.disabledImage = disabledImage;
		
		this.enabledTooltip = enabledTooltip;
		this.disabledTooltip = disabledTooltip;

        // Add a listener so that clicking on this image does the same thing as
        // clicking on the parent container.
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                Event event = new Event();
                event.widget = parent;

                parent.notifyListeners(SWT.MouseDown, event);
            }
        });
	}
	
    /**
     * Enable the image.
     */
    public void enable() {
        label.setImage(enabledImage);
        label.setToolTipText(enabledTooltip);
	}
	
    /**
     * Disable the image.
     */
    public void disable() {
        label.setImage(disabledImage);
        label.setToolTipText(disabledTooltip);
	}
	
    /**
     * Enable or disable the image.
     * 
     * @param enable
     *            True to enable the image. False to disable.
     */
    public void setIsEnabled(Boolean enable) {
    	if (enable == null) {
    		disable();
    	} else if (enable) {
            enable();
        } else {
            disable();
        }
	}

    /**
     * Sets the background for the image.
     * 
     * @param color
     *            The color of background to set.
     */
    public void setBackground(Color color) {
        label.setBackground(color);
    }
}
