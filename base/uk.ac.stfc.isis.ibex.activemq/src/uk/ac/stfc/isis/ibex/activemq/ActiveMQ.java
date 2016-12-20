/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.activemq;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.activemq.message.IMessageParser;
import uk.ac.stfc.isis.ibex.instrument.Instrument;

/**
 * Called when the plugin is first started.
 */
public class ActiveMQ extends AbstractUIPlugin {
    private static ActiveMQ instance;
	private static BundleContext context;
	
    private List<MQConnection> connections = new ArrayList<>();

    /**
     * @return The singleton instance of this class.
     */
    public static ActiveMQ getInstance() {
        return instance;
    }

    /**
     * Creates the singleton, this is called by eclipse when the plugin is
     * loaded.
     */
    public ActiveMQ() {
        super();
        instance = this;
    }

    /**
     * @return The context for the plugin.
     */
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		ActiveMQ.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		ActiveMQ.context = null;
	}

    /**
     * Get a new Active MQ connection.
     * 
     * @param port
     *            The port to connect to.
     * @param topic
     *            The topic or queue to connect to.
     * @param parser
     *            A parser that will parse the data from ActiveMQ.
     * @return The Active MQ Connection.
     */
    public MQConnection getNewConnection(String port, String topic, IMessageParser parser) {
        String currentInstrument = Instrument.getInstance().currentInstrument().hostName();
        MQConnection handler = new MQConnection(currentInstrument, port, topic, parser);
        connections.add(handler);
        return handler;
    }

    /**
     * @return All the connections created through this plugin.
     */
    public List<MQConnection> getConnections() {
        return connections;
    }
}
