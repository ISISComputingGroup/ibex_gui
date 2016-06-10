package uk.ac.stfc.isis.ibex.synopticlike;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class SynopticPart {
	private Button btnPerspective1;
	
	@Inject 
	private MApplication application;
	
	@Inject
	private EPartService partService;
	
	@Inject
	private EModelService modelService;
	
	private int opiNum = 1;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		btnPerspective1 = new Button(parent, SWT.NONE);
		btnPerspective1.setText("Open OPI");
		btnPerspective1.setLayoutData(new GridData());
		
		btnPerspective1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Open OPI");
				
				List<MPartStack> stacks = modelService.findElements(application, "uk.ac.stfc.isis.ibex.e4.app.partstack.opi", MPartStack.class, null);
				
				MPart part = MBasicFactory.INSTANCE.createPart();
			    part.setLabel("OPI " + opiNum);
			    part.setContributionURI("bundleclass://uk.ac.stfc.isis.ibex.synopticlike/uk.ac.stfc.isis.ibex.synopticlike.OpiPart");
			    part.setCloseable(true);
			     
			    stacks.get(0).getChildren().add(part);
			    partService.showPart(part, PartState.ACTIVATE);
			    
			    stacks.get(0).setVisible(true);
			    
			    opiNum += 1;
			}
		});

		
	}

	@Focus
	public void setFocus() {
		
	}

}
