
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2025 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.configserver.configuration;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.configserver.editing.INamedInComponent;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class representing one block inside a BlockServer configuration.
 * 
 * Note: The values from this class are populated from the BlockServer JSON via
 * reflection. Therefore variable names must reflect those expected from the
 * JSON.
 */
@SuppressWarnings("checkstyle:membername")
public class Block extends ModelObject implements IRuncontrol, INamedInComponent {

    /**
     * Default value to give to periodic scan.
     */
    public static final int DEFAULT_SCAN_RATE = 30; // Seconds

	private String name;
	private String pv;
	private boolean visible;
	private boolean local;
	/**
	 * The name of the component that this block is part of.
	 */
	protected String component;

    private boolean runcontrol;
    private double lowlimit;
    private double highlimit;
    private boolean suspend_on_invalid;

    // Logging configurations, default is logging every DEFAULT_SCAN_RATE
    // seconds
    private boolean log_periodic = true;
    private int log_rate = DEFAULT_SCAN_RATE;
    private float log_deadband;
    
    // Block set configuration
    private boolean set_block;
    private String set_block_val;

    // Alarms config information
    private boolean alarmenabled;
    private boolean alarmlatched;
	private double alarmlowlimit;
    private double alarmhighlimit;
    private double alarmdelay;
    private String alarmguidance;
    
    /**
     * Creates a new block given input properties.
     * 
     * @param name the block name
     * @param pv the name of the PV this block should point at
     * @param visible whether the block should be shown
     * @param local whether the PV is local to the instrument
     */
	public Block(String name, String pv, boolean visible, boolean local) {
		this(name, pv, visible, local, null, 0.0f, 0.0f, false, false, true, DEFAULT_SCAN_RATE, 0.0f, false, "", false,
				false, 0.0d, 0.0d, 0.0d, "");
	}
		
    /**
     * Creates a new block given input properties.
     * 
     * @param name the block name
     * @param pv the name of the PV this block should point at
     * @param visible whether the block should be shown
     * @param local whether the PV is local to the instrument
     * @param component the component the block belongs to
     * @param lowLimit the low limit for run-control
     * @param highLimit the high limit for run-control
     * @param suspendOnInvalid whether to suspend data collection if the block is in invalid alarm
     * @param runcontrol whether run-control is enabled
     * @param logPeriodic whether the block is sampled periodically in the
     *            archiver
     * @param logRate time between archive samples (seconds)
     * @param logDeadband deadband for the block to be archived
     * @param blockSet A boolean value indicating whether or not to set a value on block on config change.
     * @param blockSetVal The value to set the block to on config change if the blockSet is true.
     * @param alarmEnabled whether alarm are enabled for this block
     * @param alarmLatched whether alarm are latched for this block
     * @param alarmLowLimit the low limit for alarm
     * @param alarmHighLimit the high limit for alarm
     * @param alarmDelay the delay before the alarm is triggered
     * @param alarmGuidance guidance text for alarm
     */
	public Block(String name, String pv, boolean visible, boolean local, String component, double lowLimit,
			double highLimit, boolean suspendOnInvalid, Boolean runcontrol, boolean logPeriodic, int logRate,
			float logDeadband, boolean blockSet, String blockSetVal, boolean alarmEnabled, boolean alarmLatched,
			Double alarmLowLimit, Double alarmHighLimit, Double alarmDelay, String alarmGuidance) {
		this.name = name;
		this.pv = pv;
		this.visible = visible;
		this.local = local;
		this.component = component;
		this.lowlimit = lowLimit;
		this.highlimit = highLimit;
		this.runcontrol = runcontrol;
		this.log_deadband = logDeadband;
		this.log_periodic = logPeriodic;
		this.log_rate = logRate;
		this.suspend_on_invalid = suspendOnInvalid;
		this.set_block = blockSet;
		this.set_block_val = blockSetVal;
		this.alarmenabled = alarmEnabled;
		this.alarmlatched = alarmLatched;
		this.alarmlowlimit = alarmLowLimit;
		this.alarmhighlimit = alarmHighLimit;
		this.alarmdelay = alarmDelay;
		this.alarmguidance = alarmGuidance;
	}
	
    /**
     * Copy constructor. Creates a new block as a copy of an input block.
     * 
     * @param other the block to be copied
     */
	public Block(Block other) {
		this(other.name, other.pv, other.visible, other.local, other.component, other.lowlimit, other.highlimit,
				other.suspend_on_invalid, other.runcontrol, other.log_periodic, other.log_rate, other.log_deadband,
				other.set_block, other.set_block_val, other.alarmenabled, other.alarmlatched, other.alarmlowlimit,
				other.alarmhighlimit, other.alarmdelay, other.alarmguidance);
	}

    /**
     * Gets the block name.
     * 
     * @return the block name
     */
	@Override
    public String getName() {
		return name;
	}

    /**
     * Sets the block name.
     * 
     * @param name the new block name
     */
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
    /**
     * Gets the name of the PV pointed at by this block.
     * 
     * @return the name of the PV pointed at by this block
     */
	public String getPV() {
		return pv;
	}

    /**
     * Sets whether the block should be sampled periodically in the archiver.
     * 
     * @param periodic whether the block should be sampled periodically in the
     *            archiver
     */
    public void setLogPeriodic(boolean periodic) {
        firePropertyChange("log_periodic", this.log_periodic, this.log_periodic = periodic);
    }

    /**
     * Gets whether the block is sampled periodically in the archiver.
     * 
     * @return whether the block is sampled periodically in the archiver
     */
    public boolean getLogPeriodic() {
        return log_periodic;
    }

    /**
     * Sets the time between archived samples (seconds).
     * 
     * @param rate the new time between archived samples (seconds)
     */
    public void setLogRate(int rate) {
        firePropertyChange("log_rate", this.log_rate, this.log_rate = rate);
    }

    /**
     * Gets the time between archived samples (seconds).
     * 
     * @return the time between archived samples (seconds)
     */
    public int getLogRate() {
        return log_rate;
    }

    /**
     * Sets the deadband for the block to be archived.
     * 
     * @param deadband the new deadband for the block to be archived
     */
    public void setLogDeadband(float deadband) {
        firePropertyChange("log_deadband", this.log_deadband, this.log_deadband = deadband);
    }

    /**
     * Gets the deadband for the block to be archived.
     * 
     * @return the deadband for the block to be archived
     */
    public float getLogDeadband() {
        return log_deadband;
    }

    /**
     * Sets the name of the PV pointed at by this block.
     * 
     * @param pv the new name of the PV pointed at by this block
     */
	public void setPV(String pv) {
		firePropertyChange("PV", this.pv, this.pv = pv);
	}
	
    /**
     * Gets whether the block is shown.
     * 
     * @return whether the block is shown
     */
	public boolean getIsVisible() {
		return visible;
	}

    /**
     * Sets whether the block should be shown.
     * 
     * @param isVisible whether the block is shown
     */
	public void setIsVisible(boolean isVisible) {
		firePropertyChange("isVisible", this.visible, this.visible = isVisible);
	}
	
    /**
     * Gets whether the PV is local to the instrument.
     * 
     * @return whether the PV is local to the instrument
     */
    public boolean getIsLocal() {
        return local;
    }

    /**
     * Sets whether the PV should be local to the instrument.
     * 
     * @param isLocal whether the PV should be local to the instrument
     */
    public void setIsLocal(boolean isLocal) {
        firePropertyChange("isLocal", this.local, this.local = isLocal);
    }

    /**
     * Gets whether run-control is enabled.
     * 
     * @return whether run-control is enabled
     */
    @Override
	public Boolean getRunControlEnabled() {
        return runcontrol;
	}
	
    /**
     * Sets whether run-control should be enabled.
     * 
     * @param runcontrol whether run-control should be enabled
     */
    @Override
	public void setRunControlEnabled(Boolean runcontrol) {
        firePropertyChange("runControlEnabled", this.runcontrol, this.runcontrol = runcontrol);
	}
	
    /**
     * Gets the low limit for run-control.
     * 
     * @return the low limit for run-control
     */
    @Override
	public Double getRunControlLowLimit() {
        return lowlimit;
    }

    /**
     * Sets the low limit for run-control.
     * 
     * @param rclow the new low limit for run-control
     */
    @Override
	public void setRunControlLowLimit(Double rclow) {
        firePropertyChange("runControlLowLimit", this.lowlimit, this.lowlimit = rclow);
    }

    /**
     * Gets the high limit for run-control.
     * 
     * @return the high limit for run-control
     */
    @Override
	public Double getRunControlHighLimit() {
        return highlimit;
    }

    /**
     * Sets whether run control will suspend if invalid.
     * 
     * @param suspendIfInvalid true if run control will suspend if invalid
     */
    @Override
	public void setSuspendIfInvalid(Boolean suspendIfInvalid) {
        firePropertyChange("suspendOnInvalid", this.suspend_on_invalid, this.suspend_on_invalid = suspendIfInvalid);
    }
    
    /**
     * Gets whether run control will suspend if invalid.
     * 
     * @return Whether run control will suspend if invalid
     */
    @Override
	public Boolean getSuspendIfInvalid() {
        return suspend_on_invalid;
    }

    /**
     * Sets the high limit for run-control.
     * 
     * @param rchigh the new high limit for run-control
     */
    @Override
	public void setRunControlHighLimit(Double rchigh) {
        firePropertyChange("runControlHighLimit", this.highlimit, this.highlimit = rchigh);
    }

    /**
     * Gets the component the block belongs to.
     * 
     * @return the component the block belongs to
     */
	public String getComponent() {
		return component;
	}

    /**
     * Gets whether the block belongs to a component.
     * 
     * @return whether the block belongs to a component
     */
	@Override
    public boolean inComponent() {
		return !Strings.isNullOrEmpty(component);
	}
	
    /**
     * Sets whether the block should be set on config change.
     * 
     * @param blockSet whether or not the block should be set on config change
     */
    public void setBlockSet(boolean blockSet) {
        firePropertyChange("blockSet", this.set_block, this.set_block = blockSet);
    }

    /**
     * Gets whether the block is set on config change.
     * 
     * @return whether the block is set on config change
     */
    public boolean getblockSet() {
        return set_block;
    }
    
    /**
     * Sets the value a block should be set to on config change.
     * 
     * @param blockSetVal the value a block should be set to on config change
     */
    public void setBlockSetVal(String blockSetVal) {
        firePropertyChange("blockSetVal", this.set_block_val, this.set_block_val = blockSetVal);
    }

    /**
     * Gets the value a block should be set to on config change.
     * 
     * @return the value a block should be set to on config change
     */
    public String getblockSetVal() {
        return set_block_val;
    }
	
	/**
	 * @return the alarmEnabled
	 */
	public boolean isAlarmEnabled() {
		return alarmenabled;
	}

	/**
	 * @param alarmEnabled the alarmEnabled to set
	 */
	public void setAlarmEnabled(boolean alarmEnabled) {
		firePropertyChange("alarmEnabled", this.alarmenabled, this.alarmenabled = alarmEnabled);
	}

	/**
	 * @return the alarmLatched
	 */
	public boolean isAlarmLatched() {
		return alarmlatched;
	}

	/**
	 * @param alarmLatched the alarmLatched to set
	 */
	public void setAlarmLatched(boolean alarmLatched) {
		firePropertyChange("alarmLatched", this.alarmlatched, this.alarmlatched = alarmLatched);
	}

	/**
	 * @return the alarmLowLimit
	 */
	public double getAlarmLowLimit() {
		return alarmlowlimit;
	}

	/**
	 * @param alarmLowLimit the alarmLowLimit to set
	 */
	public void setAlarmLowLimit(double alarmLowLimit) {
		firePropertyChange("alarmLowLimit", this.alarmlowlimit, this.alarmlowlimit = alarmLowLimit);
	}

	/**
	 * @return the alarmHighLimit
	 */
	public double getAlarmHighLimit() {
		return alarmhighlimit;
	}

	/**
	 * @param alarmHighLimit the alarmHighLimit to set
	 */
	public void setAlarmHighLimit(double alarmHighLimit) {
		firePropertyChange("alarmHighLimit", this.alarmhighlimit, this.alarmhighlimit = alarmHighLimit);
	}

	/**
	 * @return the alarmDelay
	 */
	public double getAlarmDelay() {
		return alarmdelay;
	}

	/**
	 * @param alarmDelay the alarmDelay to set
	 */
	public void setAlarmDelay(double alarmDelay) {
		firePropertyChange("alarmDelay", this.alarmdelay, this.alarmdelay = alarmDelay);
	}

	/**
	 * @return the alarmGuidance
	 */
	public String getAlarmGuidance() {
		return alarmguidance;
	}

	/**
	 * @param alarmGuidance the alarmGuidance to set
	 */
	public void setAlarmGuidance(String alarmGuidance) {
		firePropertyChange("alarmGuidance", this.alarmguidance, this.alarmguidance = alarmGuidance);
	}

	@Override
	public String toString() {
		return name;
	}
}
