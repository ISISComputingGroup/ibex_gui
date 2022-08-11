
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2021 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A model containing the visibility settings for each perspective in the GUI.
 * 
 * Perspective visibility setting can be saved either on the server, and so affect all clients looking at the instrument, 
 * or locally, only affecting this client.
 */
public class PerspectiveInfo extends ModelObject {
    private String name;
    private String id;
    private boolean remoteVisible;
    private boolean localVisible;
    private boolean remoteEditable;
    
    /**
     * Instantiates a new perspective info model.
     * 
     * @param name
     *            the human readable name of the perspective
     * @param id
     *            the eclipse ID of the perspective
     * @param remoteVisible
     *            whether the perspective starts as being visible if using the server settings
     * @param localVisible
     *            whether the perspective starts as being visible if using the local settings
     */
    public PerspectiveInfo(String name, String id, boolean remoteVisible, boolean localVisible) {
        this.name = name;
        this.id = id;
        this.remoteVisible = remoteVisible;
        this.localVisible = localVisible;
        this.remoteEditable = true;
    }

    /**
     * Get the human readable name of the perspective.
     *
     * @return the name of the perspective
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the eclipse ID for the perspective.
     * 
     * @return The eclipse ID for the perspective.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets whether this perspective is visible when using the remote server settings.
     * 
     * @return true if it is visible under these settings, false otherwise
     */
    public boolean getVisibleRemotely() {
        return remoteVisible;
    }

    /**
     * Set whether the perspective should be visible if using the remote server settings.
     * 
     * @param remoteVisible true to set the perspective to be visible when using the server settings
     */
    public void setVisibleRemotely(boolean remoteVisible) {
        firePropertyChange("visibleRemotely", this.remoteVisible, this.remoteVisible = remoteVisible);
    }
    
    /**
     * Set whether the remote server perspective settings are currently editable by the client.
     * 
     * @param remoteEditable true if the remote settings are editable, false otherwise
     */
    public void setRemoteEditable(boolean remoteEditable) {
        firePropertyChange("remoteEditable", this.remoteEditable, this.remoteEditable = remoteEditable);
    }

    /**
     * Get whether the remote server perspective settings are currently editable by the client.
     * 
     * @return true if the remote settings are editable, false otherwise
     */
    public boolean getRemoteEditable() {
        return remoteEditable;
    }
    
    /**
     * Gets whether this perspective is visible when using the settings local to this client.
     * 
     * @return true if it is visible under these settings, false otherwise
     */
    public boolean getVisibleLocally() {
        return localVisible;
    }
    
    /**
     * Sets whether this perspective is visible when using the settings local to this client.
     * 
     * @param localVisible true if it is visible under these settings, false otherwise
     */
    public void setVisibleLocally(boolean localVisible) {
        firePropertyChange("visibleLocally", this.localVisible, this.localVisible = localVisible);
    }
}
