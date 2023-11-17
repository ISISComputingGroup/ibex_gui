package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

class Constants {
	protected static final String BUTTON_TITLE_SAVE = "Save Script";
	protected static final String BUTTON_TITLE_SAVE_AS = "Save Script As";
	protected static final String BUTTON_TITLE_LOAD = "Load Script";
	
	protected static final String BUTTON_TITLE_ADD_ROW_TO_END = "Add Row to End";
	protected static final String BUTTON_TITLE_INSERT_ROW_BELOW = "Insert Row Below";
	protected static final String BUTTON_TITLE_DELETE_ROWS = "Clear All Rows";
	
	protected static final String BUTTON_TOOLTIP_ADD_ROW_TO_END = "Add a new row to the end of the table";
	protected static final String BUTTON_TOOLTIP_INSERT_ROW_BELOW = "Insert a new row below the selected line in the table";
	protected static final String BUTTON_TOOLTIP_DELETE_ROWS = "Delete all rows in the table";

    protected static final String LOADING_MESSAGE = "Loading...";
    protected static final String RELOADING_MESSAGE = "Reloading...";
    
	protected static final Image IMAGE_RUN = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.scriptgenerator", "icons/play.png");
	protected static final Image IMAGE_PAUSE = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.scriptgenerator", "icons/pause.png");
	protected static final Image IMAGE_STOP = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.scriptgenerator", "icons/stop.png");
	
	protected static final Image IMAGE_UP_ARROW = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_up.png");
	protected static final Image IMAGE_DOWN_ARROW = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_down.png");
	
	/**
	 * Do not use this constructor.
	 */
	private Constants() {
		// Utility class is not meant to be instantiated
	    throw new UnsupportedOperationException();
	}
}