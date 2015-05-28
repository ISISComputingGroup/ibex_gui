package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;

public class Blocks extends EditableConfigurationTest {
		
	@Test
	public void a_new_block_can_be_added() {
		EditableConfiguration edited = edit(emptyConfig());
		assertEmpty(edited.asConfiguration().getBlocks());
		
		edited.addNewBlock();
		assertNotEmpty(edited.asConfiguration().getBlocks());
	}
	
	@Test
	public void a_block_can_be_removed() {
		blocks.add(GAPX);
		EditableConfiguration edited = edit(config());
		assertContains(edited.asConfiguration().getBlocks(), GAPX);
		
		EditableBlock gapx = getFirst(edited.getEditableBlocks());
		edited.removeBlock(gapx);
		assertEmpty(edited.asConfiguration().getBlocks());
	}
}
