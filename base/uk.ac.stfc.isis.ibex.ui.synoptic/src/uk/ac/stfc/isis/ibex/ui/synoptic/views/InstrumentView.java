package uk.ac.stfc.isis.ibex.ui.synoptic.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.ui.synoptic.Activator;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticPresenter;
import uk.ac.stfc.isis.ibex.ui.synoptic.widgets.InstrumentSynoptic;

public class InstrumentView extends ViewPart {
	
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.views.InstrumentView"; //$NON-NLS-1$

	private InstrumentSynoptic instrument;
	private final SynopticPresenter presenter = Activator.getDefault().presenter();
	
	private final Display display = Display.getCurrent();
	
	public InstrumentView() {
	}

	@Override
	public void createPartControl(Composite parent) {		
		instrument = new InstrumentSynoptic(parent, SWT.NONE);
		instrument.setComponents(presenter.components());

		presenter.addPropertyChangeListener("components", new PropertyChangeListener() {	
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
				display.asyncExec(new Runnable() {		
					@Override
					public void run() {
						instrument.setComponents(presenter.components());						
					}
				});
			}
		});
	}
	
	@Override
	public void setFocus() {	
	}
}
