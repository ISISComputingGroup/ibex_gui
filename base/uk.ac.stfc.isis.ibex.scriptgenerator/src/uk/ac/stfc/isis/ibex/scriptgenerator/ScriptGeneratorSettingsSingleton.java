package uk.ac.stfc.isis.ibex.scriptgenerator;

/***
 * The singleton class for settings of the Script Generator, to hold values such
 * as whether skipping or pausing on invalid actions is selected.
 */
public final class ScriptGeneratorSettingsSingleton {
	private static ScriptGeneratorSettingsSingleton instance;
	private boolean skipEnabled = false;

	private ScriptGeneratorSettingsSingleton() {
	}

	/***
	 * Method to get the instance of this class.
	 * 
	 * @return the instance of the class
	 */
	public static ScriptGeneratorSettingsSingleton getInstance() {
		if (instance == null) {
			instance = new ScriptGeneratorSettingsSingleton();
		}
		return instance;
	}

	/***
	 * Get the value of skip enabled setting, where true is enabled and false is not
	 * enabled.
	 * 
	 * @return true or false
	 */
	public boolean getSkipEnabled() {
		return skipEnabled;
	}

	/***
	 * Set the value of skip enabled setting.
	 * 
	 * @param enabled
	 */
	public void setSkipEnabled(boolean enabled) {
		skipEnabled = enabled;
	}
}
