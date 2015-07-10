
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

package uk.ac.stfc.isis.ibex.configserver.editing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Component;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.internal.ComponentFilteredConfiguration;
import uk.ac.stfc.isis.ibex.configserver.internal.DisplayUtils;
import uk.ac.stfc.isis.ibex.configserver.internal.IocDescriber;
import uk.ac.stfc.isis.ibex.configserver.internal.IocFilteredConfiguration;
import uk.ac.stfc.isis.ibex.model.ModelObject;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class EditableConfiguration extends ModelObject {

	private static final String DEFAULT_BLOCK_NAME = "NEW_BLOCK";
	private static final String DEFAULT_GROUP_NAME = "NEW_GROUP";
	
	private final DefaultName blockName = new DefaultName(DEFAULT_BLOCK_NAME);
	private final DefaultName groupName = new DefaultName(DEFAULT_GROUP_NAME);

	private String name;
	private String description;
	private String synoptic;
	private String dateCreated;
	private String dateModified;
	private final List<EditableIoc> editableIocs = new ArrayList<>();
	private final List<EditableGroup> editableGroups = new ArrayList<>();
	private final List<EditableBlock> editableBlocks = new ArrayList<>();
	private final List<EditableBlock> availableBlocks = new ArrayList<>();
	private final EditableComponents editableComponents;
	private List<String> history = new ArrayList<>();
	

	private final List<PV> pvs;
	private final IocDescriber descriptions;
	
	private final PropertyChangeListener blockRenameListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			String oldName = (String)evt.getOldValue();
			String newName = (String)evt.getNewValue();
			
			// Recreate the collection before the rename occurred.
			Collection<Block> blocksBeforeRename = getBlocks();
			Block renamed = getBlockByName(blocksBeforeRename, newName);
			blocksBeforeRename.remove(renamed);
			
			Block oldBlock = new Block(renamed);
			oldBlock.setName(oldName);
			blocksBeforeRename.add(oldBlock);
			
			makeBlockUnavailable((EditableBlock) oldBlock);
			makeBlockAvailable((EditableBlock) renamed);
			
			firePropertyChange("blocks", blocksBeforeRename, getBlocks());
		}
	};
	
	public EditableConfiguration(
			Configuration config,
			Collection<EditableIoc> iocs,
			Collection<Component> components,
			Collection<PV> pvs,
			IocDescriber descriptions) {
		this.name = config.name();
		this.description = config.description();
		this.synoptic = config.synoptic();

		this.history = new ArrayList<>();
		
		for (String date : config.getHistory()) {
			this.history.add(date);
		}
		
		this.pvs = new ArrayList<>(pvs);
		this.descriptions = descriptions;
		
		for (Block block : config.getBlocks()) {
			EditableBlock eb = new EditableBlock(block);
			editableBlocks.add(eb);
			makeBlockAvailable(eb);
			addRenameListener(eb);
		}
		
		for (Group group : config.getGroups()) {
			editableGroups.add(new EditableGroup(this, group));
		}
	
		mergeSelectedAndAvailableIocs(config.getIocs(), iocs);
		
		editableComponents = new EditableComponents(config.getComponents(), components);
		editableComponents.addPropertyChangeListener(passThrough());
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getSynoptic() {
		return synoptic;
	}
	
	public String getDateCreated() {
		if (history.size() != 0) {
			return history.get(0);
		}
		else {
			return "";
		}
	}
	
	public String getDateModified() {
		if (history.size() != 0) {
			return history.get(history.size() - 1);
		}
		else {
			return "";
		}
	}
	
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	public void setDescription(String description) {
		firePropertyChange("description", this.description, this.description = description);
	}
	
	public void setSynoptic(String synoptic) {
		firePropertyChange("synoptic", this.synoptic, this.synoptic = synoptic);
	}
	
	public void setDateCreated(String dateCreated) {
		firePropertyChange("dateCreated", this.dateCreated, this.dateCreated = dateCreated);
	}
	
	public void setDateModified(String dateModified) {
		firePropertyChange("dateModified", this.dateModified, this.dateModified = dateModified);
	}
	
	private Collection<Block> getBlocks() {
		return Lists.newArrayList(Iterables.transform(editableBlocks, new Function<EditableBlock, Block>() {
			@Override
			public Block apply(EditableBlock block) {
				return block;
			}
		}));
	}
	
	
	private Collection<Group> getGroups() {
		return Lists.newArrayList(Iterables.transform(getEditableGroups(), new Function<EditableGroup, Group>() {
			@Override
			public Group apply(EditableGroup group) {
				return group;
			}
			
		}));
	}
	
	private Collection<Ioc> getIocs() {
		return Lists.newArrayList(Iterables.transform(editableIocs, new Function<EditableIoc, Ioc>() {
			@Override
			public Ioc apply(EditableIoc ioc) {
				return ioc;
			}
		}));
	}
	
	private Collection<Component> getComponents() {
		return editableComponents.getSelected();
	}
	
	private Collection<String> getHistory() {
		return history;
	}
	
	public Collection<PV> pvs() {
		return new ArrayList<>(pvs);
	}
	
	public Collection<EditableBlock> getEditableBlocks() {
		return new ArrayList<>(editableBlocks);
	}
	
	public Collection<EditableBlock> getAvailableBlocks() {
		return new ArrayList<>(availableBlocks);
	}
	
	public Collection<EditableGroup> getEditableGroups() {
		return new ArrayList<>(DisplayUtils.removeOtherGroup(editableGroups));
	}

	public EditableComponents getEditableComponents() {
		return editableComponents;
	}

	public Collection<EditableIoc> getEditableIocs() {
		return editableIocs;
	}
	
	public EditableBlock addNewBlock() {
		Collection<Block> blocksBeforeAdd = getBlocks();
		
		String name = blockName.getUnique(blockNames());
		EditableBlock block = new EditableBlock(new Block(name, "", true, true, null));
		editableBlocks.add(0, block);
		makeBlockAvailable(block);
		addRenameListener(block);
		
		firePropertyChange("blocks", blocksBeforeAdd, getBlocks());
		
		return block;
	}
	
	public void makeBlockUnavailable(EditableBlock block) {
		availableBlocks.remove(block);
	}
	
	public void makeBlockAvailable(EditableBlock block) {
		if (!availableBlocks.contains(block)) {
			availableBlocks.add(0, block);
		}
	}
	
	public void removeBlock(EditableBlock block) {
		Collection<Block> blocksBefore = getBlocks();
		editableBlocks.remove(block);
		makeBlockUnavailable(block);
		firePropertyChange("blocks", blocksBefore, getBlocks());
	}
	
	public EditableGroup addNewGroup() {
		Collection<EditableGroup> editableGroupsBefore = getEditableGroups();
		Collection<Group> groupsBefore = getGroups();
		
		String name = groupName.getUnique(groupNames());
		EditableGroup newGroup = new EditableGroup(this, new Group(name));
		editableGroups.add(newGroup);
		
		firePropertyChange("editableGroups", editableGroupsBefore, getEditableGroups());
		firePropertyChange("groups", groupsBefore, getGroups());
		
		return newGroup;
	}
	
	public void removeGroup(EditableGroup group) {
		Collection<EditableGroup> editableGroupsBefore = getEditableGroups();
		Collection<Group> groupsBefore = getGroups();
		
		editableGroups.remove(group);
		
		firePropertyChange("editableGroups", editableGroupsBefore, getEditableGroups());
		firePropertyChange("groups", groupsBefore, getGroups());
	}
	
	// Return in a form suitable for saving as a configuration
	public Configuration asConfiguration() {
		Configuration config = new Configuration(getName(), getDescription(), getSynoptic(), getIocs(), getBlocks(), getGroups(), getComponents(), getHistory());
		return new IocFilteredConfiguration(new ComponentFilteredConfiguration(config));
	}
	
	// Return in a form suitable for saving as a component - ie without contained components
	public Configuration asComponent() {
		Configuration config = asConfiguration();
		return new Configuration(
				config.name(), config.description(), config.synoptic(), config.getIocs(), config.getBlocks(), config.getGroups(), 
				Collections.<Component>emptyList(), config.getHistory());
	}
	
	public void swapGroups(EditableGroup group1, EditableGroup group2) {
		Collection<EditableGroup> editableGroupsBefore = getEditableGroups();
		Collection<Group> groupsBefore = getGroups();
		
		//Need to find indexes because the NONE group may throw result in non-sequential numbers
		int index1 = editableGroups.indexOf(group1);
		int index2 = editableGroups.indexOf(group2);
		
		Collections.swap(editableGroups, index1, index2);
		
		firePropertyChange("editableGroups", editableGroupsBefore, getEditableGroups());
		firePropertyChange("groups", groupsBefore, getGroups());	

}
	
	private void mergeSelectedAndAvailableIocs(Collection<Ioc> selected, Collection<EditableIoc> available) {
		Map<String, EditableIoc> iocs = new HashMap<>();
		for (EditableIoc ioc : available) {
			iocs.put(ioc.getName(), ioc);
		}
		
		// IOCs from the actual configuration contain the active macros and description
		for (Ioc ioc : selected) {
			String name = ioc.getName();
			EditableIoc replacementIoc = new EditableIoc(ioc);
			EditableIoc iocToReplace = iocs.get(name);
			
			replacementIoc.setAvailableMacros(iocToReplace.getAvailableMacros());
			// Put will replace existing entries
			iocs.put(name, replacementIoc);
		}
		
		setIocDescriptions(iocs);
		
		editableIocs.addAll(iocs.values());
		Collections.sort(editableIocs);
	}
	
	/**
	 * Iterate over the IOCs in the map, and add the description.
	 * 
	 * @param iocs A map of the iocs
	 */
	private void setIocDescriptions(Map<String, EditableIoc> iocs) {
		Iterator<Entry<String, EditableIoc>> it = iocs.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, EditableIoc> ioc = it.next();
			ioc.getValue().setIocDescriber(descriptions.getDescription(ioc.getValue().getName()));
		}
	}
	
	private Collection<String> blockNames() {
		List<String> names = new ArrayList<>();
		for (Block block : getBlocks()) {
			names.add(block.getName());
		}
		
		return names;
	}
	
	private static Block getBlockByName(Iterable<Block> blocks, final String name) {
		return Iterables.find(blocks, new Predicate<Block>() {
			@Override
			public boolean apply(Block block) {
				return block.getName().equals(name);
			}
		});
	}

	private Collection<String> groupNames() {
		List<String> names = new ArrayList<>();
		for (Group group : getGroups()) {
			names.add(group.getName());
		}
		
		return names;
	}
	
	private void addRenameListener(EditableBlock block) {
		block.addPropertyChangeListener("name", blockRenameListener);
	}
}
