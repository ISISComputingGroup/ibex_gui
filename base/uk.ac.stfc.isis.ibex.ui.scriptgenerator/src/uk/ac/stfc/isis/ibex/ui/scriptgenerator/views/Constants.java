/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2023 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.ResourceManager;

/**
 * Constants used on the script generator page.
 */
final class Constants {
	protected static final String BUTTON_TITLE_SAVE = "Save Script";
	protected static final String BUTTON_TITLE_SAVE_AS = "Save Script As";
	protected static final String BUTTON_TITLE_LOAD = "Load Script";

	protected static final String BUTTON_TITLE_ADD_ROW_TO_END = "Add Row to End";
	protected static final String BUTTON_TITLE_INSERT_ROW_BELOW = "Insert Row Below";
	protected static final String BUTTON_TITLE_DELETE_ROWS = "Clear All Rows";

	protected static final String CHECKBOX_TITLE_PARAM_TRANSFER = "Transfer Compatible Parameters";
	protected static final String CHECKBOX_TITLE_INVALID_PAUSE = "Invalid Actions are Paused on";
	protected static final String CHECKBOX_TITLE_INVALID_SKIP = "Invalid Actions are Skipped";

	protected static final String BUTTON_TOOLTIP_ADD_ROW_TO_END = "Add a new row to the end of the table";
	protected static final String BUTTON_TOOLTIP_INSERT_ROW_BELOW = "Insert a new row below the selected line in the table";
	protected static final String BUTTON_TOOLTIP_DELETE_ROWS = "Delete all rows in the table";
	protected static final String TOOLTIP_PARAM_TRANSFER = "Enable action parameter transferring where possible when changing script definitions";
	protected static final String TOOLTIP_INVALID_PAUSE = "Enable toggle to pause whenever an invalid action is found";
	protected static final String TOOLTIP_INVALID_SKIP = "Enable toggle to skip whenever an invalid action is found";

	protected static final String LOADING_MESSAGE = "Loading...";
	protected static final String RELOADING_MESSAGE = "Reloading...";

	/**
	 * Defines the default behaviour of action parameter transferring.
	 */
	protected static final boolean PARAM_TRANSFER_DEFAULT = true;
	
	/**
	 * When true dynamic scripting skips over invalid actions, when false
	 * encountering an invalid action while running pauses the execution.
	 */
	protected static final boolean INVALID_SKIP_DEFAULT = false;

	protected static final Image IMAGE_RUN = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.scriptgenerator",
			"icons/play.png");
	protected static final Image IMAGE_PAUSE = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.scriptgenerator",
			"icons/pause.png");
	protected static final Image IMAGE_STOP = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.scriptgenerator",
			"icons/stop.png");

	protected static final Image IMAGE_UP_ARROW = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui",
			"icons/move_up.png");
	protected static final Image IMAGE_DOWN_ARROW = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui",
			"icons/move_down.png");

	/**
	 * A clear colour for use in other script generator table columns when a row is
	 * valid.
	 */
	protected static final Color CLEAR_COLOUR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	protected static final Display DISPLAY = Display.getDefault();
	/**
	 * A dark red for use in the validity column when a row is invalid.
	 */
	protected static final Color INVALID_DARK_COLOR = DISPLAY.getSystemColor(SWT.COLOR_RED);
	/**
	 * A light read for use in the other script generator table columns when a row
	 * is invalid.
	 */
	protected static final Color INVALID_LIGHT_COLOR = new Color(DISPLAY, 255, 204, 203);
	/**
	 * The string to use to denote an unknown amount of estimated time.
	 */
	protected static final String UNKNOWN_TEXT = "Unknown";
	/**
	 * A green for use in the validity column when a row is valid.
	 */
	protected static final Color VALID_COLOR = DISPLAY.getSystemColor(SWT.COLOR_GREEN);
	/**
	 * A clear colour for use in other script generator table columns when a row is
	 * valid.
	 */
	protected static final Color CLEAR_COLOR = DISPLAY.getSystemColor(SWT.COLOR_WHITE);
	/**
	 * The maximum number of lines to display in the "Get Validity Errors" dialog
	 * box before suppressing others.
	 */
	protected static final int MAX_ERRORS_TO_DISPLAY_IN_DIALOG = 10;
	protected static final String UNSAVED_CHANGES_MARKER = " (*)";
	/**
	 * The header of the validity column.
	 */
	protected static final String VALIDITY_COLUMN_HEADER = "Validity";
	/**
	 * The header of the action column.
	 */
	protected static final String ACTION_NUMBER_COLUMN_HEADER = "Action";
	/**
	 * The header of the estimated run time column.
	 */
	protected static final String ESTIMATED_RUN_TIME_COLUMN_HEADER = "Estimated run time";

	/**
	 * Do not use this constructor.
	 */
	private Constants() {
		// Utility class is not meant to be instantiated
		throw new UnsupportedOperationException();
	}
}