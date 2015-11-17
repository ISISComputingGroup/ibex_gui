
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

public abstract class ForwardingWritable<TIn, TOut> extends BaseWritable<TIn> {

    private Closable resource;

	private ConfigurableWriter<TIn, TOut> forwardingWriter = new BaseWriter<TIn, TOut>() {
		@Override
		public void write(TIn value) {
			writeToWritables(transform(value));
		}
		
		@Override
        public void onError(Exception e) {
			error(e);
		};
		
		@Override
        public void onCanWriteChanged(boolean canWrite) {
			canWriteChanged(canWrite);
		};
	};
	
    private Subscription readingSubscription;
	private Subscription writingSubsciption;
	
	@Override
	public void write(TIn value) {
		if (value != null) {
			forwardingWriter.write(value);
		}
	}
	
	@Override
	public void close() {
		cancelSubscriptions();
        closeResource();
	}
	
    public void setWritable(Writable<TOut> destination) {
		cancelSubscriptions();
		forwardingWriter.onCanWriteChanged(false);
		forwardingWriter.onCanWriteChanged(destination.canWrite());
		
        readingSubscription = destination.subscribe(forwardingWriter);
		writingSubsciption = forwardingWriter.writeTo(destination);

        closeResource();
        resource = destination;
	}
	
    protected abstract TOut transform(TIn value);

	private void cancelSubscriptions() {
        if (readingSubscription != null) {
            readingSubscription.removeObserver();
        }

		if (writingSubsciption != null) {
			writingSubsciption.removeObserver();
		}
	}

    private void closeResource() {
        if (resource != null) {
            resource.close();
        }
    };
}
