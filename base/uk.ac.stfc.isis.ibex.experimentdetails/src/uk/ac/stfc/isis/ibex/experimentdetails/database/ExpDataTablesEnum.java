package uk.ac.stfc.isis.ibex.experimentdetails.database;

public enum ExpDataTablesEnum {
	USER_TABLE("user"),
	ROLE_TABLE("role"),
	EXPERIMENT_TABLE("experiment"),
	EXPERIMENT_TEAMS_TABLE("experimentteams");

    private final String representation;
    
    private ExpDataTablesEnum(String representation) {
    	this.representation = representation;
    }
    
    @Override
    public String toString() {
    	return this.representation;
    }
}