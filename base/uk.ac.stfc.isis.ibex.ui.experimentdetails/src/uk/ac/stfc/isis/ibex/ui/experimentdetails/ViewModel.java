package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import uk.ac.stfc.isis.ibex.experimentdetails.ExperimentDetails;
import uk.ac.stfc.isis.ibex.experimentdetails.Model;
import uk.ac.stfc.isis.ibex.ui.widgets.observable.WritableObservableAdapter;

public class ViewModel {
	
	public final Model model = ExperimentDetails.getInstance().model();
	
	public final WritableObservableAdapter rbNumber = new WritableObservableAdapter(model.rbNumberSetter(), model.rbNumber());
	
	public ViewModel() {
		
	}
	
}
