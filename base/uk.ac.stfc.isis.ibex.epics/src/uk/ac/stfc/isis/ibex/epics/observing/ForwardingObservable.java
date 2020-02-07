
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.epics.observing;


/**
 * An observable whose source is another observable.
 *
 * @param <T>
 *            The type of the value that you are observing.
 */
public class ForwardingObservable<T> extends TransformingObservable<T, T> {
	
	public final String name;

    /**
     * Sets up an unnamed forwarding observable with a closable source.
     * 
     * @param source
     *            the source observable
     */
	public ForwardingObservable(ClosableObservable<T> source) {
		this("", source);
	}
		
    /**
     * Sets up a forwarding observable with a closable source.
     * 
     * @param source
     *            the source observable
     * @param name
     * 				an optional name for this forwarding observable to help with identification
     */
    public ForwardingObservable(String name, ClosableObservable<T> source) {
    	setSource(source);
    	this.name = name;
    }

	@Override
	protected T transform(T value) {
		return value;
	}
}
