package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

/**
 * Describes the position of the user's cursor on a matplotlib plot.
 */
public record MatplotlibCursorPosition(int x, int y, boolean inPlot) {
	
	/**
	 * The default position to report to matplotlib if no mouse events have occcured yet or the mouse is outside the canvas.
	 */
    public static final MatplotlibCursorPosition OUTSIDE_CANVAS = new MatplotlibCursorPosition(0, 0, false);
}
