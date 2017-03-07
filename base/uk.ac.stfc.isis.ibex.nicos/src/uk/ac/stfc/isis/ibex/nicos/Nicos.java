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

package uk.ac.stfc.isis.ibex.nicos;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.activemq.ActiveMQ;
import uk.ac.stfc.isis.ibex.activemq.SendReceiveSession;

/**
 * Constructor for the NICOS plugin, which provides a connection between NICOS
 * and the GUI.
 */
public class Nicos extends AbstractUIPlugin {
	
	private static BundleContext context;
	private static Nicos instance;
    private NicosModel model;

    /**
     * @return The instance of this singleton.
     */
	public static Nicos getDefault() {
		return instance;
	}
	
    /**
     * This class creates and manages the connection to NICOS.
     */
	public Nicos() {
        instance = this;
    }

    /**
     * @return The model that is connected to nicos on the instrument.
     */
    public NicosModel getModel() {
        if (model == null) {
            SendReceiveSession sendReceiveQueue =
                    ActiveMQ.getInstance().getSendReceiveQueue("ss_admin", "username", "S2mQzcSPfaVu8KNie22s");
            // TODO remove password and think about closing session
            model = new NicosModel(sendReceiveQueue);
        }
        return model;
	}
	
    /**
     * @return The context for the plugin.
     */
    static BundleContext getContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
     * BundleContext)
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Nicos.context = bundleContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        Nicos.context = null;
    }
}
