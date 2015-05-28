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

/*
 * A composite with a vertical gradient as a background
 */
public class VerticalGradientComposite extends Composite {

	private static final Display DISPLAY = Display.getCurrent();

	private Color top = new Color(DISPLAY, 220, 220, 220);
	private Color bottom = DISPLAY.getSystemColor(SWT.COLOR_WHITE);
	
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
        Image newImage = new Image(DISPLAY, 1, Math.max(1, rect.height));
        
        GC gc = new GC(newImage);
        gc.setForeground(top);
        gc.setBackground(bottom);
        gc.fillGradientRectangle(rect.x, rect.y, 1, rect.height, true);
        gc.dispose();
        
		return newImage;
	}
}
