package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import uk.ac.stfc.isis.ibex.alarm.Alarm;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.AlarmButtonViewModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ButtonViewModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.CollapseSidebarButton;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.PerspectiveButton;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.PerspectiveButtonViewModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ResetLayoutButton;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ResetLayoutButtonViewModel;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.RefreshPvConnectionButton;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls.ScriptServerButtonViewModel;
import uk.ac.stfc.isis.ibex.nicos.Nicos;

/**
 * The view containing the perspective buttons.
 */
public class PerspectiveSwitcherView {

    private PerspectivesProvider perspectivesProvider;
    private Collection<ButtonViewModel> buttonModels = new ArrayList<>();
    private CollapseSidebarButton collapseSidebarButton;
    private Label separator;
    private GridData separatorGD;

    /**
     * Width of the button when maximised.
     */
    public static final int MAXIMISED_BUTTON_WIDTH = 188;
    /**
     * Width of the button when minimised.
     */
    public static final int MINIMISED_BUTTON_WIDTH = 30;

    /**
     * The model service.
     */
    @Inject
    public static EModelService modelService;

    /**
     * The E4 application.
     */
    @Inject
    public static MApplication app;

    /**
     * The part service.
     */
    @Inject
    public static EPartService partService;

    @Inject
    private IEventBroker broker;

    /**
     * Create and initialise the controls within the view.
     * 
     * @param parent The parent container
     */
    @PostConstruct
    @SuppressWarnings("magicnumber")
    public void draw(Composite parent) {
        GridLayout glParent = new GridLayout(2, false);
        glParent.marginHeight = 0;
        glParent.marginWidth = 0;
        glParent.horizontalSpacing = 0;
        parent.setLayout(glParent);
        Composite composite = new Composite(parent, SWT.None);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        perspectivesProvider = new PerspectivesProvider(app, partService, modelService);

        addResetCurrentPerspectiveShortcut(composite);
		addRefreshPvConnectionButton(composite);
        addSeparator(composite);
        addPerspectiveShortcuts(composite);
        addCollapseButton(parent);

        maximise();
    }

	private void addRefreshPvConnectionButton(Composite parent) {
		ButtonViewModel model = new ButtonViewModel("Refresh PVs");
	    buttonModels.add(model);
	    
	    RefreshPvConnectionButton button = new RefreshPvConnectionButton(parent, model);
	    button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
            	RefreshPvConnectionButton button = (RefreshPvConnectionButton) e.getSource();
            	button.refreshPvConnections();
            }
	    });
	}

    private void addCollapseButton(Composite parent) {
        collapseSidebarButton = new CollapseSidebarButton(parent);
        collapseSidebarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                if (collapseSidebarButton.isCollapsed()) {
                    maximise();
                } else {
                    minimise();
                }
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
            } else if (perspective.getLabel().equals("Script Server")) {
                model = new ScriptServerButtonViewModel(Nicos.getDefault().getModel(), perspective.getLabel());
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
            broker.subscribe(UIEvents.UIElement.TOPIC_VISIBLE, new EventHandler() {

                @Override
                public void handleEvent(Event event) {
                    Object newValue = event.getProperty(EventTags.ELEMENT);

                    // only run this, if the NEW_VALUE is a MPerspective
                    if (!(newValue instanceof MPerspective)) {
                        return;
                    }

                    if (perspective.equals((MPerspective) newValue)) {
                        model.setVisible((Boolean) event.getProperty(EventTags.NEW_VALUE));
                    }
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
        buttonModels.add(model);

        ResetLayoutButton button = new ResetLayoutButton(parent, perspectivesProvider, model);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                maximise();
            }
        });
    }

    private void minimise() {
        separatorGD.widthHint = MINIMISED_BUTTON_WIDTH;
        buttonModels.forEach(m -> m.minimise(MINIMISED_BUTTON_WIDTH));
        collapseSidebarButton.collapse();
    }

    private void maximise() {
        separatorGD.widthHint = MAXIMISED_BUTTON_WIDTH;
        buttonModels.forEach(m -> m.maximise(MAXIMISED_BUTTON_WIDTH));
        collapseSidebarButton.expand();
    }
}