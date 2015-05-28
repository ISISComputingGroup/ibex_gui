package uk.ac.stfc.isis.ibex.ui.perspectives.switcher;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Display;

public class FlashingButton implements Runnable {

	private static final Color RED = SWTResourceManager.getColor(255, 0, 0);

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
	
	public void run() {
		Thread thisThread = Thread.currentThread();
		while (flashThread == thisThread) {
			if (flashOn) {
				changeColour(on);
			} else {
				changeColour(off);
			}
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				flashThread = null;
			}

			flashOn = !flashOn;
		}
	}

	private void changeColour(final Color c) {
		display.asyncExec(new Runnable() {
			public void run() {
				button.setBackground(c);
			}
		});
	}
}
