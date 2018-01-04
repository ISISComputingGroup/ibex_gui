
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

package uk.ac.stfc.isis.ibex.help;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.help.internal.Observables;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * Activator for the backend that holds help information.
 */
public class Help extends Closer implements BundleActivator {

	private static BundleContext context;
	private static Help instance;

    /**
     * @return The bundle context for the bundle.
     */
	static BundleContext getContext() {
		return context;
	}

	private Observables observables;
	private UpdatedValue<String> serverRevision;
	private UpdatedValue<String> date;
	
    /**
     * Constructor that creates and registers the observables.
     */
	public Help() {
		instance = this;
        observables = new Observables();
		serverRevision = registerForClose(new TextUpdatedObservableAdapter(observables.serverRevision));
		date = registerForClose(new TextUpdatedObservableAdapter(observables.serverDate));
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		Help.context = bundleContext;
	}

    /**
     * @return The singleton instance of this activator.
     */
	public static Help getInstance() {
		return instance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		Help.context = null;
		close();
	}
	
    /**
     * @return An updating string that holds the server revision.
     */
	public UpdatedValue<String> revision() {
		return serverRevision;
	}
	
    /**
     * @return An updating string that holds the date of the server revision.
     */
	public UpdatedValue<String> date() {
		return date;
	}

    public String GetPvPrefix() {
        return Instrument.getInstance().getPvPrefix();
    }
}
