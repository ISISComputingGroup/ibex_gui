package uk.ac.stfc.isis.ibex.ui.log.widgets;

import java.util.Calendar;

import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;

public interface ISearchModel {
	void search(LogMessageFields field, String value, Calendar from, Calendar to);

	void clearSearch();
}
