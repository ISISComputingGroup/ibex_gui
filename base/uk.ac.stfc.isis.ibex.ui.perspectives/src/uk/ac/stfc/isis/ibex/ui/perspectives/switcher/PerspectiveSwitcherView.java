package uk.ac.stfc.isis.ibex.ui.perspectives.switcher;

import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.perspectives.Activator;
import uk.ac.stfc.isis.ibex.ui.perspectives.IsisPerspective;

public class PerspectiveSwitcherView extends ViewPart implements ISizeProvider {
	public PerspectiveSwitcherView() {
	}

	private static Font BUTTON_FONT = SWTResourceManager.getFont("Arial", 12, SWT.NORMAL);
	private static Color BACKGROUND = SWTResourceManager.getColor(250, 250, 252);
	
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.perspectives.PerspectiveSwitcher"; //$NON-NLS-1$

	public static final int FIXED_WIDTH = 200;
	
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(BACKGROUND);
		container.setLayout(new GridLayout(1, false));
				
		List<IsisPerspective> perspectives = Activator.getDefault().perspectives().get();
		
		for (IsisPerspective perspective : perspectives) {	
			PerspectiveButton button = buttonForPerspective(container, perspective);
			configureButton(perspective, button);
		}
	
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	private void configureButton(IsisPerspective perspective, PerspectiveButton button) {
		button.setText(perspective.name());
		button.setImage(perspective.image());
		button.setAlignment(SWT.LEFT);
		button.setFont(BUTTON_FONT);
				
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd.heightHint = 30;
		gd.minimumHeight = 30;
		
		button.setLayoutData(gd);
	}

	private PerspectiveButton buttonForPerspective(Composite container,
			IsisPerspective perspective) {
		return isLogPerspective(perspective) 
				? new LogButton(container, perspective.ID()) 
				: new PerspectiveButton(container, perspective.ID());
	}

	private boolean isLogPerspective(IsisPerspective perspective) {
		return perspective.ID() == "uk.ac.stfc.isis.ibex.ui.log.perspective";
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		@SuppressWarnings("unused")
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		@SuppressWarnings("unused")
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public int getSizeFlags(boolean width) {
		return width ? SWT.MIN | SWT.MAX : SWT.NONE;
	}

	@Override
	public int computePreferredSize(boolean width, int availableParallel,
			int availablePerpendicular, int preferredResult) {
		return  width ? FIXED_WIDTH : 0;
	}
}
