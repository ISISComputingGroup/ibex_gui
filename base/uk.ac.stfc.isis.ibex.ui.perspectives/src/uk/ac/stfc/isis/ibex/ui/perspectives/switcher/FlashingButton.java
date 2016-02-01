
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

package uk.ac.stfc.isis.ibex.ui.perspectives.switcher;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

public class FlashingButton implements Runnable {

	private static final Color RED = SWTResourceManager.getColor(255, 0, 0);
	private static final int SLEEP_TIME = 800;
	
	private final CLabel button;
	private boolean flashOn;
	private final Display display;
	private Thread flashThread;

	private Color off;
	private Color on;
	
	public FlashingButton(final CLabel button, Display display) {
		this.button = button;
		this.display = display;
		flashOn = true;
		
		on = RED;
	}

	public void start() {
		if (null == flashThread) {
			flashThread = new Thread(this);
			flashThread.start();
		}
	}

	public void stop() {
		flashThread = null;
		button.setBackground(off);
	}

	public void setDefaultColour(Color background) {
		this.off = background;
	}

	public void setFlashedColour(Color foreground) {
		this.on = foreground;
	}
	
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

	private void changeColour(final Color c) {
		display.asyncExec(new Runnable() {
			@Override
            public void run() {
				button.setBackground(c);
			}
		});
	}

    public void setToOffColour() {
        changeColour(off);
    }

    public void setToOnColour() {
        changeColour(on);
    }
}
