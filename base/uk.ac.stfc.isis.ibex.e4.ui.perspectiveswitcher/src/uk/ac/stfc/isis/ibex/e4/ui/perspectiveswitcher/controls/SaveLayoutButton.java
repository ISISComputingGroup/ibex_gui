package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import javax.inject.Inject;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence.SavePerspectiveLayout;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Reset Layout button. Sits apart from the Perspective Buttons.
 * 
 */
public class SaveLayoutButton extends Button {

    private final SelectionAdapter adapter;

    /**
     * Constructor.
     * 
     * @param parent
     *            Composite
     * @param perspectivesProvider
     *            PerspectivesProvider
     */
    public SaveLayoutButton(
    		Composite parent, 
    		final MApplication app, 
    		final EPartService partService, 
    		final EModelService modelService, 
    		final MWindow window
    	) {
    	
        super(parent, ResetLayoutButton.RESET_PERSPECTIVE_URI, "Sets the layout of the current perspective back to its default",
                new ButtonViewModel());
        
        model.setText("Save Layout");
        adapter = new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent event) {
        		IsisLog.getLogger(getClass()).info(String.format("%s %s %s %s", app, partService, modelService, window));
        		new SavePerspectiveLayout().execute(app, partService, modelService, window);
        	}
		};
    }

    @Override
    protected void mouseClickAction() {
        adapter.widgetSelected(null);
    }
}
