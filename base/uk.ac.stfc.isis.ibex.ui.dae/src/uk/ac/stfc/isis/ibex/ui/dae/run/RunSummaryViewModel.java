package uk.ac.stfc.isis.ibex.ui.dae.run;

import uk.ac.stfc.isis.ibex.dae.IDae;
import uk.ac.stfc.isis.ibex.dae.actions.DaeActions;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.log.ILogMessageProducer;
import uk.ac.stfc.isis.ibex.log.Log;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.widgets.observable.WritableObservableAdapter;

public class RunSummaryViewModel extends Closer {
	
	private IDae model;
	
	private UpdatedValue<String> instrument;
	private UpdatedValue<String> runStatus;
	private UpdatedValue<String> runNumber;
	private UpdatedValue<String> isisCycle;	
	private WritableObservableAdapter title;
				
	private ILogMessageProducer logModel;
	
	public void bind(final IDae model) {
		this.model = model;
		
		instrument = registerForClose(new TextUpdatedObservableAdapter(model.instrument()));
		runStatus = registerForClose(new TextUpdatedObservableAdapter(registerForClose(new InstrumentState(model.runState()))));
		runNumber = registerForClose(new TextUpdatedObservableAdapter(model.runNumber()));
		isisCycle = registerForClose(new TextUpdatedObservableAdapter(model.isisCycle()));;
		title = registerForClose(new WritableObservableAdapter(model.setTitle(), model.title()));
								
		logModel = Log.getInstance().producer();
	}
	
	public UpdatedValue<String> instrument() {
		return instrument;
	}
	
	public UpdatedValue<String> runStatus() {
		return runStatus;
	}
	
	public UpdatedValue<String> runNumber() {
		return runNumber;
	}
	
	public UpdatedValue<String> isisCycle() {
		return isisCycle;
	}
	
	public WritableObservableAdapter title() {
		return title;
	}
	
	public DaeActions actions() {
		return model.actions();
	}
	
	public ILogMessageProducer logMessageSource() {
		return logModel;
	}
}
