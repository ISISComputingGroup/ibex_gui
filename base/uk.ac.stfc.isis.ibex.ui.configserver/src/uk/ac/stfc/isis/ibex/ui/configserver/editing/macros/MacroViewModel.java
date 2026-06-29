package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro.HasDefault;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * View model for the macro, mainly deals with the logic of having a default.
 */
public class MacroViewModel extends ModelObject {
	private final Macro macro;


	/**
	 * Constructor. Sets the use default based on provided macro.
	 * 
	 * @param macro The underlying macro for this view model.
	 */
	public MacroViewModel(Macro macro) {
		this.macro = macro;
		macro.addPropertyChangeListener("value", passThrough());
		setUseDefault((macro.getValue() == null) || (macro.getValue().equals("")));
	}

	/**
	 * @return macro description
	 */
	public String getDescription() {
		return macro.getDescription();
	}

	/**
	 * @return macro name
	 */
	public String getName() {
		return macro.getName();
	}

	/**
	 * Set Macro value.
	 * 
	 * @param value new Macro value
	 */
	public void setValue(String value) {
		macro.setValue(value);
	}
	
	/**
	 * @return macro value.
	 */
	public String getValue() {
		return macro.getValue();
	}

	/**
	 * @return macro regex pattern
	 */
	public String getPattern() {
		return macro.getPattern();
	}

	/**
	 * @return whether the default value should be used or not
	 */
	public boolean getUseDefault() {
		return macro.getUseDefault();
	}

	/**
	 * 
	 * Sets whether the default value should be used or not
	 * If the value is null we now need to set it to empty string or else it will not be
	 * sent over to the block server.
	 * @param useDefault whether the default value should be used or not
	 */
	public void setUseDefault(boolean useDefault) {
		if (useDefault) {
			macro.setValue("");
		}
		macro.setUseDefault(useDefault);
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
		String macroDisplayVal = "(default)";
		if (macro.getValue() != "") {
			macroDisplayVal = macro.getValue();
		}
		return macroDisplayVal;
	}
}
