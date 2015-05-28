package uk.ac.stfc.isis.ibex.ui.synoptic.beamline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class BeamlineCompositeContainer extends BeamlineComposite {

	private final List<BeamlineComposite> targets = new ArrayList<>();
	private final GridLayout grid = new GridLayout(1, false);
	
	private int targetLineHeight;
	
	/*
	 * Composite for storing beamline components. 
	 */
	public BeamlineCompositeContainer(Composite parent, int style) {
		super(parent, style);
		
		super.setLayout(grid);
		grid.marginHeight = 0;
		grid.verticalSpacing = 0;
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	@Override
	public void setLayout(Layout layout) {
		// Do nothing, layout is fixed
	}

	public void registerBeamlineTarget(BeamlineComposite target) {
		targets.add(target);		
		setTargetSize(target);
		grid.numColumns = targets.size();
		
		targetLineHeight = Math.max(targetLineHeight, target.beamLineHeight());
		alignVerticalHeights();
	}

	@Override
	public int beamLineHeight() {
		return targetLineHeight;
	}
	
	private static void setTargetSize(Composite composite) {
		composite.pack();
		GridData gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd.minimumHeight = composite.getSize().y;
		gd.heightHint = gd.minimumHeight;
		composite.setLayoutData(gd);
	}
	
	private void alignVerticalHeights() {
		for (BeamlineComposite target : targets) {
			alignVerticalHeight(target);
		}
	}

	private void alignVerticalHeight(BeamlineComposite target) {
		GridData gd = (GridData) target.getLayoutData();
		gd.verticalIndent = targetLineHeight - target.beamLineHeight();
	}
	
	/**
	 * Overrides standard getBounds() method, to ensure only extent of children is used
	 * 
	 * @return Width and height give maximum extent of container
	 */
	@Override
	public Rectangle getBounds() {
		Control[] controls = getChildren();
		int maxX = 0, maxY = 0;
		
		for (Control control : controls) {
			Rectangle bounds = control.getBounds();
			maxX = Math.max(bounds.width + bounds.x, maxX);
			maxY = Math.max(bounds.height + bounds.y, maxY);
			System.out.println(control.getBounds().width + ":" + control.getBounds().height);
		}
		
		// Add the margins
		maxX += grid.marginWidth;
		maxY += grid.marginHeight;
		
		// Rectangle(x, y, width, height) - assume starts at 0
		return new Rectangle(0, 0, maxX, maxY);
	}
}
