package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveSelectionAdapter;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

public class PerspectiveSwitcherView {

    private static final Font LABEL_FONT = SWTResourceManager.getFont("Arial", 16, SWT.NONE);
	private ToolBar toolBar;
	private PerspectivesProvider perspectivesProvider; 
	private static final String INITIALLY_ACTIVE_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.alarms.<Alarms>";

	public PerspectiveSwitcherView() {
	}

	@PostConstruct
	public void draw(Composite parent, MApplication app, EPartService partService, EModelService modelService) {
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout());
		
		toolBar = new ToolBar(composite, SWT.VERTICAL);
		toolBar.setFont(LABEL_FONT);
		
		perspectivesProvider = new PerspectivesProvider(app, partService, modelService);
		addPerspectiveShortcuts();
	}

	private void addPerspectiveShortcuts() {		
		PerspectiveSelectionAdapter selectionAdapter = new PerspectiveSelectionAdapter(perspectivesProvider);
		for (MPerspective perspective : perspectivesProvider.getPerspectives()) {
			ToolItem shortcut = new ToolItem(toolBar, SWT.RADIO);
			shortcut.setText(perspective.getLabel());
			shortcut.setToolTipText(perspective.getTooltip());
			shortcut.setImage(ResourceManager.getPluginImageFromUri(perspective.getIconURI()));
			shortcut.setSelection(perspective.getElementId()==INITIALLY_ACTIVE_ID);
			shortcut.setData("TARGET_ID", perspective.getElementId());
			shortcut.addSelectionListener(selectionAdapter);
		}
	}
}