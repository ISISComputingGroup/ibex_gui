package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class PerspectiveInfo extends ModelObject {
    private String name;
    private boolean remoteVisible;
    private boolean localVisible;
    
    /**
     * Instantiates a new perspective info model.
     *
     * @param configServer
     *            the Config Server
     * @param name
     *            the name
     * @param isRunning
     *            whether the IOC is running
     * @param description
     *            description of the IOC
     */
    public PerspectiveInfo(String name, boolean remoteVisible, boolean localVisible) {
        this.name = name;
        this.remoteVisible = remoteVisible;
        this.localVisible = localVisible;
    }

    /**
     * Gets the name of the IOC corresponding to this IOCState.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        firePropertyChange("name", this.name, this.name = name);
    }
    
    /**
     * Gets whether this IOC is running or not.
     *
     * @return true if it is running; false otherwise
     */
    public boolean getVisibleRemotely() {
        return remoteVisible;
    }

    public void setVisibleRemotely(boolean remoteVisible) {
        firePropertyChange("visibleRemotely", this.remoteVisible, this.remoteVisible = remoteVisible);
    }
    
    /**
     * Gets the description.
     *
     * @return the description
     */
    public boolean getVisibleLocally() {
        return localVisible;
    }
    
    public void setVisibleLocally(boolean localVisible) {
        firePropertyChange("visibleLocally", this.localVisible, this.localVisible = localVisible);
    }
}
