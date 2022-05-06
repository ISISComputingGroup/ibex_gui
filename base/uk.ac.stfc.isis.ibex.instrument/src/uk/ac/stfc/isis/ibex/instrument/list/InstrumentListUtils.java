
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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.internal.LocalHostInstrumentInfo;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Given an observable containing a list of instruments, filters out invalid
 * instruments, e.g. due to parsing error.
 * 
 */
public final class InstrumentListUtils {	
	
	private static final Logger LOG = IsisLog.getLogger(InstrumentListUtils.class);
	
	private static final URL ALLOWED_GROUPS_PATH = new InstrumentListUtils().getClass().getResource("/resources/allowed_groups.conf");
	
    /**
     * An empty constructor required for check style.
     */
    private InstrumentListUtils() {
    }
    
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
            Collection<InstrumentInfo> instruments, Logger logger) {
        if (instruments == null) {
            logger.warn("Error while parsing instrument list PV - no instrument could be read");
            return new ArrayList<>();
        }

        Iterable<InstrumentInfo> validInstruments = Iterables.filter(instruments, new Predicate<InstrumentInfo>() {
            @Override
            public boolean apply(InstrumentInfo item) {
            	return item.name() != null;
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


    /**
     * Given an observable for a list of instruments, filter out invalid
     * instruments.
     * 
     * @param instruments instruments to filter
     * @param allowedGroups list of allowed groups
     * 
     * @return the valid instruments extracted from the input observable
     */
    public static Collection<InstrumentInfo> filterByAllowedGroup(
            Collection<? extends InstrumentInfo> instruments,
            Optional<? extends List<String>> allowedGroups) {
    	
        Predicate<InstrumentInfo> isInstAllowed = new Predicate<>() {
            @Override
            public boolean apply(InstrumentInfo instrumentInfo) {
                if (allowedGroups.isEmpty()) {
                	// No allowed groups file defined - so allowed to see all instruments.
                	return true;
                } else if (instrumentInfo.name().equals(new LocalHostInstrumentInfo().name())) {
                	// Always allowed to see localhost
                	return true;
                } else {
                	// Check if any of the groups that this instrument is in is in the list of groups
                	// which we're allowed to observe.
                	final Collection<String> unwrappedGroups = allowedGroups.get();
                	return unwrappedGroups.stream()
                			.anyMatch(group -> instrumentInfo.groups().contains(group));
                }
            }
        };
        

        return instruments.stream()
        		.filter(isInstAllowed)
        		.collect(Collectors.toList());
    }


    /**
     * Gets a list of allowed groups that this client is permitted to observe.
     * 
     * Examples:
     *     Optional<ArrayList<String>> = ["MUON", "SANS"]  - this client is allowed to look at MUON and SANS instruments
     *     Optional<ArrayList<String>> = Optional.empty()  - this client is allowed to look at any instrument
     *     
     * If present, the list will always contain at least one value.
     * @return A list of allowed groups
     */
    private static Optional<List<String>> loadAllowedGroups() {
        try {
        	String content;
        	try {
			    content = Resources.toString(ALLOWED_GROUPS_PATH, StandardCharsets.UTF_8).strip();
        	} catch (NullPointerException e) {
        		// Above will throw a null pointer exception in tests as they don't have access to resources,
        		return Optional.empty();
        	}
			
			if (content.isEmpty()) {
				return Optional.empty();
			}
			
			List<String> instrumentAllowList = content.contains(",") ? Arrays.asList(content.split(",")) : Arrays.asList(content);
			
			if (instrumentAllowList.size() > 0) {
	        	return Optional.of(instrumentAllowList);
	        } else {
	        	return Optional.empty();
	        }
			
		} catch (IOException e) {
			LOG.warn("Cannot read allowed groups - assuming all groups are allowed");
			return Optional.empty();
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
                topInstrument = new InstrumentInfo(instrument.name(), instrument.pvPrefix(), localhost.hostName(), new ArrayList<>());
            } else {
                instrumentsAlphabetical.add(instrument);
            }
        }

        Collections.sort(instrumentsAlphabetical, (InstrumentInfo info1, InstrumentInfo info2) -> info1.name().compareTo(info2.name()));
        instrumentsAlphabetical.add(0, topInstrument);
        return instrumentsAlphabetical;
    }

    /**
     * Return list of instruments filtered by the allowlist, if present.
     * @param input The current list of instruments.
     * @return list of instruments filtered by allowlist and invalid values removed.
     */
	public static Collection<InstrumentInfo> applyInstAllowedGroup(Collection<InstrumentInfo> input) {
		return filterByAllowedGroup(input, loadAllowedGroups());
	}

	/**
	 * Check if allow list exists.
	 * @return True if allow list exists.
	 */
	public static boolean allowListExists() {
		return loadAllowedGroups().isPresent();
	}
}

