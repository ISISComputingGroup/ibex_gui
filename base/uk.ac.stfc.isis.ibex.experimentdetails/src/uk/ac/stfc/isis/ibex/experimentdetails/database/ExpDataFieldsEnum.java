package uk.ac.stfc.isis.ibex.experimentdetails.database;

public enum ExpDataFieldsEnum {
	EXPERIMENT_ID("experimentID"),
	USER_ID("userID"),
	ROLE_ID("roleID"),
	
	NAME("name"),
	ORGANISATION("organisation"),
    PRIORITY("priority"),
    STARTDATE("startDate"),
    DURATION("duration");

    private final String representation;
    
    private ExpDataFieldsEnum(String representation) {
    	this.representation = representation;
    }
    
    @Override
    public String toString() {
    	return this.representation;
    }
}