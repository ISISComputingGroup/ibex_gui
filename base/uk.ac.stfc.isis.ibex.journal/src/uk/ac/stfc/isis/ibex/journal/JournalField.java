package uk.ac.stfc.isis.ibex.journal;

/**
 * Enum constants representing the column names in the SQL schema.
 */
public enum JournalField {
	
	RUN_NUMBER("Run number", "run_number"),
    TITLE("Title", "title"),
	START_TIME("Start time", "start_time"),
	DURATION("Duration", "duration"),
	UAMPS("uAh", "uamps"),
	RB_NUMBER("RB Number", "rb_number"),
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
