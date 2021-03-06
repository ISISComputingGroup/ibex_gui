
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.synoptic.internal;

import java.util.List;

import uk.ac.stfc.isis.ibex.configserver.AlarmState;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.ReadableComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.WritableComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.TargetBuilder;
import uk.ac.stfc.isis.ibex.targets.Target;

/**
 * Holds all the information relating to a synoptic in the component, such as
 * the PVs, the targets and the target properties.
 */
public class ObservableComponent extends BaseComponent {

	private final Variables variables;
	private Target target;

	/**
	 * Initialise the Observable Component.
	 * @param componentDescription the component description
	 * @param variables the component Variables
	 */
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
        ForwardingObservable<String> valueReader = variables.defaultReaderRemote(pv.address());
        ForwardingObservable<AlarmState> alarmReader = variables.defaultReaderRemoteAlarm(pv.address());
		switch (pv.recordType().io()) {
			case WRITE:
                Writable<String> destination = variables.defaultWritableRemote(pv.address());
                ForwardingObservable<String> readerWithoutUnits = variables
                        .defaultReaderRemoteWithoutUnits(pv.address());
				return new WritableComponentProperty(pv.displayName(), readerWithoutUnits, alarmReader, destination);
			case READ:
			default:
				return new ReadableComponentProperty(pv.displayName(), valueReader, alarmReader);
		}
	}
}
