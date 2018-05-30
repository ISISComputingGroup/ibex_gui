package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveResetAdapter;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.PerspectiveButton;

/**
 * The view containing the perspective buttons.
 */
public class PerspectiveSwitcherView {

    private static final Font LABEL_FONT = SWTResourceManager.getFont("Arial", 16, SWT.NONE);
	private static final String RESET_PERSPECTIVE_URI = "platform:/plugin/uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher/icons/reset.png";
	private PerspectivesProvider perspectivesProvider; 
	
	@Inject
	private EModelService modelService;
	
	@Inject 
	private MApplication app;
	
	@Inject 
	private EPartService partService;
	
	@Inject
	private IEventBroker broker; 
	
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
		perspectives.sort(new Comparator<MPerspective>() 
		{
			@Override
			public int compare(MPerspective p1, MPerspective p2) {
				return p1.getLabel().compareTo(p2.getLabel());
				}
			}
		);
		
		for (final MPerspective perspective : perspectives) {
			final PerspectiveButton shortcut = new PerspectiveButton(parent, perspective, perspectivesProvider);
			
			broker.subscribe(UIEvents.ElementContainer.TOPIC_SELECTEDELEMENT, new EventHandler() {
				@Override
				public void handleEvent(Event event) {
				    Object newValue = event.getProperty(EventTags.NEW_VALUE);

				    // only run this, if the NEW_VALUE is a MPerspective
				    if (!(newValue instanceof MPerspective)) {
				      return;
				    }
				    shortcut.setSelection(perspective.equals((MPerspective) newValue));
				}
			});
		}
	}	
	
	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	private void addResetCurrentPerspectiveShortcut(Composite parent) {
		Button shortcut = new Button(parent, SWT.NONE);
		shortcut.setAlignment(SWT.LEFT);
		shortcut.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		shortcut.setFont(LABEL_FONT);
		shortcut.setText("Reset layout");
		shortcut.setToolTipText("Sets the layout of the current perspective back to its default");	
		shortcut.addSelectionListener(new PerspectiveResetAdapter(perspectivesProvider));
		shortcut.setImage(ResourceManager.getPluginImageFromUri(RESET_PERSPECTIVE_URI));
	}
}