package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import javax.inject.Inject;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

public class PerspectiveSwitcherView {

    private static final Font LABEL_FONT = SWTResourceManager.getFont("Arial", 16, SWT.NONE);
	private ToolBar toolBar;
	
	private MApplication app;
	private EPartService partService;
	private EModelService modelService;
	
	private static final String PLUGIN_WITH_PERSPECTIVE_ICONS = "uk.ac.stfc.isis.ibex.e4.ui";
	private static final String PERSPECTIVE_FOLDER_PREFIX = "perspectiveIcons/";
	private static final String PERSPECTIVE_PLUGIN_PREFIX = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.";

	@Inject
	public PerspectiveSwitcherView() {
	}

	@PostConstruct
	public void draw(Composite parent, MApplication app, EPartService partService, EModelService modelService) {
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout());
		
		toolBar = new ToolBar(composite, SWT.VERTICAL);
		toolBar.setFont(LABEL_FONT);
		
		this.app = app;
		this.partService = partService;
		this.modelService = modelService;		
		
		// TODO: Perspectives should not be hard coded. Use snippets and an extension point to define these
		addPerspectiveShortcut("Alarms", PERSPECTIVE_PLUGIN_PREFIX + "alarms", "Alarms.png", true);
	}

	private void addPerspectiveShortcut(String name, String partId, String iconFile, boolean selected) {
		ToolItem shortcut = new ToolItem(toolBar, SWT.RADIO);
		shortcut.setText(name);
		shortcut.setToolTipText(name);
		Image img = ResourceManager.getPluginImage(PLUGIN_WITH_PERSPECTIVE_ICONS, PERSPECTIVE_FOLDER_PREFIX + iconFile);
		shortcut.setImage(img);
		shortcut.setSelection(selected);
		shortcut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
		        MPerspective element = (MPerspective) modelService.find(partId, app);
		        if (element != null) {
		        	partService.switchPerspective(element);
		        }
		        else {
		        	System.out.println("Unable to find perspective part: " + partId);
		        }
			}	
		});
	}
}