
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

package uk.ac.stfc.isis.ibex.epics.observing;

import static java.util.Objects.requireNonNull;

/**
 * An unsubscriber, which will remove an observer from an observable when it's
 * cancelSubscription method is called.
 *
 * @param <T> the type of values being subscribed to
 */
public class Unsubscriber<T> implements Subscription {

	private Subscribable<T> subscribable;
	private T subscriber;

	/**
	 * Creates a new unsubscriber. When this object's cancelSubscription method is called,
	 * the observer will be removed from the observable.
	 *
	 * @param subscribable - the object to subscribe to
	 * @param subscriber - the object which is subscribing
	 */
	public Unsubscriber(Subscribable<T> subscribable, T subscriber) {
		this.subscribable = requireNonNull(subscribable);
		this.subscriber = requireNonNull(subscriber);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelSubscription() {
		if (subscribable != null && subscriber != null) {
			subscribable.unsubscribe(subscriber);
		}

		// Don't need to keep either of these alive after this point.
		subscriber = null;
		subscribable = null;
	}
}
