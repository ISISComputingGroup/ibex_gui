package uk.ac.stfc.isis.ibex.configserver.editing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.internal.DisplayUtils;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class EditableGroup extends Group {

	private final List<EditableBlock> blocksInGroup;
	private List<EditableBlock> availableBlocks;
	private final EditableConfiguration config;
	
	public EditableGroup(final EditableConfiguration configuration, Group group) {
		super(group);
		
		config = configuration;
		List<EditableBlock> selectedBlocks = lookupBlocksByName(config.getEditableBlocks(), group.getBlocks());
		blocksInGroup = selectedBlocks;
		availableBlocks = (List<EditableBlock>) config.getAvailableBlocks();
		for (EditableBlock block : selectedBlocks) {
			availableBlocks.remove(block);
			if (!group.getName().equals("NONE")) {
				config.makeBlockUnavailable(block);
			}
		}
		
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
	
	public Collection<EditableBlock> makeEditable(Collection<Block> blocks) {
		List<EditableBlock> editable = new ArrayList<>();
		for (Block block : blocks) {
			editable.add(new EditableBlock(block));
		}
		
		return editable;
	}
	
	public Collection<EditableBlock> getUnselectedBlocks() {
		return new ArrayList<>(config.getAvailableBlocks());
	}
	
	public Collection<EditableBlock> getSelectedBlocks() {
		return new ArrayList<>(blocksInGroup);
	}
	
	public void toggleSelection(Collection<EditableBlock> blocksToToggle) {
		Collection<EditableBlock> selectedBefore = getSelectedBlocks();
		Collection<EditableBlock> unselectedBefore = getUnselectedBlocks();
		Collection<String> blocksBefore = getBlocks();
		
		for (EditableBlock block : blocksToToggle) {
			if (blocksInGroup.contains(block)) {
				blocksInGroup.remove(block);
				config.makeBlockAvailable(block);
			}
			else {
				blocksInGroup.add(block);
				config.makeBlockUnavailable(block);
			}
		}
		
		firePropertyChange("selectedBlocks", selectedBefore, getSelectedBlocks());
		firePropertyChange("unselectedBlocks", unselectedBefore, getUnselectedBlocks());
		firePropertyChange("blocks", blocksBefore, getBlocks());	
	}
	
	public void moveBlockUp(String blockName) {
		EditableBlock blockToMoveUp = lookupBlockByName(blocksInGroup, blockName);
		int blockToMoveUpIndex = blocksInGroup.indexOf(blockToMoveUp);
		if (blockToMoveUpIndex > 0) {
			EditableBlock blockToMoveDown = blocksInGroup.get(blockToMoveUpIndex - 1);
			blocksInGroup.set(blockToMoveUpIndex, blockToMoveDown);
			blocksInGroup.set(blockToMoveUpIndex - 1, blockToMoveUp);
		}
	}
	
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
	
	public boolean isEditable() {
		return !hasSubConfig();
	}
	
	private synchronized void updateBlocksFromConfig(EditableConfiguration config) {
		Collection<EditableBlock> selectedBefore = getSelectedBlocks();
		Collection<EditableBlock> unselectedBefore = getUnselectedBlocks();
		Collection<String> blocksBefore = getBlocks();
		
		removeDeletedBlocks(config);
		addNewBlocks(config);
		
		firePropertyChange("selectedBlocks", selectedBefore, getSelectedBlocks());
		firePropertyChange("unselectedBlocks", unselectedBefore, getUnselectedBlocks());
		firePropertyChange("blocks", blocksBefore, getBlocks());
	}

	private void removeDeletedBlocks(EditableConfiguration config) {
		Collection<EditableBlock> configBlocks = config.getEditableBlocks();
		for (EditableBlock block : blocksInGroup) {
			if (!configBlocks.contains(block)) {
				blocksInGroup.remove(block);
			}
		}
	}
	
	private void addNewBlocks(EditableConfiguration config) {
		availableBlocks = (List<EditableBlock>) config.getAvailableBlocks();
	}
	
	private List<String> blockNames(Collection<? extends Block> blocks) {
		return Lists.newArrayList(Iterables.transform(blocks, new Function<Block, String>() {
			@Override
			public String apply(Block block) {
				return block.getName();
			}
		}));
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
		EditableBlock block = new EditableBlock(new Block(name, "", true, true, null));
		List<String> allBlockNames = blockNames(blocks);
		int blockIndex = allBlockNames.indexOf(name);
		if (blockIndex >= 0) {
			block = Iterables.get(blocks, blockIndex);
		}
		return block;
	}
	
}
