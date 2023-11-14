package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/**
 * A factory pattern for creating commonly used buttons.
 */
public class IBEXButtonFactory {
	
	/**
	 * Creates a button that fills all available horizontal space.
	 * 
	 * @param parent a composite control which will be the parent of the new instance (cannot be null)
	 * @param text the title of the button
	 * @param tooltip the tooltip of the button (can be null)
	 * @param image the image of the button displayed before the title (can be null)
	 * @param onClickListener the event handler for clicks
	 * @return a new button instance
	 */
	static public Button expanding(Composite parent, String text, String tooltip, Image image, Listener onClickListener) {
		int style = SWT.NONE;
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		return create(parent, style, text, tooltip, image, onClickListener, layoutData);
	}
	
	/**
	 * Creates a button takes up only as much horizontal space as its content needs.
	 *
	 * @param parent a composite control which will be the parent of the new instance (cannot be null)
	 * @param text the title of the button
	 * @param tooltip the tooltip of the button (can be null)
	 * @param image the image of the button displayed before the title (can be null)
	 * @param onClickListener the event handler for clicks
	 * @return a new button instance
	 */
	static public Button compact(Composite parent, String text, String tooltip, Image image, Listener onClickListener) {
		int style = SWT.NONE;
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, false, false);

		return create(parent, style, text, tooltip, image, onClickListener, layoutData);
	}
	
	/**
     * Creates a button using the given parameters.
     * 
     * @param parent the Composite into which the button is placed
     * @param style 
     * @param text the title of the button
     * @param tooltip the tooltip string for the button (or null)
     * @param image the image icon of the button
     * @param clickConsumer the action that happens when button is clicked (click event is propagated)
     * @param layoutData the layout data for the button
     * @return the new button instance
     */
    static public Button create(Composite parent, int style, String text, String tooltip, Image image, Listener onClickListener, Object layoutData) {
    	Button btn = new Button(parent, SWT.NONE);
        if (text != null)
        	btn.setText(text);
		btn.setToolTipText(tooltip);
		if (image != null)
        	btn.setImage(image);
        if (onClickListener != null)
        	btn.addListener(SWT.Selection, onClickListener);
        btn.setLayoutData(layoutData);
		return btn;
    }
}
