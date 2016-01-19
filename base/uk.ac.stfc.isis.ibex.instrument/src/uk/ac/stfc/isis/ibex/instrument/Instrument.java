
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.instrument;


public class Instrument implements BundleActivator {

    private static final Logger LOG = IsisLog.getLogger("Instrument");

    private static Instrument instance;
	private static BundleContext context;
	
    public static Instrument getInstance() {
    	return instance; 
    }
	
	private List<InstrumentInfo> instruments = new ArrayList<>();
	private SettableUpdatedValue<String> instrumentName = new SettableUpdatedValue<>();
	private InstrumentInfo instrumentInfo;
	private final InstrumentInfo localhost;
	
	private final Preferences initalPreference = ConfigurationScope.INSTANCE.getNode("uk.ac.stfc.isis.ibex.instrument").node("preferences");
	
	private static String INITIAL_INSTRUMENT = "initial";
	
	public Instrument() {
		instance = this;
		
		localhost = new LocalHostInstrumentInfo();
		instruments.add(localhost);
		instruments.add(new InstrumentInfo("LARMOR"));
		instruments.add(new InstrumentInfo("ALF"));
		instruments.add(new InstrumentInfo("DEMO"));
		instruments.add(new InstrumentInfo("IMAT"));
		
		setInstrument(initialInstrument());	
	}
    
	public UpdatedValue<String> name() {
		return instrumentName;
	}
	
    public String getPvPrefix() {
        return instrumentInfo.pvPrefix();
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
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		Instrument.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		Instrument.context = null;
	}

	public void setInstrument(InstrumentInfo selectedInstrument) {
		this.instrumentInfo = selectedInstrument;

        if (!instrumentInfo.hasValidHostName()) {
            LOG.error("Invalid host name:" + instrumentInfo.hostName());
            return;
		}

        instrumentName.setValue(selectedInstrument.name());

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
