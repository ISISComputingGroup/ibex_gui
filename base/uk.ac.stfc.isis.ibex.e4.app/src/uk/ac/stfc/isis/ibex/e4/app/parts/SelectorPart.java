package uk.ac.stfc.isis.ibex.e4.app.parts;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class SelectorPart {
	private Button btnPerspective1;
	private Button btnPerspective2;
	private Button btnPerspective3;
	
	@Inject 
	private MApplication application;
	
	@Inject
	private EPartService partService;
	
	@Inject
	private EModelService modelService;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		btnPerspective1 = new Button(parent, SWT.NONE);
		btnPerspective1.setText("DAE");
		btnPerspective1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		btnPerspective1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Perspective 1 selected");
				
				List<MPerspective> perspectives = modelService.findElements(application, "uk.ac.stfc.isis.ibex.e4.app.perspective1", MPerspective.class, null);
				partService.switchPerspective(perspectives.get(0));
			}
		});

		btnPerspective2 = new Button(parent, SWT.NONE);
		btnPerspective2.setText("Perspective 2");
		btnPerspective2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		btnPerspective2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Perspective 2 selected");
				
				List<MPerspective> perspectives = modelService.findElements(application, "uk.ac.stfc.isis.ibex.e4.app.perspective2", MPerspective.class, null);
				partService.switchPerspective(perspectives.get(0));
				
			}
		});
		
		btnPerspective3 = new Button(parent, SWT.NONE);
		btnPerspective3.setText("Perspective 3");
		btnPerspective3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		btnPerspective3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Perspective 3 selected");
				
				List<MPerspective> perspectives = modelService.findElements(application, "uk.ac.stfc.isis.ibex.e4.app.perspective3", MPerspective.class, null);
				partService.switchPerspective(perspectives.get(0));
				
			}
		});
	}

	@Focus
	public void setFocus() {
		
	}

}
