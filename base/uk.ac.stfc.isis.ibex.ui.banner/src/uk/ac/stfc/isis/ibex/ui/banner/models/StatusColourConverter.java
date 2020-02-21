package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;


public class StatusColourConverter extends Converter<Boolean, Color>{
	
    private static final Color DARK_RED = SWTResourceManager.getColor(192, 0, 0);
    private static final Color GREEN = SWTResourceManager.getColor(51, 255, 153);
    
	public StatusColourConverter() {
		super(Boolean.class, Color.class);
	}
	
    @Override
    public Color convert(Boolean isRunning) {
        if(isRunning){
        	return GREEN;
        } else {
        	return DARK_RED;
        }
    }
}
