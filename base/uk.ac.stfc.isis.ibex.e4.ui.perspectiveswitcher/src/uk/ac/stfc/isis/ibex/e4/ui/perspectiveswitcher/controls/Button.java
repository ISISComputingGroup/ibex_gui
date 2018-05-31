package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

public abstract class Button extends CLabel {
	
	protected static final Color FOCUSSED = SWTResourceManager.getColor(220, 235, 245);
	protected static final Color DEFOCUSSED = SWTResourceManager.getColor(247, 245, 245);
    protected static final Color ACTIVE = SWTResourceManager.getColor(120, 170, 210);

    private static final Font BUTTON_FONT = SWTResourceManager.getFont("Arial", 12, SWT.NORMAL);
    private static final Font ACTIVE_FONT = SWTResourceManager.getFont("Arial", 12, SWT.BOLD);

	public Button(Composite parent, String text, String imageUri, String tooltip) {
		super(parent, SWT.SHADOW_OUT);
		
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		setText(text);
		setToolTipText(tooltip);
		setImage(ResourceManager.getPluginImageFromUri(imageUri));

		setFont(BUTTON_FONT);
        setBackground(DEFOCUSSED);

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
		setBackground(ACTIVE);
		setFont(ACTIVE_FONT);
	}
	
    /**
     * Set the background colour of the button when the mouse enters it.
     */
	protected void mouseEnterAction() {
		if (getBackground()!=ACTIVE) {
			setBackground(FOCUSSED);
		}
	}
	
    /**
     * Set the background colour of the button when the mouse exits it.
     */
	protected void mouseExitAction() {
		if (getBackground()!=ACTIVE) {
			setBackground(DEFOCUSSED);
		}
	}
}
