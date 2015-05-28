package uk.ac.stfc.isis.ibex.ui.synoptic.component;

import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.BeamlineComposite;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.BeamlineCompositeContainer;

public final class ComponentView {
	
	private ComponentView() { }

	public static BeamlineComposite create(BeamlineCompositeContainer parent, Component component) {
		BeamlineComposite target = component.components().isEmpty() 
									? createBasicComponent(parent, component) 
									: createGroupView(parent, component);
				
		parent.registerBeamlineTarget(target);
		
		return target;
	}

	private static GroupView createGroupView(Composite parent, Component component) {
		return new GroupView(parent, component);
	}

	private static BasicComponent createBasicComponent(Composite parent, Component component) {
		BasicComponent componentView = new BasicComponent(parent);
		componentView.setName(component.name());
		componentView.setImage(ComponentIcons.iconForComponent(component));
		componentView.setProperties(component);
		if (component.target() != null) {
			componentView.setTargetName(component.target().name());
		}
		return componentView;
	}
}
