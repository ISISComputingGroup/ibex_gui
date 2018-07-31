package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;

/**
 * Button class, for buttons.
 *
 */
public abstract class Button extends CLabel {

    protected ButtonViewModel model;
    private final DataBindingContext bindingContext = new DataBindingContext();
    @SuppressWarnings("checkstyle:magicnumber")
    private int buttonWidthMin = 200;

    /**
     * Button constructor.
     * 
     * @param parent
     *            Composite
     * @param imageUri
     *            String
     * @param tooltip
     *            String
     * @param model
     *            ButtonViewModel
     */
    @SuppressWarnings("unchecked")
    public Button(Composite parent, String imageUri, String tooltip, ButtonViewModel model) {
        super(parent, SWT.SHADOW_OUT);

        setLayout(new GridLayout());
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        // Needs to be wider than default to allow for larger text because its font is bold when the button is active.
        gridData.minimumWidth = buttonWidthMin;
        setLayoutData(gridData);

        setToolTipText(tooltip);
        setImage(ResourceManager.getPluginImageFromUri(imageUri));

        addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent arg0) {
                mouseEnterAction();
            }
        });

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
        });
        this.model = model;
        bindingContext.bindValue(WidgetProperties.background().observe(this),
                BeanProperties.value("color").observe(model));
        bindingContext.bindValue(WidgetProperties.text().observe(this), BeanProperties.value("text").observe(model));
        bindingContext.bindValue(WidgetProperties.font().observe(this), BeanProperties.value("font").observe(model));
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
