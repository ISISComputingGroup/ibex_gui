package uk.ac.stfc.isis.ibex.journal;

/**
 * Enum constants representing the column names in the SQL schema.
 */
public enum JournalField {
	
	/** 
	 * Run number. 
	 */
	RUN_NUMBER("Run number", "run_number"),
	/** 
	 * Title. 
	 */
    TITLE("Title", "title"),
    /** 
	 * Start time. 
	 */
	START_TIME("Start time", "start_time"),
	/** 
	 * Duration. 
	 */
	DURATION("Duration", "duration"),
	/** 
	 * Uamps. 
	 */
	UAMPS("uAh", "uamps"),
	/** 
	 * RB Number. 
	 */
	RB_NUMBER("RB Number", "rb_number"),
	/** 
	 * Users. 
	 */
	USERS("Users", "users");
	
	private final String friendlyName;
    private final String sqlFieldName;
    
    private JournalField(String friendlyName, String sqlFieldName) {
    	this.friendlyName = friendlyName;
    	this.sqlFieldName = sqlFieldName;
    }
    
    /**
     * Gets the column name of this field in the SQL schema.
     * @return the column name
     */
    public String getSqlFieldName() {
    	return sqlFieldName;
    }
    
    /**
     * Gets a friendly, user-facing name of this field.
     * @return the name
     */
    public String getFriendlyName() {
    	return friendlyName;
    }
    
}
