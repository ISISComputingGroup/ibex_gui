package uk.ac.stfc.isis.ibex.ui.synoptic.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.synoptic.widgets.Navigator;
import uk.ac.stfc.isis.ibex.ui.synoptic.widgets.SynopticSelection;

public class NavigationView extends ViewPart implements ISizeProvider {
		
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.views.NavigationView"; //$NON-NLS-1$
	
	private static final Color BACKGROUND = SWTResourceManager.getColor(240, 240, 240);
	public static final int FIXED_HEIGHT = 35;
	
	public NavigationView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(BACKGROUND);
		parent.setLayout(new GridLayout(6, false));
		
		Navigator navigator = new Navigator(parent, SWT.NONE);
		navigator.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		navigator.setBackground(BACKGROUND);
		
		SynopticSelection synopticSelection = new SynopticSelection(parent, SWT.NONE);
		synopticSelection.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		synopticSelection.setBackground(BACKGROUND);		
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getSizeFlags(boolean width) {
		return SWT.MIN | SWT.MAX;
	}

	@Override
	public int computePreferredSize(boolean width, int availableParallel,
			int availablePerpendicular, int preferredResult) {
		return  width ? -1 : FIXED_HEIGHT;
	}
}
