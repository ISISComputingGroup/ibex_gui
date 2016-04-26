
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
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Contains the functionality to display a Blocks value and run-control settings
 * in a GUI.
 * 
 * Rather than inheriting from Block it holds a reference to the Block as this
 * provides better encapsulation of Block's functionality.
 *
 */
public class DisplayBlock extends ModelObject {

    public static final String TEXT_COLOR = "textColor";
    public static final String BACKGROUND_COLOR = "backgroundColor";

    public static final Color DARK_RED = SWTResourceManager.getColor(192, 0, 0);
    public static final Color WHITE = SWTResourceManager.getColor(255, 255, 255);
    public static final Color BLACK = SWTResourceManager.getColor(0, 0, 0);
    public static final Color GREEN = SWTResourceManager.getColor(51, 255, 153);

    private final String blockServerAlias;

    private final Block block;

    private String value;
    private String description;

    /**
     * Indicates whether the block in currently within run-control range
     */
    private Boolean inRange;

    /**
     * The current low limit run-control setting. This can be different from
     * what is set in the configuration.
     */
    private String lowlimit;

    /**
     * The current high limit run-control setting. This can be different from
     * what is set in the configuration.
     */
    private String highlimit;

    /**
     * Specifies whether the block is currently under run-control. This can be
     * different from what is set in the configuration.
     */
    private boolean runcontrol;

    private String runControlSymbol;
    private Color textColor;
    private Color backgroundColor;
    private String symbol;

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
        }
    };

    public DisplayBlock(Block block, ForwardingObservable<String> valueSource,
            ForwardingObservable<String> descriptionSource,
            ForwardingObservable<String> inRangeSource,
            ForwardingObservable<String> lowLimitSource,
            ForwardingObservable<String> highLimitSource,
            ForwardingObservable<String> enabledSource, String blockServerAlias) {
        this.block = block;
        this.blockServerAlias = blockServerAlias;

        valueSource.addObserver(valueAdapter);
        descriptionSource.addObserver(descriptionAdapter);
        inRangeSource.addObserver(inRangeAdapter);
        lowLimitSource.addObserver(lowLimitAdapter);
        highLimitSource.addObserver(highLimitAdapter);
        enabledSource.addObserver(enabledAdapter);

        // Initialise text and background colours
        setColors(true);
    }

    public String getName() {
        return block.getName();
    }

    public String getValue() {
        return value;
    }
    
    public String getRunControlSymbol(){
    	return runControlSymbol;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsVisible() {
        return block.getIsVisible();
    }

    /**
     * @return whether the block is within its run-control limits.
     */
    public Boolean getInRange() {
        return inRange;
    }

    /**
     * @return the current low limit for run-control.
     */
    public String getLowLimit() {
        return lowlimit;
    }

    /**
     * @return the current high limit for run-control.
     */
    public String getHighLimit() {
        return highlimit;
    }

    /**
     * @return whether run-control is currently enabled.
     */
    public Boolean getEnabled() {
        return runcontrol;
    }

    /**
     * @return the low limit set in the configuration.
     */
    public String getConfigurationLowLimit() {
        return Float.toString(block.getRCLowLimit());
    }

    /**
     * @return the high limit set in the configuration.
     */
    public String getConfigurationHighLimit() {
        return Float.toString(block.getRCHighLimit());
    }

    /**
     * @return whether run-control is enabled in the configuration.
     */
    public Boolean getConfigurationEnabled() {
        return block.getRCEnabled();
    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public String getSymbol(){
    	return symbol;
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
        setSymbol(inRange);
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
        setColors(inRange);
        setSymbol(inRange);
    }
    
    private void setSymbol(boolean inRange){
    	String s = "1";
        if (getEnabled() == null) {
            s = "2";
            return;
        }
        if (getEnabled()) {
            if (inRange) {
                s = " \u2713 ";
            } else {
                s = " X ";
            }
        } else {
            s = "3";
        }
        firePropertyChange("symbol", this.symbol, this.symbol = s);
    }

    private void setColors(boolean inRange) {
        if (getEnabled() == null) {
            setTextColor(BLACK);
            setBackgroundColor(WHITE);
            return;
        }

        if (getEnabled()) {
            if (inRange) {
                setTextColor(BLACK);
                setBackgroundColor(GREEN);
            } else {
                setTextColor(WHITE);
                setBackgroundColor(DARK_RED);
            }
        } else {
            setTextColor(BLACK);
            setBackgroundColor(WHITE);
        }
    }

    private synchronized void setTextColor(Color color) {
        firePropertyChange(TEXT_COLOR, this.textColor, this.textColor = color);
    }

    private synchronized void setBackgroundColor(Color color) {
        firePropertyChange(BACKGROUND_COLOR, this.backgroundColor, this.backgroundColor = color);
    }
}
