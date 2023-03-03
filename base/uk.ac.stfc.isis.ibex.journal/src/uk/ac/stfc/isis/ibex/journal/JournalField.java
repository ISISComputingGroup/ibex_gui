package uk.ac.stfc.isis.ibex.journal;

import java.util.ArrayList;
import java.util.Arrays;

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
    RUN_NUMBER("Run number", "run_number", JournalSortDirection.DESCENDING),
    /** 
     * Title. 
     */
    TITLE("Title", "title", JournalSortDirection.ASCENDING),
    /** 
     * Start time. 
     */
    START_TIME("Start time", "start_time", new DateTimeJournalFormatter(), JournalSortDirection.DESCENDING),
    /** 
     * End time. 
     */
    END_TIME("End time", "end_time", new DateTimeJournalFormatter(), JournalSortDirection.DESCENDING),
    /** 
     * Duration. 
     */
    DURATION("Duration", "duration", new DurationJournalFormatter(), JournalSortDirection.ASCENDING),
    /** 
     * Uamps. 
     */
    UAMPS("Uamps", "uamps", new DecimalPlacesFormatter(4), JournalSortDirection.DESCENDING),
    /** 
     * Rb number. 
     */
    RB_NUMBER("RB number", "rb_number", JournalSortDirection.DESCENDING),
    /** 
     * Users. 
     */
    USERS("Users", "users", JournalSortDirection.ASCENDING),
    /** 
     * Simulation mode. 
     */
    SIMULATION_MODE("Simulation mode", "simulation_mode", JournalSortDirection.DESCENDING),
    /** 
     * Local contact. 
     */
    LOCAL_CONTACT("Local contact", "local_contact", JournalSortDirection.ASCENDING),
    /** 
     * User institute. 
     */
    USER_INSTITUTE("User institute", "user_institute", JournalSortDirection.ASCENDING),
    /** 
     * Instrument name. 
     */
    INSTRUMENT_NAME("Instrument name", "instrument_name", JournalSortDirection.ASCENDING),
    /** 
     * Sample id. 
     */
    SAMPLE_ID("Sample ID", "sample_id", JournalSortDirection.ASCENDING),
    /** 
     * Measurement first run. 
     */
    MEASUREMENT_FIRST_RUN("Measurement first run", "measurement_first_run", JournalSortDirection.DESCENDING),
    /** 
     * Measurement id. 
     */
    MEASUREMENT_ID("Measurement ID", "measurement_id", JournalSortDirection.DESCENDING),
    /** 
     * Measurement label. 
     */
    MEASUREMENT_LABEL("Measurement label", "measurement_label", JournalSortDirection.ASCENDING),
    /** 
     * Measurement type. 
     */
    MEASUREMENT_TYPE("Measurement type", "measurement_type", JournalSortDirection.ASCENDING),
    /** 
     * Measurement subid. 
     */
    MEASUREMENT_SUBID("Measurement subid", "measurement_subid", JournalSortDirection.DESCENDING),
    /** 
     * Raw frames. 
     */
    RAW_FRAMES("Raw frames", "raw_frames", JournalSortDirection.DESCENDING),
    /** 
     * Good frames. 
     */
    GOOD_FRAMES("Good frames", "good_frames", JournalSortDirection.DESCENDING),
    /** 
     * Number periods. 
     */
    NUMBER_PERIODS("Number periods", "number_periods", JournalSortDirection.ASCENDING),
    /** 
     * Number spectra. 
     */
    NUMBER_SPECTRA("Number spectra", "number_spectra", JournalSortDirection.DESCENDING),
    /** 
     * Number detectors. 
     */
    NUMBER_DETECTORS("Number detectors", "number_detectors", JournalSortDirection.DESCENDING),
    /** 
     * Number time regimes. 
     */
    NUMBER_TIME_REGIMES("Number time regimes", "number_time_regimes", JournalSortDirection.DESCENDING),
    /** 
     * Frame sync. 
     */
    FRAME_SYNC("Frame sync", "frame_sync", JournalSortDirection.ASCENDING),
    /** 
     * Icp version. 
     */
    ICP_VERSION("ICP version", "icp_version", JournalSortDirection.ASCENDING),
    /** 
     * Detector table. 
     */
    DETECTOR_TABLE("Detector table", "detector_table", JournalSortDirection.ASCENDING),
    /** 
     * Spectra table. 
     */
    SPECTRA_TABLE("Spectra table", "spectra_table", JournalSortDirection.ASCENDING),
    /** 
     * Wiring table. 
     */
    WIRING_TABLE("Wiring table", "wiring_table", JournalSortDirection.ASCENDING),
    /** 
     * Monitor spectrum. 
     */
    MONITOR_SPECTRUM("Monitor spectrum", "monitor_spectrum", JournalSortDirection.ASCENDING),
    /** 
     * Monitor sum. 
     */
    MONITOR_SUM("Monitor sum", "monitor_sum", JournalSortDirection.DESCENDING),
    /** 
     * Total mevents. 
     */
    TOTAL_MEVENTS("Total mevents", "total_mevents", JournalSortDirection.DESCENDING),
    /** 
     * Comment. 
     */
    COMMENT("Comment", "comment", JournalSortDirection.ASCENDING),
    /** 
     * Field label. 
     */
    FIELD_LABEL("Field label", "field_label", JournalSortDirection.ASCENDING),
    /** 
     * Instrument geometry. 
     */
    INSTRUMENT_GEOMETRY("Instrument geometry", "instrument_geometry", JournalSortDirection.ASCENDING),
    /** 
     * Script name. 
     */
    SCRIPT_NAME("Script name", "script_name", JournalSortDirection.ASCENDING),
    /** 
     * Sample name. 
     */
    SAMPLE_NAME("Sample name", "sample_name", JournalSortDirection.ASCENDING),
    /** 
     * Sample orientation. 
     */
    SAMPLE_ORIENTATION("Sample orientation", "sample_orientation", JournalSortDirection.ASCENDING),
    /** 
     * Temperature label. 
     */
    TEMPERATURE_LABEL("Temperature label", "temperature_label", JournalSortDirection.ASCENDING),
    /** 
     * Np ratio. 
     */
    NP_RATIO("NP ratio", "np_ratio", JournalSortDirection.ASCENDING),
    /** 
     * Isis cycle. 
     */
    ISIS_CYCLE("Isis cycle", "isis_cycle", JournalSortDirection.DESCENDING),
    /** 
     * Event mode. 
     */
    EVENT_MODE("Event mode", "event_mode", JournalSortDirection.DESCENDING);
	
	private final String friendlyName;
    private final String sqlFieldName;
    private final IJournalFormatter formatter;
    private final JournalSortDirection sortDirection;

    JournalField(String friendlyName, String sqlFieldName, JournalSortDirection sortDirection) {
        this(friendlyName, sqlFieldName, new NoopJournalFormatter(), sortDirection);
    }
    
    JournalField(String friendlyName, String sqlFieldName, IJournalFormatter formatter, JournalSortDirection sortDirection) {
        this.friendlyName = friendlyName;
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
}
