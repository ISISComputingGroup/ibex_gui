package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.AlarmButton;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.PerspectiveButton;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ResetLayoutButton;

/**
 * The view containing the perspective buttons.
 */
public class PerspectiveSwitcherView {

	private PerspectivesProvider perspectivesProvider; 
	
	@Inject
	private EModelService modelService;
	
	@Inject 
	private MApplication app;
	
	@Inject 
	private EPartService partService;
	
	/**
	 * Create and initialise the controls within the view.
	 * 
	 * @param parent The parent container

	 */
	@PostConstruct
	public void draw(Composite parent) {
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		perspectivesProvider = new PerspectivesProvider(app, partService, modelService);
		
		addPerspectiveShortcuts(composite);
		addSeparator(composite);
		addResetCurrentPerspectiveShortcut(composite);
	}

	private void addPerspectiveShortcuts(Composite parent) {
		List<MPerspective> perspectives = perspectivesProvider.getPerspectives();
		
		Comparator<MPerspective> comparator = new Comparator<MPerspective>() 
		{
			@Override
			public int compare(MPerspective p1, MPerspective p2) {
				return p1.getLabel().compareTo(p2.getLabel());
				}
		};
		Collections.sort(perspectives, comparator);
		
		for (final MPerspective perspective : perspectives) {
			if (perspective.getLabel().equals("Alarms")) {
				new AlarmButton(parent, perspective, perspectivesProvider);
			}
			else {
				new PerspectiveButton(parent, perspective, perspectivesProvider);
			}
		}
	}	
	
	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	private void addResetCurrentPerspectiveShortcut(Composite parent) {
		new ResetLayoutButton(parent, perspectivesProvider);
	}
}