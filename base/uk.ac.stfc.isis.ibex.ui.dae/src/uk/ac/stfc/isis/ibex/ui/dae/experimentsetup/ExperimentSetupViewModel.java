package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.ExperimentSetup;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods.PeriodsViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels.TimeChannelsViewModel;

public class ExperimentSetupViewModel {

	private ExperimentSetup model;
	
	private DataAcquisitionViewModel daeSettings = new DataAcquisitionViewModel();
	private TimeChannelsViewModel timeChannels = new TimeChannelsViewModel();
	private PeriodsViewModel periodSettings = new PeriodsViewModel();
	
	public void setModel(ExperimentSetup model) {
		this.model = model;	
		
		daeSettings.setModel(model.daeSettings());
		daeSettings.setUpdateSettings(model.updateSettings());
		daeSettings.setWiringTableList(model.wiringList());
		daeSettings.setDetectorTableList(model.detectorTables());
		daeSettings.setSpectraTableList(model.spectraTables());
		
		timeChannels.setModel(model.timeChannels());
		
		periodSettings.setSettings(model.periodSettings());		
	}
	
	public void updateDae() {
		model.sendAllSettings();
	}
	
	public TimeChannelsViewModel timeChannels() {
		return timeChannels;
	}
	
	public DataAcquisitionViewModel daeSettings() {
		return daeSettings;
	}
	
	public PeriodsViewModel periodSettings() {
		return periodSettings;
	}
}
