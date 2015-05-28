package uk.ac.stfc.isis.ibex.ui.weblinks;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;

public class WebLinksView extends ViewPart {
	public WebLinksView() {
	}
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.weblinks.WebLinksView";
	private Browser browser;
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		browser = new Browser(parent, SWT.NONE);
		setUrl(UrlSetter.getUrl());
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	public void setUrl(String url) {
		browser.setUrl(url);
		
	}

}
