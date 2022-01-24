
/**
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableGroup;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * The view model for the log settings for a block.
 */
public class BlockGroupViewModel extends ErrorMessageProvider {
    
    private final EditableBlock editingBlock;
    private final EditableConfiguration editingConfig;
    
    private boolean enabled = false;
    private String comboText;
    private String labelText;
    private EditableGroup activeGroup;
    

    /**
     * Constructor.
     * 
     * @param editingBlock the block being edited
     * @param config the configuration being edited
     */
    public BlockGroupViewModel(final EditableBlock editingBlock, EditableConfiguration config) {
    	this.editingBlock = editingBlock;
    	this.editingConfig = config;
    }
    
    

    /**
     * Sets whether logging is enabled. Sets to default value if zero value on
     * enable
     * 
     * @param enabled - Is logging being enabled (true) or disabled (false)
     */
    public void setEnabled(boolean enabled) {
        // Update enabled value first. We might need the new value in
        // updatePeriodic.
        firePropertyChange("enabled", this.enabled, this.enabled = enabled);
        
    }

    /**
     * Gets whether logging is enabled.
     * 
     * @return whether logging is enabled
     */
    public boolean getEnabled() {
        return enabled;
    }

    /**
     * Set the label text.
     * 
     * @param text the new text
     */
    public void setLabelText(String text) {
        firePropertyChange("labelText", this.labelText, this.labelText = text);
    }

    /**
     * @return the label text
     */
    public String getLabelText() {
        return labelText;
    }

    /**
     * Set the combo-box text.
     * 
     * @param selection the new text
     */
    public void setComboText(String selection) {
    	
    	for (EditableGroup group : this.editingConfig.getEditableGroups()) {
    		if (selection.equals(group.getName())) {
    			this.activeGroup = group;
    		}
    	}
        firePropertyChange("comboText", this.comboText, this.comboText = selection);
    }
    
    public String[] getGroups() {
    	List<String> names = this.editingConfig.getGroupNames();
    	String[] ret = new String[names.size()];
    	return names.toArray(ret);
    }
    
    /**
     * @return the combo-box text
     */
    public String getComboText() {
    	return comboText;
    }
    
    /**
     * Update the settings on the block.
     */
    public void updateBlock() {
    	if (this.enabled) {
    		List<EditableBlock> blockToAdd = new ArrayList<>();
    		blockToAdd.add(this.editingBlock);
    		this.activeGroup.toggleSelection(blockToAdd);
    	}
    }
}
