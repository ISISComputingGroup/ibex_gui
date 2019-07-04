package uk.ac.stfc.isis.ibex.journal;

import java.util.Calendar;

public class JournalSearchCalendar extends JournalSearch<Calendar>{

    public JournalSearchCalendar(JournalField field) {
        super(field);
    }

    @Override
    public JournalSearchWidgetType getWidgetType() {
        return JournalSearchWidgetType.CALENDAR_SEARCH;
    }

}
