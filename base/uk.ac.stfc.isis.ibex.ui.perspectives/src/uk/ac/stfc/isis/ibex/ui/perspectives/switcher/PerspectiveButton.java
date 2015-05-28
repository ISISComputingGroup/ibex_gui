package uk.ac.stfc.isis.ibex.ui.perspectives.switcher;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.log.Log;
import uk.ac.stfc.isis.ibex.log.LogCounter;
import uk.ac.stfc.isis.ibex.ui.UI;

public class PerspectiveButton extends CLabel {
	
	protected static final Color FOCUSSED = SWTResourceManager.getColor(220, 235, 245);
	protected static final Color DEFOCUSSED = SWTResourceManager.getColor(247, 245, 245);
	
	protected static LogCounter counter = Log.getInstance().getCounter();
	
	public PerspectiveButton(Composite parent, final String perspective) {
		super(parent, SWT.SHADOW_OUT);
		
		addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseDown(MouseEvent e) {
				UI.getDefault().switchPerspective(perspective);
				mouseClickAction();
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				mouseDown(e);
			}
		});

		addMouseTrackListener(new MouseTrackAdapter() {			
			@Override
			public void mouseExit(MouseEvent e) {
				mouseExitAction();
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				mouseEnterAction();
			}
		});		
		
		setBackground(DEFOCUSSED);
	}

	protected void mouseClickAction() {
		// Restart counting of log messages when not observing
		// the log message viewer
		counter.start();
	}
	
	protected void mouseEnterAction() {
		setBackground(FOCUSSED);
	}
	
	protected void mouseExitAction() {
		setBackground(DEFOCUSSED);
	}
}
