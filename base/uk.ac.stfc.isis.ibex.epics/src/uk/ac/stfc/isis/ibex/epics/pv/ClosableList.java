
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

package uk.ac.stfc.isis.ibex.epics.pv;

import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Class representing a collection of closeable values. Calling close on this list will close all items
 * and clear them from the list.
 *
 * @param <T> - the type of object held by this list.
 */
public class ClosableList<T extends Closable> extends ArrayList<T> implements Closable {

	private static final long serialVersionUID = 8636379885861243532L;
	private static final Logger LOG = IsisLog.getLogger(ClosableList.class);

	/**
	 * Closes all of the resources owned by this closeable list.
	 */
	@Override
	public void close() {
		for (T item : this) {
			try {
			    item.close();
			} catch (RuntimeException e) {
				LoggerUtils.logErrorWithStackTrace(LOG, "Failed to close item in closeable list: " + e.getMessage(), e);
			}
		}

		// Now that all items are closed, no longer need the items in this list to hang around.
		// Clear the list to allow the closables to be GC'd.
		this.clear();
	}
}
