
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
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;

public abstract class ForwardingWriter<TIn, TOut> implements ConfigurableWriter<TIn, TOut>, Closable {

	private ConfigurableWriter<TIn, TOut> writer;
	private Set<Subscription> subscriptions = new CopyOnWriteArraySet<>();

	/**
	 * Sets the writer.
	 * @param writer the writer
	 */
	protected void setWriter(ConfigurableWriter<TIn, TOut> writer) {
		this.writer = writer;
	}

	@Override
	public void write(TIn value) throws IOException {
		writer.write(value);
	}

	@Override
	public void uncheckedWrite(TIn value) {
	    writer.uncheckedWrite(value);
	}

	@Override
	public boolean canWrite() {
		return writer.canWrite();
	}

	@Override
	public Subscription subscribe(Writable<TOut> writable) {
		Subscription writerSubscription = writer.subscribe(writable);
		subscriptions.add(writerSubscription);

		return writerSubscription;
	}

	@Override
	public void unsubscribe(Writable<TOut> writable) {
		writer.unsubscribe(writable);
	}

	@Override
	public void close() {
		for (Subscription subscription : subscriptions) {
			subscription.removeObserver();
		}
	}
}
