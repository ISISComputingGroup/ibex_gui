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

package uk.ac.stfc.isis.ibex.alarm;

import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModel;
import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModelListener;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Class which provides an interaction between the perspective switcher and the alarm system
 */
public class Alarm extends AbstractUIPlugin {

	private static BundleContext context;
	private static Alarm instance;
	
	public static Alarm getInstance() {
		return instance;
	    }

	public static Alarm getDefault() {
		return instance;
	}

	private AlarmClientModel alarmModel;
	private AlarmCounter counter;
	AlarmClientModelListener listener;
	
	public Alarm() {
		super();
		instance = this;
		try {
			alarmModel = AlarmClientModel.getInstance();
		} catch (Exception e) {
			// Not sure what to do about this, so not doing anything at the moment
		}
		counter = new AlarmCounter(alarmModel);
	}

	
	static BundleContext getContext() {
		return context;
	}
	
	
	public void start(BundleContext bundleContext) throws Exception {
		Alarm.context = bundleContext;
	    }

	    public void stop(BundleContext bundleContext) throws Exception {
		Alarm.context = null;
	    }
	
	/**
	 * @return the counter being used
	 */
	public AlarmCounter getCounter() {
		return counter;
	}
	
}
