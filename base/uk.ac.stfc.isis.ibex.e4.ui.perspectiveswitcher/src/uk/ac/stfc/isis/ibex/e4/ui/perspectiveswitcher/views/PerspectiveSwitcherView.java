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

	@Inject
	public PerspectiveSwitcherView() {
	}

	@PostConstruct
	public void draw(Composite parent, MApplication app, EPartService partService, EModelService modelService) {
		System.out.println("Drawing perspective switcher...");
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout());
		
		toolBar = new ToolBar(composite, SWT.VERTICAL);
		toolBar.setFont(LABEL_FONT);
		
		addPerspectiveShortcut("Alarms", "uk.ac.stfc.isis.ibex.client.e4.product.perspective.alarms", "perspectiveIcons/Alarms.png", app, partService, modelService, true);
		addPerspectiveShortcut("Beam status", "uk.ac.stfc.isis.ibex.client.e4.product.perspective.beamstatus", "perspectiveIcons/BeamStatus.png", app, partService, modelService, false);
		addPerspectiveShortcut("DAE", "uk.ac.stfc.isis.ibex.client.e4.product.perspective.dae", "perspectiveIcons/DAE.png", app, partService, modelService, false);
	}

	public void addPerspectiveShortcut(String name, String partId, String iconPath, MApplication app, EPartService partService, EModelService modelService, boolean selected) {
		ToolItem shortcut = new ToolItem(toolBar, SWT.RADIO);
		shortcut.setText(name);
		shortcut.setToolTipText(name);
		Image img = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.e4.ui", iconPath);
		shortcut.setImage(img);
		shortcut.setSelection(selected);
		shortcut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
		        MPerspective element = (MPerspective) modelService.find(partId, app);
		        partService.switchPerspective(element);
			}	
		});
	}
}