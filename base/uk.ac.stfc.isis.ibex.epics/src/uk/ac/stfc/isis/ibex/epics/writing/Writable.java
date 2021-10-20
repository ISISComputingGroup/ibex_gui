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

package uk.ac.stfc.isis.ibex.epics.writing;

import java.io.IOException;

import uk.ac.stfc.isis.ibex.epics.observing.Subscribable;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;

/**
 * Allows writting to a value, with indication of whether the value can be
 * written. For example if this is a PV writable the value is written to the PV
 * and the can write state can be queried.
 *
 * @param <T>
 *            Type that is written
 */
public interface Writable<T> extends Closable, Subscribable<ConfigurableWriter<?, ?>> {

    /**
     *
     * @return True if value can be written; False otherwise. I.e. if this is PV
     *         returns true if client is on the instrument
     */
	boolean canWrite();

    /**
     * Write the value to destination.
     *
     * @param value
     *            the value to write
     * @throws IOException
     *              if the write failed
     */
	void write(T value) throws IOException;

	/**
	 * Write the value to destination.
	 *
	 * Throws a runtime exception on failed writes.
	 *
	 * This method should not be used unless you are
	 * certain that the write cannot possibly fail.
	 *
	 * @param value
	 *             the value to write
	 */
	void uncheckedWrite(T value);
}
