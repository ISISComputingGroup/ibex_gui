package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.FillLayout;

public class StatusPages extends Composite {

	private static final String MCR_NEWS_PAGE_URL 
		= "http://www.isis.stfc.ac.uk/files/mcr-news/mcrnews.txt";
	
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.beamstatus.views.BeamStatusView"; //$NON-NLS-1$
	
	private static final long WEB_PAGE_REFRESH_PERIOD = 30000;	// milliseconds

	
	@SuppressWarnings("unused")
	private StatusPanel statusPanel;
	
	@SuppressWarnings("unused")
	private Composite statusSpacer;
	
	private Browser statusGraphBrowser;
	private Browser newsBrowser;
	
	public StatusPages(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		CTabFolder tabFolder = new CTabFolder(this, SWT.NONE);				
		
		CTabItem tbtmWebPage = new CTabItem(tabFolder, SWT.NONE);
		tbtmWebPage.setText("Beam Status Graph");
		
		statusGraphBrowser = new Browser(tabFolder, SWT.NONE);
		statusGraphBrowser.setJavascriptEnabled(false);
		statusGraphBrowser.setUrl("http://www.isis.stfc.ac.uk/beam-status/");
		tbtmWebPage.setControl(statusGraphBrowser);
		
		CTabItem tbtmMCRNews = new CTabItem(tabFolder, SWT.NONE);
		tbtmMCRNews.setText("MCR News");
		
		newsBrowser = new Browser(tabFolder, SWT.NONE);
		newsBrowser.setJavascriptEnabled(false);
		tbtmMCRNews.setControl(newsBrowser);
		newsBrowser.setUrl(MCR_NEWS_PAGE_URL);
		
		startTimer();
		tabFolder.setSelection(0);
	}
	
	private void startTimer() {
		Timer timer = new Timer();	
		long delay = WEB_PAGE_REFRESH_PERIOD;	
		timer.scheduleAtFixedRate(updateBrowser(newsBrowser), delay, WEB_PAGE_REFRESH_PERIOD);
	}
	

	private TimerTask updateBrowser(final Browser browser) {
		return new TimerTask() {
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {			
					@Override
					public void run() {
						browser.refresh();					
					}
				});
			}
		};	
	}

}
