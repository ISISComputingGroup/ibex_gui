
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.instrument.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Given an observable containing a list of instruments, filters out invalid
 * instruments, e.g. due to parsing error.
 * 
 */
public final class InstrumentListUtils {	
    /**
     * An empty constructor required for check style.
     */
    private InstrumentListUtils() {
    }

    /**
     * Location of instrument whitelist in preferences file
     */
    private static final String LOCAL_INSTRUMENT_LIST_PREFS = "inst-whitelist";
    private static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.instrument";
    
    /**
     * Given an observable for a list of instruments, filter out invalid
     * instruments.
     * 
     * @param instruments instruments to filter
     * @param logger a logger to log information and warnings about invalid
     *            instruments
     * 
     * @return the valid instruments extracted from the input observable
     */
    public static Collection<InstrumentInfo> filterValidInstruments(
            Collection<InstrumentInfo> instruments, Logger logger,
            Optional<ArrayList<String>> instrumentWhitelist) {
        if (instruments == null) {
            logger.warn("Error while parsing instrument list PV - no instrument could be read");
            return new ArrayList<>();
        }

        Iterable<InstrumentInfo> validInstruments = Iterables.filter(instruments, new Predicate<InstrumentInfo>() {
            @Override
            public boolean apply(InstrumentInfo item) {
                boolean instrumentWhitelisted;
                boolean nameNotNull = item.name() != null;
                
                if (!instrumentWhitelist.isPresent()) {
                	instrumentWhitelisted = true;
                } else if (instrumentWhitelist.get().size() == 0) {
                	instrumentWhitelisted = true;
                } else {
                	instrumentWhitelisted = instrumentWhitelist.get().contains(item.name());
                }
                
            	return nameNotNull & instrumentWhitelisted;
            }
        });
        

        Collection<InstrumentInfo> returnValue = Lists.newArrayList(validInstruments);
        if (returnValue.size() < instruments.size()) {
            logger.warn("Error while parsing instrument list PV - one or more instruments could not be read");
        } else {
            logger.info("Instrument list PV was read successfully");
        }

        return returnValue;
    }




    private static Optional<ArrayList<String>> loadWhitelist() {
        ArrayList<String> instrumentWhitelist = new ArrayList<String>();
        IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID);
        Preferences whitelistPrefs = prefs.node(LOCAL_INSTRUMENT_LIST_PREFS);
        for (String inst:whitelistPrefs.get("whitelist", "").split(",")) {
			if (!inst.trim().isBlank()) {
				instrumentWhitelist.add(inst);
			}
		}
        
        if (instrumentWhitelist.size() > 0) {
        	return Optional.of(instrumentWhitelist);
        } else {
        	return Optional.absent();
        }
    }
    
    
    /**
     * Combine the localhost and instruments list to create one list to show the
     * user. Localhost should be at the top. If there is an instrument with the
     * same host name as localhost this name should be used instead of the
     * current name and that instrument should not appear in the list.
     * 
     * @param instruments list of instruments
     * @param localhost the localhost instrument
     * @return list of instruments alphetically order with localhost at the top
     */
    public static Collection<InstrumentInfo> combineInstrumentsAndLocalHost(
            Collection<InstrumentInfo> instruments,
            InstrumentInfo localhost) {

        List<InstrumentInfo> instrumentsAlphabetical = new ArrayList<>();

        InstrumentInfo topInstrument = localhost;
        for (InstrumentInfo instrument : instruments) {
            if (instrument.hostName().equals(localhost.name())) {
                topInstrument = new InstrumentInfo(instrument.name(), instrument.pvPrefix(), localhost.hostName());
            } else {
                instrumentsAlphabetical.add(instrument);
            }
        }

        Collections.sort(instrumentsAlphabetical, (InstrumentInfo info1, InstrumentInfo info2) -> info1.name().compareTo(info2.name()));
        instrumentsAlphabetical.add(0, topInstrument);
        return instrumentsAlphabetical;
    }

    /**
     * Return list of instruments filtered by the whitelist, if present.
     * @param input The current list of instruments.
     * @param logger The ibex logger.
     * @return list of instruments filtered by whitelist and invalid values removed.
     */
	public static Collection<InstrumentInfo> filterValidInstruments(Collection<InstrumentInfo> input, Logger logger) {
		return filterValidInstruments(input, logger, loadWhitelist());
	}
}

