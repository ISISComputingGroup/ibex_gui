package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.commands;

import org.eclipse.e4.core.di.annotations.Execute;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveResetAdapter;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import org.eclipse.core.commands.ExecutionException;

/**
 * Class containing the eclipse command to reset perspectives.
 */
public class ResetPerspective {

    /**
     * Eclipse command to rest perspectives.
     */
    @Execute
    public void execute() throws ExecutionException {
    	IsisLog.getLogger(getClass()).info("Resetting perspective layouts via eclipse handler.");
        new PerspectiveResetAdapter().resetPerspective();
    }
}
