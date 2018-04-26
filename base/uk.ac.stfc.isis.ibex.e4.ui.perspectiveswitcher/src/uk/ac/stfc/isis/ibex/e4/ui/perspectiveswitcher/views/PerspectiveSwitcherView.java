package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveResetAdapter;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

/**
 * The view containing the perspective buttons.
 */
public class PerspectiveSwitcherView {

    private static final Font LABEL_FONT = SWTResourceManager.getFont("Arial", 16, SWT.NONE);
	private static final String RESET_PERSPECTIVE_URI = "platform:/plugin/uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher/icons/reset.png";
	private ToolBar toolBar;
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
		
		toolBar = new ToolBar(composite, SWT.VERTICAL);
		toolBar.setFont(LABEL_FONT);
		
		perspectivesProvider = new PerspectivesProvider(app, partService, modelService);
		
		addPerspectiveShortcuts();
		addSeparator();
		addResetCurrentPerspectiveShortcut();
	}

	private void addPerspectiveShortcuts() {		
		for (MPerspective perspective : perspectivesProvider.getPerspectives()) {
			ToolItem shortcut = new ToolItem(toolBar, SWT.RADIO);
			shortcut.setText(perspective.getLabel());
			shortcut.setToolTipText(perspective.getTooltip());
			shortcut.setImage(ResourceManager.getPluginImageFromUri(perspective.getIconURI()));
			shortcut.setSelection(perspectivesProvider.isSelected(perspective));
			shortcut.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					perspectivesProvider.getPartService().switchPerspective(perspective);
				}
			});
			partService.addPartListener(new IPartListener() {
				
				@Override
				public void partActivated(MPart part) {
					//This is a hack to tell when a perspective changes. A perspective change forces a part to activate.
					shortcut.setSelection(perspectivesProvider.isSelected(perspective));
				}

				@Override
				public void partBroughtToTop(MPart part) {
				}

				@Override
				public void partDeactivated(MPart part) {
				}

				@Override
				public void partHidden(MPart part) {
				}

				@Override
				public void partVisible(MPart part) {
				}
			});
		}
	}
	
	private void addSeparator() {
		new ToolItem(toolBar, SWT.SEPARATOR);
	}

	private void addResetCurrentPerspectiveShortcut() {
		ToolItem shortcut = new ToolItem(toolBar, SWT.NONE);
		shortcut.setText("Reset layout");
		shortcut.setToolTipText("Sets the layout of the current perspective back to its default");	
		shortcut.addSelectionListener(new PerspectiveResetAdapter(perspectivesProvider));
		shortcut.setImage(ResourceManager.getPluginImageFromUri(RESET_PERSPECTIVE_URI));
	}
}