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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.targetselector;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

/**
 * Only allows text input if the new text is a combination of alphanumerics,
 * spaces and underscores.
 */
public class ComponentNameVerifier implements VerifyListener {
    /**
     * A regex matching any combination of alphanumerics, spaces and
     * underscores.
     */
    private static final String ALPHANUMERIC_SPACES_AND_UNDERSCORES_REGEX = "[a-zA-Z0-9_ ]*";

    /**
     * Only allows text input if the new text is a combination of alphanumerics,
     * spaces and underscores.
     * 
     * @see {org.eclipse.swt.events.VerifyText}
     * 
     * @param e
     *            the eclipse event.
     */
    @Override
    public void verifyText(VerifyEvent e) {
        e.doit = e.text.matches(ALPHANUMERIC_SPACES_AND_UNDERSCORES_REGEX);
    }
}
