
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.internal.DisplayUtils;

/**
 * Extends Group to make it easier to edit.
 *
 */
public class EditableGroup extends Group {

	private final List<EditableBlock> blocksInGroup;
	private final EditableConfiguration config;

	/**
	 * A constructor for an easily editable group.
	 * 
	 * @param configuration
	 *                     The configuration to which the group belongs.
	 * @param group
	 *                     The group to edit.
	 */
	public EditableGroup(final EditableConfiguration configuration, Group group) {
		super(group);

		config = configuration;
		blocksInGroup = lookupBlocksByName(config.getAllBlocks(), group.getBlocks());

		config.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateBlocksFromConfig(config);
			}
		});
	}

	@Override
	public String getName() {
		return DisplayUtils.renameGroup(super.getName());
	}

	/**
	 * Allows to make a collection of blocks easier to edit.
	 * @param blocks
	 *             The collection of blocks to edit.
	 * @return
	 *             The blocks that are now easier to edit.
	 */
	public Collection<EditableBlock> makeEditable(Collection<Block> blocks) {
		List<EditableBlock> editable = new ArrayList<>();
		for (Block block : blocks) {
			editable.add(new EditableBlock(block));
		}

		return editable;
	}

	/**
	 * Returns a collection of blocks that are unselected.
	 * @return
	 *         A collection of blocks that are unselected.
	 */
	public Collection<EditableBlock> getUnselectedBlocks() {
		return new ArrayList<>(config.getBlocksOutsideGroup());
	}

	/**
     * Returns a collection of blocks that are selected.
     * @return
     *         A collection of blocks that are selected.
     */
	public Collection<EditableBlock> getSelectedBlocks() {
		return new ArrayList<>(blocksInGroup);
	}

	/**
	 * Selects or unselected blocks.
	 * @param blocksToToggle
	 *                     The blocks to be selected or unselected.
	 */
	public void toggleSelection(Collection<EditableBlock> blocksToToggle) {
		Collection<EditableBlock> selectedBefore = getSelectedBlocks();
		Collection<EditableBlock> unselectedBefore = getUnselectedBlocks();
		Collection<String> blocksBefore = getBlocks();

		for (EditableBlock block : blocksToToggle) {
			if (blocksInGroup.contains(block)) {
				blocksInGroup.remove(block);
			} else {
				blocksInGroup.add(block);
			}
		}

		firePropertyChange("selectedBlocks", selectedBefore, getSelectedBlocks());
		firePropertyChange("unselectedBlocks", unselectedBefore, getUnselectedBlocks());
		firePropertyChange("blocks", blocksBefore, getBlocks());
	}

	/**
	 * Moves a block up within the group.
	 * @param blockName
	 *                 The block to move up within the group.
	 */
	public void moveBlockUp(String blockName) {
		EditableBlock blockToMoveUp = lookupBlockByName(blocksInGroup, blockName);
		int blockToMoveUpIndex = blocksInGroup.indexOf(blockToMoveUp);
		if (blockToMoveUpIndex > 0) {
			EditableBlock blockToMoveDown = blocksInGroup.get(blockToMoveUpIndex - 1);
			blocksInGroup.set(blockToMoveUpIndex, blockToMoveDown);
			blocksInGroup.set(blockToMoveUpIndex - 1, blockToMoveUp);
		}
	}

	/**
     * Moves a block down within the group.
     * @param blockName
     *                 The block to move down within the group.
     */
	public void moveBlockDown(String blockName) {
		EditableBlock blockToMoveDown = lookupBlockByName(blocksInGroup, blockName);
		int blockToMoveDownIndex = blocksInGroup.indexOf(blockToMoveDown);
		if (blockToMoveDownIndex < blocksInGroup.size() - 1) {
			EditableBlock blockToMoveUp = blocksInGroup.get(blockToMoveDownIndex + 1);
			blocksInGroup.set(blockToMoveDownIndex, blockToMoveUp);
			blocksInGroup.set(blockToMoveDownIndex + 1, blockToMoveDown);
		}
	}

	@Override
	public Collection<String> getBlocks() {
		return blockNames(blocksInGroup);
	}

	/**
	 * Returns true if the group is editable (ie does not have a component).
	 * @return
	 *         True if the group is editable
	 */
	public boolean isEditable() {
		return !hasComponent();
	}

	private synchronized void updateBlocksFromConfig(EditableConfiguration config) {
		Collection<EditableBlock> selectedBefore = getSelectedBlocks();
		Collection<String> blocksBefore = getBlocks();

		removeDeletedBlocks(config);

		firePropertyChange("selectedBlocks", selectedBefore, getSelectedBlocks());
		// Force the unselected blocks property change to trigger
		firePropertyChange("unselectedBlocks", null, getUnselectedBlocks());
		firePropertyChange("blocks", blocksBefore, getBlocks());
	}

	private void removeDeletedBlocks(EditableConfiguration config) {
		Collection<EditableBlock> configBlocks = config.getAllBlocks();
		for (Iterator<EditableBlock> iterator = blocksInGroup.iterator(); iterator.hasNext();) {
			EditableBlock block = iterator.next();
			if (!configBlocks.contains(block)) {
				iterator.remove();
			}
		}
	}

	private List<String> blockNames(Collection<? extends Block> blocks) {
	    return blocks.stream()
	    		.map(Block::getName)
	    		.collect(Collectors.toList());
	}

	private List<EditableBlock> lookupBlocksByName(Collection<EditableBlock> blocks, final Collection<String> names) {
		List<EditableBlock> selectedBlocks = new ArrayList<EditableBlock>();
		List<String> allBlockNames = blockNames(blocks);
		for (String name : names) {
			int blockIndex = allBlockNames.indexOf(name);
			if (blockIndex >= 0) {
				EditableBlock block = Iterables.get(blocks, blockIndex);
				selectedBlocks.add(block);
			}
		}
		return selectedBlocks;
	}

	private EditableBlock lookupBlockByName(Collection<EditableBlock> blocks, final String name) {
		return lookupBlocksByName(blocks, Arrays.asList(name)).get(0);
	}

}
