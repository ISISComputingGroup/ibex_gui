package uk.ac.stfc.isis.ibex.configserver.displaying;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.configserver.internal.DisplayUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class DisplayGroup {

	private final String name;
	private final List<DisplayBlock> blocks = new ArrayList<>();
	private final ConfigServer configServer;
	private final Collection<Block> allBlocks;
			
	public DisplayGroup(
			Group group, 
			Collection<Block> blocks,
			ConfigServer configServer) {
		this.configServer = configServer;
		this.allBlocks = blocks;
		name = group.getName();
		setBlocks(group.getBlocks());
	}

	public String name() {
		return DisplayUtils.renameGroup(name);
	}
	
	public Collection<DisplayBlock> blocks() {
		return new ArrayList<>(blocks);
	}
	
	private void setBlocks(Collection<String> blockNames) {
		for (final String name : blockNames) {
			blocks.add(displayBlock(name));
		}
	}

	private DisplayBlock displayBlock(String name) {
		return new DisplayBlock(
				block(name), 
				configServer.blockValue(name), 
				configServer.blockDescription(name), 
				configServer.blockServerAlias(name));
	}

	private Block block(final String name) {
		return Iterables.find(allBlocks, nameMatches(name));
	}
	
	private Predicate<Block> nameMatches(final String name) {
		return new Predicate<Block>() {
			@Override
			public boolean apply(Block block) {
				return block.getName().equals(name);
			}
		};
	}
}
