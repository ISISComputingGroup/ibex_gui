
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

import uk.ac.stfc.isis.ibex.epics.pv.Closable;

public class ClosingWritable<T> extends ForwardingWritable<T, T> implements ClosableWritable<T> {

	private Closable resource;
	
	public ClosingWritable(ClosableWritable<T> destination) {
		setWritable(destination);
	}
	
	public void setWritable(ClosableWritable<T> destination) {
		super.setWritable(destination);
		closeResource();
		resource = destination;
	}

	@Override
	protected T transform(T value) {
		return value;
	}

	@Override
	public void close() {
		closeResource();
	}

	private void closeResource() {
		if (resource != null) {
			resource.close();
		}
	};
}
