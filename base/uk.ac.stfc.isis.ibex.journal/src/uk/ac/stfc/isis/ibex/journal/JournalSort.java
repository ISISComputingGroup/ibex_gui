package uk.ac.stfc.isis.ibex.journal;

/**
 * A data type representing a sort field and direction.
 */
public class JournalSort {
    
    /**
     * An enum representation of the two sort directions.
     */
    public enum JournalSortDirection {
        /**
         * Ascending sort direction.
         */
        ASCENDING("ASC"),
        /**
         * Descending sort direction.
         */
        DESCENDING("DESC");
        
        private final String sqlRepresentation;
        
        /**
         * Enum constructor.
         * 
         * @param sqlRepresentation String for use an the SQL query
         */
        JournalSortDirection(String sqlRepresentation) {
            this.sqlRepresentation = sqlRepresentation;
        }
        
        /**
         * @return The SQL String representation of the sort direction.
         */
        public String getSql() {
            return sqlRepresentation;
        }
    }
    
    JournalField sortField;
    JournalSortDirection direction;
    
    /**
     * Create a journal sort class.
     * 
     * @param sortField The field to sort with
     * @param direction The direction to sort in
     */
    public JournalSort(JournalField sortField, JournalSortDirection direction) {
        this.sortField = sortField;
        this.direction = direction;
    }

    public void swapDirection() {
        if (direction == JournalSortDirection.ASCENDING) {
            direction = JournalSortDirection.DESCENDING;
        } else {
            direction = JournalSortDirection.ASCENDING;
        }
    }
    
}
