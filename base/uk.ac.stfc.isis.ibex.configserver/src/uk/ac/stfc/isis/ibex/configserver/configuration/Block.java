
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

package uk.ac.stfc.isis.ibex.configserver.configuration;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class representing one block inside a BlockServer configuration.
 * 
 * Note: The values from this class are populated from the BlockServer JSON via
 * reflection. Therefore variable names must reflect those expected from the
 * JSON.
 */
@SuppressWarnings("checkstyle:membername")
public class Block extends ModelObject {

    /**
     * Default value to give to periodic scan.
     */
    public static final int DEFAULT_SCAN_RATE = 30; // Seconds

	private String name;
	private String pv;
	private boolean visible;
	private boolean local;
	private String component;

    private boolean runcontrol;
    private float lowlimit;
    private float highlimit;

    // Logging configurations, default is logging every DEFAULT_SCAN_RATE
    // seconds
    private boolean log_periodic = true;
    private int log_rate = DEFAULT_SCAN_RATE;
    private float log_deadband;

    /**
     * Creates a new block given input properties.
     * 
     * @param name the block name
     * @param pv the name of the PV this block should point at
     * @param visible whether the block should be shown
     * @param local whether the PV is local to the instrument
     */
    public Block(String name, String pv, boolean visible, boolean local) {
        this(name, pv, visible, local, null, 0.0f, 0.0f, false, true, DEFAULT_SCAN_RATE, 0.0f);
    }
		
    /**
     * Creates a new block given input properties.
     * 
     * @param name the block name
     * @param pv the name of the PV this block should point at
     * @param visible whether the block should be shown
     * @param local whether the PV is local to the instrument
     * @param component the component the block belongs to
     * @param lowlimit the low limit for run-control
     * @param highlimit the high limit for run-control
     * @param runcontrol whether run-control is enabled
     * @param logPeriodic whether the block is sampled periodically in the
     *            archiver
     * @param logRate time between archive samples (seconds)
     * @param logDeadband deadband for the block to be archived
     */
    public Block(String name, String pv, boolean visible, boolean local, String component, float lowlimit,
            float highlimit, Boolean runcontrol, boolean logPeriodic, int logRate, float logDeadband) {
		this.name = name;
		this.pv = pv;
		this.visible = visible;
		this.local = local;
		this.component = component;
        this.lowlimit = lowlimit;
        this.highlimit = highlimit;
        this.runcontrol = runcontrol;
        this.log_deadband = logDeadband;
        this.log_periodic = logPeriodic;
        this.log_rate = logRate;
	}
	
    /**
     * Copy constructor. Creates a new block as a copy of an input block.
     * 
     * @param other the block to be copied
     */
	public Block(Block other) {
        this(other.name, other.pv, other.visible, other.local, other.component, other.lowlimit, other.highlimit,
                other.runcontrol, other.log_periodic, other.log_rate, other.log_deadband);
	}

    /**
     * Gets the block name.
     * 
     * @return the block name
     */
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
    public boolean getRCEnabled() {
        return runcontrol;
	}
	
    /**
     * Sets whether run-control should be enabled.
     * 
     * @param runcontrol whether run-control should be enabled
     */
    public void setRCEnabled(boolean runcontrol) {
        firePropertyChange("RCEnabled", this.runcontrol, this.runcontrol = runcontrol);
	}
	
    /**
     * Gets the low limit for run-control.
     * 
     * @return the low limit for run-control
     */
    public float getRCLowLimit() {
        return lowlimit;
    }

    /**
     * Sets the low limit for run-control.
     * 
     * @param rclow the new low limit for run-control
     */
    public void setRCLowLimit(float rclow) {
        firePropertyChange("RCLowLimit", this.lowlimit, this.lowlimit = rclow);
    }

    /**
     * Gets the high limit for run-control.
     * 
     * @return the high limit for run-control
     */
    public float getRCHighLimit() {
        return highlimit;
    }

    /**
     * Sets the high limit for run-control.
     * 
     * @param rchigh the new high limit for run-control
     */
    public void setRCHighLimit(float rchigh) {
        firePropertyChange("RCHighLimit", this.highlimit, this.highlimit = rchigh);
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
	public boolean hasComponent() {
		return !Strings.isNullOrEmpty(component);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
