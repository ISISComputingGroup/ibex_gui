package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

/**
 * Describes the types of cursors which can be used.
 */
public enum MatplotlibCursorType {
	/**
	 * Normal cursor type.
	 */
	DEFAULT,
	
	/**
	 * 'Crosshair' cursor type (for zooming).
	 */
	CROSSHAIR,
	
	/**
	 * Grabbing hand cursor type (for panning).
	 */
	HAND;
	
}
