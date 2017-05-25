
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

import uk.ac.stfc.isis.ibex.epics.conversion.DoNothingConverter;

/**
 * A Forwarding Writable which performs an action after it has written its value
 * to the destination.
 *
 * @param <T>
 *            the type of the item to write
 */
public class ForwardingWritableWithAction<T> extends ForwardingWritable<T, T> {


    private ForwardingWritableAction action;

    /**
     * Instantiates a new forwarding writable with action.
     *
     * @param action the action to perform after writing a value
     * @param destination the destination writable
     */
    public ForwardingWritableWithAction(ForwardingWritableAction action, Writable<T> destination) {
        super(destination, new DoNothingConverter<T>());
        this.action = action;
	}
	
	@Override
    public void write(T value) throws IOException {
		super.write(value);
        action.action();
	}
}
