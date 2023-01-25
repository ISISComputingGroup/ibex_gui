package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;
import java.util.Map;
import org.eclipse.swt.graphics.ImageData;


/**
 * Encapsulates all the data belonging to a matplotlib plot canvas.
 */
public record MatplotlibCanvasData(ImageData image, Map<String, Integer> zoomSelectionArea) { }
