package uk.ac.stfc.isis.ibex.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Utilities for SWT images.
 */
public final class ImageUtils {
    
    private ImageUtils() {
	
    }
    
    /**
     * Gives an image a "disabled" look by greying it out.
     * 
     * @param source the source image
     * @return the image with a disabled look
     */
    public static Image disabled(final Image source) {
	return new Image(Display.getDefault(), source, SWT.IMAGE_DISABLE);
    }
}
