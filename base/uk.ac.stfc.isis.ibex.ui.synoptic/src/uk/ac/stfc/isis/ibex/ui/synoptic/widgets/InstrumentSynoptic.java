package uk.ac.stfc.isis.ibex.ui.synoptic.widgets;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.FillLayout;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.BeamlineCompositeContainer;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.DotDashedLine;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.LineDecoration;
import uk.ac.stfc.isis.ibex.ui.synoptic.component.ComponentView;

/*
 * The synoptic's components with an overlaid beamline. 
 */
public class InstrumentSynoptic extends Composite {

	private BeamlineCompositeContainer instrumentComposite;
	private LineDecoration beamline;
	private ScrolledComposite scrolledComposite;
	
	public InstrumentSynoptic(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.VERTICAL));

		scrolledComposite = new ScrolledComposite(this, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);	
		scrolledComposite.getHorizontalBar().setPageIncrement(500);
		scrolledComposite.addControlListener(new ControlListener(){
			@Override
			public void controlMoved(ControlEvent arg0) {
				// Ignore
			}
			@Override
			public void controlResized(ControlEvent arg0) {
				scrolledComposite.getHorizontalBar().setPageIncrement(scrolledComposite.getSize().x);
			}
		});

		reset();
	}

	public void setComponents(List<? extends Component> components) {
		reset();
		display(components);
		resize();
	}

	private void reset() {
		clearInstrument();
		instrumentComposite = new BeamlineCompositeContainer(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(instrumentComposite);
	}
	
	private void clearInstrument() {
		if (instrumentComposite != null) {
			for (Control child : instrumentComposite.getChildren()) {
				child.dispose();
			}
			
			instrumentComposite.dispose();
		}
	}

	private void display(List<? extends Component> components) {	
		if (components.isEmpty()) {
			return;
		}
		
		for (Component component : components) {
			ComponentView.create(instrumentComposite, component);
		}
		
		if (components.size() > 1) {
			addBeamline();				
		}
	}

	private void addBeamline() {
		beamline = new LineDecoration(instrumentComposite, new DotDashedLine(1), instrumentComposite.beamLineHeight());
		beamline.addLineTo(instrumentComposite);
	}
	
	private void resize() {
		instrumentComposite.layout();
		instrumentComposite.pack();
		Rectangle bounds = instrumentComposite.getBounds(); 
		scrolledComposite.setMinSize(bounds.width, bounds.height);
	}
}
