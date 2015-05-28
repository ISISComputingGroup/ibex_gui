package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.dashboard.DashboardObservables;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class BannerModel extends Closer {
		
	private final UpdatedValue<String> instrumentName;
	private final InstrumentState instrumentState;
	private final BannerText bannerText;
	private final UpdatedValue<String> runNumber;
	private final ShutterState shutterState;
	
	public BannerModel(DashboardObservables observables) {
		instrumentName = Instrument.getInstance().name();
		instrumentState = registerForClose(new InstrumentState(observables.dae.runState));
		bannerText = new BannerText(instrumentName, instrumentState.text());
		
		runNumber = registerForClose(new TextUpdatedObservableAdapter(observables.dae.runNumber));
		shutterState = registerForClose(new ShutterState(observables.shutter));
	}
	
	public UpdatedValue<String> bannerText() {
		return bannerText;
	}
	
	public UpdatedValue<String> instrumentName() {
		return instrumentName;
	}
	
	public UpdatedValue<String> runState() {
		return instrumentState.text();
	}
	
	public UpdatedValue<Color> background() {
		return instrumentState.color();
	}
	
	public UpdatedValue<String> runNumber() {
		return runNumber;
	}
	
	public UpdatedValue<String> shutter() {
		return shutterState.text();
	}
}
