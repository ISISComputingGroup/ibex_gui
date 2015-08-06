
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
	private Boolean inRange;
	private String lowLimit;
	private String highLimit;
	private Boolean enabled;
	
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
		public void onConnectionStatus(boolean isConnected) {
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
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setDescription("No description available");
			}
		}
	};
	
	private final BaseObserver<String> inRangeAdapter = new BaseObserver<String>() {	
		@Override
		public void onValue(String value) {
			if (value.equals("NO")) {
				setInRange(false);
			} else {
				// If in doubt set to true
				setInRange(true);
			}
		}

		@Override
		public void onError(Exception e) {
			// If in doubt set to true
			setInRange(true);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				// If in doubt set to true
				setInRange(true);
			}
		}
	};
	
	private final BaseObserver<String> lowLimitAdapter = new BaseObserver<String>() {	
		@Override
		public void onValue(String value) {
			setLowLimit(value);
		}

		@Override
		public void onError(Exception e) {
			setLowLimit("error");
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setLowLimit("disconnected");
			}
		}
	};
	
	private final BaseObserver<String> highLimitAdapter = new BaseObserver<String>() {	
		@Override
		public void onValue(String value) {
			setHighLimit(value);
		}

		@Override
		public void onError(Exception e) {
			setHighLimit("error");
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setLowLimit("disconnected");
			}
		}
	};
	
	private final BaseObserver<String> enabledAdapter = new BaseObserver<String>() {	
		@Override
		public void onValue(String value) {
			if (value.equals("YES")) {
				setEnabled(true);
			} else {
				// If in doubt set to false
				setEnabled(false);
			}
		}

		@Override
		public void onError(Exception e) {
			// If in doubt set to false
			setEnabled(false);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				// If in doubt set to true
				setEnabled(false);
			}
		}
	};
	
	public DisplayBlock(
			Block block,
			InitialiseOnSubscribeObservable<String> valueSource,
			InitialiseOnSubscribeObservable<String> descriptionSource,
			InitialiseOnSubscribeObservable<String> inRangeSource,
			InitialiseOnSubscribeObservable<String> lowLimitSource,
			InitialiseOnSubscribeObservable<String> highLimitSource,
			InitialiseOnSubscribeObservable<String> enabledSource,
			String blockServerAlias) {
		super(block);
		this.blockServerAlias = blockServerAlias;
				
		valueSource.addObserver(valueAdapter);
		descriptionSource.addObserver(descriptionAdapter);
		inRangeSource.addObserver(inRangeAdapter);
		lowLimitSource.addObserver(lowLimitAdapter);
		highLimitSource.addObserver(highLimitAdapter);
		enabledSource.addObserver(enabledAdapter);
	}
	
	public String getValue() {
		return value;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Boolean getInRange() {
		return inRange;
	}
	
	public String getLowLimit() {
		return lowLimit;
	}
	
	public String getHighLimit() {
		return highLimit;
	}
	
	public Boolean getEnabled() {
		return enabled;
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
	
	private synchronized void setInRange(Boolean inRange) {
		firePropertyChange("inRange", this.inRange, this.inRange = inRange);
	}
	
	private synchronized void setLowLimit(String limit) {
		firePropertyChange("lowLimit", this.lowLimit, this.lowLimit = limit);
	}
	
	private synchronized void setHighLimit(String limit) {
		firePropertyChange("highLimit", this.highLimit, this.highLimit = limit);
	}
	
	private synchronized void setEnabled(Boolean enabled) {
		firePropertyChange("enabled", this.enabled, this.enabled = enabled);
	}
}
