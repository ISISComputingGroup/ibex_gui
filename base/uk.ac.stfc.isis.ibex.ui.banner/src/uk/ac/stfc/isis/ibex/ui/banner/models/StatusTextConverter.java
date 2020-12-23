package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.core.databinding.conversion.Converter;


public class StatusTextConverter extends Converter<Boolean, String>{
	
    private static final String RUNNING_TEXT = "RUNNING";
    private static final String NOT_RUNNING_TEXT = "NOT RUNNING";
    
	public StatusTextConverter() {
		super(Boolean.class, String.class);
	}
	
    @Override
    public String convert(Boolean isRunning) {
        if (isRunning) {
        	return RUNNING_TEXT;
        } else {
        	return NOT_RUNNING_TEXT;
        }
    }
}
