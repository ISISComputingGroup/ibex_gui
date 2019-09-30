package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class MacroViewModel extends ModelObject {
	private final Macro macro;
	
	public MacroViewModel(Macro macro) {
		this.macro = macro;
		macro.addPropertyChangeListener("name", passThrough());
		macro.addPropertyChangeListener("description", passThrough());
		macro.addPropertyChangeListener("pattern", passThrough());
		macro.addPropertyChangeListener("value", passThrough());
		macro.addPropertyChangeListener("useDefault", passThrough());
		macro.addPropertyChangeListener("defaultValue", passThrough());
	}
	
	public String getDescription() {
		return macro.getDescription();
	}
	
	public String getName() {
		return macro.getName();
	}
	
	public void setValue(Optional<String> value) {
		macro.setValue(value);
	}
	
	public Optional<String> getValue() {
		return macro.getValue();
	}
	
	public String getPattern() {
		return macro.getPattern();
	}

	public boolean getUseDefault() {
		return macro.getUseDefault();
	}
	
	public void setUseDefault(boolean checked) {
		macro.setUseDefault(checked);
	}
	
	public String getDefaultDisplay() {
		return macro.getDefaultDisplay();
	}
	
	public String getEditCellValue() {
		return macro.getEditCellValue();
	}
	
	public String getValueDisplay() {
		return macro.getValueDisplay();
	}
	
}
