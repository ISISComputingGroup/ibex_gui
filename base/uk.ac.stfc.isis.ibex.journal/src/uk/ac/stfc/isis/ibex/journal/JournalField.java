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
    RUN_NUMBER("Run Number", JournalFieldCategory.TIME_AND_FRAME, "run_number", JournalSortDirection.DESCENDING),
    /** 
     * Title. 
     */
    TITLE("Title", JournalFieldCategory.NAME, "title", JournalSortDirection.ASCENDING),
    /** 
     * Start time. 
     */
    START_TIME("Start Time", JournalFieldCategory.TIME_AND_FRAME, "start_time", new DateTimeJournalFormatter(), JournalSortDirection.DESCENDING),
    /** 
     * End time. 
     */
    END_TIME("End time", JournalFieldCategory.TIME_AND_FRAME, "end_time", new DateTimeJournalFormatter(), JournalSortDirection.DESCENDING),
    /** 
     * Duration. 
     */
    DURATION("Duration", JournalFieldCategory.TIME_AND_FRAME, "duration", new DurationJournalFormatter(), JournalSortDirection.ASCENDING),
    /** 
     * Uamps. 
     */
    UAMPS("Uamps", JournalFieldCategory.TIME, "uamps", new DecimalPlacesFormatter(4), JournalSortDirection.DESCENDING),
    /** 
     * Rb number. 
     */
    RB_NUMBER("RB number", JournalFieldCategory.TIME, "rb_number", JournalSortDirection.DESCENDING),
    /** 
     * Users. 
     */
    USERS("Users", JournalFieldCategory.TIME, "users", JournalSortDirection.ASCENDING),
    /** 
     * Simulation mode. 
     */
    SIMULATION_MODE("Simulation mode", JournalFieldCategory.TIME, "simulation_mode", JournalSortDirection.DESCENDING),
    /** 
     * Local contact. 
     */
    LOCAL_CONTACT("Local contact", JournalFieldCategory.TIME, "local_contact", JournalSortDirection.ASCENDING),
    /** 
     * User institute. 
     */
    USER_INSTITUTE("User institute", JournalFieldCategory.TIME, "user_institute", JournalSortDirection.ASCENDING),
    /** 
     * Instrument name. 
     */
    INSTRUMENT_NAME("Instrument name", JournalFieldCategory.NAME, "instrument_name", JournalSortDirection.ASCENDING),
    /** 
     * Sample id. 
     */
    SAMPLE_ID("Sample ID", JournalFieldCategory.TIME, "sample_id", JournalSortDirection.ASCENDING),
    /** 
     * Measurement first run. 
     */
    MEASUREMENT_FIRST_RUN("Measurement first run", JournalFieldCategory.TIME, "measurement_first_run", JournalSortDirection.DESCENDING),
    /** 
     * Measurement id. 
     */
    MEASUREMENT_ID("Measurement ID", JournalFieldCategory.TIME, "measurement_id", JournalSortDirection.DESCENDING),
    /** 
     * Measurement label. 
     */
    MEASUREMENT_LABEL("Measurement label", JournalFieldCategory.TIME, "measurement_label", JournalSortDirection.ASCENDING),
    /** 
     * Measurement type. 
     */
    MEASUREMENT_TYPE("Measurement type", JournalFieldCategory.TIME, "measurement_type", JournalSortDirection.ASCENDING),
    /** 
     * Measurement subid. 
     */
    MEASUREMENT_SUBID("Measurement subid", JournalFieldCategory.TIME, "measurement_subid", JournalSortDirection.DESCENDING),
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
    NUMBER_PERIODS("Number periods", JournalFieldCategory.TIME, "number_periods", JournalSortDirection.ASCENDING),
    /** 
     * Number spectra. 
     */
    NUMBER_SPECTRA("Number spectra", JournalFieldCategory.TIME, "number_spectra", JournalSortDirection.DESCENDING),
    /** 
     * Number detectors. 
     */
    NUMBER_DETECTORS("Number detectors", JournalFieldCategory.TIME, "number_detectors", JournalSortDirection.DESCENDING),
    /** 
     * Number time regimes. 
     */
    NUMBER_TIME_REGIMES("Number time regimes", JournalFieldCategory.TIME, "number_time_regimes", JournalSortDirection.DESCENDING),
    /** 
     * Frame sync. 
     */
    FRAME_SYNC("Frame sync", JournalFieldCategory.TIME_AND_FRAME, "frame_sync", JournalSortDirection.ASCENDING),
    /** 
     * Icp version. 
     */
    ICP_VERSION("ICP version", JournalFieldCategory.TIME, "icp_version", JournalSortDirection.ASCENDING),
    /** 
     * Detector table. 
     */
    DETECTOR_TABLE("Detector table", JournalFieldCategory.TIME, "detector_table", JournalSortDirection.ASCENDING),
    /** 
     * Spectra table. 
     */
    SPECTRA_TABLE("Spectra table", JournalFieldCategory.TIME, "spectra_table", JournalSortDirection.ASCENDING),
    /** 
     * Wiring table. 
     */
    WIRING_TABLE("Wiring table", JournalFieldCategory.TIME, "wiring_table", JournalSortDirection.ASCENDING),
    /** 
     * Monitor spectrum. 
     */
    MONITOR_SPECTRUM("Monitor spectrum", JournalFieldCategory.TIME, "monitor_spectrum", JournalSortDirection.ASCENDING),
    /** 
     * Monitor sum. 
     */
    MONITOR_SUM("Monitor sum", JournalFieldCategory.TIME, "monitor_sum", JournalSortDirection.DESCENDING),
    /** 
     * Total mevents. 
     */
    TOTAL_MEVENTS("Total mevents", JournalFieldCategory.TIME, "total_mevents", JournalSortDirection.DESCENDING),
    /** 
     * Comment. 
     */
    COMMENT("Comment", JournalFieldCategory.TIME, "comment", JournalSortDirection.ASCENDING),
    /** 
     * Field label. 
     */
    FIELD_LABEL("Field label", JournalFieldCategory.NAME, "field_label", JournalSortDirection.ASCENDING),
    /** 
     * Instrument geometry. 
     */
    INSTRUMENT_GEOMETRY("Instrument geometry", JournalFieldCategory.TIME, "instrument_geometry", JournalSortDirection.ASCENDING),
    /** 
     * Script name. 
     */
    SCRIPT_NAME("Script name", JournalFieldCategory.TIME, "script_name", JournalSortDirection.ASCENDING),
    /** 
     * Sample name. 
     */
    SAMPLE_NAME("Sample name", JournalFieldCategory.TIME, "sample_name", JournalSortDirection.ASCENDING),
    /** 
     * Sample orientation. 
     */
    SAMPLE_ORIENTATION("Sample orientation", JournalFieldCategory.TIME, "sample_orientation", JournalSortDirection.ASCENDING),
    /** 
     * Temperature label. 
     */
    TEMPERATURE_LABEL("Temperature label", JournalFieldCategory.TIME, "temperature_label", JournalSortDirection.ASCENDING),
    /** 
     * Np ratio. 
     */
    NP_RATIO("NP ratio", JournalFieldCategory.TIME, "np_ratio", JournalSortDirection.ASCENDING),
    /** 
     * Isis cycle. 
     */
    ISIS_CYCLE("Isis cycle", JournalFieldCategory.TIME, "isis_cycle", JournalSortDirection.DESCENDING),
    /** 
     * Event mode. 
     */
    EVENT_MODE("Event mode", JournalFieldCategory.TIME, "event_mode", JournalSortDirection.DESCENDING);
	
	private final String friendlyName;
    private final String sqlFieldName;
    private final JournalFieldCategory category;
    private final IJournalFormatter formatter;
    private final JournalSortDirection sortDirection;

    JournalField(String friendlyName, JournalFieldCategory category, String sqlFieldName, JournalSortDirection sortDirection) {
        this(friendlyName, category, sqlFieldName, new NoopJournalFormatter(), sortDirection);
    }
    
    JournalField(String friendlyName, JournalFieldCategory category, String sqlFieldName, IJournalFormatter formatter, JournalSortDirection sortDirection) {
        this.friendlyName = friendlyName;
        this.category = category;
        this.sqlFieldName = sqlFieldName;
        this.formatter = formatter;
        this.sortDirection = sortDirection;
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
    
    /**
     * Gets a formatter to convert this field's SQL representation into a user-facing representation.
     * @return the name
     */
    public IJournalFormatter getFormatter() {
    	return formatter;
    }
    
    /**
     * Takes a friendly name and find its associated field. Throws an error if no such field exists.
     * @param name the friendly name
     * @return a journal field
     */
    public static JournalField getFieldFromFriendlyName(String name) {
        return (new ArrayList<JournalField>(Arrays.asList(JournalField.values())))
                .stream().filter(f -> f.getFriendlyName().equals(name))
                .findFirst().get();
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
