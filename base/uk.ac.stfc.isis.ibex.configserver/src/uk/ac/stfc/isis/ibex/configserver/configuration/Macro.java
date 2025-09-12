/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

import com.google.gson.annotations.SerializedName;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Defines a Macro and its value for an IOC.
 * 
 * The available Macros list for each IOC comes from the BlockServer.
 * 
 */
public class Macro extends ModelObject {
    /**
     * The name of the macro.
     */
	private String name;
	/**
	 * The currently set value of the macro. If this is null the default is used.
	 * Ideally an Optional should be here but GSON doesn't understand optionals :(
	 */
	private String value;
	/**
	 * The description of the macro.
	 */
	private String description;
	/**
	 * The regex pattern that the value needs to match.
	 */
	private String pattern;
	/**
	 * The default value of the macro which is used when no value has been set.
	 */
	private String defaultValue;
	/**
	 * Whether the default value is being used or not, defaults to true.
	 */
	private Boolean useDefault = true;
	/**
	 * Whether the macro has a default value or not (or if it is unknown).
	 */
	private HasDefault hasDefault;
	
	/**
	 * An enum representing the existence or not of a default value.
	 */
	public enum HasDefault {
	    /**
	     * The macro has a default.
	     */
	    @SerializedName("YES")
	    YES,
	    /**
	     * The macro does not have a default.
	     */
	    @SerializedName("NO")
	    NO,
	    /**
         * Whether the macro has a default or not is unknown.
         */
	    @SerializedName("UNKNOWN")
	    UNKNOWN
	}

    /**
	 * GSON requires the default constructor to create the macro properly from
	 * JSON.
	 * 
	 * If the default constructor is not present then GSON uses reflection to
	 * create the object. This mean the parent class's constructor is NOT called.
	 * GSON does not care if this is private.
	 */
    @SuppressWarnings("unused")
	private Macro() {
	}

	/**
	 * Create this Macro based on an existing Macro.
	 * 
	 * @param other exiting Macro to clone
	 */
	public Macro(Macro other) {
		this(other.getName(), other.getValue(), other.getDescription(), other.getPattern(), other.getDefaultValue(), other.getHasDefault(), other.getUseDefault());
	}

	/**
	 * Create a new Macro.
	 * 
	 * @param name macro name
	 * @param value macro value
	 * @param description macro description
	 * @param pattern Regex pattern macro value should follow
	 * @param defaultValue the default value of the macro
	 * @param hasDefault if the macro has a default, does not, or unknown
	 */
	public Macro(String name, String value, String description, String pattern, String defaultValue, HasDefault hasDefault, boolean useDefault) {
		this.name = name;
		this.value = value;
		this.description = description;
		this.pattern = pattern;
		this.defaultValue = defaultValue;
		this.hasDefault = hasDefault;
		this.useDefault = useDefault;
	}
	

	/**
     * @return if the macro has a default, does not have a default, or unknown. 
     */
    public HasDefault getHasDefault() {
        return hasDefault;
    }

	/**
	 * @return macro name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set Macro value and fire a property change.
	 * 
	 * @param value new Macro value
	 */
	public void setValue(String value) {
		firePropertyChange("value", this.value, this.value = value);
	}
	
	/**
	 * Set whether the macro is using it's default.
	 * 
	 * @param value new useDefault value
	 */
	public void setUseDefault(boolean value) {
		firePropertyChange("useDefault", this.useDefault, this.useDefault = value);
	}
	
	/**
     * @return if the macro is set to use it's default value. 
     */
    public boolean getUseDefault() {
        return this.useDefault;
    }

	 /**
     * @return macro value, null indicates the default value is being used
     */
	public String getValue() {
		return value;
	}

	 /**
     * @return macro description
     */
	public String getDescription() {
		return description;
	}

	/**
     * @return macro regex pattern
     */
	public String getPattern() {
		return pattern;
	}

    /**
     * @return default macro value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

	@Override
	public String toString() {
		return name + "=" + value + " useDefault = " +this.getUseDefault();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Macro)) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		Macro other = (Macro) obj;
		return name.equals(other.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
