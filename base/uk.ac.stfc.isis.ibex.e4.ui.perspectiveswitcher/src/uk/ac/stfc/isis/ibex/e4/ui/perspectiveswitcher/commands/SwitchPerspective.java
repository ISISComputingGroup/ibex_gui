package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.commands;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;

public class SwitchPerspective {
	
	@Inject public MApplication app;
	
	@Inject public EPartService partService;
	
	@Inject public EModelService modelService;
	
	@Execute
	public void execute(@Named("uk.ac.stfc.isis.ibex.e4.client.switchperspectives.perspectiveid") String id) {
		System.out.println("HELLO: " + id);
	    (new PerspectivesProvider(app, partService, modelService)).switchPerspective(id);
			
	}
}
