package uk.ac.stfc.isis.ibex.ui.ioccontrol.table;

import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;

/**
 * A comparator for IOCs.
 */
public class IocComparator extends ColumnComparator<IocState> {
	@Override
	public int compare(IocState ioc1, IocState ioc2) {
        int rc = 0;
        switch (propertyIndex) {
        case 0:
            rc = ioc1.getName().compareTo(ioc2.getName());
            break;
        case 1:
            rc = ioc1.getDescription().compareTo(ioc2.getDescription());
            break;
        case 2:
        	rc = compareBoolean(ioc1.getIsRunning(), ioc2.getIsRunning());
            break;
        default:
            rc = 0;
        }
        
        return rc;
	}
}
