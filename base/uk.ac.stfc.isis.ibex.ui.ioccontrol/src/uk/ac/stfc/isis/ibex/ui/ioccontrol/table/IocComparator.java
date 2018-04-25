package uk.ac.stfc.isis.ibex.ui.ioccontrol.table;

import org.eclipse.jface.viewers.Viewer;

import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;

/**
 * A comparator for IOCs.
 */
public class IocComparator extends ColumnComparator {
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
    	IocState ioc1 = (IocState) e1;
    	IocState ioc2 = (IocState) e2;
        int rc = 0;
        switch (propertyIndex) {
        case 0:
            rc = ioc1.getName().compareTo(ioc2.getName());
            break;
        case 1:
            rc = ioc1.getDescription().compareTo(ioc2.getDescription());
            break;
        case 2:
            if (ioc1.getIsRunning() == ioc2.getIsRunning()) {
                rc = 0;
            } else {
                rc = (ioc1.getIsRunning() ? 1 : -1);
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
