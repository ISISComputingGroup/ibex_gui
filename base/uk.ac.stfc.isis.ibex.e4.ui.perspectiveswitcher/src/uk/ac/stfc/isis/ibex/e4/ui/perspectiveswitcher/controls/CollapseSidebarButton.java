package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * Class for the button to collapse or expand the perspective switcher.
 */
public class CollapseSidebarButton extends CLabel {
    
    private boolean collapsed = false;
    
    private static final Image COLLAPSE_SIDEBAR_IMG =
            ResourceManager.getPluginImageFromUri("platform:/plugin/uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher/icons/collapse.png");
    private static final Image EXPAND_SIDEBAR_IMG =
            ResourceManager.getPluginImageFromUri("platform:/plugin/uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher/icons/expand.png");
    
    private static final Color FOCUSSED = SWTResourceManager.getColor(210, 210, 210);
    private static final Color DEFOCUSSED = SWTResourceManager.getColor(225, 225, 225);

    /**
     * Create a button which collapses and expands the perspective switcher.
     * @param parent the parent
     */
    public CollapseSidebarButton(Composite parent) {
        super(parent, SWT.SHADOW_NONE);
        setImage(COLLAPSE_SIDEBAR_IMG);
        setBackground(DEFOCUSSED);
        
        addMouseTrackListener(new MouseTrackAdapter() {
            @Override
            public void mouseEnter(MouseEvent e) {
                setBackground(FOCUSSED);
            }
            @Override
            public void mouseExit(MouseEvent e) {
                setBackground(DEFOCUSSED);
            }
        });
        
        GridData gd = new GridData(SWT.RIGHT, SWT.FILL, false, true);
        
        setAlignment(SWT.CENTER);
        setLayoutData(gd);
    }

    /**
     * @return if the side bar is collapsed
     */
    public boolean isCollapsed() {
        return collapsed;
    }

    /**
     * Set the button to collapsed mode.
     */
    public void collapse() {
        setImage(EXPAND_SIDEBAR_IMG);
        collapsed = true;
    }

    /**
     * Set the button to expanded mode.
     */
    public void expand() {
        setImage(COLLAPSE_SIDEBAR_IMG);
        collapsed = false;
    }
    
}
