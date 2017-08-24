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

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveResetAdapter;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveSelectionAdapter;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

public class PerspectiveSwitcherView {

    private static final Font LABEL_FONT = SWTResourceManager.getFont("Arial", 16, SWT.NONE);
	private ToolBar toolBar;
	// TODO: Refactor this into a proper viewModel
	private PerspectivesProvider perspectivesProvider; 

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
		addSeparator();
		addResetCurrentPerspectiveShortcut();
	}

	private void addPerspectiveShortcuts() {		
		PerspectiveSelectionAdapter selectionAdapter = new PerspectiveSelectionAdapter(perspectivesProvider);
		for (MPerspective perspective : perspectivesProvider.getPerspectives()) {
			ToolItem shortcut = new ToolItem(toolBar, SWT.RADIO);
			// TODO: E4 creates an orphan of some perspectives where the label is surrounded by <>. I haven't found a way to stop it.
			shortcut.setText(perspective.getLabel().replace("<","").replace(">",""));
			shortcut.setToolTipText(perspective.getTooltip());
			shortcut.setImage(ResourceManager.getPluginImageFromUri(perspective.getIconURI()));
			shortcut.setSelection(perspectivesProvider.isSelected(perspective));
			shortcut.setData(PerspectiveSelectionAdapter.ID_KEY, perspective.getElementId());
			shortcut.addSelectionListener(selectionAdapter);
		}
	}
	
	private void addSeparator() {
		new ToolItem(toolBar, SWT.SEPARATOR);
	}

	private void addResetCurrentPerspectiveShortcut() {
		ToolItem shortcut = new ToolItem(toolBar, SWT.NONE);
		shortcut.setText("Reset perspective");
		shortcut.setToolTipText("Sets the layout of the current perspective back to its default");	
		shortcut.addSelectionListener(new PerspectiveResetAdapter(perspectivesProvider));	
	}
}