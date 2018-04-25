package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import org.eclipse.jface.viewers.Viewer;

import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;

/**
 * A comparator for user details.
 */
public class UserDetailsComparator extends ColumnComparator {
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
    	UserDetails user1 = (UserDetails) e1;
    	UserDetails user2 = (UserDetails) e2;
        int rc = 0;
        switch (propertyIndex) {
        case 0:
            rc = user1.getName().compareTo(user2.getName());
            break;
        case 1:
            rc = user1.getInstitute().compareTo(user2.getInstitute());
            break;
        case 2:
        	rc = user1.getRole().compareTo(user2.getRole());
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
