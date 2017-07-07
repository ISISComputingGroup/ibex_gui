package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.views;

import javax.inject.Inject;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import uk.ac.stfc.isis.ibex.e4.ui.prototyping.PerspectiveView;

public class PerspectiveSwitcherView {
	
	private ToolBar toolBar;

	@Inject
	public PerspectiveSwitcherView() {
	}

	@PostConstruct
	public void draw(Composite parent, IExtensionRegistry registry) {
		Composite composite = new Composite(parent, SWT.None);
		RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
		rowLayout.marginLeft = rowLayout.marginRight = 8;
		rowLayout.marginTop = 6;
		rowLayout.marginBottom = 4;
		composite.setLayout(rowLayout);
		
		toolBar = new ToolBar(composite, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		
		IConfigurationElement[] a = registry.getConfigurationElementsFor("uk.ac.stfc.isis.ibex.e4.ui.UI");
		for (Object o : a) {
			System.out.println(o.toString());
		}
	}

	public void addPerspectiveShortcut(PerspectiveView perspective) {
		ToolItem shortcut = new ToolItem(toolBar, SWT.RADIO);
		shortcut.setText(perspective.getName());
		shortcut.setToolTipText(perspective.getName());
	}
};