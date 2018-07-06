package uk.ac.stfc.isis.ibex.validators;

/**
 * Warning message class.
 *
 */
public class WarningMessage {
    private String message;
    private boolean warning;
    
    /**
     * Creator for an empty warning message (ie when no warning is given).
     *
     */
    public WarningMessage() {
        warning = false;
    }
    
    /**
     * Creator for a warning message.
     *
     *@param warning
     *              true if there is a warning.
     *
     *@param message
     *                  the warning message.
     */
    public WarningMessage(boolean warning, String message) {
        this.warning = warning;
        this.message = message;
    }
    
    /**
     * Allows to get the warning message.
     * 
     * @return
     *          the warning message.
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Class used to set a warning.
     * 
     * @param message
     *                  the warning message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Check if there is a warning.
     * 
     * @return
     *          true if there is a warning, false otherwise.
     */
    public boolean isWarning() {
        return warning;
    }
    
    /**
    *Class to set the existence of a warning.
    * 
    * @param inWarning
    *                  true if there is a warning.
    */
    public void setWarning(boolean inWarning) {
        this.warning = inWarning;
    }
}
