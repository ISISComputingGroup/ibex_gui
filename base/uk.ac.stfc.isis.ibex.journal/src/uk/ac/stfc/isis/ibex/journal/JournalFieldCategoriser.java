package uk.ac.stfc.isis.ibex.journal;

public class JournalFieldCategoriser {	
	/**
	 * Enum used to categorise all fields in the journal viewer for grouping and reference in the UI.
	 */
	public enum JournalFieldCategory {
		
		
		TIME_AND_FRAME("Times And Frames", "Field Groupings for times and frames."),
		NAME("Names And Labels", "Field Groupings for names and labels."),
		TIME("Time", "Field Groupings for time.");
//		TIME("Time", "Field Groupings for time."),
//		TIME("Time", "Field Groupings for time."),
//		TIME("Time", "Field Groupings for time."),
		
		
		private final String title;
		private final String tooltip;
		
		JournalFieldCategory(String title, String tooltip) {
			this.title = title;
			this.tooltip = tooltip;
		}
		
		public String getCategoryTitle() {
			return title;
		}
		public String getCategoryTooltip() {
			return tooltip;
		}
		
	}
	
	JournalField categoryField;
	JournalFieldCategory category;
    
    /**
     * Create a journal sort class.
     * 
     * @param sortField The field to sort with
     * @param direction The direction to sort in
     */
    public JournalFieldCategoriser(JournalField categoryField, JournalFieldCategory category) {
        this.categoryField = categoryField;
        this.category = category;
    }


    /**
     * @return the sortField
     */
    public JournalField getCategoryField() {
        return categoryField;
    }
    
    public static String getFriendlyCategoryName(String rawName) {
    	return JournalFieldCategory.valueOf(rawName).getCategoryTitle();
    }

    /**
     * @return the direction
     */
    public JournalFieldCategory getCategory() {
        return category;
    }
	
	
	
}
