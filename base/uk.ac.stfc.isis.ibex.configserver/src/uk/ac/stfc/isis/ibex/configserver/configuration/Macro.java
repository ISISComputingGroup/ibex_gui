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

import java.util.Optional;

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
	 * The currently set value of the macro.
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
	 * Whether the macro has a default value or not (or if it is unknown).
	 */
	private HasDefault hasDefault;
	/**
	 * Whether the default value should be used or not.
	 */
	private transient boolean useDefault;
	
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
		this(other.getName(), other.getValue().isPresent() ? other.getValue().get() : null,
		        other.getDescription(), other.getPattern(), other.getDefaultValue(), other.getHasDefault());
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
	public Macro(String name, String value, String description, String pattern, String defaultValue, HasDefault hasDefault) {
		this.name = name;
		this.value = value;
		this.description = description;
		this.pattern = pattern;
		this.defaultValue = defaultValue;
		this.hasDefault = hasDefault;
	}

	/**
     * @return if the macro has a default, does not have a default, or unknown. 
     */
    protected HasDefault getHasDefault() {
        return hasDefault;
    }

    /**
	 * Set Macro name and fire a property change.
	 * 
	 * @param name new Macro name
	 */
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
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
	public void setValue(Optional<String> value) {
	    Optional<String> oldValue = getValue();
	    this.value = value.isPresent() ? value.get() : null;
		firePropertyChange("value", oldValue, value);
	}

	 /**
     * @return macro value
     */
	public Optional<String> getValue() {
		return Optional.ofNullable(value);
	}
	
	/**
	 * @return macro value for displaying to the user
	 */
	public String getValueDisplay() {
	    return getValue().orElse("(default)");
	}
	
	/**
	 * @return macro value to put in the cell when the user clicks on it to edit it
	 */
	public String getEditCellValue() {
	    if (useDefault) {
	        setUseDefault(false);
	        setValue(Optional.of(""));
	    }
	    return getValueDisplay();
	}

	/**
	 * Set macro description.
	 * 
	 * @param description new macro description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	 /**
     * 
     * @return macro description
     */
	public String getDescription() {
		return description;
	}

	/**
	 * Set macro regex pattern.
	 * 
	 * @param pattern new regex pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
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
    
    /**
     * @return default macro value for displaying to the user
     */
    public String getDefaultDisplay() {
        if (hasDefault == HasDefault.YES) {
            return defaultValue.equals("") ? "(default is the empty string)" : defaultValue;
        } else if (hasDefault == HasDefault.NO) {
            return "(no default)";
        } else {
            return "(default unknown)";
        }
    }
    
    /**
     * @return whether the default value should be used or not
     */
    public boolean getUseDefault() {
        return useDefault;
    }

    /**
     * @param useDefault whether the default value should be used or not
     */
    public void setUseDefault(boolean useDefault) {
        firePropertyChange("useDefault", this.useDefault, this.useDefault = useDefault);
    }
    
    /**
     * Updates the useDefault and the value of the macro given the 'Use Default?' checkbox being checked or unchecked.
     * 
     * @param checked if the checkbox is checked or not
     */
    public void updateFromUseDefaultCheck(boolean checked) {
        setUseDefault(checked);
        if (checked) {
            setValue(Optional.empty());
        } else {
            setValue(Optional.of(""));
        }
    }

	@Override
	public String toString() {
		return name + "=" + value;
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
