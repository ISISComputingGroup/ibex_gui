package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence.SavePerspectiveLayout;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

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
        		try {
        			new SavePerspectiveLayout().execute(app, partService, modelService, window);
        		} catch (RuntimeException e) {
        			LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), e.getMessage(), e);
        		}
        	}
		};
    }

    @Override
    protected void mouseClickAction() {
        adapter.widgetSelected(null);
    }
}
