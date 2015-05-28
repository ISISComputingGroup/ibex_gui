/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.widgets.styles;

/**
 * Enumeration values to be used in the constructor of a RecordSetter
 * to select options for its display and functionality.
 */
public enum RecordSetterStyle {
	
	/**
	 * Specifies that the RecordSetter will provide a 'Set' button
	 * that sends its current value to the record on click.
	 */
	BUTTON,
	
	/**
	 * Specifies that the RecordSetter will send its current value to
	 * the record when it loses focus.
	 */
	FOCUS,
	
	/**
	 * Specifies that the RecordSetter will provide a small icon that
	 * indicates the current status of the control (disconnected, 
	 * invalid data, valid data, and data sent).
	 */
	ICON,
	
	/**
	 * Specifies that the RecordSetter will provide more prominent colour
	 * highlighting to indicate status
	 */
	HIGHLIGHTING,
	
	/**
	 * Specifies that the RecordSetter will provide all available 
	 * functionality (set on focus loss, set button, status icon, and 
	 * status highlighting).
	 */
	FULL,
	
	/**
	 * Specifies that the RecordSetter will use default functionality
	 * and appearance(BUTTON + ICON).
	 */
	DEFAULT
}
