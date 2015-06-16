package uk.ac.stfc.isis.ibex.ui.weblinks;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;

public class UrlSetter implements InstrumentInfoReceiver {
	
	private static final String SECI_LINKS_URL = "http://dataweb.isis.rl.ac.uk/SeciLinks/default.htm?Instrument=";
	
	public static String getUrl() {
		String instname = Instrument.getInstance().currentInstrument().name();
		return SECI_LINKS_URL + instname;
	}

	@Override
	public void setInstrument(InstrumentInfo instrument) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		if (page == null) {
			return;
		}
		
		IViewPart view = page.findView(WebLinksView.ID);
		
		if (view == null) {
			return;
		}
		
		if (view instanceof WebLinksView) {
			WebLinksView webview = (WebLinksView) view;
			webview.setUrl(getUrl());
		}

	}

}
