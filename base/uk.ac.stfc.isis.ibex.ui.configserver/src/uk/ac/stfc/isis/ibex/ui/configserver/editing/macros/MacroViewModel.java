package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro.HasDefault;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class MacroViewModel extends ModelObject {
	private final Macro macro;
	
	private String displayDefault;
	
	/**
	 * Whether the default value should be used or not.
	 */
	private boolean useDefault;
	
	public MacroViewModel(Macro macro) {
		this.macro = macro;
		macro.addPropertyChangeListener("name", passThrough());
		macro.addPropertyChangeListener("description", passThrough());
		macro.addPropertyChangeListener("pattern", passThrough());
		macro.addPropertyChangeListener("value", passThrough());
		macro.addPropertyChangeListener("defaultValue", arg0 -> setDisplayDefault(macro.getDefaultValue()));

		setUseDefault(macro.getValue() == null);
	}
	
	public String getDescription() {
		return macro.getDescription();
	}
	
	public String getName() {
		return macro.getName();
	}
	
	public void setValue(Optional<String> value) {
		macro.setValue(value.isPresent() ? value.get() : null);
	}
	
	public Optional<String> getValue() {
		return Optional.ofNullable(macro.getValue());
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
     * @param useDefault whether the default value should be used or not
     */
    public void setUseDefault(boolean useDefault) {
        firePropertyChange("useDefault", this.useDefault, this.useDefault = useDefault);
    }
	
    /**
     * @return default macro value for displaying to the user
     */
    public String getDisplayDefault() {
    	HasDefault hasDefault = macro.getHasDefault();
        if (hasDefault == HasDefault.YES) {
            return getValue().get().equals("") ? "(default is the empty string)" : macro.getDefaultValue();
        } else if (hasDefault == HasDefault.NO) {
            return "(no default)";
        } else {
            return "(default unknown)";
        }
    }
    
	private void setDisplayDefault(String displayDefault) {
		firePropertyChange("displayDefault", this.displayDefault, this.displayDefault = getDisplayDefault());
	}
	
	/**
	 * @return macro value to put in the cell when the user clicks on it to edit it
	 */
	public String getEditCellValue() {
	    if (getUseDefault()) {
	        setUseDefault(false);
	        setValue(Optional.of(""));
	    }
	    return getDisplayValue();
	}
	
	/**
	 * @return macro value for displaying to the user
	 */
	public String getDisplayValue() {
	    return getValue().orElse("(default)");
	}
	
    /**
     * Updates the useDefault and the value of the macro given the 'Use Default?' checkbox being checked or unchecked.
     * 
     * @param checked if the checkbox is checked or not
     */
    public void updateFromUseDefaultCheck(boolean checked) {
        setUseDefault(checked);
        if (checked) {
            setValue(Optional.empty());
        } else {
            setValue(Optional.of(""));
        }
    }
}
