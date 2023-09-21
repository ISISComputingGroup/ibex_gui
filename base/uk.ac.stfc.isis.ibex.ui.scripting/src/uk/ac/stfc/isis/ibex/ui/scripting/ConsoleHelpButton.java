package uk.ac.stfc.isis.ibex.ui.scripting;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Handler for console menu bar button that links to the scripting page on the user wiki.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ConsoleHelpButton extends WorkbenchWindowControlContribution {
	private Button button;
	
	private static final String BUTTON_TEXT = "Help";
	private static final String WIKI_LINK = "https://shadow.nd.rl.ac.uk/ibex_user_manual/Scripting-View.rest";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, SWT.NONE);
		button.setText(BUTTON_TEXT);
		button.addSelectionListener(new SelectionAdapter() {
			  public void widgetSelected(SelectionEvent e) {
				  try {
					PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(WIKI_LINK));
				  } catch (PartInitException | MalformedURLException ex) {
						LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), "Failed to open URL in browser: " + WIKI_LINK, ex);
				  }
			  }
		});
		return button;
	}
}