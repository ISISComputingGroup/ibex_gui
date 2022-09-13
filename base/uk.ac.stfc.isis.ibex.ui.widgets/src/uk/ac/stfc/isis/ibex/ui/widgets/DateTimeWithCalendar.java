/*
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */

package uk.ac.stfc.isis.ibex.ui.widgets;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

/**
 * Extended Datetime class so that we can pass calendar to viewModel rather than SWT DateTime 
 * It returns the calendar from where we can get date/time.
 */
public class DateTimeWithCalendar extends DateTime {

	/**
	 * Creates a new DateTimeWithCalendar.
	 * @param parent the parent composite
	 * @param style the SWT style flags
	 */
	public DateTimeWithCalendar(Composite parent, int style) {
		super(parent, style);
	}


	@Override
	protected void checkSubclass() {
		/** 
		 * Do nothing, overriding the function that checks if this is subclass or else 
		 * eclipse will not allow to create subclass of DateTime.
		 */
	}
	
	/**
	 * return date in Calendar format.
	 * @return returns whatever date is set by user
	 */
	public Calendar getDateTime() {
		return new GregorianCalendar(
					 this.getYear(),
					 this.getMonth(), 
					 this.getDay(),
					 this.getHours(), 
					 this.getMinutes(),
					 this.getSeconds());
	}
}
