package uk.ac.stfc.isis.ibex.experimentdetails.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.experimentdetails.Role;
import uk.ac.stfc.isis.ibex.experimentdetails.UserDetails;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class SearchModel extends ModelObject {
	private Collection<UserDetails> searchResults = new ArrayList<UserDetails>();
	private static final Logger LOG = IsisLog.getLogger(SearchModel.class);
	
	public void searchExperiments(String searchName, Role searchRole) {
		try {
			Rdb rdb = Rdb.connectToDatabase();
			ExperimentIDQuery query = new ExperimentIDQuery(rdb);
			List<UserDetails> userDetails = query.getExperiments(searchName, searchRole);
			
			setSearchResults(userDetails);
			
		} catch (Exception ex) {
		    LOG.error("Error searching experiments: "
				    + ex.getMessage());
		}
	}
	
	private void setSearchResults(Collection<UserDetails> newResults) {
		firePropertyChange("searchResults", this.searchResults, this.searchResults = newResults);
	}
	
	public Collection<UserDetails> getSearchResults() {
		return searchResults;
	}
	
	public void clearResults() {
		firePropertyChange("searchResults", this.searchResults, this.searchResults = new ArrayList<UserDetails>());
	}
}
