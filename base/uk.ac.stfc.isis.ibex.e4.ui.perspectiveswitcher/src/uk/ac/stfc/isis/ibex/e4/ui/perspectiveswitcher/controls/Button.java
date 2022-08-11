package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.ResourceManager;

/**
 * Button class, for buttons.
 *
 */
public abstract class Button extends CLabel {

    /**
     * The view model that dictates how the button should behave.
     */
    protected ButtonViewModel model;
    private final DataBindingContext bindingContext = new DataBindingContext();

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
    public Button(Composite parent, String imageUri, String tooltip, ButtonViewModel model) {
        super(parent, SWT.SHADOW_OUT);

        setLayout(new GridLayout());
        setRightMargin(0);
        
        GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        model.addPropertyChangeListener("width", new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Display.getDefault().asyncExec(new Runnable() {
                    
                    @Override
                    public void run() {
                        gridData.widthHint = (int) evt.getNewValue();
                        requestLayout();
                    }
                });
            }
        });
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
        
        model.addPropertyChangeListener("visible", new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                GridData data = (GridData) getLayoutData();
                boolean nowVisible = (boolean) e.getNewValue();
                data.exclude = !nowVisible;
                setVisible(nowVisible);
                parent.requestLayout();
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
