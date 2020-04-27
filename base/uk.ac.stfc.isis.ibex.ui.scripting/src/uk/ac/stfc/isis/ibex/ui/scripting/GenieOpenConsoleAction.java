package uk.ac.stfc.isis.ibex.ui.scripting;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.internal.console.ConsoleFactoryExtension;
import org.eclipse.ui.internal.console.ConsoleManager;
import org.eclipse.ui.internal.console.OpenConsoleAction;

/**
 * This class is used to replace "Open Console" icon from the toolbar of console.
 * Doing so will allow us to remove built in "PyDev Console" from pop up menu and add our
 * own "PyDev Console" which does not show any dialog when "PyDev console" is selected by the user.
 */
@SuppressWarnings("restriction")
public class GenieOpenConsoleAction extends OpenConsoleAction {
	private ConsoleFactoryExtension[] fFactoryExtensions;
	private Menu fMenu;
	
	public GenieOpenConsoleAction( ) {
		super();
		fFactoryExtensions = getSortedFactories();
		
	}
	
	private ConsoleFactoryExtension[] getSortedFactories() {
		ConsoleFactoryExtension[] factoryExtensions = ((ConsoleManager) ConsolePlugin.getDefault().getConsoleManager()).getConsoleFactoryExtensions();
		Arrays.sort(factoryExtensions, new Comparator<ConsoleFactoryExtension>() {

			@Override
			public int compare(ConsoleFactoryExtension e1, ConsoleFactoryExtension e2) {
				if (e1.isNewConsoleExtenson()) {
					return -1;
				}
				if (e2.isNewConsoleExtenson()) {
					return 1;
				}
				String first = e1.getLabel();
				String second = e2.getLabel();
				return first.compareTo(second);
			}
		});
		return factoryExtensions;
	}
	
	@Override
	public Menu getMenu(Control parent) {
		if (fMenu != null) {
			fMenu.dispose();
		}

		fMenu= new Menu(parent);
		int accel = 1;
		ConsoleFactoryExtension pyDevExt = null;
		for (ConsoleFactoryExtension extension : fFactoryExtensions) {
			if (!WorkbenchActivityHelper.filterItem(extension) && extension.isEnabled()) {
				ImageDescriptor image = extension.getImageDescriptor();
				String label = extension.getLabel();
				// Do not add PyDev console to pop up menu
				if (label.contains("PyDev Console")) {
					pyDevExt = extension;
					continue;
				}
				// Add our PyDev console extension to pop up menu
				if (label.contains("PyDevConsoleNoDialog")) {
					label = "PyDev Console";
					image = pyDevExt.getImageDescriptor();
				}
				addActionToMenu(fMenu, new ConsoleFactoryAction(label, image, extension), accel);
				accel++;
				if (extension.isNewConsoleExtenson()) {
					new Separator("new").fill(fMenu, -1); //$NON-NLS-1$
				}
			}
		}
		return fMenu;
	}
	
	private void addActionToMenu(Menu parent, Action action, int accelerator) {
		if (accelerator < 10) {
			StringBuilder label= new StringBuilder();
			//add the numerical accelerator
			label.append('&');
			label.append(accelerator);
			label.append(' ');
			label.append(action.getText());
			action.setText(label.toString());
		}

		ActionContributionItem item= new ActionContributionItem(action);
		item.fill(parent, -1);
	}
	
	private class ConsoleFactoryAction extends Action {

		private ConsoleFactoryExtension fConfig;
		private IConsoleFactory fFactory;

		public ConsoleFactoryAction(String label, ImageDescriptor image, ConsoleFactoryExtension extension) {
			setText(label);
			if (image != null) {
				setImageDescriptor(image);
			}
			fConfig = extension;
		}

		@Override
		public void run() {
			try {
				if (fFactory == null) {
					fFactory = fConfig.createFactory();
				}

				fFactory.openConsole();
			} catch (CoreException e) {
				ConsolePlugin.log(e);
			}
		}

		@Override
		public void runWithEvent(Event event) {
			run();
		}
	}
	
}
