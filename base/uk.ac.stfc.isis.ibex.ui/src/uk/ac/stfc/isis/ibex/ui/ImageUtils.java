package uk.ac.stfc.isis.ibex.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageUtils {
    public static Image disabled(final Image source) {
    	return new Image(Display.getDefault(), source, SWT.IMAGE_DISABLE);
    }
}
