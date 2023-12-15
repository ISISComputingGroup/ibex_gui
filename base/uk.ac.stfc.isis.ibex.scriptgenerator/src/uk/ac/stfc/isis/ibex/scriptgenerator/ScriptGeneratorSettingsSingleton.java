package uk.ac.stfc.isis.ibex.scriptgenerator;

// TODO import uk.ac.stfc.isis.ibex.ui.scriptgenerator.views.Constants;

public class ScriptGeneratorSettingsSingleton {
	private static ScriptGeneratorSettingsSingleton instance;
	private boolean skipEnabled = false;
	
	
	private ScriptGeneratorSettingsSingleton() {}
	
	public static ScriptGeneratorSettingsSingleton getInstance() {
		if (instance == null) {
			instance = new ScriptGeneratorSettingsSingleton();
		}
		return instance;
	}
	
	public boolean getSkipEnabled() {
		return skipEnabled;
	}
	
	public void setSkipEnabled(boolean enabled) {
		skipEnabled = enabled;
	}
}
