package uk.ac.stfc.isis.ibex.experimentdetails.database;

public enum ExperimentDataTags {
	TAG_USER_TABLE("user"),
	TAG_ROLE_TABLE("role"),
	TAG_EXPERIMENT_TABLE("experiment"),
	TAG_EXPERIMENT_TEAMS_TABLE("experimentteams"),
	
	TAG_EXPERIMENT_ID("experimentID"),
	TAG_USER_ID("userID"),
	TAG_ROLE_ID("roleID"),
	
	TAG_NAME("name"),
	TAG_ORGANISATION("organisation"),
    TAG_PRIORITY("priority"),
    TAG_STARTDATE("startDate"),
    TAG_DURATION("duration");

    private final String representation;
    
    private ExperimentDataTags(String representation) {
    	this.representation = representation;
    }
    
    @Override
    public String toString() {
    	return this.representation;
    }
}