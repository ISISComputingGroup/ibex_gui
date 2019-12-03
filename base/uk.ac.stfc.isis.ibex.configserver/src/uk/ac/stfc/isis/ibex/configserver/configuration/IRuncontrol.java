package uk.ac.stfc.isis.ibex.configserver.configuration;

/**
 * Interface for classes that implement run-control functionality.
 */
public interface IRuncontrol {
	
    /**
     * Gets whether run-control is enabled.
     * 
     * @return whether run-control is enabled
     */
    Boolean getRunControlEnabled();
	
    /**
     * Sets whether run-control should be enabled.
     * 
     * @param enabled whether run-control should be enabled
     */
    void setRunControlEnabled(Boolean enabled);
    
    /**
     * Gets whether run-control is suspended if the block goes invalid.
     * 
     * @return whether run-control is suspended if the block goes invalid.
     */
    Boolean getSuspendIfInvalid();
	
    /**
     * Sets whether run-control is suspended if the block goes invalid.
     * 
     * @param enabled whether run-control is suspended if the block goes invalid.
     */
    void setSuspendIfInvalid(Boolean enabled);
    
    /**
     * Gets the low limit for run-control.
     * 
     * @return the low limit for run-control
     */
    Double getRunControlLowLimit();

    /**
     * Sets the low limit for run-control.
     * 
     * @param rclow the new low limit for run-control
     */
    void setRunControlLowLimit(Double rclow);

    /**
     * Gets the high limit for run-control.
     * 
     * @return the high limit for run-control
     */
    Double getRunControlHighLimit();

    /**
     * Sets the high limit for run-control.
     * 
     * @param rchigh the new high limit for run-control
     */
    void setRunControlHighLimit(Double rchigh);
}
