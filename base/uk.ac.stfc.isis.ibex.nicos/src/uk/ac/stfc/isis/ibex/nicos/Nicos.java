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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.nicos.comms.RepeatingJob;
import uk.ac.stfc.isis.ibex.nicos.comms.ZMQSession;
import uk.ac.stfc.isis.ibex.nicos.comms.ZMQWrapper;

/**
 * Constructor for the NICOS plugin, which provides a connection between NICOS
 * and the GUI.
 */
public class Nicos extends Plugin {
	
	private static BundleContext context;
	private static Nicos instance;

    private final NicosModel model;

    private static final int CONNECT_POLL_TIME = 10000;

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
        ZMQSession session = new ZMQSession(new ZMQWrapper());
        RepeatingJob connectionJob = new RepeatingJob("NICOSConnection", CONNECT_POLL_TIME) {
            
            @Override
            protected IStatus doTask(IProgressMonitor monitor) {
                model.connect(Instrument.getInstance().currentInstrument());
                return Status.OK_STATUS;
            }
        };
        model = new NicosModel(session, connectionJob);
    }

    /**
     * @return The model that is connected to nicos on the instrument.
     */
    public NicosModel getModel() {
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
