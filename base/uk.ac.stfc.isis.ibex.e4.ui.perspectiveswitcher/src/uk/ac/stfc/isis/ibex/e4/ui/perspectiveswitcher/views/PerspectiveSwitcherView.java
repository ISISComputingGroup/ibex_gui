package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.e4.core.services.events.IEventBroker;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.alarm.Alarm;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.AlarmButtonViewModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.LoadLayoutButton;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.PerspectiveButton;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.PerspectiveButtonViewModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ResetLayoutButton;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.SaveLayoutButton;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

/**
 * The view containing the perspective buttons.
 */
public class PerspectiveSwitcherView {

	private PerspectivesProvider perspectivesProvider;

	@Inject private EModelService modelService;
	@Inject private MApplication app;
	@Inject private EPartService partService;
	@Inject private IEventBroker broker;
	@Inject private MWindow window;

	/**
	 * Create and initialise the controls within the view.
	 * 
	 * @param parent
	 *            The parent container
	 */
	@PostConstruct
	public void draw(Composite parent) {
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		perspectivesProvider = new PerspectivesProvider(app, partService, modelService);

		addResetCurrentPerspectiveShortcut(composite);
		addSaveLayoutButton(composite);
		addLoadLayoutButton(composite);
		
		addSeparator(composite);
		addPerspectiveShortcuts(composite);
	}

	private void addPerspectiveShortcuts(Composite parent) {
		List<MPerspective> perspectives = perspectivesProvider.getPerspectives()
				.sorted((p1, p2) -> p1.getLabel().compareTo(p2.getLabel()))
				.collect(Collectors.toList());

		for (final MPerspective perspective : perspectives) {
			final PerspectiveButtonViewModel model;
			if (perspective.getLabel().equals("Alarms")) {
				model = new AlarmButtonViewModel(Alarm.getInstance().getCounter(), perspective.getLabel());
			} else {
				model = new PerspectiveButtonViewModel(perspective.getLabel());
			}
			new PerspectiveButton(parent, perspective, perspectivesProvider, model);
			broker.subscribe(UIEvents.ElementContainer.TOPIC_SELECTEDELEMENT, new EventHandler() {
				@Override
				public void handleEvent(Event event) {
					Object newValue = event.getProperty(EventTags.NEW_VALUE);

					// only run this, if the NEW_VALUE is a MPerspective
					if (!(newValue instanceof MPerspective)) {
						return;
					}

					model.setActive(perspective.equals((MPerspective) newValue));
				}
			});
		}
	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	private void addResetCurrentPerspectiveShortcut(Composite parent) {
		new ResetLayoutButton(parent, perspectivesProvider);
	}
	
	private void addSaveLayoutButton(Composite parent) {
		new SaveLayoutButton(parent, app, partService, modelService, window);
	}
	
	private void addLoadLayoutButton(Composite parent) {
		new LoadLayoutButton(parent);
	}
}