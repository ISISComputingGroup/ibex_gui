package uk.ac.stfc.isis.ibex.ui.widgets;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Universal 'help' button - links to appropriate wiki page.
 */
public class HelpButton {
	
	private static final String SYMBOLIC_PATH = "uk.ac.stfc.isis.ibex.ui.widgets";
	private static final String HELP_ICON2 = "/icons/helpIcon.png";
	private static final String TOOLTIP_TEXT = "Open user manual link in browser for help with '%s': \n%s";

	/**
	 * Creates the help button which links to appropriate part of the wiki.
	 * 
	 * @param parent 		Parent element
	 * @param wikiLink 		Link string to wiki
	 * @param description	Description of help for tooltip
	 */
	public HelpButton(Composite parent, String wikiLink, String description) {
		String tooltipDesc = String.format(TOOLTIP_TEXT, description, wikiLink);
		
		//create button
		Button helpButton = new Button(parent, SWT.PUSH);
		helpButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		helpButton.setImage(ResourceManager.getPluginImage(SYMBOLIC_PATH, HELP_ICON2));
		helpButton.setToolTipText(tooltipDesc);
		
		helpButton.addSelectionListener(new SelectionAdapter() {
			  public void widgetSelected(SelectionEvent e) {
				  try {
					PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(wikiLink));
				  } catch (PartInitException | MalformedURLException ex) {
						LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), "Failed to open URL in browser: " + wikiLink, ex);
				  }
			  }
		});
	}
	
	
}
