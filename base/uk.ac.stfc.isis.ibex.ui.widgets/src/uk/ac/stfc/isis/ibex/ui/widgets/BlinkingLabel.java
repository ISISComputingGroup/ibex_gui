
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

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class BlinkingLabel extends Label {

	private static final Color COLOUR_ON = SWTResourceManager.getColor(SWT.COLOR_RED);
	private static final Color COLOUR_OFF = SWTResourceManager.getColor(SWT.COLOR_BLACK);
	private static final long DEFAULT_DELAY = 700;
	
	private final Display display = Display.getCurrent();

	private long delay;
	private Color blinked;
	private Color unblinked;
	
	private Timer timer;
	private boolean isBlinked;
	private boolean isBlinking = true;

	private boolean blinkBackground = false;
	
	private TimerTask blink = new TimerTask() {
		@Override
		public void run() {
			display.asyncExec(new Runnable() {	
				@Override
				public void run() {
					setBlinked(!isBlinked);		
				}
			});
		}
	};

	public BlinkingLabel(Composite parent, int style, Color blinked, Color unblinked, long delay) {
		super(parent, clearBits(style, SWT.BACKGROUND | SWT.FOREGROUND));		
		blinkBackground = isBitSet(style, SWT.BACKGROUND) && !isBitSet(style, SWT.FOREGROUND);
	
		this.blinked = blinked;
		this.unblinked = unblinked;
		this.delay = delay;
		
		start();
	}
	
	public BlinkingLabel(Composite parent, int style) {
		this(parent, style, COLOUR_ON, COLOUR_OFF, DEFAULT_DELAY);		
	}
	
	public void setBlinking(boolean shouldBlink) {
		if (isBlinking == shouldBlink) {
			return;
		}
		
		if (shouldBlink) {
			start();
		} else {
			stop();
		}	
	}
	
	@Override
	protected void checkSubclass() {
		// Do nothing, allow sub-classing.
	}
	
	private void start() {
		timer = new Timer();
		timer.scheduleAtFixedRate(blink, delay, delay);
		isBlinking = true;
	}

	private void stop() {
		timer.cancel();
		isBlinking = false;
	}
	
	private void setBlinked(boolean isBlinked) {
		this.isBlinked = isBlinked;
		Color color = isBlinked ? blinked : unblinked;
		
		if (blinkBackground) {
			setBackground(color);
		} else {
			setForeground(color);			
		}
	}
	
    private static boolean isBitSet(int value, int bits) {
    	return (value & bits) == bits;
    }
	
    private static int clearBits(int value, int bits) {
    	return value & ~bits;
    }
}
