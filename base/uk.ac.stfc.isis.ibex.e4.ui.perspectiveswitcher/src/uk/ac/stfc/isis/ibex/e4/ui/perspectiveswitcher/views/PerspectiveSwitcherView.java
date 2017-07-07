package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import javax.inject.Inject;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import uk.ac.stfc.isis.ibex.e4.perspectiveswitcher.PerspectiveSwitcher;

@SuppressWarnings("unused")
public class PerspectiveSwitcherView {
	
	private ToolBar toolBar;
	private PerspectiveSwitcher perspectiveSwitcher = new PerspectiveSwitcher();

	@Inject
	public PerspectiveSwitcherView() {
	}

	@PostConstruct
	public void draw(Composite parent, MApplication app, EPartService partService, EModelService modelService) {
		System.out.println("Drawing perspective switcher...");
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout());
		
		toolBar = new ToolBar(composite, SWT.VERTICAL);
		
		addPerspectiveShortcut("Alarms", "uk.ac.stfc.isis.ibex.client.e4.product.perspective.alarms", app, partService, modelService);
		addPerspectiveShortcut("Beam status", "uk.ac.stfc.isis.ibex.client.e4.product.perspective.beamstatus", app, partService, modelService);
		addPerspectiveShortcut("DAE", "uk.ac.stfc.isis.ibex.client.e4.product.perspective.dae", app, partService, modelService);
	}

	public void addPerspectiveShortcut(String name, String partId, MApplication app, EPartService partService, EModelService modelService) {
		ToolItem shortcut = new ToolItem(toolBar, SWT.RADIO);
		shortcut.setText(name);
		shortcut.setToolTipText(name);
		shortcut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
		        MPerspective element = (MPerspective) modelService.find(partId, app);
		        partService.switchPerspective(element);
			}	
		});
	}
};