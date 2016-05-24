
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

package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * A composite with a vertical gradient as a background.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class VerticalGradientComposite extends Composite {

	private static final Display DISPLAY = Display.getCurrent();

    private Color top = SWTResourceManager.getColor(220, 220, 220);
	private Color bottom = DISPLAY.getSystemColor(SWT.COLOR_WHITE);
    private Image gradientImage;
	
	public VerticalGradientComposite(Composite parent, int style) {
		super(parent, style);
		
		addListener(SWT.Resize, new Listener() {		
			@Override
			public void handleEvent(Event event) {
		        setBackgroundImage(gradient());				
			}
		});
	}
	
	public VerticalGradientComposite(Composite parent, int style, Color top, Color bottom) {
		this(parent, style);
		this.top = top;
		this.bottom = bottom;
	}

	private Image gradient() {
		Rectangle rect = getClientArea();
        if (gradientImage != null) {
            gradientImage.dispose();
        }
        gradientImage = new Image(DISPLAY, 1, Math.max(1, rect.height));
        
        GC gc = new GC(gradientImage);
        gc.setForeground(top);
        gc.setBackground(bottom);
        gc.fillGradientRectangle(rect.x, rect.y, 1, rect.height, true);
        gc.dispose();
        
		return gradientImage;
	}

    @Override
    public void dispose() {
        super.dispose();
        gradientImage.dispose();
    }
}
