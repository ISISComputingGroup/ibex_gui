
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

public class BaseCachingObservable<T> extends BaseObservable<T> implements CachingObservable<T> {
	
	private T value;
	private boolean isConnected;
	private Exception lastError;
		
	public T value() {
		return value;
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public Exception lastError() {
		return lastError;
	}
	
	@Override
	protected void setValue(T value) {
		if (value == null) {
			return;
		}
		
		this.value = value;
		super.setValue(value);
	}
	
	@Override
	protected void setError(Exception e) {
		lastError = e;
		super.setError(e);
	}
	
	@Override
	protected void setConnectionChanged(boolean isConnected) {
		this.isConnected = isConnected;
		super.setConnectionChanged(isConnected);
	}
}
