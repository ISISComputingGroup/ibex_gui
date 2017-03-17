
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2017
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

package uk.ac.stfc.isis.ibex.configserver.editing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.internal.ComponentFilteredConfiguration;
import uk.ac.stfc.isis.ibex.configserver.internal.DisplayUtils;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.validators.GroupNamesProvider;

/**
 * Holds an editable configuration, and notifies any listeners set to changes to
 * this class.
 * 
 * Configuration includes name, description, a default synoptic, date created,
 * date modified and lists of editable IOCs, groups, blocks and components and
 * available blocks (blocks not from the current instrument).
 * 
 */
public class EditableConfiguration extends ModelObject implements GroupNamesProvider {

    /** The property change identifier associated with editing groups. */
    public static final String EDITABLE_GROUPS = "editableGroups";

    /** The default group name text to apply to new groups. */
    private static final String DEFAULT_GROUP_NAME = "NEW_GROUP";
    /** The default Name to apply to groups. */
    private final DefaultName groupName = new DefaultName(DEFAULT_GROUP_NAME);
    /** The name of the configuration. */
    private String name;
    /** A description of the configuration. */
    private String description;
    /** The default synoptic to load with the configuration. */
    private String synoptic;
    /** The date the configuration was created. */
    private String dateCreated;
    /** The date the configuration was last modified. */
    private String dateModified;
    /** All IOCs available to the instrument. */
    private Collection<EditableIoc> allIocs = new ArrayList<>();
    /** The IOCs associated with the configuration. */
    private final List<EditableIoc> selectedIocs = new ArrayList<>();
    /** The groups associated with the configuration. */
    private final List<EditableGroup> editableGroups = new ArrayList<>();
    /** The blocks associated with the configuration. */
    private final List<EditableBlock> editableBlocks = new ArrayList<>();
    /**
     * The blocks that are available to the configuration (i.e. including those
     * associated with components.
     */
    private final List<EditableBlock> availableBlocks = new ArrayList<>();
    /** The components associated with the configuration. */
    private final EditableComponents editableComponents;
    /** Dates when the configuration has been changed. */
    private List<String> history = new ArrayList<>();

    /** Available PVs. */
    private final List<PV> pvs;
    
    /** If this is a component. */
    private boolean isComponent;

    /**
     * Listener for block renaming events.
     */
    private final PropertyChangeListener blockRenameListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String oldName = (String) evt.getOldValue();
            String newName = (String) evt.getNewValue();

            // Recreate the collection before the rename occurred.
            Collection<Block> blocksBeforeRename = getBlocks();
            Block renamed = getBlockByName(blocksBeforeRename, newName);
            blocksBeforeRename.remove(renamed);

            Block oldBlock = new Block(renamed);
            oldBlock.setName(oldName);
            blocksBeforeRename.add(oldBlock);

            firePropertyChange("blocks", blocksBeforeRename, getBlocks());
        }
    };

    /**
     * @param config
     *            The root configuration to derive the editable configuration
     *            from
     * @param iocs
     *            The IOCs available to the configuration
     * @param components
     *            The components available to the configuration
     * @param pvs
     *            The PVs available to the configuration
     */
    public EditableConfiguration(
			Configuration config,
			Collection<EditableIoc> iocs,
            Collection<Configuration> components,
            Collection<PV> pvs) {
		this.name = config.name();
		this.description = config.description();
		this.synoptic = config.synoptic();
        this.allIocs = iocs;

        this.history = new ArrayList<>();

        for (String date : config.getHistory()) {
            this.history.add(date);
        }

        this.pvs = new ArrayList<>(pvs);

        for (Block block : config.getBlocks()) {
            EditableBlock eb = new EditableBlock(block);
            editableBlocks.add(eb);
            makeBlockAvailable(eb);
            addRenameListener(eb);
        }

        for (Group group : config.getGroups()) {
            editableGroups.add(new EditableGroup(this, group));
        }

        Map<String, EditableIoc> iocMap = new HashMap<>();
        for (EditableIoc ioc : allIocs) {
            iocMap.put(ioc.getName(), ioc);
        }
        initMacros(iocMap);

        for (Ioc ioc : config.getIocs()) {
            final EditableIoc generalIOC = iocMap.get(ioc.getName());
            EditableIoc editableIOC = new EditableIoc(ioc, generalIOC.getDescription());
            editableIOC.setAvailableMacros(generalIOC.getAvailableMacros());
            addIoc(editableIOC);
        }

        Collection<Configuration> selectedComponents = getComponentDetails(config.getComponents(), components);
        editableComponents = new EditableComponents(selectedComponents, components);
    }

    /**
     * @return The configuration name
     */
    public String getName() {
        return name;
    }

    /**
     * @return The configuration description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The name of the default synoptic
     */
    public String getSynoptic() {
        return synoptic;
    }

    /**
     * @return The date the configuration was created
     */
    public String getDateCreated() {
        if (history.size() != 0) {
            return history.get(0);
        } else {
            return "";
        }
    }

    /**
     * @return The date the configuration was last modified
     */
    public String getDateModified() {
        if (history.size() != 0) {
            return history.get(history.size() - 1);
        } else {
            return "";
        }
    }

    /**
     * @param name
     *            The new configuration name
     */
    public void setName(String name) {
        firePropertyChange("name", this.name, this.name = name);
    }

    /**
     * @param description
     *            The new configuration description
     */
    public void setDescription(String description) {
        firePropertyChange("description", this.description, this.description = description);
    }

    /**
     * @param synoptic
     *            The name of the new default synoptic for the configuration
     */
    public void setSynoptic(String synoptic) {
        firePropertyChange("synoptic", this.synoptic, this.synoptic = synoptic);
    }

    /**
     * @param dateCreated
     *            The date the configuration was created
     */
    public void setDateCreated(String dateCreated) {
        firePropertyChange("dateCreated", this.dateCreated, this.dateCreated = dateCreated);
    }

    /**
     * @param dateModified
     *            The date the configuration was last modified
     */
    public void setDateModified(String dateModified) {
        firePropertyChange("dateModified", this.dateModified, this.dateModified = dateModified);
    }

    /**
     * Whether the configuration is new or not (has previously been modified).
     * 
     * @return True if configuration is new.
     */
    public boolean getIsNew() {
        boolean isNew = (history.size() == 0);
        return isNew;
    }

    /**
     * @return The blocks associated with the configuration
     */
    Collection<Block> getBlocks() {
        return Lists.newArrayList(Iterables.transform(editableBlocks, new Function<EditableBlock, Block>() {
            @Override
            public Block apply(EditableBlock block) {
                return block;
            }
        }));
    }

    /**
     * @return The groups associated with the configuration
     */
    private Collection<Group> getGroups() {
        return Lists.newArrayList(Iterables.transform(getEditableGroups(), new Function<EditableGroup, Group>() {
            @Override
            public Group apply(EditableGroup group) {
                return group;
            }

        }));
    }

    /**
     * @return The IOCs associated with the configuration
     */
    private Collection<Ioc> getIocs() {
        return Lists.newArrayList(Iterables.transform(selectedIocs, new Function<EditableIoc, Ioc>() {
            @Override
            public Ioc apply(EditableIoc ioc) {
                return ioc;
            }
        }));
    }

    /**
     * @return The components associated with the configuration
     */
	private Collection<ComponentInfo> getComponents() {
        Collection<ComponentInfo> result = new ArrayList<ComponentInfo>();
        for (Configuration compDetails : editableComponents.getSelected()) {
            result.add(new ComponentInfo(compDetails));
        }
        return result;
	}
	
    /**
     * @return The dates when the configuration has been modified
     */
    private Collection<String> getHistory() {
        return history;
    }

    /**
     * @return The PVs associated with the configuration
     */
    public Collection<PV> pvs() {
        return new ArrayList<>(pvs);
    }

    /**
     * Adds an IOC to the configuration.
     * 
     * @param ioc
     *            The IOC to be added.
     */
    public void addIoc(EditableIoc ioc) {
        Collection<Ioc> iocsBeforeAdd = getIocs();
        selectedIocs.add(ioc);
        firePropertyChange("iocs", iocsBeforeAdd, getIocs());
    }

    /**
     * Remove one or multiple IOCs from the configuration.
     * 
     * @param iocs
     *            the list of IOCs to remove
     */
    public void removeIocs(List<EditableIoc> iocs) {
        Collection<EditableIoc> iocsBefore = getSelectedIocs();
        for (EditableIoc ioc : iocs) {
            selectedIocs.remove(ioc);
        }
        firePropertyChange("iocs", iocsBefore, getSelectedIocs());
    }

    /**
     * @return The available IOCs not associated with the configuration.
     */
    public Collection<EditableIoc> getAvailableIocs() {
        List<EditableIoc> result = new ArrayList<EditableIoc>();
    
        List<String> selectedIocNames = new ArrayList<String>();
        for (EditableIoc ioc : this.selectedIocs) {
            selectedIocNames.add(ioc.getName());
        }
    
        for (EditableIoc ioc : allIocs) {
            if (!selectedIocNames.contains(ioc.getName())) {
                result.add(ioc);
            }
        }
        Collections.sort(result);
        return result;
    }

    /**
     * @return The editable IOCs associated with the configuration
     */
    public Collection<EditableIoc> getSelectedIocs() {
        return selectedIocs;
    }

    // Add available macros to IOCs that are part of the configuration.
    private void initMacros(Map<String, EditableIoc> available) {
        for (EditableIoc selectedIoc : selectedIocs) {
            Collection<Macro> availableMacros = available.get(selectedIoc.getName()).getAvailableMacros();
            selectedIoc.setAvailableMacros(availableMacros);
        }
    
        Collections.sort(selectedIocs);
    }

    /**
     * @return The editable blocks associated with the configuration
     */
    public Collection<EditableBlock> getEditableBlocks() {
        return new ArrayList<>(editableBlocks);
    }

    /**
     * @return All of the blocks available to the configuration
     */
    public Collection<EditableBlock> getAvailableBlocks() {
        return new ArrayList<>(availableBlocks);
    }

    /**
     * @return The editable groups associated with the configuration
     */
    public Collection<EditableGroup> getEditableGroups() {
        return new ArrayList<>(DisplayUtils.removeOtherGroup(editableGroups));
    }

    /**
     * @return The editable components associated with the configuration
     */
    public EditableComponents getEditableComponents() {
        return editableComponents;
    }

    /**
     * 
     * Add a new block to the configuration.
     * 
     * @param block
     *            the EditableBlock to be added
     * @throws DuplicateBlockNameException
     *             if the name of the block being added is identical to one
     *             already in the configuration
     */
    public void addNewBlock(EditableBlock block) throws DuplicateBlockNameException {
        if (blockNameIsUnique(block.getName())) {
            Collection<Block> blocksBeforeAdd = getBlocks();
            editableBlocks.add(0, block);
            makeBlockAvailable(block);
            addRenameListener(block);
            firePropertyChange("blocks", blocksBeforeAdd, getBlocks());
        } else {
            throw new DuplicateBlockNameException();
        }
    }

    /**
     * Checks whether a given block name is unique or whether a block of that
     * name already exists.
     * 
     * @param name
     *            the name whose uniqueness is checked
     * @return whether the name is unique as boolean
     */
    public boolean blockNameIsUnique(String name) {
        for (EditableBlock existingBlock : editableBlocks) {
            if (existingBlock.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Makes a block unavailable (i.e. block is assigned to a group).
     * 
     * @param block
     *            the block to make unavailable
     */
    public void makeBlockUnavailable(EditableBlock block) {
        availableBlocks.remove(block);
    }

    /**
     * Makes a block available (i.e. block can be assigned to a group).
     * 
     * @param block
     *            the block to make available
     */
    public void makeBlockAvailable(EditableBlock block) {
        if (!availableBlocks.contains(block)) {
            availableBlocks.add(0, block);
        }
    }

    /**
     * Remove a block from the configuration.
     * 
     * @param block
     *            the block to remove
     */
    public void removeBlock(EditableBlock block) {
        Collection<Block> blocksBefore = getBlocks();
        editableBlocks.remove(block);
        makeBlockUnavailable(block);
        firePropertyChange("blocks", blocksBefore, getBlocks());
    }

    /**
     * Remove multiple blocks from the configuration.
     * 
     * @param blocks
     *            the list of blocks to remove
     */
    public void removeBlocks(List<EditableBlock> blocks) {
        Collection<Block> blocksBefore = getBlocks();
        for (EditableBlock block : blocks) {
            editableBlocks.remove(block);
            makeBlockUnavailable(block);
        }
        firePropertyChange("blocks", blocksBefore, getBlocks());
    }

    /**
     * Add a new editable group to the configuration, constructed using the
     * default name.
     * 
     * @return The new group
     */
    public EditableGroup addNewGroup() {
        Collection<EditableGroup> editableGroupsBefore = getEditableGroups();
        Collection<Group> groupsBefore = getGroups();

        String name = groupName.getUnique(getGroupNames());
        EditableGroup newGroup = new EditableGroup(this, new Group(name));
        editableGroups.add(newGroup);

        firePropertyChange(EDITABLE_GROUPS, editableGroupsBefore, getEditableGroups());
        firePropertyChange("groups", groupsBefore, getGroups());

        return newGroup;
    }

    /**
     * Remove a group from the configuration.
     * 
     * @param group
     *            The group to remove
     */
    public void removeGroup(EditableGroup group) {
        Collection<EditableGroup> editableGroupsBefore = getEditableGroups();
        Collection<Group> groupsBefore = getGroups();

        // Remove selected blocks
        group.toggleSelection(group.getSelectedBlocks());

        editableGroups.remove(group);

        firePropertyChange(EDITABLE_GROUPS, editableGroupsBefore, getEditableGroups());
		firePropertyChange("groups", groupsBefore, getGroups());
    }

    /**
     * Return in a form suitable for saving as a configuration.
     * 
     * @return the underlying configuration
     */
    public Configuration asConfiguration() {
        Configuration config = new Configuration(getName(), getDescription(), getSynoptic(), getIocs(), getBlocks(),
                getGroups(), getComponents(), getHistory());
        return new ComponentFilteredConfiguration(config);
    }

    /**
     * Return in a form suitable for saving as a component - ie without
     * contained components.
     * 
     * @return the configuration as a component
     */
    public Configuration asComponent() {
        Configuration config = asConfiguration();
        return new Configuration(config.name(), config.description(), config.synoptic(), config.getIocs(),
                config.getBlocks(), config.getGroups(), Collections.<ComponentInfo>emptyList(), config.getHistory());
    }

    /**
     * Swaps the indices of two groups in the configuration (for moving them up
     * and down).
     * 
     * @param group1
     *            The first group
     * @param group2
     *            The second group
     */
    public void swapGroups(EditableGroup group1, EditableGroup group2) {
        Collection<EditableGroup> editableGroupsBefore = getEditableGroups();
        Collection<Group> groupsBefore = getGroups();

        // Need to find indexes because the NONE group may throw result in
        // non-sequential numbers
        int index1 = editableGroups.indexOf(group1);
        int index2 = editableGroups.indexOf(group2);

        Collections.swap(editableGroups, index1, index2);

        firePropertyChange("editableGroups", editableGroupsBefore, getEditableGroups());
        firePropertyChange("groups", groupsBefore, getGroups());
    }

    /**
     * Return a Block object associated to a given block name.
     * 
     * @param blocks
     *            a list of blocks in the configuration
     * @param name
     *            the name of the block in question
     * @return the Block object
     */
    private static Block getBlockByName(Iterable<Block> blocks, final String name) {
        return Iterables.find(blocks, new Predicate<Block>() {
            @Override
            public boolean apply(Block block) {
                return block.getName().equals(name);
            }
        });
    }

    /**
     * Add a block to the rename listener.
     * 
     * @param block
     *            The block to add to the listener
     */
    private void addRenameListener(EditableBlock block) {
        block.addPropertyChangeListener("name", blockRenameListener);
    }

    /**
     * Get the current list of groups in the configuration.
     * 
     * @return the list of group names
     */
    @Override
    public List<String> getGroupNames() {
        List<String> names = new ArrayList<>();
        for (Group group : getGroups()) {
            names.add(group.getName());
        }

        return names;
    }
    
    /**
     * Sets whether this configuration is a component or not.
     * 
     * @param isComponent
     *            true if this is a component
     */
    public void setIsComponent(boolean isComponent) {
        firePropertyChange("isComponent", isComponent, this.isComponent = isComponent);
    }
    
    /**
     * Get whether this configuration is a component or not.
     * 
     * @returns true if this is a component
     */
    public boolean getIsComponent() {
        return isComponent;
    }
    private Collection<Configuration> getComponentDetails(Collection<ComponentInfo> selected,
            Collection<Configuration> available) {
        Collection<Configuration> result = new ArrayList<Configuration>();
        for (ComponentInfo compInfo : selected) {
            for (Configuration details : available) {
                if (compInfo.getName().equals(details.name())) {
                    result.add(details);
                }
            }
        }
        return result;
    }
}
