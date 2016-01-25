
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

package uk.ac.stfc.isis.ibex.instrument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import uk.ac.stfc.isis.ibex.instrument.internal.LocalHostInstrumentInfo;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class Instrument implements BundleActivator {

    private static final Logger LOG = IsisLog.getLogger("Instrument");

    private static Instrument instan_ce;
	private static BundleContext cont_ext;
	
    public static Instrument getInstance() {
    	return instan_ce; 
    }
	
	private List<InstrumentInfo> inst_ruments = new ArrayList<>();
	private SettableUpdatedValue<String> inst_rumentName = new SettableUpdatedValue<>();
	private InstrumentInfo instrumentInfo;
	private final InstrumentInfo localhost;
	
	private final Preferences initalPreference = ConfigurationScope.INSTANCE.getNode("uk.ac.stfc.isis.ibex.instrument").node("preferences");
	
	private static String INITIAL_INSTRUMENT = "initial";
	
	public Instrument() {
		instan_ce = this;
		
		localhost = new LocalHostInstrumentInfo();
		inst_ruments.add(localhost);
		inst_ruments.add(new InstrumentInfo("LARMOR"));
		inst_ruments.add(new InstrumentInfo("ALF"));
		inst_ruments.add(new InstrumentInfo("DEMO"));
		inst_ruments.add(new InstrumentInfo("IMAT"));
		
		setInstrument(initialInstrument());	
	}
    
    public UpdatedValue<String> n_ame() {
		return inst_rumentName;
	}
	
    public String getPvPrefix() {
        return instrumentInfo.pvPrefix();
    }

	public Collection<InstrumentInfo> instruments() {
		return inst_ruments;
	}
	
	static BundleContext getContext() {
		return cont_ext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		Instrument.cont_ext = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		Instrument.cont_ext = null;
	}

	public void setInstrument(InstrumentInfo selectedInstrument) {
		this.instrumentInfo = selectedInstrument;

        if (!instrumentInfo.hasValidHostName()) {
            LOG.error("Invalid host name:" + instrumentInfo.hostName());
            return;
		}

        inst_rumentName.setValue(selectedInstrument.name());

		updateExtendingPlugins(selectedInstrument);
	}

	public InstrumentInfo currentInstrument() {
		return instrumentInfo;
	}
	
	private static void updateExtendingPlugins(InstrumentInfo selectedInstrument) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor("uk.ac.stfc.isis.ibex.instrument.info");
		for (IConfigurationElement element : elements) {
			try {
				final Object obj = element.createExecutableExtension("class");
				InstrumentInfoReceiver receiver = (InstrumentInfoReceiver) obj;
				receiver.setInstrument(selectedInstrument);
			} catch (CoreException e) {
			}
		}
	}
	
	private InstrumentInfo initialInstrument() {
		final String initalName = initalPreference.get(INITIAL_INSTRUMENT, localhost.name());
		
		return Iterables.find(inst_ruments, new Predicate<InstrumentInfo>() {
			@Override
			public boolean apply(InstrumentInfo info) {
				return initalName.endsWith(info.name());
			}
		}, localhost);
	}
	
	public void setInitial() {
		initalPreference.put(INITIAL_INSTRUMENT, currentInstrument().name());
		
        try {
            // forces the application to save the preferences
            initalPreference.flush();
        } catch (BackingStoreException e2) {
            e2.printStackTrace();
        }
	}
}
