package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.jface.viewers.Viewer;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;

/**
 * A comparator for blocks.
 */
public class BlocksComparator extends ColumnComparator {
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
    	EditableBlock block1 = (EditableBlock) e1;
    	EditableBlock block2 = (EditableBlock) e2;
        int rc = 0;
        switch (propertyIndex) {
        case 0:
            rc = block1.getName().compareTo(block2.getName());
            break;
        case 1:
            rc = block1.getPV().compareTo(block2.getPV());
            break;
        case 2:
            if (block1.getIsVisible() == block2.getIsVisible()) {
                rc = 0;
            } else {
                rc = (block2.getIsVisible() ? 1 : -1);
            }
            break;
        default:
            rc = 0;
        }
        // If descending order, flip the direction
        if (direction == DESCENDING) {
            rc = -rc;
        }
        return rc;
    }
}
