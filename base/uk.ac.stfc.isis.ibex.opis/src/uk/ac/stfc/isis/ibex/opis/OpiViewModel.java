package uk.ac.stfc.isis.ibex.opis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

/**
 * This class keeps a static reference to all the OpiView objects that have been created.
 * 
 * Then, when the instrument switches, all possible OPIs are told to reinitialize themselves
 * which causes them to pick up the new macros.
 */
public class OpiViewModel {
    private static List<OpiView> VIEWS = new ArrayList<>();
    
    /**
     * Attempts to refresh all the OPIs that were registered in this class.
     */
    public synchronized static void refreshViews() {
    	for (OpiView view : VIEWS) {
			Display.getDefault().asyncExec(() -> {
				try {
					view.initialiseOPI();
				} catch (Exception e) {
					// Don't care
				}
			});
    	}
    }
    
    /**
     * Register an OPI to be reloaded on instrument change.
     * 
     * @param view the opi to register
     */
    public synchronized static void addView(OpiView view) {
    	VIEWS.add(view);
    }
}
