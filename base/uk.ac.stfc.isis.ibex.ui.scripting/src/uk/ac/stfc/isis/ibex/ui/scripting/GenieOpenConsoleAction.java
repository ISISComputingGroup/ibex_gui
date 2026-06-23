package uk.ac.stfc.isis.ibex.ui.scripting;

import org.apache.commons.lang.ArrayUtils;
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
	private ImageDescriptor pyDevImageDescriptor;
	/**
	 * Maximum number of actions we can have in open console drop down menu for keyboard shortcuts.
	 */
	private static final int MAX_NUMBER_OF_ITEMS_FOR_KEYBOARD_SHORTCUTS = 9;
	
	/**
	 * Initialise factory extensions.
	 */
	public GenieOpenConsoleAction() {
		super();
		fFactoryExtensions = getFactories();
	}
	
	private ConsoleFactoryExtension[] getFactories() {	
		ConsoleFactoryExtension[] factoryExtensions = ((ConsoleManager) ConsolePlugin.getDefault().getConsoleManager()).getConsoleFactoryExtensions();
		return removePyDevConsoleFactory(factoryExtensions);
	}
	
	
	private ConsoleFactoryExtension[] removePyDevConsoleFactory(ConsoleFactoryExtension[] factoryExtensions) {
		final int idxOfPyDevConsoleFactoryExtension = 1;
		pyDevImageDescriptor = factoryExtensions[idxOfPyDevConsoleFactoryExtension].getImageDescriptor();
		return (ConsoleFactoryExtension[]) ArrayUtils.remove(factoryExtensions, idxOfPyDevConsoleFactoryExtension);
	}
	
	@Override
	public Menu getMenu(Control parent) {
		if (fMenu != null) {
			fMenu.dispose();
		}

		fMenu = new Menu(parent);
		int accel = 1;
		for (ConsoleFactoryExtension extension : fFactoryExtensions) {
			if (!WorkbenchActivityHelper.filterItem(extension) && extension.isEnabled()) {
				ImageDescriptor image = extension.getImageDescriptor();
				String label = extension.getLabel();
				// rename the label here
				if (label.contains("PyDevConsoleNoDialog")) {
					label = "PyDev Console";
					image = this.pyDevImageDescriptor;
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
		if (accelerator <= MAX_NUMBER_OF_ITEMS_FOR_KEYBOARD_SHORTCUTS) {
			StringBuilder label = new StringBuilder();
			//add the numerical accelerator
			label.append('&');
			label.append(accelerator);
			label.append(' ');
			label.append(action.getText());
			action.setText(label.toString());
		}

		ActionContributionItem item = new ActionContributionItem(action);
		item.fill(parent, -1);
	}
	
	private class ConsoleFactoryAction extends Action {

		private ConsoleFactoryExtension fConfig;
		private IConsoleFactory fFactory;

		ConsoleFactoryAction(String label, ImageDescriptor image, ConsoleFactoryExtension extension) {
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
