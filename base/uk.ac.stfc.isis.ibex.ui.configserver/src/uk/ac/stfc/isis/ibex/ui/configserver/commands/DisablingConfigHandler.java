package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * This class is a specific type of ConfigHandler that will disable the command
 * if the underlying PV becomes read-only.
 *
 * @param <T> The type of data expected from the underlying PV
 */
public abstract class DisablingConfigHandler<T> extends ConfigHandler<T> {
	
	private final Logger LOG = IsisLog.getLogger(getClass());

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
		LoggerUtils.logIfExtraDebug(LOG, "(Ticket 3001) canWrite set to " + canWrite);
		setBaseEnabled(canWrite);		
	}
}
