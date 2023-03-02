//package uk.ac.stfc.isis.ibex.opis;
//
//import java.util.Collections;
//import java.util.Set;
//import java.util.WeakHashMap;
//import org.eclipse.swt.widgets.Display;
//
///**
// * This class keeps a static reference to all the OpiView objects that have been created.
// * 
// * Then, when the instrument switches, all possible OPIs are told to reinitialize themselves
// * which causes them to pick up the new macros.
// * 
// * This class holds a set of weak references to the views, so that old views which eclipse is no 
// * longer using can be garbage collected. A weak reference "doesn't count" towards the reference 
// * count for the purposes of checking that an object can be GC'd, so if an OpiView instance is 
// * *only* referred to by this class, then it's resources can be released. This prevents a potential
// * memory leak.
// */
//public final class OpiViewModel {
//	/**
//	 * (Weak) set of OpiViews that have been created.
//	 */
//    private static final Set<OpiView> VIEWS = Collections.newSetFromMap(new WeakHashMap<OpiView, Boolean>());
//    
//    private OpiViewModel() {
//	    // Private constructor for utility class
//    }
//    
//    /**
//     * Attempts to refresh all the OPIs that were registered in this class.
//     * 
//     * This is done asynchronously on the GUI thread.
//     */
//    public static synchronized void refreshViews() {
//    	for (OpiView view : VIEWS) {
//			Display.getDefault().asyncExec(() -> {
//				try {
//					view.initialiseOPI();
//				} catch (Exception e) {
//					// If we can't reinitialise the OPI, it's probably because eclipse
//					// has disposed of the view (but it hasn't yet been GC'd). This can
//					// cause all sorts of exceptions to be thrown, so ignore this case.
//				}
//			});
//    	}
//    }
//    
//    /**
//     * Register an OPI to be reloaded on instrument change.
//     * 
//     * @param view the opi to register
//     */
//    public static synchronized void addView(OpiView view) {
//    	VIEWS.add(view);
//    }
//}
