
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

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;

/**
 * Contains the functionality to display a blocks value and run-control settings in a GUI.
 *
 */
public class DisplayBlock extends Block {
	
	public static final String TEXT_COLOR = "textColor";
	public static final String BACKGROUND_COLOR = "backgroundColor";	
	
	public static final Color DARK_RED = SWTResourceManager.getColor(192, 0, 0);
	public static final Color WHITE = SWTResourceManager.getColor(255, 255, 255);
	public static final Color BLACK = SWTResourceManager.getColor(0, 0, 0);

	private final String blockServerAlias;

	private String value;
	private String description;
	private Boolean inRange;
    private String lowlimit;
    private String highlimit;
    private Boolean runcontrol;
	
	private Color textColor;
	private Color backgroundColor;
	
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
		return lowlimit;
	}
	
	public String getHighLimit() {
		return highlimit;
	}
	
	public Boolean getEnabled() {
		return runcontrol;
	}
	
	public Color getTextColor() {
		return textColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
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
		setColors(inRange);
		firePropertyChange("inRange", this.inRange, this.inRange = inRange);
	}
	
	private synchronized void setLowLimit(String limit) {
		firePropertyChange("lowLimit", this.lowlimit, this.lowlimit = limit);
	}
	
	private synchronized void setHighLimit(String limit) {
		firePropertyChange("highLimit", this.highlimit, this.highlimit = limit);
	}
	
	private synchronized void setEnabled(Boolean enabled) {
		firePropertyChange("enabled", this.runcontrol, this.runcontrol = enabled);
	}
	
	private void setColors(boolean inRange) {
		if (inRange) {
			setTextColor(BLACK);
			setBackgroundColor(WHITE);
		} else {
			setTextColor(WHITE);
			setBackgroundColor(DARK_RED);
		}		
	}
	
	private synchronized void setTextColor(Color color) {
		firePropertyChange(TEXT_COLOR, this.textColor, this.textColor = color);
	}
	
	private synchronized void setBackgroundColor(Color color) {
		firePropertyChange(BACKGROUND_COLOR, this.backgroundColor, this.backgroundColor = color);
	}
}
