
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

    /**
     * Logs messages related to the instrument.
     */
    private static final Logger LOG = IsisLog.getLogger("Instrument");

    /**
     * Singleton instance of the instrument.
     */
    private static Instrument instance;

    /**
     * The context for the bundle.
     */
    private static BundleContext context;

    /**
     * @return The instrument instance
     */
    public static Instrument getInstance() {
        return instance;
    }

    /**
     * The preference lookup key for the initial instrument.
     */
    private static String initialInstrument = "initial";

    /**
     * The current instrument name as an updated value - needed for instance by
     * banner.
     */
    private SettableUpdatedValue<String> instrumentName = new SettableUpdatedValue<>();

    /**
     * The current instrument information.
     */
    private InstrumentInfo instrumentInfo;
    
    /**
     * The local instrument information.
     */
    private final InstrumentInfo localhost = new LocalHostInstrumentInfo();

    /**
     * An observable for the instrument list.
     */
    private final InstrumentListObservable instrumentsObservable = new InstrumentListObservable(LOG);

    /**
     * The current list of instruments read from the instrument observable. We
     * always start with localhost.
     */
    @SuppressWarnings("serial")
    private Collection<InstrumentInfo> instruments = new ArrayList<InstrumentInfo>() {
        {
            add(localhost);
        }
    };

    /**
     * Initial instrument preference.
     */
    private final Preferences initalPreference =
            ConfigurationScope.INSTANCE.getNode("uk.ac.stfc.isis.ibex.instrument").node("preferences");

    /**
     * Constructor. Create a singleton instance of the instrument and set an
     * observer for the instrument list.
     */
    public Instrument() {
        instance = this;
        setInstrumentsObserver();
    }

    /**
     * Adds an observer to the instrument list observable.
     */
    private void setInstrumentsObserver() {
        instrumentsObservable.addObserver(new BaseObserver<Collection<InstrumentInfo>>() {

            boolean firstConnection = true;

            @Override
            public void onValue(Collection<InstrumentInfo> value) {
                Collection<InstrumentInfo> newInstruments = new ArrayList<>();
                // Localhost is always added to the instrument list separately
                newInstruments.add(localhost);

                List<InstrumentInfo> instrumentsAlphabetical = new ArrayList<>(value);
                Collections.sort(instrumentsAlphabetical, alphabeticalNameComparator());
                newInstruments.addAll(instrumentsAlphabetical);
                instruments = newInstruments;
            }

            @Override
            public void onError(Exception e) {
                LOG.error("Error whilst reading instrument list : " + e.getMessage());
            }

            @Override
            public void onConnectionStatus(boolean isConnected) {
                if (isConnected) {
                    firstConnection = false;
                } else if (!firstConnection) {
                    LOG.error("Connection to the instrument list has been lost");
                }
            }
        });
        setInstrument(initialInstrument());
    }

    /**
     * @return The current instrument name as an updated value.
     */
    public UpdatedValue<String> name() {
        return instrumentName;
    }

    /**
     * @return The PV prefix for the current instrument.
     */
    public String getPvPrefix() {
        return instrumentInfo.pvPrefix();
    }

    /**
     * @return The current list of instruments. Note this is read from an
     *         observable so can vary with time.
     */
    public Collection<InstrumentInfo> getInstruments() {
        return instruments;
    }

    /**
     * @return The bundle context.
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
        // instrumentsObservable.close();
        Instrument.context = null;
    }

    /**
     * Sets the current instrument.
     * 
     * @param selectedInstrument The instrument to switch to.
     */
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

    /**
     * @return The information about the current instrument.
     */
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
                InstrumentUtils.logErrorWithStackTrace(LOG, "Unable to update extended plugins", e);
            }
        }
    }

    /**
     * @return Get the information for the initial instrument. Currently fixed
     *         as localhost. Requires that localhost always be added to the
     *         instrument list at startup.
     */
    private InstrumentInfo initialInstrument() {
        
        // We can't set the initial instrument until we've got some values from
        // the PV
        return waitForInstrument(initalPreference.get(initialInstrument, localhost.name()));
    }

    /**
     * Set the initial instrument which is used at the subsequent startup.
     */
    public void setInitial() {
        initalPreference.put(initialInstrument, currentInstrument().name());

        try {
            // forces the application to save the preferences
            initalPreference.flush();
        } catch (BackingStoreException e) {
            InstrumentUtils.logErrorWithStackTrace(LOG, "Unable to set initial instrument", e);
        }
    }

    /**
     * Compares instrument infos by their name in alphabetical order.
     * 
     * @return The comparison value of two instrument info names alphabetically
     */
    private static Comparator<InstrumentInfo> alphabeticalNameComparator() {
        return new Comparator<InstrumentInfo>() {
            @Override
            public int compare(InstrumentInfo info1, InstrumentInfo info2) {
                return info1.name().compareTo(info2.name());
            }
        };
    }
    
    /**
     * Waits for the specified instrument to appear in the instrument list.
     * 
     * @param name Instrument name to wait for
     * @return The instrument info for the corresponding instrument
     */
    public InstrumentInfo waitForInstrument(final String name) {
        final Predicate<InstrumentInfo> predicate = new Predicate<InstrumentInfo>() {
            @Override
            public boolean apply(InstrumentInfo info) {
                return name.endsWith(info.name());
            }
        };

        final int maxRetries = 200;
        final int waitLength = 100; // milliseconds
        int i = 0;
        while (!Iterables.any(instruments, predicate) && i++ <= maxRetries) {
            
            try {
                Thread.sleep(waitLength);
            } catch (InterruptedException e) {
                InstrumentUtils.logErrorWithStackTrace(LOG, "System interrupted whilst waiting for instrument", e);
            }
        }
        return Iterables.find(instruments, predicate, localhost);
    }
}
