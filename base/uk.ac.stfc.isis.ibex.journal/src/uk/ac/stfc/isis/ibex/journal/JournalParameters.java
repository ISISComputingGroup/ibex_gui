
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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

package uk.ac.stfc.isis.ibex.journal;

import java.util.Calendar;
import java.util.Optional;

/**
 * Encapsulates journal search parameters.
 */
public class JournalParameters {
    private JournalField field = JournalField.RUN_NUMBER;
    private Optional<String> searchString = Optional.empty();
    private Optional<Integer> fromNumber = Optional.empty();
    private Optional<Integer> toNumber = Optional.empty();
    private Optional<Calendar> fromTime = Optional.empty();
    private Optional<Calendar> toTime = Optional.empty();
    
    private boolean set = false;
    
    /**
     * @return Returns true if any of the optional parameters are not empty, false otherwise.
     */
    public boolean hasOptionalParameters() {
        return searchString.isPresent() || fromNumber.isPresent() || toNumber.isPresent() || fromTime.isPresent() || toTime.isPresent();
    }
    
    /**
     * Sets the field.
     * @param field The field to set.
     */
    public void setField(JournalField field) {
        this.field = field;
    }
    
    /**
     * Sets the search string. A set other than setField can only be called once.
     * @param searchString The search string to set.
     */
    public void setSearchString(Optional<String> searchString) {
        if (set) {
            throw new IllegalStateException("Parameters can only be of one type.");
        }
        this.searchString = searchString;
        set = true;
    }
    
    /**
     * Sets the to and from numbers. A set other than setField can only be called once.
     * @param fromNumber The from number to set.
     * @param toNumber The to number to set.
     */
    public void setNumbers(Optional<Integer> fromNumber, Optional<Integer> toNumber) {
        if (set) {
            throw new IllegalStateException("Parameters can only be of one type.");
        }
        this.fromNumber = fromNumber;
        this.toNumber = toNumber;
        set = true;
    }
    
    /**
     * Sets the to and from times.  A set other than setField can only be called once.
     * @param fromTime The from time to set.
     * @param toTime The to time to set.
     */
    public void setTimes(Optional<Calendar> fromTime, Optional<Calendar> toTime) {
        if (set) {
            throw new IllegalStateException("Parameters can only be of one type.");
        }
        this.fromTime = fromTime;
        this.toTime = toTime;
        set = true;
    }

    /**
     * @return the field
     */
    public JournalField getField() {
        return field;
    }

    /**
     * @return the searchString
     */
    public Optional<String> getSearchString() {
        return searchString;
    }

    /**
     * @return the fromNumber
     */
    public Optional<Integer> getFromNumber() {
        return fromNumber;
    }

    /**
     * @return the toNumber
     */
    public Optional<Integer> getToNumber() {
        return toNumber;
    }

    /**
     * @return the fromTime
     */
    public Optional<Calendar> getFromTime() {
        return fromTime;
    }

    /**
     * @return the toTime
     */
    public Optional<Calendar> getToTime() {
        return toTime;
    }
}
