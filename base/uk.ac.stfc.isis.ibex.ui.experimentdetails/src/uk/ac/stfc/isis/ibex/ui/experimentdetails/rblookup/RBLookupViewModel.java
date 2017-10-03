package uk.ac.stfc.isis.ibex.ui.experimentdetails.rblookup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import uk.ac.stfc.isis.ibex.experimentdetails.ExperimentDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.database.SearchModel;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The view model for the RB number look-up dialog.
 */
public class RBLookupViewModel extends ModelObject {
	private boolean okEnabled = false;
	private boolean dateEnabled = false;
    private String nameSearch = "";
	private RoleViews roleSearch = RoleViews.ANY;
	private Date dateSearch = new Date();
	private Collection<UserDetails> searchResults;
	private UserDetails selectedUser;
	
	private SearchModel search = ExperimentDetails.getInstance().searchModel();		
	
	private final PropertyChangeListener resultsListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			firePropertyChange("searchResults", searchResults, searchResults = search.getSearchResults());
		}
	};
	
    /**
     * The constructor.
     */
	public RBLookupViewModel() {
		search.addPropertyChangeListener("searchResults", resultsListener);
	}
	
    /**
     * Set whether the OK button is enabled.
     * 
     * @param enabled whether it is enabled or not
     */
	public void setOkEnabled(boolean enabled) {
		firePropertyChange("okEnabled", okEnabled, okEnabled = enabled);
	}
	
    /**
     * Get whether the OK button is enabled.
     * 
     * @return true for enabled
     */
	public boolean getOkEnabled() {
		return okEnabled;
	}
	
    /**
     * Set whether the date field is enabled.
     * 
     * @param enabled whether it is enabled or not
     */
	public void setDateEnabled(boolean enabled) {
		firePropertyChange("dateEnabled", dateEnabled, dateEnabled = enabled);
		searchForExperimentID();
	}
	
    /**
     * Get whether the date field is enabled.
     * 
     * @return true for enabled
     */
	public boolean getDateEnabled() {
		return dateEnabled;
	}
	
    /**
     * Set the date on which to search for RB numbers.
     * 
     * @param date the specific date
     */
	public void setDateSearch(Date date) {
		firePropertyChange("dateSearch", dateSearch, dateSearch = date);
		searchForExperimentID();
	}
	
    /**
     * Get the date parameter set for the search
     * 
     * @return the date
     */
	public Date getDateSearch() {
		return dateSearch;
	}
	
    /**
     * Set the name to search for.
     * 
     * @param name the name
     */
	public void setNameSearch(String name) {
		firePropertyChange("nameSearch", nameSearch, nameSearch = name);
        searchForExperimentID();
	}
	
    /**
     * The types of roles available to search on.
     * 
     * @return the available roles
     */
	public RoleViews getRoleSearch() {
		return roleSearch;
	}
	
    /**
     * Set the type of role to search for.
     * 
     * @param roleSelection the selected role
     */
	public void setRoleSearch(RoleViews roleSelection) {
		firePropertyChange("roleSearch", roleSearch, roleSearch = roleSelection);
		searchForExperimentID();
	}
	
    /**
     * Set the chosen user.
     * 
     * @param newUser the selected user
     */
	public void setSelectedUser(UserDetails newUser) {
		setOkEnabled(newUser == null ? false : true);
		firePropertyChange("selectedUser", selectedUser, selectedUser = newUser);
	}
	
    /**
     * Get the selected user.
     * 
     * @return the user
     */
	public UserDetails getSelectedUser() {
		return selectedUser;
	}
	
    /**
     * Run the search.
     */
	public void searchForExperimentID() {	
		GregorianCalendar calendar = null;
		
		if (dateEnabled) {
			calendar = new GregorianCalendar();
			calendar.setTime(dateSearch);
		}
		
		search.searchExperiments(nameSearch, roleSearch.getModelRole(), calendar);
	}
	
    /**
     * Get the search results from having run the search.
     * 
     * @return the list of users
     */
	public Collection<UserDetails> searchResults() {
		return searchResults;
	}
	
    /**
     * Cleans up the data when the dialog closes.
     */
	public void close() {
		search.removePropertyChangeListener("searchResults", resultsListener);
		search.clearResults();
	}
}
