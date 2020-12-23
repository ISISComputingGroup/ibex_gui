package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.core.databinding.conversion.Converter;


public class ServerStatusTextConverter extends Converter<ServerStatus, String>{
	
    private static final String RUNNING_TEXT = "RUNNING";
    private static final String NOT_RUNNING_TEXT = "NOT RUNNING";
    
	public ServerStatusTextConverter() {
		super(ServerStatus.class, String.class);
	}
	
    @Override
    public String convert(ServerStatus status) {
    	return status.toString();
    }
}
