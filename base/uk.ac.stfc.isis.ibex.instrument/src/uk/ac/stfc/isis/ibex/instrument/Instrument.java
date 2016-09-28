
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
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

package uk.ac.stfc.isis.ibex.instrument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.epics.pvmanager.ChannelHandler;
import org.epics.pvmanager.PVManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.instrument.internal.LocalHostInstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.list.InstrumentListObservable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class Instrument implements BundleActivator {

    private static final Logger LOG = IsisLog.getLogger("Instrument");

    private static Instrument instance;
    private static BundleContext context;

    public static Instrument getInstance() {
        return instance;
    }

    private SettableUpdatedValue<String> instrumentName = new SettableUpdatedValue<>();
    private InstrumentInfo instrumentInfo;
    private final InstrumentInfo localhost;

    private final InstrumentListObservable instrumentsObservable = new InstrumentListObservable(LOG);
    private Collection<InstrumentInfo> instruments = new ArrayList<InstrumentInfo>();

    private final BaseObserver<Collection<InstrumentInfo>> instrumentsListener =
            new BaseObserver<Collection<InstrumentInfo>>() {
                @Override
                public void onValue(Collection<InstrumentInfo> value) {
                    instruments = new ArrayList<>();
                    instruments.add(localhost);

                    List<InstrumentInfo> instrumentsAlphabetical = new ArrayList<>(value);
                    Collections.sort(instrumentsAlphabetical, alphabeticalNameComparator());
                    instruments.addAll(instrumentsAlphabetical);
                }

                @Override
                public void onError(Exception e) {
                }

                @Override
                public void onConnectionStatus(boolean isConnected) {
                }
            };

    private final Preferences initalPreference =
            ConfigurationScope.INSTANCE.getNode("uk.ac.stfc.isis.ibex.instrument").node("preferences");

    private static String initialInstrument = "initial";

    public Instrument() {
        instance = this;
        localhost = new LocalHostInstrumentInfo();
        instrumentsObservable.addObserver(instrumentsListener);
        setInstrument(initialInstrument());
    }

    public UpdatedValue<String> name() {
        return instrumentName;
    }

    public String getPvPrefix() {
        return instrumentInfo.pvPrefix();
    }

    public Collection<InstrumentInfo> instruments() {
        return instruments;
    }

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
        Instrument.context = bundleContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        instrumentsObservable.close();
        Instrument.context = null;
    }

    public void setInstrument(InstrumentInfo selectedInstrument) {
        this.instrumentInfo = selectedInstrument;

        if (!instrumentInfo.hasValidHostName()) {
            LOG.error("Invalid host name:" + instrumentInfo.hostName());
            return;
        }

        instrumentName.setValue(selectedInstrument.name());

        updateExtendingPlugins(selectedInstrument);
        logNumberOfChannels();
    }

    private void logNumberOfChannels() {
        int count = 0;
        Iterator<Map.Entry<String, ChannelHandler>> it =
                PVManager.getDefaultDataSource().getChannels().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ChannelHandler> pair = it.next();
            ChannelHandler ch = pair.getValue();
            if (ch.isConnected()) {
                count++;
            }
        }
        LOG.debug("Changing to instrument " + instrumentInfo.hostName() + ", Number of connected channels = "
                + Integer.toString(count));
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
                e.printStackTrace();
            }
        }
    }

    private InstrumentInfo initialInstrument() {
        final String initalName = initalPreference.get(initialInstrument, localhost.name());

        return Iterables.find(instruments(), new Predicate<InstrumentInfo>() {
            @Override
            public boolean apply(InstrumentInfo info) {
                return initalName.endsWith(info.name());
            }
        }, localhost);
    }

    public void setInitial() {
        initalPreference.put(initialInstrument, currentInstrument().name());

        try {
            // forces the application to save the preferences
            initalPreference.flush();
        } catch (BackingStoreException e2) {
            e2.printStackTrace();
        }
    }

    private Comparator<InstrumentInfo> alphabeticalNameComparator() {
        return new Comparator<InstrumentInfo>() {
            @Override
            public int compare(InstrumentInfo info1, InstrumentInfo info2) {
                return info1.name().compareTo(info2.name());
            }
        };
    }
}
