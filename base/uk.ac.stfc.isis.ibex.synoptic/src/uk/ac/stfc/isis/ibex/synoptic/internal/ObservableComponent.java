package uk.ac.stfc.isis.ibex.synoptic.internal;

import java.util.List;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.ReadableComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.Target;
import uk.ac.stfc.isis.ibex.synoptic.model.WritableComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.TargetBuilder;

public class ObservableComponent extends BaseComponent {

	private final Variables variables;
	private Target target;

	public ObservableComponent(ComponentDescription componentDescription, Variables variables) {
		super(componentDescription);
		this.variables = variables;
		
		addProperties(componentDescription.pvs());
		addComponents(componentDescription.components());
		
		target = new TargetBuilder(this, componentDescription.target()).target();
		
		String targetName = target == null ? "null " : target.name();
		Synoptic.LOG.info("Component: " + name() + " targets " + targetName);
	}
	
	private ObservableComponent(ObservableComponent other) {
		super(other);
		this.variables = other.variables;
		target = other.target;
	}
	
	@Override
	public Target target() {
		return target;
	}

	@Override
	public void setTarget(Target target) {
		this.target = target;		
	}
	
	@Override
	public Component copy() {
		return new ObservableComponent(this);
	}
	
	private void addProperties(List<PV> pvs) {
		for (PV pv : pvs) {
			addProperty(getProperty(pv));
		}
	}

	private void addComponents(List<ComponentDescription> components) {
		for (ComponentDescription component : components) {
			super.addComponent(new ObservableComponent(component, variables));
		}
	}

	private ComponentProperty getProperty(PV pv) {
		InitialiseOnSubscribeObservable<String> reader = variables.defaultReader(pv.address());
		switch(pv.recordType().io()) {
			case WRITE:
				Writable<String> destination = variables.defaultWritable(pv.address());
				InitialiseOnSubscribeObservable<String> readerWithoutUnits = variables.defaultReaderWithoutUnits(pv.address());
				return new WritableComponentProperty(pv.displayName(), readerWithoutUnits, destination);
			case READ:
			default:
				return new ReadableComponentProperty(pv.displayName(), reader);
		}
	}
}
