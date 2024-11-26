package uk.ac.stfc.isis.ibex.ui;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Helper class that wraps a {@link org.eclipse.swt.widgets.Link}.
 */
public class LinkWrapper {
	
	private Link link;
	
	/**
	 * @param parent Parent element.
	 * @param text	 The text for the link.
	 * @param link	 The link string.
	 */
	public LinkWrapper(Composite parent, String text, String link) {
		this.link = new Link(parent, SWT.NONE);
	    this.link.setText("<a href=\"%s\">%s</a>".formatted(link, text));
	    this.link.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	        	try {
					PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URI(e.text).toURL());
				} catch (PartInitException | MalformedURLException | URISyntaxException ex) {
					LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), ex.getMessage(), ex);
				}
	        }
	    });
	}
	
	/**
	 * @param layoutData the new layout data for the receiver.
	 * @see org.eclipse.swt.widgets.Control#setLayoutData(Object)
	 */
	public void setLayoutData(Object layoutData) {
		link.setLayoutData(layoutData);
	}
}
