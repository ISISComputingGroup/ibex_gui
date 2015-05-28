package uk.ac.stfc.isis.ibex.instrument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import uk.ac.stfc.isis.ibex.instrument.baton.BannerObservables;
import uk.ac.stfc.isis.ibex.instrument.internal.DefaultSettings;
import uk.ac.stfc.isis.ibex.instrument.internal.LocalHostInstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.pv.PVAddressBook;
import uk.ac.stfc.isis.ibex.instrument.pv.PVChannels;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class Instrument implements BundleActivator {

    private static Instrument instance;
	private static BundleContext context;
	
    public static Instrument getInstance() { 
    	return instance; 
    }
	
    private final Settings settings;
	private final PVAddressBook addresses;
	private final PVChannels channels;
	private final BannerObservables baton;

	private List<InstrumentInfo> instruments = new ArrayList<>();
	private SettableUpdatedValue<String> instrumentName = new SettableUpdatedValue<>();
	private InstrumentInfo instrumentInfo;
	private final InstrumentInfo localhost;
	
	private final Preferences initalPreference = ConfigurationScope.INSTANCE.getNode("uk.ac.stfc.isis.ibex.instrument").node("preferences");
	
	private static String INITIAL_INSTRUMENT = "initial";
	
	public Instrument() {
		instance = this;
		settings = new DefaultSettings();
		
		localhost = new LocalHostInstrumentInfo();
		instruments.add(localhost);
		instruments.add(new InstrumentInfo("LARMOR"));
		instruments.add(new InstrumentInfo("ALF"));
		instruments.add(new InstrumentInfo("DEMO"));
		
		addresses = new PVAddressBook(settings.pvPrefix());
		channels = new PVChannels(addresses);
		baton = new BannerObservables(channels);
		
		setInstrument(initialInstrument());	
	}
    
	public UpdatedValue<String> name() {
		return instrumentName;
	}
	
	public Settings settings() {
		return settings;
	}
	
	public Channels channels() {
		 return channels;
	}
	
	public BannerObservables baton() {
		return baton;
	}
	
	public Collection<InstrumentInfo> instruments() {
		return instruments;
	}
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Instrument.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Instrument.context = null;
	}

	public void setInstrument(InstrumentInfo selectedInstrument) {
		this.instrumentInfo = selectedInstrument;
		instrumentName.setValue(selectedInstrument.name());

		addresses.setPrefix(selectedInstrument.pvPrefix());
		updateExtendingPlugins(selectedInstrument);
	}

	public InstrumentInfo currentInstrument() {
		return instrumentInfo;
	}
	
	private static void updateExtendingPlugins(InstrumentInfo selectedInstrument) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor("uk.ac.stfc.isis.ibex.instrument.info");
		for (IConfigurationElement element : elements) {
			try {
				final Object obj = element.createExecutableExtension("class");
				InstrumentInfoReceiver receiver = (InstrumentInfoReceiver) obj;
				receiver.setInstrument(selectedInstrument);
			} catch (CoreException e) {
			}
		}
	}
	
	private InstrumentInfo initialInstrument() {
		final String initalName = initalPreference.get(INITIAL_INSTRUMENT, localhost.name());
		
		return Iterables.find(instruments, new Predicate<InstrumentInfo>() {
			@Override
			public boolean apply(InstrumentInfo info) {
				return initalName.endsWith(info.name());
			}
		}, localhost);
	}
	
	public void setInitial() {
		initalPreference.put(INITIAL_INSTRUMENT, currentInstrument().name());
		
        try {
            // forces the application to save the preferences
            initalPreference.flush();
        } catch (BackingStoreException e2) {
            e2.printStackTrace();
        }
	}
}
