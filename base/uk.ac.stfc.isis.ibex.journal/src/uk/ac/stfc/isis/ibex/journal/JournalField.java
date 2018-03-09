package uk.ac.stfc.isis.ibex.journal;

import uk.ac.stfc.isis.ibex.journal.formatters.DateTimeJournalFormatter;
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
    RUN_NUMBER("Run number", "run_number"),
    /** 
     * Title. 
     */
    TITLE("Title", "title"),
    /** 
     * Start time. 
     */
    START_TIME("Start time", "start_time", new DateTimeJournalFormatter()),
    /** 
     * End time. 
     */
    END_TIME("End time", "end_time", new DateTimeJournalFormatter()),
    /** 
     * Duration. 
     */
    DURATION("Duration", "duration", new DurationJournalFormatter()),
    /** 
     * Uamps. 
     */
    UAMPS("Uamps", "uamps"),
    /** 
     * Rb number. 
     */
    RB_NUMBER("RB number", "rb_number"),
    /** 
     * Users. 
     */
    USERS("Users", "users"),
    /** 
     * Simulation mode. 
     */
    SIMULATION_MODE("Simulation mode", "simulation_mode"),
    /** 
     * Local contact. 
     */
    LOCAL_CONTACT("Local contact", "local_contact"),
    /** 
     * User institute. 
     */
    USER_INSTITUTE("User institute", "user_institute"),
    /** 
     * Instrument name. 
     */
    INSTRUMENT_NAME("Instrument name", "instrument_name"),
    /** 
     * Sample id. 
     */
    SAMPLE_ID("Sample ID", "sample_id"),
    /** 
     * Measurement first run. 
     */
    MEASUREMENT_FIRST_RUN("Measurement first run", "measurement_first_run"),
    /** 
     * Measurement id. 
     */
    MEASUREMENT_ID("Measurement ID", "measurement_id"),
    /** 
     * Measurement label. 
     */
    MEASUREMENT_LABEL("Measurement label", "measurement_label"),
    /** 
     * Measurement type. 
     */
    MEASUREMENT_TYPE("Measurement type", "measurement_type"),
    /** 
     * Measurement subid. 
     */
    MEASUREMENT_SUBID("Measurement subid", "measurement_subid"),
    /** 
     * Raw frames. 
     */
    RAW_FRAMES("Raw frames", "raw_frames"),
    /** 
     * Good frames. 
     */
    GOOD_FRAMES("Good frames", "good_frames"),
    /** 
     * Number periods. 
     */
    NUMBER_PERIODS("Number periods", "number_periods"),
    /** 
     * Number spectra. 
     */
    NUMBER_SPECTRA("Number spectra", "number_spectra"),
    /** 
     * Number detectors. 
     */
    NUMBER_DETECTORS("Number detectors", "number_detectors"),
    /** 
     * Number time regimes. 
     */
    NUMBER_TIME_REGIMES("Number time regimes", "number_time_regimes"),
    /** 
     * Frame sync. 
     */
    FRAME_SYNC("Frame sync", "frame_sync"),
    /** 
     * Icp version. 
     */
    ICP_VERSION("ICP version", "icp_version"),
    /** 
     * Detector table. 
     */
    DETECTOR_TABLE("Detector table", "detector_table"),
    /** 
     * Spectra table. 
     */
    SPECTRA_TABLE("Spectra table", "spectra_table"),
    /** 
     * Wiring table. 
     */
    WIRING_TABLE("Wiring table", "wiring_table"),
    /** 
     * Monitor spectrum. 
     */
    MONITOR_SPECTRUM("Monitor spectrum", "monitor_spectrum"),
    /** 
     * Monitor sum. 
     */
    MONITOR_SUM("Monitor sum", "monitor_sum"),
    /** 
     * Total mevents. 
     */
    TOTAL_MEVENTS("Total mevents", "total_mevents"),
    /** 
     * Comment. 
     */
    COMMENT("Comment", "comment"),
    /** 
     * Field label. 
     */
    FIELD_LABEL("Field label", "field_label"),
    /** 
     * Instrument geometry. 
     */
    INSTRUMENT_GEOMETRY("Instrument geometry", "instrument_geometry"),
    /** 
     * Script name. 
     */
    SCRIPT_NAME("Script name", "script_name"),
    /** 
     * Sample name. 
     */
    SAMPLE_NAME("Sample name", "sample_name"),
    /** 
     * Sample orientation. 
     */
    SAMPLE_ORIENTATION("Sample orientation", "sample_orientation"),
    /** 
     * Temperature label. 
     */
    TEMPERATURE_LABEL("Temperature label", "temperature_label"),
    /** 
     * Np ratio. 
     */
    NP_RATIO("NP ratio", "np_ratio"),
    /** 
     * Isis cycle. 
     */
    ISIS_CYCLE("Isis cycle", "isis_cycle"),
    /** 
     * Event mode. 
     */
    EVENT_MODE("Event mode", "event_mode");
	
	private final String friendlyName;
    private final String sqlFieldName;
    private final IJournalFormatter formatter;
    
    private JournalField(String friendlyName, String sqlFieldName) {
    	this(friendlyName, sqlFieldName, new NoopJournalFormatter());
    }
    
    private JournalField(String friendlyName, String sqlFieldName, IJournalFormatter formatter) {
    	this.friendlyName = friendlyName;
    	this.sqlFieldName = sqlFieldName;
    	this.formatter = formatter;
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
    
}
