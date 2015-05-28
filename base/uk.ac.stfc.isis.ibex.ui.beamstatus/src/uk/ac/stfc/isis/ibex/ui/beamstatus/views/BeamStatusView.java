package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class BeamStatusView extends ViewPart {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.beamstatus.views.BeamStatusView"; //$NON-NLS-1$

	public BeamStatusView() {
		setPartName("BeamStatusView");
	}
	
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.verticalSpacing = 0;
		gl_parent.marginHeight = 0;
		parent.setLayout(gl_parent);
		
		StatusPanel status = new StatusPanel(parent, SWT.NONE);
		GridData gd_status = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_status.minimumHeight = 600;
		gd_status.widthHint = 420;
		gd_status.minimumWidth = 420;
		status.setLayoutData(gd_status);
		StatusPages pages = new StatusPages(parent, SWT.NONE);
		pages.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

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
