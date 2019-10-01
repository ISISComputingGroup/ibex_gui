package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro.HasDefault;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class MacroViewModel extends ModelObject {
	private final Macro macro;
	
	/**
	 * Whether the default value should be used or not.
	 */
	private boolean useDefault;
	
	public MacroViewModel(Macro macro) {
		this.macro = macro;
		macro.addPropertyChangeListener("value", passThrough());

		setUseDefault(macro.getValue() == null);
	}
	
	public String getDescription() {
		return macro.getDescription();
	}
	
	public String getName() {
		return macro.getName();
	}
	
	public void setValue(String value) {
		macro.setValue(value);
	}
	
	public String getPattern() {
		return macro.getPattern();
	}

    /**
     * @return whether the default value should be used or not
     */
    public boolean getUseDefault() {
        return useDefault;
    }

    /**
     * Sets whether the default value is being used. 
     * 
     * The underlying macro value must be set to null if the default is being used.
     * If we were using the default and now we're not set the value to empty string.
     * 
     * @param useDefault whether the default value should be used or not
     */
    public void setUseDefault(boolean useDefault) {
    	if (useDefault) {
    		macro.setValue(null);
    	} else if (this.useDefault) {
    		macro.setValue("");
    	}
        firePropertyChange("useDefault", this.useDefault, this.useDefault = useDefault);
    }
	
    /**
     * @return default macro value for displaying to the user
     */
    public String getDisplayDefault() {
    	HasDefault hasDefault = macro.getHasDefault();
    	String macroDefaultValue = macro.getDefaultValue();
        if (hasDefault == HasDefault.YES) {
            return macroDefaultValue.equals("") ? "(default is the empty string)" : macroDefaultValue;
        } else if (hasDefault == HasDefault.NO) {
            return "(no default)";
        } else {
            return "(default unknown)";
        }
    }
	
	/**
	 * @return macro value for displaying to the user
	 */
	public String getDisplayValue() {
	    return Optional.ofNullable(macro.getValue()).orElse("(default)");
	}
}
