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

public class RBLookupViewModel extends ModelObject{
	private boolean okEnabled = false;
	private boolean dateEnabled = false;
    private String nameSearch = "";
	private RoleViews roleSearch = RoleViews.ANY;
	private Date dateSearch = new Date();
	private Collection<UserDetails> searchResults;
	private UserDetails selectedUser;
	
	private SearchModel SEARCH = ExperimentDetails.getInstance().searchModel();		
	
	private final PropertyChangeListener resultsListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			firePropertyChange("searchResults", searchResults, searchResults=SEARCH.getSearchResults());
		}
	};
	
	public RBLookupViewModel() {
		SEARCH.addPropertyChangeListener("searchResults", resultsListener);
	}
	
	public void setOkEnabled(boolean enabled) {
		firePropertyChange("okEnabled", okEnabled, okEnabled=enabled);
	}
	
	public boolean getOkEnabled() {
		return okEnabled;
	}
	
	public void setDateEnabled(boolean enabled) {
		firePropertyChange("dateEnabled", dateEnabled, dateEnabled=enabled);
		searchForExperimentID();
	}
	
	public boolean getDateEnabled() {
		return dateEnabled;
	}
	
	public void setDateSearch(Date date) {
		firePropertyChange("dateSearch", dateSearch, dateSearch=date);
		searchForExperimentID();
	}
	
	public Date getDateSearch() {
		return dateSearch;
	}
	
	public void setNameSearch(String name) {
		firePropertyChange("nameSearch", nameSearch, nameSearch=name);
        searchForExperimentID();
	}
	
	public RoleViews getRoleSearch() {
		return roleSearch;
	}
	
	public void setRoleSearch(RoleViews roleSelection) {
		firePropertyChange("roleSearch", roleSearch, roleSearch=roleSelection);
		searchForExperimentID();
	}
	
	public void setSelectedUser(UserDetails newUser) {
		setOkEnabled(newUser == null ? false : true);
		firePropertyChange("selectedUser", selectedUser, selectedUser=newUser);
	}
	
	public UserDetails getSelectedUser() {
		return selectedUser;
	}
	
	public void searchForExperimentID() {	
		GregorianCalendar calendar = null;
		
		if (dateEnabled) {
			calendar = new GregorianCalendar();
			calendar.setTime(dateSearch);
		}
		
		SEARCH.searchExperiments(nameSearch, roleSearch.getModelRole(), calendar);
	}
	
	public Collection<UserDetails> searchResults() {
		return searchResults;
	}
	
	public void close() {
		SEARCH.removePropertyChangeListener("searchResults", resultsListener);
		SEARCH.clearResults();
	}
}
