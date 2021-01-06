package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.core.databinding.conversion.Converter;

/**
 * Converts a ServerStatus enum into a display string.
 */
public class ServerStatusTextConverter extends Converter<ServerStatus, String> {
	
	/**
	 * The constructor.
	 */
	public ServerStatusTextConverter() {
		super(ServerStatus.class, String.class);
	}
	
    @Override
    public String convert(ServerStatus status) {
    	return status.toString();
    }
}
