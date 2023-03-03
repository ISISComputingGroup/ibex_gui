package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

/**
 * Describes the state of a zoom drag selection.
 */
public enum MatplotlibDragSelectionType {
	/**
	 * Drag selection begins.
	 */
	DRAG_START,
	/**
	 * Drag selection area is changing.
	 */
	DRAG_UPDATE,
	/**
	 * Drag selection has ended.
	 */
	DRAG_END;
}
