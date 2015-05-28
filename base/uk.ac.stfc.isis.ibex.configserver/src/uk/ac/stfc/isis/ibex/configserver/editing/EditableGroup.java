package uk.ac.stfc.isis.ibex.configserver.editing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.internal.DisplayUtils;
import uk.ac.stfc.isis.ibex.model.ExclusiveSetPair;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class EditableGroup extends Group {

	private final ExclusiveSetPair<EditableBlock> blocks;
	
	public EditableGroup(final EditableConfiguration config, Group group) {
		super(group);
				
		blocks = new ExclusiveSetPair<>(config.getEditableBlocks());
		Collection<EditableBlock> selectedBlocks = lookupBlocksByName(config.getEditableBlocks(), group.getBlocks());
		blocks.move(selectedBlocks);
		
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
		return new ArrayList<>(blocks.unselected());
	}
	
	public Collection<EditableBlock> getSelectedBlocks() {
		return new ArrayList<>(blocks.selected());
	}
	
	public void toggleSelection(Collection<EditableBlock> blocksToToggle) {
		Collection<EditableBlock> selectedBefore = getSelectedBlocks();
		Collection<EditableBlock> unselectedBefore = getUnselectedBlocks();
		Collection<String> blocksBefore = getBlocks();
		
		for (EditableBlock block : blocksToToggle) {
			blocks.move(block);
		}
		
		firePropertyChange("selectedBlocks", selectedBefore, getSelectedBlocks());
		firePropertyChange("unselectedBlocks", unselectedBefore, getUnselectedBlocks());
		firePropertyChange("blocks", blocksBefore, getBlocks());
	}
	
	@Override
	public Collection<String> getBlocks() {
		return blockNames(blocks.selected());
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
		Set<EditableBlock> missing = new HashSet<>(blocks.all());
		missing.removeAll(config.getEditableBlocks());
		for (EditableBlock block : missing) {
			blocks.remove(block);
		}
	}
	
	private void addNewBlocks(EditableConfiguration config) {
		for (EditableBlock block : config.getEditableBlocks()) {
			blocks.addUnselected(block);
		}
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
		return Lists.newArrayList(Iterables.filter(blocks, new Predicate<Block>() {
			@Override
			public boolean apply(final Block block) {
				return anyNameMatches(names, block);
			}
		}));
	}
	
	private static boolean anyNameMatches(final Collection<String> names, final Block block) {
		return Iterables.any(names, new Predicate<String>() {
			@Override
			public boolean apply(String name) {
				return block.getName().equals(name);
			}
		});
	}
}
