package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.Map;

/**
 * Listens for changes on actions validity in the actions table
 */
interface ActionsValidityChangeListener {
	
	/**
	 * Execute when all actions in the table are valid
	 */
	void onValid();
	
	/**
	 * Execute when there are invalid actions in the table
	 */
	void onInvalid(Map<Integer, String> errors);
}