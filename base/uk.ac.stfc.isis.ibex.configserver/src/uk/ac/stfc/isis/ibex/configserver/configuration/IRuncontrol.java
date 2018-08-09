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
    public Boolean getRCEnabled();
	
    /**
     * Sets whether run-control should be enabled.
     * 
     * @param runcontrol whether run-control should be enabled
     */
    public void setRCEnabled(Boolean runcontrol);
    
    /**
     * Gets the low limit for run-control.
     * 
     * @return the low limit for run-control
     */
    public Double getRCLowLimit();

    /**
     * Sets the low limit for run-control.
     * 
     * @param rclow the new low limit for run-control
     */
    public void setRCLowLimit(Double rclow);

    /**
     * Gets the high limit for run-control.
     * 
     * @return the high limit for run-control
     */
    public Double getRCHighLimit();

    /**
     * Sets the high limit for run-control.
     * 
     * @param rchigh the new high limit for run-control
     */
    public void setRCHighLimit(Double rchigh);
}
