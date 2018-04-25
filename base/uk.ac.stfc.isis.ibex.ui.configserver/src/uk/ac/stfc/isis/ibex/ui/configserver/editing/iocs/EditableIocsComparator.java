package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;

/**
 * A comparator for IOCs.
 */
public class EditableIocsComparator extends ColumnComparator<EditableIoc> {
 	@Override
	public int compare(EditableIoc ioc1, EditableIoc ioc2) {
        int rc = 0;
        switch (propertyIndex) {
        case 0:
            rc = ioc1.getName().compareTo(ioc2.getName());
            break;
        case 1:
            rc = ioc1.getDescription().compareTo(ioc2.getDescription());
            break;
        case 2:
            rc = ioc1.getSimLevel().compareTo(ioc2.getSimLevel());
            break;
        case 3:
        	rc = compareBoolean(ioc1.getAutostart(), ioc2.getAutostart());
            break;
        case 4:
        	rc = compareBoolean(ioc1.getRestart(), ioc2.getRestart());
            break;
        default:
            rc = 0;
        }
        return rc;
	}
}
