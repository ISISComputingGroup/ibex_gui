package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * Contains properties used in the script generator.
 */
public final class ScriptGeneratorProperties {
	private ScriptGeneratorProperties() {
	}
	
	 /**
     * A property that denotes whether the language to generate and check validity errors in is supported.
     */
    public static final String LANGUAGE_SUPPORT_PROPERTY = "language_supported";

    /**
     * A property that denotes whether there has been a threading error in generation or validity checking.
     */
    public static final String THREAD_ERROR_PROPERTY = "thread error";

    /**
     * A property that carries the validity error messages to listen for in order to update table rows.
     */
    public static final String VALIDITY_ERROR_MESSAGE_PROPERTY = "validity error messages";
    
    /**
	 * The property to listen for changes in a Generator containing whether or not
	 * all script generator contents are valid or not (bool).
	 */
	public static final String PARAM_VALIDITY_PROPERTY = "parameter validity";
	
    /**
     * A property that carries the time estimation to listen for in order to update table rows.
     */
    public static final String TIME_ESTIMATE_PROPERTY = "time estimate";
    
    /**
     * A property that carries the custom estimation to listen for in order to update table rows.
     */
    public static final String CUSTOM_ESTIMATE_PROPERTY = "custom estimate";
    
    /**
	 * The property to listen for changes in a Generator containing the generated
	 * script (String).
	 */
    public static final String GENERATED_SCRIPT_PROPERTY = "generated script";

	/**
	 * The property to listen for changes in a Generator containing the generated
	 * script (String).
	 */
    public static final String GENERATED_SCRIPT_FILENAME_PROPERTY = "generated script filename";

    /**
     * A property to listen to for when actions change in the model.
     */
    public static final String ACTIONS_PROPERTY = "actions";

    /**
     * A property to fire a change of when there is an error generating a script.
     */
    public static final String SCRIPT_GENERATION_ERROR_PROPERTY = "script generation error";

    /**
     * A property that is changed when script definitions are switched.
     */
    public static final String SCRIPT_DEFINITION_SWITCH_PROPERTY = "scriptDefinition";

    /**
     * A property to notify listeners when python becomes ready or not ready.
     */
    public static final String PYTHON_READINESS_PROPERTY = "python ready";
    
    /**
     * A property to notify listeners when parameters change.
     */
    public static final String PARAMETERS_PROPERTY = "parameters";
    
    /**
     * A property to notify listeners when action parameters change.
     */
    public static final String ACTION_PARAMETERS_PROPERTY = "actionParameters";
    
    /**
	 * The property of an action to listen to for changes.
	 */
	public static final String VALUE_PROPERTY = "value";
	
	 /**
     * The property to fire a change of if the action becomes valid or invalid.
     */
	public static final String VALIDITY_PROPERTY = "validity";
	
	/**
     * The property to fire a change of if the action becomes executing or not executing.
     */
	public static final String EXECUTING_PROPERTY = "executing";

}
