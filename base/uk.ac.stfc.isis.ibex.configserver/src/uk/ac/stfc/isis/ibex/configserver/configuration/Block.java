
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

public class Block extends ModelObject {

	private String name;
	private String pv;
	private boolean visible;
	private boolean local;
	private String subconfig;

    private boolean runcontrol;
    private float lowlimit;
    private float highlimit;

    // Logging configurations, default is no logging
    private boolean log_periodic = true;
    private int log_rate = 0;
    private float log_deadband;

    public Block(String name, String pv, boolean visible, boolean local) {
        this(name, pv, visible, local, null, 0.0f, 0.0f, false, false, 0, 0.0f);
    }
		
    public Block(String name, String pv, boolean visible, boolean local, String subconfig, float lowlimit,
            float highlimit, Boolean runcontrol, boolean logPeriodic, int logRate, float logDeadband) {
		this.name = name;
		this.pv = pv;
		this.visible = visible;
		this.local = local;
		this.subconfig = subconfig;
        this.lowlimit = lowlimit;
        this.highlimit = highlimit;
        this.runcontrol = runcontrol;
        this.log_deadband = logDeadband;
        this.log_periodic = logPeriodic;
        this.log_rate = logRate;
	}
	
	public Block(Block other) {
        this(other.name, other.pv, other.visible, other.local, other.subconfig, other.lowlimit, other.highlimit,
                other.runcontrol, other.log_periodic, other.log_rate, other.log_deadband);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	public String getPV() {
		return pv;
	}

    public void setLogPeriodic(boolean periodic) {
        firePropertyChange("log_periodic", this.log_periodic, this.log_periodic = periodic);
    }

    public boolean getLogPeriodic() {
        return log_periodic;
    }

    public void setLogRate(int rate) {
        firePropertyChange("log_rate", this.log_rate, this.log_rate = rate);
    }

    public int getLogRate() {
        return log_rate;
    }

    public void setLogDeadband(float deadband) {
        firePropertyChange("log_deadband", this.log_deadband, this.log_deadband = deadband);
    }

    public float getLogDeadband() {
        return log_deadband;
    }

	public void setPV(String pv) {
		firePropertyChange("PV", this.pv, this.pv = pv);
	}
	
	public boolean getIsVisible() {
		return visible;
	}

	public void setIsVisible(boolean isVisible) {
		firePropertyChange("isVisible", this.visible, this.visible = isVisible);
	}
	
    public boolean getIsLocal() {
        return local;
    }

    public void setIsLocal(boolean isLocal) {
        firePropertyChange("isLocal", this.local, this.local = isLocal);
    }

    public boolean getRCEnabled() {
        return runcontrol;
	}
	
    public void setRCEnabled(boolean runcontrol) {
        firePropertyChange("RCEnabled", this.runcontrol, this.runcontrol = runcontrol);
	}
	
    public float getRCLowLimit() {
        return lowlimit;
    }

    public void setRCLowLimit(float rclow) {
        firePropertyChange("RCLowLimit", this.lowlimit, this.lowlimit = rclow);
    }

    public float getRCHighLimit() {
        return highlimit;
    }

    public void setRCHighLimit(float rchigh) {
        firePropertyChange("RCHighLimit", this.highlimit, this.highlimit = rchigh);
    }

	public String subconfig() {
		return subconfig;
	}

	public boolean hasSubConfig() {
		return !Strings.isNullOrEmpty(subconfig);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
