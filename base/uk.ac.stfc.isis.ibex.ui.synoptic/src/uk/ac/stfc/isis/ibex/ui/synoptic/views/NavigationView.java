package uk.ac.stfc.isis.ibex.ui.synoptic.views;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticSelectionDialog;
import uk.ac.stfc.isis.ibex.ui.synoptic.widgets.InstrumentBreadCrumb;
import uk.ac.stfc.isis.ibex.ui.synoptic.widgets.Navigator;

public class NavigationView extends ViewPart implements ISizeProvider {
		
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.views.NavigationView"; //$NON-NLS-1$
	
	private static final Color BACKGROUND = SWTResourceManager.getColor(240, 240, 240);
	public static final int FIXED_HEIGHT = 35;
	
	private static Synoptic synoptic = Synoptic.getInstance();
	
	public NavigationView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(BACKGROUND);
		parent.setLayout(new GridLayout(6, false));
		
		Navigator navigator = new Navigator(parent, SWT.NONE);
		navigator.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		navigator.setBackground(BACKGROUND);
		
		InstrumentBreadCrumb instrumentTrail = new InstrumentBreadCrumb(parent, SWT.NONE);
		instrumentTrail.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
		instrumentTrail.setBackground(BACKGROUND);
		new Label(parent, SWT.NONE);
		
		Button loadButton = new Button(parent, SWT.NONE);
		loadButton.setText("Switch Synoptic");
		loadButton.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
		loadButton.setBackground(BACKGROUND);
		
		loadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				SynopticSelectionDialog dialog = new SynopticSelectionDialog(shell, "Switch Synoptic", synoptic.availableSynoptics());
				if (dialog.open() == Window.OK) {
					synoptic.setViewerSynoptic(dialog.selectedSynoptic());
				}
			}
		});
		
		Button refreshButton = new Button(parent, SWT.NONE);
		refreshButton.setText("Refresh Synoptic");
		refreshButton.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
		refreshButton.setBackground(BACKGROUND);
		
		refreshButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SynopticInfo synopticToRefresh = synoptic.getSynopticInfo();
				synoptic.setViewerSynoptic(synopticToRefresh);		
			}
		});
		
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
