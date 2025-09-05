package uk.ac.stfc.isis.ibex.ui.scripting;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Handler for console menu bar button that links to the scripting page on the user wiki.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ConsoleHelpButton extends WorkbenchWindowControlContribution {
	private Button button;
	
//	private static final String BUTTON_TEXT = "Help";
	private static final String TOOLTIP_TEXT = "Open user manual link in browser for help with scripting";
	private static final String SYMBOLIC_PATH = "uk.ac.stfc.isis.ibex.ui.widgets";
	private static final String HELP_ICON = "/icons/helpIcon.png";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createControl(Composite parent) {
		Properties linkProps = new Properties();
		try {
			final var resourceFilePath = FileLocator.resolve(ConsoleHelpButton.class.getResource("/resources/helplink.properties")).getPath();
			linkProps.load(new FileInputStream(resourceFilePath)); 
		} catch (IOException | IllegalArgumentException ex) {
			LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), ex.getMessage(), ex);
		}
		String WIKI_LINK = linkProps.getProperty("help_link");
		
		button = new Button(parent, SWT.NONE);
//		button.setText(BUTTON_TEXT);
		button.setImage(ResourceManager.getPluginImage(SYMBOLIC_PATH, HELP_ICON));
		button.setToolTipText(TOOLTIP_TEXT);
		button.addSelectionListener(new SelectionAdapter() {
			  public void widgetSelected(SelectionEvent e) {
				  try {
					PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URI(WIKI_LINK).toURL());
				  } catch (PartInitException | MalformedURLException | URISyntaxException ex) {
						LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), "Failed to open URL in browser: " + WIKI_LINK, ex);
				  }
			  }
		});
		return button;
	}
}