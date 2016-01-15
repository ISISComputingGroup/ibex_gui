
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.configserver.internal;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Editing;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.ObservableEditableConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;

public class ConfigEditing extends Closer implements Editing {

	private final ConfigServer configServer;

	public ConfigEditing(ConfigServer configServer) {
		this.configServer = configServer;
	}
	
	@Override
	public ForwardingObservable<EditableConfiguration> currentConfig() {
		return edit(configServer.currentConfig());
	}

	@Override
	public ForwardingObservable<EditableConfiguration> blankConfig() {
		return edit(configServer.blankConfig());
	}	
	
	@Override
	public ForwardingObservable<EditableConfiguration> config(String configName) {
		return edit(configServer.config(configName));
	}

	@Override
	public ForwardingObservable<EditableConfiguration> component(String componentName) {
		return edit(configServer.component(componentName));
	}
	
	private ForwardingObservable<EditableConfiguration> edit(ClosableObservable<Configuration> config) {
		return registerForClose(new ForwardingObservable<>(new ObservableEditableConfiguration(config, configServer)));
	}
}
