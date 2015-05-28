package uk.ac.stfc.isis.ibex.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.ui.dialogs.WaitForDialog;

public class WaitFor {
	
	private WaitForDialog dialog;	
	private final Display display = Display.getDefault();
	
	private final Collection<Waiting> waiters = new ArrayList<>();
	
	public WaitFor() {
		configure();
	}
	
	private void configure() {
		for (IConfigurationElement element : getRegistered()) {
			try {
				Waiting waiter = extractWaiter(element);	
				waiters.add(waiter);
				waiter.addPropertyChangeListener("isWaiting", showWaitDialog(waiter));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private PropertyChangeListener showWaitDialog(final Waiting waiter) {
		return new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				display.asyncExec(handleWait(waiter));
			}
		};
	}
	
	private void doWait() {
		if (dialog == null) {
			createDialog();
		}
		

		dialog.open();
		dialog.setCursor(SWT.CURSOR_ARROW);
	}

	private void stopWait() {
		if (dialog != null) {
			dialog.close();
		}		
	}
	
	// Must call from the UI thread.
	private void createDialog() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		dialog = new WaitForDialog(shell);
		dialog.setBlockOnOpen(true);
	}
	
	private IConfigurationElement[] getRegistered() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		return registry.getConfigurationElementsFor("uk.ac.stfc.isis.ibex.ui.wait");		
	}

	private Waiting extractWaiter(IConfigurationElement element) throws CoreException {
		return (Waiting) element.createExecutableExtension("class");
	}
	
	private Runnable handleWait(final Waiting waiter) {
		return new Runnable() {
			@Override
			public void run() {
				UI.getDefault().switchPerspective(PerspectiveSwitcher.LOG_PERSPECTIVE_ID);
			
				boolean isWaiting = waiter.isWaiting();
				if (isWaiting) {
					doWait();
				} else {
					stopWait();
				}						
			}
		};
	}
}
