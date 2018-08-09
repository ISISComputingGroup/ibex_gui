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
    public Boolean getRunControlEnabled();
	
    /**
     * Sets whether run-control should be enabled.
     * 
     * @param runcontrol whether run-control should be enabled
     */
    public void setRunControlEnabled(Boolean enabled);
    
    /**
     * Gets the low limit for run-control.
     * 
     * @return the low limit for run-control
     */
    public Double getRunControlLowLimit();

    /**
     * Sets the low limit for run-control.
     * 
     * @param rclow the new low limit for run-control
     */
    public void setRunControlLowLimit(Double rclow);

    /**
     * Gets the high limit for run-control.
     * 
     * @return the high limit for run-control
     */
    public Double getRunControlHighLimit();

    /**
     * Sets the high limit for run-control.
     * 
     * @param rchigh the new high limit for run-control
     */
    public void setRunControlHighLimit(Double rchigh);
}
