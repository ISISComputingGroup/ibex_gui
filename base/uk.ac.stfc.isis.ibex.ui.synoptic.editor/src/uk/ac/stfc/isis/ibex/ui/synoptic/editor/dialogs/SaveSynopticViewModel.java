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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;

/**
 * The view model for the save synoptic dialog box.
 */
public class SaveSynopticViewModel extends ErrorMessageProvider {
    private static final int MAX_SYNOPTIC_NAME_LENGTH = 30;
    private Collection<String> existingSynoptics = SynopticInfo.names(Synoptic.getInstance().availableSynoptics());
    private String synopticName;
    private Boolean savingAllowed;

    /**
     * Constructor for the view model.
     * 
     * @param currentName
     *            The original name of the synoptic.
     */
    public SaveSynopticViewModel(String currentName) {
        setSynopticName(currentName);
    }

    /**
     * Checks if the synoptic name is a duplicate.
     * 
     * @return True if the name is a duplicate.
     */
    public boolean isDuplicate() {
        return isDuplicate(synopticName);
    }

    private boolean isDuplicate(final String name) {
        return Iterables.any(existingSynoptics, new Predicate<String>() {
            @Override
            public boolean apply(String existing) {
                return existing.equalsIgnoreCase(name);
            }
        });
    }

    /**
     * Set the new name for the synoptic.
     * 
     * @param synopticName
     *            The new synoptic name.
     */
    public void setSynopticName(String synopticName) {
        checkInput(synopticName);
        firePropertyChange("synopticName", synopticName, this.synopticName = synopticName);
    }

    /**
     * Get the synoptic name.
     * 
     * @return The synoptic name
     */
    public String getSynopticName() {
        return synopticName;
    }

    /**
     * Set whether the synoptic is allowed to be saved.
     * 
     * @param allowed
     *            True if the synoptic can be saved
     */
    public void setSavingAllowed(Boolean allowed) {
        firePropertyChange("savingAllowed", savingAllowed, savingAllowed = allowed);
    }

    /**
     * Get whether the synoptic can be saved.
     * 
     * @return True if the synoptic can be saved.
     */
    public Boolean getSavingAllowed() {
        return savingAllowed;
    }

    private void checkInput(String name) {
        setError(false, null);

        setSavingAllowed(!(checkNameForError(name)));

        checkNameForWarning(name);
    }

    /**
     * @param name
     *            The name to check
     * @return True if the name contains an error
     */
    private boolean checkNameForError(String name) {
        if (Strings.isNullOrEmpty(name)) {
            setError(true, "Name cannot be blank");
        } else if (name.length() > MAX_SYNOPTIC_NAME_LENGTH) {
            setError(true, "Name cannot be more than " + MAX_SYNOPTIC_NAME_LENGTH + " characters long");
        } else if (!name.matches("^[a-zA-Z].*")) {
            setError(true, "Name must start with a character");
        } else if (!name.matches("[a-zA-Z0-9_]*")) {
            setError(true, "Name must only contain alphanumerics and underscores");
        }
        return getError().isError();
    }

    private void checkNameForWarning(String name) {
        if (isDuplicate(name)) {
            setError(true, "A synoptic with this name already exists");
        }
    }
}
