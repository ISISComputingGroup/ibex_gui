package uk.ac.stfc.isis.ibex.ui.synoptic.widgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;

public class SynopticSelectionViewModel extends ModelObject {

	private static Synoptic synoptic = Synoptic.getInstance();
	private static final InitialiseOnSubscribeObservable<DisplayConfiguration> CONFIG = 
			Configurations.getInstance().display().displayCurrentConfig();
	
	private Subscription configSubscription;
	private Subscription synopticSubscription;
	
	private ArrayList<String> synopticList;
	
	private String selected;
	
	private final BaseObserver<DisplayConfiguration> configObserver = new BaseObserver<DisplayConfiguration>() {
		@Override
		public void onValue(DisplayConfiguration value) {
			setSynopticList(synoptic.availableSynoptics(), value.defaultSynoptic());
		}

		@Override
		public void onError(Exception e) {
			setSynopticList(synoptic.availableSynoptics(), null);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setSynopticList(synoptic.availableSynoptics(), null);
			}
		}	
	};	
	
	private final BaseObserver<Collection<SynopticInfo>> availableSynopticsObserver = new BaseObserver<Collection<SynopticInfo>>() {
		@Override
		public void onValue(Collection<SynopticInfo> value) {
			setSynopticList(value, CONFIG.getValue().defaultSynoptic());
		}

		@Override
		public void onError(Exception e) {
			setSynopticList(new ArrayList<SynopticInfo>(), null);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setSynopticList(new ArrayList<SynopticInfo>(), null);
			}
		}	
	};
	
	private final PropertyChangeListener currentSynopticObserver = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			SynopticInfo synopticInfo = (SynopticInfo)evt.getNewValue();
			if (synopticInfo != null) {
				firePropertyChange("selected", selected, selected = synopticInfo.name());
			}
		}
	};
	
	public SynopticSelectionViewModel () {
		if (configSubscription != null) {
			configSubscription.removeObserver();
		}

		if (synopticSubscription != null) {
			synopticSubscription.removeObserver();
		}
		
		configSubscription = CONFIG.addObserver(configObserver);
		synopticSubscription = synoptic.availableSynopticsInfo().addObserver(availableSynopticsObserver);
		synoptic.currentObservingViewerModel().addPropertyChangeListener("synopticInfo", currentSynopticObserver);
	}
	
	public void setSelected(String selected) {
		synoptic.setViewerSynoptic(selected);
		firePropertyChange("selected", this.selected, this.selected = selected);
	}
	
	public void setSynopticList(Collection<SynopticInfo> synoptics, String defaultSynoptic) {
		ArrayList<String> names = new ArrayList<String>(SynopticInfo.names(synoptics));
		
		firePropertyChange("synopticList", synopticList, synopticList = names);
	}
	
	public String getSelected() {
		return selected;
	}
	
	public ArrayList<String> getSynopticList() {
		return synopticList;
	}
	
	public int getSynopticNumber() {
		// No synoptic loaded
		if (synoptic.getSynopticInfo() == null) {
			return -1;
		}
		
		String currentSynopticName = synoptic.getSynopticInfo().name();
		
		ArrayList<String> availableSynoptics = new ArrayList<String>(SynopticInfo.names(synoptic.availableSynoptics()));
		
		return availableSynoptics.indexOf(currentSynopticName);
	}
	
	public void refreshSynoptic() {
		SynopticInfo synopticToRefresh = synoptic.getSynopticInfo();
		synoptic.setViewerSynoptic(synopticToRefresh);
	}
	
}
