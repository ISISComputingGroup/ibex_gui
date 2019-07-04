
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

import java.util.Optional;

public abstract class JournalSearch<T> {
    private final JournalField field;
    
    
    public abstract JournalSearchWidgetType getWidgetType();
    

    private Optional<T> to = Optional.empty();
    private Optional<T> from;
    
    public JournalSearch(JournalField field) {
        this.field = field;
    }
    
    public JournalField getField() {
        return field;
    }
    
    /**
     * @return whether the search has parameters or not
     */
    public boolean hasParamater() {
        return from.isPresent() || to.isPresent();
    }
    
    /**
     * @return the from
     */
    public Optional<T> getFrom() {
        return from;
    }
    
    /**
     * @param from the from to set
     */
    public void setFrom(Optional<T> from) {
        this.from = from;
    }
    
    /**
     * @return the to
     */
    public Optional<T> getTo() {
        return to;
    }
    
    /**
     * @param to the to to set
     */
    public void setTo(Optional<T> to) {
        this.to = to;
    }
}
