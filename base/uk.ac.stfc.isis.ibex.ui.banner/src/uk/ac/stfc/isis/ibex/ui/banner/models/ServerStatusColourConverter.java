package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;


/**
 * Converts a ServerStatus enum into a display Colour.
 */
public class ServerStatusColourConverter extends Converter<ServerStatus, Color> {
	
    private static final Color RED = SWTResourceManager.getColor(255, 0, 0);
    private static final Color GREEN = SWTResourceManager.getColor(51, 255, 153);
    private static final Color ORANGE = SWTResourceManager.getColor(255, 120, 50);
    private static final Color PURPLE = SWTResourceManager.getColor(255, 0, 255);
    
    /**
     * The constructor.
     */
	public ServerStatusColourConverter() {
		super(ServerStatus.class, Color.class);
	}
	
    @Override
    public Color convert(ServerStatus status) {
    	switch (status) {
    		case UP:
    			return GREEN;
    		case PARTIAL:
    			return ORANGE;
    		case DOWN:
    			return RED;
    		case UNKNOWN:
    		default:
    			return PURPLE;
        }
    }
}
