package uk.ac.stfc.isis.ibex.journal;

import java.util.ArrayList;
import java.util.Arrays;

import uk.ac.stfc.isis.ibex.journal.JournalFieldCategoriser.JournalFieldCategory;
import uk.ac.stfc.isis.ibex.journal.JournalSort.JournalSortDirection;
import uk.ac.stfc.isis.ibex.journal.formatters.DateTimeJournalFormatter;
import uk.ac.stfc.isis.ibex.journal.formatters.DecimalPlacesFormatter;
import uk.ac.stfc.isis.ibex.journal.formatters.IJournalFormatter;
import uk.ac.stfc.isis.ibex.journal.formatters.NoopJournalFormatter;
import uk.ac.stfc.isis.ibex.journal.formatters.DurationJournalFormatter;

/**
 * Enum constants representing the column names in the SQL schema.
 */
public enum JournalField {

	/**
	 * Run number.
	 */
	RUN_NUMBER("Run Number", JournalFieldCategory.NAME, "run_number", JournalSortDirection.DESCENDING),
	/**
	 * Title.
	 */
	TITLE("Title", JournalFieldCategory.TEXT, "title", JournalSortDirection.ASCENDING),
	/**
	 * Start time.
	 */
	START_TIME("Start Time", JournalFieldCategory.TIME_AND_FRAME, "start_time", new DateTimeJournalFormatter(),
			JournalSortDirection.DESCENDING),
	/**
	 * End time.
	 */
	END_TIME("End time", JournalFieldCategory.TIME_AND_FRAME, "end_time", new DateTimeJournalFormatter(),
			JournalSortDirection.DESCENDING),
	/**
	 * Duration.
	 */
	DURATION("Duration", JournalFieldCategory.TIME_AND_FRAME, "duration", new DurationJournalFormatter(),
			JournalSortDirection.ASCENDING),
	/**
	 * Uamps.
	 */
	UAMPS("Uamps", JournalFieldCategory.MEASURE, "uamps", new DecimalPlacesFormatter(4),
			JournalSortDirection.DESCENDING),
	/**
	 * Rb number.
	 */
	RB_NUMBER("RB number", JournalFieldCategory.EXP, "rb_number", JournalSortDirection.DESCENDING),
	/**
	 * Users.
	 */
	USERS("Users", JournalFieldCategory.EXP, "users", JournalSortDirection.ASCENDING),
	/**
	 * Simulation mode.
	 */
	SIMULATION_MODE("Simulation mode", JournalFieldCategory.EXP, "simulation_mode", JournalSortDirection.DESCENDING),
	/**
	 * Local contact.
	 */
	LOCAL_CONTACT("Local contact", JournalFieldCategory.EXP, "local_contact", JournalSortDirection.ASCENDING),
	/**
	 * User institute.
	 */
	USER_INSTITUTE("User institute", JournalFieldCategory.EXP, "user_institute", JournalSortDirection.ASCENDING),
	/**
	 * Instrument name.
	 */
	INSTRUMENT_NAME("Instrument name", JournalFieldCategory.NAME, "instrument_name", JournalSortDirection.ASCENDING),
	/**
	 * Sample id.
	 */
	SAMPLE_ID("Sample ID", JournalFieldCategory.SAMPLE, "sample_id", JournalSortDirection.ASCENDING),
	/**
	 * Measurement first run.
	 */
	MEASUREMENT_FIRST_RUN("Measurement first run", JournalFieldCategory.MEASURE, "measurement_first_run",
			JournalSortDirection.DESCENDING),
	/**
	 * Measurement id.
	 */
	MEASUREMENT_ID("Measurement ID", JournalFieldCategory.MEASURE, "measurement_id", JournalSortDirection.DESCENDING),
	/**
	 * Measurement label.
	 */
	MEASUREMENT_LABEL("Measurement label", JournalFieldCategory.TEXT, "measurement_label",
			JournalSortDirection.ASCENDING),
	/**
	 * Measurement type.
	 */
	MEASUREMENT_TYPE("Measurement type", JournalFieldCategory.MEASURE, "measurement_type",
			JournalSortDirection.ASCENDING),
	/**
	 * Measurement subid.
	 */
	MEASUREMENT_SUBID("Measurement subid", JournalFieldCategory.MEASURE, "measurement_subid",
			JournalSortDirection.DESCENDING),
	/**
	 * Raw frames.
	 */
	RAW_FRAMES("Raw frames", JournalFieldCategory.TIME_AND_FRAME, "raw_frames", JournalSortDirection.DESCENDING),
	/**
	 * Good frames.
	 */
	GOOD_FRAMES("Good frames", JournalFieldCategory.TIME_AND_FRAME, "good_frames", JournalSortDirection.DESCENDING),
	/**
	 * Number periods.
	 */
	NUMBER_PERIODS("Number periods", JournalFieldCategory.NUMBER, "number_periods", JournalSortDirection.ASCENDING),
	/**
	 * Number spectra.
	 */
	NUMBER_SPECTRA("Number spectra", JournalFieldCategory.NUMBER, "number_spectra", JournalSortDirection.DESCENDING),
	/**
	 * Number detectors.
	 */
	NUMBER_DETECTORS("Number detectors", JournalFieldCategory.NUMBER, "number_detectors",
			JournalSortDirection.DESCENDING),
	/**
	 * Number time regimes.
	 */
	NUMBER_TIME_REGIMES("Number time regimes", JournalFieldCategory.NUMBER, "number_time_regimes",
			JournalSortDirection.DESCENDING),
	/**
	 * Frame sync.
	 */
	FRAME_SYNC("Frame sync", JournalFieldCategory.TIME_AND_FRAME, "frame_sync", JournalSortDirection.ASCENDING),
	/**
	 * Icp version.
	 */
	ICP_VERSION("ICP version", JournalFieldCategory.INSTRUMENT, "icp_version", JournalSortDirection.ASCENDING),
	/**
	 * Detector table.
	 */
	DETECTOR_TABLE("Detector table", JournalFieldCategory.TABLE, "detector_table", JournalSortDirection.ASCENDING),
	/**
	 * Spectra table.
	 */
	SPECTRA_TABLE("Spectra table", JournalFieldCategory.TABLE, "spectra_table", JournalSortDirection.ASCENDING),
	/**
	 * Wiring table.
	 */
	WIRING_TABLE("Wiring table", JournalFieldCategory.TABLE, "wiring_table", JournalSortDirection.ASCENDING),
	/**
	 * Monitor spectrum.
	 */
	MONITOR_SPECTRUM("Monitor spectrum", JournalFieldCategory.INSTRUMENT, "monitor_spectrum",
			JournalSortDirection.ASCENDING),
	/**
	 * Monitor sum.
	 */
	MONITOR_SUM("Monitor sum", JournalFieldCategory.INSTRUMENT, "monitor_sum", JournalSortDirection.DESCENDING),
	/**
	 * Total mevents.
	 */
	TOTAL_MEVENTS("Total mevents", JournalFieldCategory.NUMBER, "total_mevents", JournalSortDirection.DESCENDING),
	/**
	 * Comment.
	 */
	COMMENT("Comment", JournalFieldCategory.TEXT, "comment", JournalSortDirection.ASCENDING),
	/**
	 * Field label.
	 */
	FIELD_LABEL("Field label", JournalFieldCategory.TEXT, "field_label", JournalSortDirection.ASCENDING),
	/**
	 * Instrument geometry.
	 */
	INSTRUMENT_GEOMETRY("Instrument geometry", JournalFieldCategory.INSTRUMENT, "instrument_geometry",
			JournalSortDirection.ASCENDING),
	/**
	 * Script name.
	 */
	SCRIPT_NAME("Script name", JournalFieldCategory.NAME, "script_name", JournalSortDirection.ASCENDING),
	/**
	 * Sample name.
	 */
	SAMPLE_NAME("Sample name", JournalFieldCategory.NAME, "sample_name", JournalSortDirection.ASCENDING),
	/**
	 * Sample orientation.
	 */
	SAMPLE_ORIENTATION("Sample orientation", JournalFieldCategory.SAMPLE, "sample_orientation",
			JournalSortDirection.ASCENDING),
	/**
	 * Temperature label.
	 */
	TEMPERATURE_LABEL("Temperature label", JournalFieldCategory.TEXT, "temperature_label",
			JournalSortDirection.ASCENDING),
	/**
	 * Np ratio.
	 */
	NP_RATIO("NP ratio", JournalFieldCategory.INSTRUMENT, "np_ratio", JournalSortDirection.ASCENDING),
	/**
	 * Isis cycle.
	 */
	ISIS_CYCLE("Isis cycle", JournalFieldCategory.EXP, "isis_cycle", JournalSortDirection.DESCENDING),
	/**
	 * Event mode.
	 */
	EVENT_MODE("Event mode", JournalFieldCategory.EXP, "event_mode", JournalSortDirection.DESCENDING);

	private final String friendlyName;
	private final String sqlFieldName;
	private final JournalFieldCategory category;
	private final IJournalFormatter formatter;
	private final JournalSortDirection sortDirection;

	JournalField(String friendlyName, JournalFieldCategory category, String sqlFieldName,
			JournalSortDirection sortDirection) {
		this(friendlyName, category, sqlFieldName, new NoopJournalFormatter(), sortDirection);
	}

	JournalField(String friendlyName, JournalFieldCategory category, String sqlFieldName, IJournalFormatter formatter,
			JournalSortDirection sortDirection) {
		this.friendlyName = friendlyName;
		this.category = category;
		this.sqlFieldName = sqlFieldName;
		this.formatter = formatter;
		this.sortDirection = sortDirection;
	}

	/**
	 * Gets the column name of this field in the SQL schema.
	 * 
	 * @return the column name
	 */
	public String getSqlFieldName() {
		return sqlFieldName;
	}

	/**
	 * Gets a friendly, user-facing name of this field.
	 * 
	 * @return the name
	 */
	public String getFriendlyName() {
		return friendlyName;
	}

	/**
	 * Gets a formatter to convert this field's SQL representation into a
	 * user-facing representation.
	 * 
	 * @return the name
	 */
	public IJournalFormatter getFormatter() {
		return formatter;
	}

	/**
	 * Takes a friendly name and find its associated field. Throws an error if no
	 * such field exists.
	 * 
	 * @param name the friendly name
	 * @return a journal field
	 */
	public static JournalField getFieldFromFriendlyName(String name) {
		return (new ArrayList<JournalField>(Arrays.asList(JournalField.values()))).stream()
				.filter(f -> f.getFriendlyName().equals(name)).findFirst().get();
	}

	/**
	 * @return the default sort direction
	 */
	public JournalSortDirection getSortDirection() {
		return sortDirection;
	}

	/**
	 * @return the respective category of the field
	 */
	public JournalFieldCategory getCategory() {
		return category;
	}
}
