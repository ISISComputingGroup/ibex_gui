package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.Editing;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

public abstract class ConfigHandler<T> extends AbstractHandler {

	protected static final ConfigServer SERVER = Configurations.getInstance().server();
	protected static final Editing EDITING = Configurations.getInstance().edit();
	
	protected final SameTypeWriter<T> configService = new SameTypeWriter<T>() {	
		public void onCanWriteChanged(boolean canWrite) {
			setBaseEnabled(canWrite);
		};	
	};
	
	public ConfigHandler(Writable<T> destination) {
		configService.writeTo(destination);
		destination.subscribe(configService);
	}
	
	protected Shell shell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
}
