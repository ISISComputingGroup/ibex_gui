package uk.ac.stfc.isis.ibex.ui.synoptic.beamline;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/*
 * Allows a horizontal line to be drawn across a control 
 * and any child controls. The vertical offset for the line
 * should be specified with respect to the parent control.
 */
public class LineDecoration {
	
	private final Line line;
	private final int verticalOffset;
	private final Control parent;
	
	private final PaintListener linePainter = new PaintListener() {
		@Override
		public void paintControl(PaintEvent e) {
			drawLine(e);
		}
	};
	
	public LineDecoration(Control parent, Line line, int verticalOffset) {
		this.parent = parent;
		this.line = line;
		this.verticalOffset = verticalOffset;
	}
	
	public void addLineTo(final Control control) {
		control.addPaintListener(linePainter);

		if (control instanceof Composite) {
			Composite composite = (Composite) control;
			for (Control child : composite.getChildren()) {
				addLineTo(child);
			}
		}
	}
	
	public void removeLineFrom(final Control control) {
		control.removePaintListener(linePainter);
		
		if (control instanceof Composite) {
			Composite composite = (Composite) control;
			for (Control child : composite.getChildren()) {
				removeLineFrom(child);
			}
		}
	}
	
	private void drawLine(PaintEvent event) {
		int offset = mapVerticalOffset(event);
		if (offset <= event.y + event.height) {
			event.gc.setLineStyle(line.swtLineStyle());
			event.gc.setLineWidth(line.width());
			event.gc.drawLine(event.x, offset, event.width, offset);
		}
	}
	
	/*
	 * Transform the vertical offset for the parent control
	 * to an offset in the control being painted.
	 */
	private int mapVerticalOffset(PaintEvent event) {
	    final Control c = (Control) event.widget;
        final Display display = c.getDisplay();
        final Rectangle area = display.map(parent, c, 0, verticalOffset, 0, 0);
        
        return area.y;
	}
}
