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

package uk.ac.stfc.isis.ibex.log;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Log extends AbstractUIPlugin {
    private static Log instance;
    private static BundleContext context;

    public static Log getInstance() {
	return instance;
    }

    public static Log getDefault() {
	return instance;
    }

    private final LogModel model;
    private LogCounter counter;

    public Log() {
	super();
	instance = this;
	model = new LogModel();
	counter = new LogCounter();
	model.addMessageConsumer(counter);
	model.start();
    }

    public ILogMessageProducer producer() {
	return model;
    }

    public LogCounter getCounter() {
	return counter;
    }

    static BundleContext getContext() {
	return context;
    }

    public void start(BundleContext bundleContext) throws Exception {
	Log.context = bundleContext;
    }

    public void stop(BundleContext bundleContext) throws Exception {
	Log.context = null;
	model.stop();
    }

    public void clearMessages() {
	model.clearMessages();
    }
}
