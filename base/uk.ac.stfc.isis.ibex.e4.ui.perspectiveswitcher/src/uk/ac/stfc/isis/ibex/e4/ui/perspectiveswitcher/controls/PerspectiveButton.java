package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
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

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

public class PerspectiveButton extends CLabel {
	
	protected static final Color FOCUSSED = SWTResourceManager.getColor(220, 235, 245);
	protected static final Color DEFOCUSSED = SWTResourceManager.getColor(247, 245, 245);
    protected static final Color ACTIVE = SWTResourceManager.getColor(120, 170, 210);

    private static final Font BUTTON_FONT = SWTResourceManager.getFont("Arial", 12, SWT.NORMAL);
    private static final Font ACTIVE_FONT = SWTResourceManager.getFont("Arial", 12, SWT.BOLD);
    
    private final MPerspective perspective;
    private final PerspectivesProvider perspectivesProvider;

	public PerspectiveButton(Composite parent, MPerspective perspective, PerspectivesProvider perspectivesProvider) {
		super(parent, SWT.SHADOW_OUT);
		
		this.perspective = perspective;
		this.perspectivesProvider = perspectivesProvider;
		
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		setFont(BUTTON_FONT);
		setText(perspective.getLabel());
		setToolTipText(perspective.getTooltip());
		setImage(ResourceManager.getPluginImageFromUri(perspective.getIconURI()));

		addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseDown(MouseEvent e) {
				perspectivesProvider.getPartService().switchPerspective(perspective);
				mouseClickAction();
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				mouseDown(e);
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
		
        setBackground(DEFOCUSSED);
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
        if (setColourToUse() == DEFOCUSSED) {
            setBackground(FOCUSSED);
        } else {
            setBackground(setColourToUse());
        }
	}
	
    /**
     * Set the background colour of the button when the mouse exits it.
     */
	protected void mouseExitAction() {
        setBackground(setColourToUse());
	}

    /**
     * The background of the button is dependent on whether it is the active
     * perspective or not. The font is updated as appropriate.
     * 
     * @return colour to use for the background
     */
    protected Color setColourToUse() {
        
        Color toUse = DEFOCUSSED;
        setFont(BUTTON_FONT);
        if (perspectivesProvider.isSelected(perspective)) {
            toUse = ACTIVE;
            setFont(ACTIVE_FONT);
        }
        return toUse;
    }
}
