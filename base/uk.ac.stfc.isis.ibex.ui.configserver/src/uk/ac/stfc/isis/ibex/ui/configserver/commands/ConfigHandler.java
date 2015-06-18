package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.Editing;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * This class forms the basis of any "commands" used in relation to the ConfigServer.
 * The ConfigServer is the plug-in that interacts with the BlockServer.
 * 
 * Commands are used by the Eclipse framework to contribute actions to the user interface 
 * (e.g. menus, context menus etc.)
 *
 * @param <T>
 */
public abstract class ConfigHandler<T> extends AbstractHandler {

	protected static final ConfigServer SERVER = Configurations.getInstance().server();
	protected static final Editing EDITING = Configurations.getInstance().edit();
	
	/**
	 * This is an inner anonymous class inherited from SameTypeWriter with added functionality
	 * for disabling the command if the underlying PV cannot be written to.
	 * 
	 * Inner anonymous class = new unnamed class being created and instanced at the same time!
	 */
	protected final SameTypeWriter<T> configService = new SameTypeWriter<T>() {	
		@Override
		public void onCanWriteChanged(boolean canWrite) {
			setBaseEnabled(canWrite);
		};	
	};
	
	/**
	 * Constructor.
	 * 
	 * @param destination where to write the data to
	 */
	public ConfigHandler(Writable<T> destination) {
		configService.writeTo(destination);
		destination.subscribe(configService);
	}
	
	protected Shell shell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
}
