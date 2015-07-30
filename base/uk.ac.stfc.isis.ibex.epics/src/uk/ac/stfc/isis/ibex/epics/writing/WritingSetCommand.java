
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

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SetCommand;

public class WritingSetCommand<T> extends SetCommand<T> implements Closable {
		
	private final Subscription destinationSubscription;
	private final Subscription writerSubscription;	
	
	private final BaseWriter<T, T> destinationWriter = new BaseWriter<T, T>() {
		@Override
		public void onCanWriteChanged(boolean canWrite) {
			setCanSend(canWrite);
		}

		@Override
		public void write(T value) {
			writeToWritables(value);
		}
		
	};
	
	private WritingSetCommand(Writable<T> destination) {
		writerSubscription = destinationWriter.writeTo(destination);
		destinationSubscription = destination.subscribe(destinationWriter);
	}
	
	public static <T> WritingSetCommand<T> forDestination(Writable<T> destination) {
		return new WritingSetCommand<T>(destination);
	}
	
	@Override
	public void send(T value) {
		destinationWriter.write(value);;
	}
	
	@Override
	public void close() {
		writerSubscription.removeObserver();
		destinationSubscription.removeObserver();
	}
}
