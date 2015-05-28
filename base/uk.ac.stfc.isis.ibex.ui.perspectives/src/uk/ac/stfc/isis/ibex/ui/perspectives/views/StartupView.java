package uk.ac.stfc.isis.ibex.ui.perspectives.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import uk.ac.stfc.isis.ibex.ui.widgets.VerticalGradientComposite;

public class StartupView extends ViewPart {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.perspectives.views.StartupView"; //$NON-NLS-1$

	public StartupView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {
		
		// Create container
		new VerticalGradientComposite(parent, SWT.NONE);
		
		createActions();
		initializeToolBar();
		initializeMenu();
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
}
