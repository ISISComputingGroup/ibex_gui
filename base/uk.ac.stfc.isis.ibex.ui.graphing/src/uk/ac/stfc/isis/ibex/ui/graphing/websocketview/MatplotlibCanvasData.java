package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;
import java.util.Map;
import org.eclipse.swt.graphics.ImageData;

/**
 * Record to encapsulate all the data belonging to the matplotlib plot canvas.
 */
public record MatplotlibCanvasData(ImageData image, Map<String, Integer> zoomSelectionArea) {
	/**
	 * Constructor for MatplotlibCanvasData.
	 * @param image
	 * @param zoomSelectionArea
	 */
	public MatplotlibCanvasData(ImageData image, Map<String, Integer> zoomSelectionArea) {
		this.image = image;
		this.zoomSelectionArea = zoomSelectionArea;
	}
}
