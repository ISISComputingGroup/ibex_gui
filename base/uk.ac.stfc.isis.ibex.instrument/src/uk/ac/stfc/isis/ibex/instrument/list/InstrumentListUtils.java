
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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

/**
 * Given an observable containing a list of instruments, filters out invalid
 * instruments, e.g. due to parsing error.
 * 
 */
public class InstrumentListUtils {
    public static Collection<InstrumentInfo> filterValidInstruments(
            Observable<Collection<InstrumentInfo>> instrumentsRBV) {
        if (!instrumentsRBV.isConnected()) {
            return new ArrayList<>();
        }

        // In case of parsing errors the collection or individual fields may be
        // null
        Collection<InstrumentInfo> instruments = instrumentsRBV.getValue();
        if (instruments == null) {
            return new ArrayList<>();
        }

        Iterable<InstrumentInfo> validInstruments = Iterables.filter(instruments, new Predicate<InstrumentInfo>() {
            @Override
            public boolean apply(InstrumentInfo item) {
                return item.name() != null;
            }
        });

        return Lists.newArrayList(validInstruments);
    }
}

