package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.nio.file.Path;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorManual;
import uk.ac.stfc.isis.ibex.ui.scriptgenerator.dialogs.ScriptGeneratorAboutDialogBox;

import org.eclipse.swt.widgets.Display;

/**
 * Container for open manual and about menu items to display in the help menu of the script generator.
 */
public class ScriptGeneratorHelpMenuItems extends SelectionAdapter {
	
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorHelpMenuItems.class);
	
	private Optional<URL> manualUrl = Optional.empty();
	private Optional<Path> scriptDefinitionsLocation = Optional.empty();
	
	/**
	 * Gets the help menu items for the help drop-down. 
	 */
	public ScriptGeneratorHelpMenuItems() {
		bindManualUrl();
	}
	
	/**
	 * Set the path of the script definitions to be displayed in the about dialog.
	 * 
	 * @param scriptDefinitionsLocation The path to the script definitions.
	 */
	public void setScriptDefinitionsLocation(Path scriptDefinitionsLocation) {
		this.scriptDefinitionsLocation = Optional.ofNullable(scriptDefinitionsLocation);
	}
	
	@Override
    public void widgetSelected(SelectionEvent e) {
        super.widgetSelected(e);
        var helpMenu = new Menu(Display.getDefault().getActiveShell(), SWT.POP_UP);
        setUpOpenManualButtonItem(helpMenu);
        setUpAboutButtonItem(helpMenu);
        helpMenu.setVisible(true);
    }
	
	private void setUpOpenManualButtonItem(Menu helpMenu) {
        var openManualItem = new MenuItem(helpMenu, SWT.PUSH);
        openManualItem.setText("Open Manual");
        manualUrl.ifPresentOrElse(
        		url -> setUpOpenManualButtonWithUrl(openManualItem, url), 
        		() -> setUpOpenManualButtonWithoutUrl(openManualItem)
	        );
	}
	
	private void setUpOpenManualButtonWithUrl(MenuItem openManualItem, URL url) {
		openManualItem.setEnabled(true);
		openManualItem.setToolTipText(url.toString());
		openManualItem.addListener(SWT.Selection, e1 -> {
	        try {
	            IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
	            browser.openURL(url);
	        } catch (PartInitException ex) {
	            LoggerUtils.logErrorWithStackTrace(LOG, "Failed to open URL in browser: " + url, ex);
	        }
        });
	}
	
	private void setUpOpenManualButtonWithoutUrl(MenuItem openManualItem) {
		openManualItem.setEnabled(false);
		openManualItem.setToolTipText("Manual url not reachable, check your internet connection");
	}
	
	private void setUpAboutButtonItem(Menu helpMenu) {
		var aboutItem = new MenuItem(helpMenu, SWT.PUSH);
        aboutItem.setText("About");
        aboutItem.addListener(SWT.Selection, e2 -> {
            displayAbout();
        });
	}

    private void displayAbout() {
    	var shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    	var dialogBox = new ScriptGeneratorAboutDialogBox(shell, scriptDefinitionsLocation);
    	dialogBox.open();
    }
    
    private void bindManualUrl() {
	    CompletableFuture.supplyAsync(() -> ScriptGeneratorManual.getUserManualUrl())
		    .thenAccept(url -> {
		        Display.getDefault().asyncExec(() -> {
		        	manualUrl = url;
		        });
	    });
    }

}
