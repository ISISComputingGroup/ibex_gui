
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

package uk.ac.stfc.isis.ibex.configserver.displaying;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;

import com.google.common.base.Strings;

public class DisplayBlock extends Block {

	private final String blockServerAlias;

	private String value;
	private String description;
	
	private final BaseObserver<String> valueAdapter = new BaseObserver<String>() {	
		@Override
		public void onValue(String value) {
			setValue(value);
		}

		@Override
		public void onError(Exception e) {
			setValue("error");
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			if (!isConnected) {
				setValue("disconnected");
			}
		}
	};
	
	private final BaseObserver<String> descriptionAdapter = new BaseObserver<String>() {	
		@Override
		public void onValue(String value) {
			setDescription(value);
		}

		@Override
		public void onError(Exception e) {
			setDescription("No description available");
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			if (!isConnected) {
				setDescription("No description available");
			}
		}
	};
	
	public DisplayBlock(
			Block block,
			InitialiseOnSubscribeObservable<String> valueSource,
			InitialiseOnSubscribeObservable<String> descriptionSource,
			String blockServerAlias) {
		super(block);
		this.blockServerAlias = blockServerAlias;
				
		valueSource.addObserver(valueAdapter);
		descriptionSource.addObserver(descriptionAdapter);
	}
	
	public String getValue() {
		return value;
	}
	
	public String getDescription() {
		return description;
	}

	public String blockServerAlias() {
		return Instrument.getInstance().currentInstrument().pvPrefix() + blockServerAlias;
	}
	
	private synchronized void setValue(String value) {
		firePropertyChange("value", this.value, this.value = Strings.nullToEmpty(value));
	}
	
	private synchronized void setDescription(String description) {
		firePropertyChange("description", this.description, this.description = Strings.nullToEmpty(description));
	}
}
