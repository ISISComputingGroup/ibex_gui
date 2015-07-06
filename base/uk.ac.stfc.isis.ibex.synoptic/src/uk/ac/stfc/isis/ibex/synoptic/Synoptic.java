package uk.ac.stfc.isis.ibex.synoptic;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.synoptic.internal.ObservingSynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

public class Synoptic extends Closer implements BundleActivator {

	public static final Logger LOG = IsisLog.getLogger(Synoptic.class);
	
	private static Synoptic instance;
	private static BundleContext context;

	private SynopticModel viewerModel;
	
	private ObservingSynopticModel viewerModelObserver;	

	private SynopticModel editorModel;

	private final Variables variables;
	
	public Synoptic() {
		instance = this;
		
		variables = registerForClose(new Variables(Instrument.getInstance().channels()));	
		
		viewerModel = new SynopticModel(variables);
		viewerModelObserver = new ObservingSynopticModel(variables, viewerModel);
		
		editorModel = new SynopticModel(variables);
	}
	
	public static Synoptic getInstance() {
		return instance;
	}
	
	public SynopticModel currentViewerModel() {
		return viewerModel;
	}
	
	public SynopticModel edit() {
		return editorModel;
	}
	
	public void setViewerSynoptic(SynopticInfo info) {
		viewerModelObserver.switchSynoptic(info);
	}
	
	public void setViewerSynoptic(String synopticName) {
		for (SynopticInfo synoptic : availableSynoptics()) {
			if (synoptic.name().equals(synopticName)) {
				viewerModelObserver.switchSynoptic(synoptic);
			}
		}
	}
	
	public SynopticModel getBlankModel() {
		return new SynopticModel(variables);
	}
	
	public Collection<SynopticInfo> availableSynoptics() {
		return variables.available.value();
	}
	
	public ArrayList<String> availableSynopticNames() {
		Collection<SynopticInfo> availableSynoptics = availableSynoptics();
		
		ArrayList<String> synoptics = new ArrayList<>();
		
		for (SynopticInfo synoptic : availableSynoptics) {
			synoptics.add(synoptic.name());
		}
		
		return synoptics;
	}
	
	public int getSynopticNumber() {
		// No synoptic loaded
		if (getSynopticInfo() == null) {
			return -1;
		}
		
		String currentSynopticName = getSynopticInfo().name();
		
		ArrayList<String> availableSynoptics = availableSynopticNames();
		
		return availableSynoptics.indexOf(currentSynopticName);
	}
	
	public SynopticInfo getSynopticInfo() {
		return viewerModelObserver.getSynopticInfo();
	}
	
	public InitialiseOnSubscribeObservable<InstrumentDescription> synoptic(SynopticInfo synoptic) {
		return variables.getSynoptic(synoptic.pv());
	}
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Synoptic.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Synoptic.context = null;
		close();
	}
}
