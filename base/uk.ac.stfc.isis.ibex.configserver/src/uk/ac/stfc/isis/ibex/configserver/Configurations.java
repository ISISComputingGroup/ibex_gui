package uk.ac.stfc.isis.ibex.configserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.configserver.internal.ConfigEditing;
import uk.ac.stfc.isis.ibex.configserver.internal.LoggingConfigurationObserver;
import uk.ac.stfc.isis.ibex.configserver.json.JsonConverters;
import uk.ac.stfc.isis.ibex.configserver.recent.RecentConfigList;
import uk.ac.stfc.isis.ibex.epics.observing.LoggingObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class Configurations extends Closer implements BundleActivator {

	public static final Logger LOG = IsisLog.getLogger(Configurations.class);

	private static Configurations instance;
	private static BundleContext context;
	
	private final Displaying displaying;
	private final Editing editing;
	private final IocControl iocControl;
	private final RecentConfigList recent;

	private final ConfigServerVariables variables;
	private final ConfigServer server;
	private final Collection<Subscription> loggingSubscriptions = new ArrayList<>();
	
	public Configurations() {
		instance = this;
		
		recent = new RecentConfigList();		
		variables = registerForClose(new ConfigServerVariables(Instrument.getInstance().channels(), new JsonConverters()));
		server = registerForClose(new ConfigServer(variables));
		displaying = registerForClose(new DisplayConfiguration(variables.currentConfig, server));	
		editing = registerForClose(new ConfigEditing(server));

		iocControl = new IocControl(server);
		addLogging();
	}
	
	public static Configurations getInstance() {
		return instance;
	}
	
	public static BundleContext getContext() {
		return context;
	}

	public ConfigServerVariables variables() {
		return variables;
	}
	
	public ConfigServer server() {
		return server;
	}
	
	public Displaying display() {
		return displaying;
	}

	public Editing edit() {
		return editing;
	}
	
	public IocControl iocControl() {
		return iocControl;
	}
	
	public List<String> recent() {
		return recent.get();
	}

	public void addRecent(String configName) {
		recent.add(configName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Configurations.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Configurations.context = null;

		for (Subscription subscription : loggingSubscriptions) {
			subscription.cancel();
		}
		
		close();
	}

	private void addLogging() {
		loggingSubscriptions.add(variables.currentConfig.subscribe(new LoggingConfigurationObserver(LOG, "Current config")));
		loggingSubscriptions.add(variables.serverStatus.subscribe(new LoggingObserver<ServerStatus>(LOG, "Server status")));
	}
}
