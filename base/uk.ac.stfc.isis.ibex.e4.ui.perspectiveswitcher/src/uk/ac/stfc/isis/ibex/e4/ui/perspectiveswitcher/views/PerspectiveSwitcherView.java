package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import uk.ac.stfc.isis.ibex.alarm.Alarm;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.AlarmButtonViewModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ButtonViewModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.PerspectiveButton;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.PerspectiveButtonViewModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ResetLayoutButton;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ResetLayoutButtonViewModel;

/**
 * The view containing the perspective buttons.
 */
public class PerspectiveSwitcherView {

	private PerspectivesProvider perspectivesProvider;
	private Collection<ButtonViewModel> buttonModels = new ArrayList<>();
	Button button;
	private Label separator;
	private GridData separatorGD;
	private boolean maximised = true;
	
	/**
     * Width of the button when maximised.
     */
    public static final int MAXIMISED_BUTTON_WIDTH = 200;
    /**
     * Width of the button when minimised.
     */
    public static final int MINIMISED_BUTTON_WIDTH = 35;

    private static final int COLLAPSE_BUTTON_WIDTH = 30;
    private static final int COLLAPSE_BUTTON_HEIGHT = 30;
    private static final String COLLAPSE_BUTTON_TOOLTIP = "Collapse/Expand";
    
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
	 * @param parent
	 *            The parent container
	 */
	@PostConstruct
	public void draw(Composite parent) {
	    parent.setLayout(new GridLayout(2, false));
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		perspectivesProvider = new PerspectivesProvider(app, partService, modelService);

		addResetCurrentPerspectiveShortcut(composite);
		addSeparator(composite);
		addPerspectiveShortcuts(composite);
		addMinimiseButton(parent);
		
		maximise();
	}
	
	private void addMinimiseButton(Composite parent) {
	    button = new Button(parent, SWT.ARROW | SWT.LEFT);
        button.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (maximised) {
                    minimise();
                } else {
                    maximise();
                }
                maximised = !maximised;
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                
            }
        });
        GridData gd = new GridData(SWT.RIGHT, SWT.FILL, false, true);
        gd.widthHint = COLLAPSE_BUTTON_WIDTH;
        gd.heightHint = COLLAPSE_BUTTON_HEIGHT;
        button.setToolTipText(COLLAPSE_BUTTON_TOOLTIP);
        button.setLayoutData(gd);
        button.addMouseTrackListener(new MouseTrackListener() {
            
            @Override
            public void mouseHover(MouseEvent e) {
            }
            
            @Override
            public void mouseExit(MouseEvent e) {
                Display.getDefault().asyncExec(() -> {
                    button.setForeground(SWTResourceManager.getColor(247, 245, 245));
                });
            }
            
            @Override
            public void mouseEnter(MouseEvent e) {
                Display.getDefault().asyncExec(() -> {
                    button.setForeground(SWTResourceManager.getColor(220, 235, 245));
                });
            }
        });
	}

	private void addPerspectiveShortcuts(Composite parent) {
		List<MPerspective> perspectives = perspectivesProvider.getPerspectives();
		
		Collections.sort(perspectives, (MPerspective p1, MPerspective p2) -> p1.getLabel().compareTo(p2.getLabel()));

		for (final MPerspective perspective : perspectives) {
			final PerspectiveButtonViewModel model;
			if (perspective.getLabel().equals("Alarms")) {
				model = new AlarmButtonViewModel(Alarm.getInstance().getCounter(), perspective.getLabel());
			} else {
				model = new PerspectiveButtonViewModel(perspective.getLabel());
			}
			buttonModels.add(model);
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
		separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		separatorGD = new GridData(SWT.FILL, SWT.FILL, true, false);
		separator.setLayoutData(separatorGD);
	}

	private void addResetCurrentPerspectiveShortcut(Composite parent) {
	    ResetLayoutButtonViewModel model = new ResetLayoutButtonViewModel();
	    model.addPropertyChangeListener("triggerReset", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                maximise();
            }
        });
	    buttonModels.add(model);
	    new ResetLayoutButton(parent, perspectivesProvider, model);
	}
	
	private void minimise() {
	    separatorGD.widthHint = MINIMISED_BUTTON_WIDTH;
	    buttonModels.forEach(m -> m.minimise(MINIMISED_BUTTON_WIDTH));
	    button.setAlignment(SWT.RIGHT);
	}
	
	private void maximise() {
        separatorGD.widthHint = MAXIMISED_BUTTON_WIDTH;
        buttonModels.forEach(m -> m.maximise(MAXIMISED_BUTTON_WIDTH));
        button.setAlignment(SWT.LEFT);
    }
}