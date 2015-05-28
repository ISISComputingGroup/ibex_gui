package uk.ac.stfc.isis.ibex.dae.experimentsetup;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.dae.DaeWritables;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeSettings;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.ObservingDaeSettings;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.ObservingPeriodSettings;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodSettings;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.ObservingTimeChannels;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeChannels;
import uk.ac.stfc.isis.ibex.dae.internal.FileList;
import uk.ac.stfc.isis.ibex.dae.updatesettings.ObservingUpdateSettings;
import uk.ac.stfc.isis.ibex.dae.updatesettings.UpdateSettings;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class ExperimentSetup extends Closer  {
	
	private final ObservingDaeSettings daeSettings;
	private final ObservingPeriodSettings periodSettings;
	private final ObservingUpdateSettings updateSettings;
	private final ObservingTimeChannels timeChannels;
	
	private final UpdatedObservableAdapter<Collection<String>> detectorTables;
	private final UpdatedObservableAdapter<Collection<String>> spectraTables;
	private final UpdatedObservableAdapter<Collection<String>> wiringTables;
	
	public ExperimentSetup(DaeObservables observables, DaeWritables writables) {
		daeSettings = registerForClose(new ObservingDaeSettings(observables.daeSettings, writables.daeSettings));
		periodSettings = registerForClose(new ObservingPeriodSettings(observables.hardwarePeriods, writables.hardwarePeriods));
		updateSettings = registerForClose(new ObservingUpdateSettings(observables.updateSettings, writables.updateSettings));
		timeChannels = registerForClose(new ObservingTimeChannels(observables.timeChannelSettings, writables.timeChannelSettings));
		
		detectorTables = createAdapter(observables.detectorTables);
		spectraTables = createAdapter(observables.spectraTables);
		wiringTables = createAdapter(observables.wiringTables);
	}
	
	public DaeSettings daeSettings() {
		return daeSettings;
	}
	
	public UpdatedValue<Collection<String>> wiringList() {
		return wiringTables;
	}
	
	public UpdatedValue<Collection<String>> detectorTables() {
		return detectorTables;
	}
	
	public UpdatedValue<Collection<String>> spectraTables() {
		return spectraTables;
	}
	
	public UpdateSettings updateSettings() {
		return updateSettings;
	}
	
	public TimeChannels timeChannels() {
		return timeChannels;
	}
	
	public PeriodSettings periodSettings() {
		return periodSettings;
	}
	
	public void sendAllSettings() {
		daeSettings.sendUpdate();
		periodSettings.sendUpdate();
		updateSettings.sendUpdate();
		timeChannels.sendUpdate();
	}
	
	private UpdatedObservableAdapter<Collection<String>> createAdapter(
			InitialiseOnSubscribeObservable<String> tables) {
		FileList files = registerForClose(new FileList(tables));
		return registerForClose(new UpdatedObservableAdapter<>(new InitialiseOnSubscribeObservable<>(files)));
	}
}
