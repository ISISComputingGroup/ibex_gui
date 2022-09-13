package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import uk.ac.stfc.isis.ibex.configserver.configuration.IRuncontrol;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.RunControlValidator;

/**
 * Abstract class for a run control view model.
 *
 */
public abstract class AbstractRunControlViewModel extends ErrorMessageProvider implements IRuncontrol {
	
	private Double lowLimit;
    private Double highLimit;
    private boolean enabled;
    private boolean suspendIfInvalid;
    
    private final RunControlValidator runControlValidator = new RunControlValidator();
    
    /**
     * The source of runcontrol values.
     */
    protected IRuncontrol source;
    
    /**
     * Field that the GUI should bind to to update high limit.
     */
    public static final String HIGH_LIMIT_BINDING_NAME = "runControlHighLimitStr";
    
    /**
     * Field that the GUI should bind to to update low limit.
     */
	public static final String LOW_LIMIT_BINDING_NAME = "runControlLowLimitStr";
	
	/**
     * Field that the GUI should bind to to update enablement.
     */
	public static final String ENABLED_BINDING_NAME = "runControlEnabled";
	
	/**
     * Field that the GUI should bind to to update suspend on invalid.
     */
	public static final String SUSPEND_ON_INVALID_BINDING_NAME = "suspendIfInvalid";
    
    /**
     * Resets the current values to those from the source.
     */
    public abstract void resetFromSource();
	
	/**
     * Set the new low limit for run control.
     * @param lowLimit the new value
     */
	@Override
	public void setRunControlLowLimit(Double lowLimit) {
		firePropertyChange("runControlLowLimit", this.lowLimit, this.lowLimit = lowLimit);
		firePropertyChange("runControlLowLimitStr", null, getRunControlLowLimitStr());
		updateErrors();
	}
	
	/**
	 * Gets the low limit for run control.
     * @return the low limit
     */
	@Override
	public Double getRunControlLowLimit() {
		return lowLimit;
	}
	
	/**
	 * Gets the low limit as a string.
	 * @return the low limit as a string
	 */
	public String getRunControlLowLimitStr() {
		Double value = getRunControlLowLimit();
		return value == null ? "" : value.toString();
	}
	
	/**
	 * Sets the low limit from a string.
	 * @param lowLimit the limit as a string
	 */
	public void setRunControlLowLimitStr(String lowLimit) {
		try {
			setRunControlLowLimit(Double.valueOf(lowLimit));
		} catch (NumberFormatException | NullPointerException e) {
			setRunControlLowLimit(null);
		}
	}

    /**
     * Set the high limit for run control.
     * @param highLimit the new value
     */
	@Override
	public void setRunControlHighLimit(Double highLimit) {
		firePropertyChange("runControlHighLimit", this.highLimit, this.highLimit = highLimit);
		firePropertyChange("runControlHighLimitStr", null, getRunControlHighLimitStr());
		updateErrors();
	}
	
	/**
	 * Get the high limit for run control.
     * @return the high limit
     */
	@Override
	public Double getRunControlHighLimit() {
		return highLimit;
	}
	
	/**
	 * Gets the high limit as a string.
	 * @return the high limit as a string
	 */
	public String getRunControlHighLimitStr() {
		Double value = getRunControlHighLimit();
		return value == null ? "" : value.toString();
	}
	
	/**
	 * Sets the high limit from a string.
	 * @param highLimit the limit as a string
	 */
	public void setRunControlHighLimitStr(String highLimit) {
		try {
			setRunControlHighLimit(Double.valueOf(highLimit));
		} catch (NumberFormatException | NullPointerException e) {
			setRunControlHighLimit(null);
		}
	}
	
    /**
     * Set whether run-control is enabled.
     * 
     * @param enabled enable or not
     */
	@Override
	public void setRunControlEnabled(Boolean enabled) {
		firePropertyChange("runControlEnabled", this.enabled, this.enabled = enabled);
		updateErrors();
	}
	
    /**
     * @return whether run-control is enabled
     */
	@Override
	public Boolean getRunControlEnabled() {
		return enabled;
	}
	
	private void updateErrors() {
		boolean isValid = runControlValidator.isValid(lowLimit, highLimit, enabled);
		setError(!isValid, runControlValidator.getErrorMessage());
		onValidate(isValid);
	}
	
	/**
	 * Sets the source of this runcontrol model (e.g. a block).
	 * @param source the new source
	 */
	public void setSource(IRuncontrol source) {
		this.source = source;
		if (source != null) {
			setRunControlHighLimit(source.getRunControlHighLimit());
			setRunControlLowLimit(source.getRunControlLowLimit());
			setRunControlEnabled(source.getRunControlEnabled());
			setSuspendIfInvalid(source.getSuspendIfInvalid());
		}
		updateErrors();
	}
	
	/**
	 * Called after a validation is done.
	 * @param validationPassed true if validation passed; false otherwise
	 */
	protected void onValidate(boolean validationPassed) {
		// Default to no implementation - subclasses can override.
	};
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getSuspendIfInvalid() {
		return suspendIfInvalid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSuspendIfInvalid(Boolean suspendIfInvalid) {
		firePropertyChange(SUSPEND_ON_INVALID_BINDING_NAME, this.suspendIfInvalid, this.suspendIfInvalid = suspendIfInvalid);
		updateErrors();
	}
}
