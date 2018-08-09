package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import uk.ac.stfc.isis.ibex.configserver.configuration.IRuncontrol;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.RunControlValidator;

public abstract class AbstractRunControlViewModel extends ErrorMessageProvider {
	
	private Double lowLimit;
    private Double highLimit;
    private boolean enabled;
    
    private final RunControlValidator runControlValidator = new RunControlValidator();
    
    protected IRuncontrol block;
    
    /**
     * Resets the current values to those of the current block's configuration.
     */
    public void resetCurrentBlock() {
        setHighLimit(block.getRCHighLimit());
        setLowLimit(block.getRCLowLimit());
        setEnabled(block.getRCEnabled());

        checkIsValid();
    }
	
	/**
     * Set the low limit.
     * 
     * @param lowLimitText the new value
     */
	public void setLowLimit(Double lowLimit) {
		firePropertyChange("lowLimit", this.lowLimit, this.lowLimit = lowLimit);
		updateErrors();
	}

    /**
     * Set the high limit.
     * 
     * @param highLimitText the new value
     */
	public void setHighLimit(Double highLimit) {
		firePropertyChange("highLimit", this.highLimit, this.highLimit = highLimit);
		updateErrors();
	}
	
	private void updateErrors() {
		// boolean isValid = runControlValidator.isValid(lowLimit, highLimit);
		boolean isValid = true;
		setError(!isValid, runControlValidator.getErrorMessage());
	}
	
    /**
     * @return the low limit
     */
	public Double getLowLimit() {
		return lowLimit;
	}
	
    /**
     * @return the high limit
     */
	public Double getHighLimit() {
		return highLimit;
	}
	
    /**
     * Set whether run-control is enabled.
     * 
     * @param enabled enable or not
     */
	public void setEnabled(boolean enabled) {
		firePropertyChange("enabled", this.enabled, this.enabled = enabled);
	}
	
    /**
     * @return whether run-control is enabled
     */
	public boolean getEnabled() {
		return enabled;
	}
	
	public void setBlock(IRuncontrol block) {
		this.block = block;
		checkIsValid();
	}
	
	private void checkIsValid() {
        boolean isValid = runControlValidator.isValid(lowLimit, highLimit);
		setError(!isValid, runControlValidator.getErrorMessage());
		onValidate(isValid);
    }
	
	protected abstract void onValidate(boolean validationPassed);
}
