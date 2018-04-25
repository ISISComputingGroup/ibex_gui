package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;

/**
 * A comparator for blocks.
 */
public class BlocksComparator extends ColumnComparator<EditableBlock> {
 	@Override
	public int compare(EditableBlock block1, EditableBlock block2) {
        int rc = 0;
        switch (propertyIndex) {
        case 0:
            rc = block1.getName().compareTo(block2.getName());
            break;
        case 1:
            rc = block1.getPV().compareTo(block2.getPV());
            break;
        case 2:
        	rc = compareBoolean(block1.getIsVisible(), block2.getIsVisible());
            break;
        default:
            rc = 0;
        }
        return rc;
	}
}
