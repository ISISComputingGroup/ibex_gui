package uk.ac.stfc.isis.ibex.experimentdetails.database;

public enum ExperimentDataTablesTags {
	TAG_USER_TABLE("user"),
	TAG_ROLE_TABLE("role"),
	TAG_EXPERIMENT_TABLE("experiment"),
	TAG_EXPERIMENT_TEAMS_TABLE("experimentteams");

    private final String representation;
    
    private ExperimentDataTablesTags(String representation) {
    	this.representation = representation;
    }
    
    @Override
    public String toString() {
    	return this.representation;
    }
}