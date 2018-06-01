package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;

public abstract class Button extends CLabel {
	
	protected ButtonViewModel model = new ButtonViewModel();

	public Button(Composite parent, String text, String imageUri, String tooltip) {
		super(parent, SWT.SHADOW_OUT);
		
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		setText(text);
		setToolTipText(tooltip);
		setImage(ResourceManager.getPluginImageFromUri(imageUri));

		addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseDown(MouseEvent e) {
				mouseClickAction();
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				mouseClickAction();
			}
		});

		addMouseTrackListener(new MouseTrackAdapter() {			
			@Override
			public void mouseExit(MouseEvent e) {
                mouseExitAction();
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				mouseEnterAction();
			}
        });
	}
	
    /**
     * Things to do when the mouse is clicked.
     */
	protected void mouseClickAction() {
	}
	
    /**
     * Set the background colour of the button when the mouse enters it.
     */
	protected void mouseEnterAction() {
		model.setFocus(true);
	}
	
    /**
     * Set the background colour of the button when the mouse exits it.
     */
	protected void mouseExitAction() {
		model.setFocus(false);
	}
}
