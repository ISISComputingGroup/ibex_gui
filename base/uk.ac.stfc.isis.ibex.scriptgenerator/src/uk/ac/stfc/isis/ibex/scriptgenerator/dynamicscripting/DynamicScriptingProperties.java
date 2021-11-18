package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

/**
 * Contains the properties used in dynamic scripting.
 */
public class DynamicScriptingProperties {
	
	/**
	 * A property to listen for when the status of the script in nicos has changed.
	 */
	public static final String SCRIPT_STATUS_PROPERTY = "scriptStatus";

	/**
	 * A property to listen for or fire when an actions script is finished. 
	 */	
	public static final String SCRIPT_FINISHED_PROPERTY = "scriptFinished";

	/**
	 * A property to listen for or fire when the state of dynamic scripting needs to be changed.
	 */
	public static final String STATE_CHANGE_PROPERTY = "stateChange";
	
	/**
	 * A property to listen for when a nicos script has been generated.
	 */
	public static final String NICOS_SCRIPT_GENERATED_PROPERTY = "nicos script generated";

}
