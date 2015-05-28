package uk.ac.stfc.isis.ibex.synoptic.internal;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ClosingSwitchableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

public class ObservingSynopticModel {

	private final InitialisableObserver<InstrumentDescription> descriptionObserver 
		= new BaseObserver<InstrumentDescription>() {

			@Override
			public void onValue(InstrumentDescription value) {
				model.setInstrumentFromDescription(value);
			}

			@Override
			public void onError(Exception e) {}
			@Override
			public void onConnectionChanged(boolean isConnected) {}
		};
		
	private final InitialisableObserver<Configuration> configSynopticObserver 
		= new BaseObserver<Configuration>() {

			@Override
			public void onValue(Configuration value) {
				String synopticName = value.synoptic();
				SynopticInfo newSynoptic = SynopticInfo.search(variables.available.value(), synopticName);
				if (newSynoptic != null) {
					switchSynoptic(newSynoptic);
				}
			}

			@Override
			public void onError(Exception e) {}
			@Override
			public void onConnectionChanged(boolean isConnected) {}
		};
	
	private final SynopticModel model;
	private final Variables variables;
	private final ClosingSwitchableObservable <InstrumentDescription> synoptic;
	
	public ObservingSynopticModel(Variables variables, SynopticModel model) {
		this.model = model;
		this.variables = variables;
				
		this.synoptic = new ClosingSwitchableObservable<InstrumentDescription>(variables.getSynoptic(""));
		this.synoptic.subscribe(descriptionObserver);
		
		Configurations.getInstance().server().currentConfig().subscribe(configSynopticObserver);
	}
	
	public void switchSynoptic(SynopticInfo newSynoptic) {
		synoptic.switchTo(variables.getSynoptic(newSynoptic.pv()));
	}
}
