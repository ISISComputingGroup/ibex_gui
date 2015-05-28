package uk.ac.stfc.isis.ibex.ui.synoptic.views;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/*
*  View that can be linked to from the synoptic view. 
*/
public final class LinkedViews {
	
	private static final Logger LOG = IsisLog.getLogger("LinkedViews");
	
	private static final Map<String, String> VIEWS = new CaseInsensitiveMap<String>();
	static {
		VIEWS.put("rotating bench", "uk.ac.stfc.isis.ibex.ui.rotatingbench");
		VIEWS.put("goniometer", "uk.ac.stfc.isis.ibex.ui.goniometer");
	}
	
	private LinkedViews() { }
	
	public static void openView(String viewName) {
		if (!VIEWS.containsKey(viewName)) {
			return;
		}
		
		String viewID = VIEWS.get(viewName);
		IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		try {
			workbenchPage.showView(viewID);
		} catch (PartInitException e) {
			LOG.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("serial")
	private static final class CaseInsensitiveMap<T> extends HashMap<String, T> {
		
		@Override
		public T put(String key, T value) {
			return super.put(key.toLowerCase(), value);
		}
		
		@Override
		public T get(Object key) {			
			return super.get(formatted(key));
		}
		
		@Override
		public boolean containsKey(Object key) {
			return super.containsKey(formatted(key));
		}
		
		private static String formatted(Object key) {
			return key == null ? null : key.toString().toLowerCase();
		}
	}
}
