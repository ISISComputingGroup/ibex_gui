
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
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Registers objects to be closed later.
 */
public class Closer implements Closable {

	private final List<Closable> resources = new ArrayList<>();
	private static final Logger LOG = IsisLog.getLogger(Closer.class);

	/**
	 * Registers a resource to be closed by this class.
	 * @param <T> - the type of the object to register for closing.
	 * @param toClose - the object to register to be closed.
	 * @return the object that was passed in.
	 */
	protected <T extends Closable> T registerForClose(T toClose) {
		// Don't add nulls to the list of items to close.
		Optional.ofNullable(toClose).ifPresent(resources::add);
		return toClose;
	}

	/**
	 * Closes the resources held by this class.
	 */
	@Override
	public void close() {
		for (Closable resource : resources) {
			try {
				resource.close();
			} catch (Exception e) {
				LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
			}
		}
	}

}
