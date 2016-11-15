package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * This class is a specific type of ConfigHandler that will disable the command
 * if the underlying PV becomes read-only.
 *
 * @param <T> The type of data expected from the underlying PV
 */
public abstract class DisablingConfigHandler<T> extends ConfigHandler<T> {

	/**
	 * The default constructor.
	 * 
	 * @param destination The PV that the command is writing to.
	 */
	public DisablingConfigHandler(Writable<T> destination) {
		super(destination);
	}

	@Override
	public void canWriteChanged(boolean canWrite) {
		setBaseEnabled(canWrite);		
	}

}
