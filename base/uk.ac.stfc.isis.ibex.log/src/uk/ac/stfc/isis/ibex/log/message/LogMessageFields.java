/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2015 Science & Technology Facilities Council.
 * All rights reserved.
 *
 * This program is distributed in the hope that it will be useful.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution.
 * EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
 * AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
 * OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
 *
 * You should have received a copy of the Eclipse Public License v1.0
 * along with this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or 
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.log.message;

/**
 * Names for log message fields as used in the XML representations.
 */
final class LogMessageFieldTags {
    static final String TAG_CONTENTS = "contents";
    static final String TAG_SEVERITY = "severity";
    static final String TAG_EVENTTIME = "eventTime";
    static final String TAG_CREATETIME = "createTime";
    static final String TAG_CLIENTNAME = "clientName";
    static final String TAG_CLIENTHOST = "clientHost";
    static final String TAG_TYPE = "type";
    static final String TAG_APPID = "applicationId";

    private LogMessageFieldTags() {
    }
}

/**
 * The Enum for all the field in the log message tables.
 */
public enum LogMessageFields {
    
    /** The contents column. */
    CONTENTS("Content", LogMessageFieldTags.TAG_CONTENTS, 500), 
    
    /** The severity column. */
    SEVERITY("Severity", LogMessageFieldTags.TAG_SEVERITY, 100), 
    
    /** The event time column. */
    EVENT_TIME("Event Time", LogMessageFieldTags.TAG_EVENTTIME, 200), 
    
    /** The create time column. */
    CREATE_TIME("Create Time", LogMessageFieldTags.TAG_CREATETIME, 0), 
    
    /** The client name column. */
    CLIENT_NAME("Sender", LogMessageFieldTags.TAG_CLIENTNAME, 150), 
    
    /** The client host column. */
    CLIENT_HOST("Sender Host", LogMessageFieldTags.TAG_CLIENTHOST, 0), 
    
    /** The type column. */
    TYPE("Type", LogMessageFieldTags.TAG_TYPE, 100), 
    
    /** The application id column. */
    APPLICATION_ID("Application ID", LogMessageFieldTags.TAG_APPID, 0);

	/** List fo all the columns. */
	public static final LogMessageFields[] COLUMNS = {
			LogMessageFields.CLIENT_NAME, LogMessageFields.CLIENT_HOST,
			LogMessageFields.CONTENTS, LogMessageFields.EVENT_TIME,
			LogMessageFields.CREATE_TIME, LogMessageFields.SEVERITY,
			LogMessageFields.TYPE, LogMessageFields.APPLICATION_ID };
	
    private String displayName;
    private String tagName;
    private int defaultColumnWidth;

    /**
     * Instantiates a new log message fields.
     *
     * @param displayName the display name
     * @param tagName the tag name
     * @param defaultColumnWidth the default column width
     */
    LogMessageFields(String displayName, String tagName,
	    int defaultColumnWidth) {
	this.displayName = displayName;
	this.tagName = tagName;
	this.defaultColumnWidth = defaultColumnWidth;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
    	return displayName;
    }

    /**
     * Gets the tag name.
     *
     * @return the tag name
     */
    public String getTagName() {
    	return tagName;
    }

    /**
     * Gets the default column width.
     *
     * @return the default column width
     */
    public int getDefaultColumnWidth() {
    	return defaultColumnWidth;
    }

    @Override
    public String toString() {
	return displayName;
    }

    /**
     * Gets the field by tag name.
     *
     * @param tagName the tag name to find
     * @return the field by tag name
     * @throws Exception if it doesn't exist
     */
    public static LogMessageFields getFieldByTagName(String tagName)
	    throws Exception {
		switch (tagName) {
		case LogMessageFieldTags.TAG_CONTENTS:
		    return CONTENTS;
		case LogMessageFieldTags.TAG_SEVERITY:
		    return SEVERITY;
		case LogMessageFieldTags.TAG_EVENTTIME:
		    return EVENT_TIME;
		case LogMessageFieldTags.TAG_CREATETIME:
		    return CREATE_TIME;
		case LogMessageFieldTags.TAG_CLIENTNAME:
		    return CLIENT_NAME;
		case LogMessageFieldTags.TAG_CLIENTHOST:
		    return CLIENT_HOST;
		case LogMessageFieldTags.TAG_TYPE:
		    return TYPE;
		case LogMessageFieldTags.TAG_APPID:
		    return APPLICATION_ID;
		default:
		    throw new Exception("Unknown log message field tag: " + tagName);
		}
    }
}
