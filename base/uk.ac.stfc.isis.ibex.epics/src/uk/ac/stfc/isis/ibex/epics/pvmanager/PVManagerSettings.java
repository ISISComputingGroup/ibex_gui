package uk.ac.stfc.isis.ibex.epics.pvmanager;

import java.io.IOException;
import org.eclipse.core.runtime.FileLocator;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Applies settings to PV manager so that it knows where to find it's configuration.
 */
public final class PVManagerSettings {
    
    /**
     * Private constructor, this is a utility class.
     */
    private PVManagerSettings() { }
    
    /**
     * This is a system property that diirt uses internally to figure out where to find diirt config files.
     */
    private static final String DIIRT_HOME_ENV_VAR = "diirt.home";
    
    /**
     * The path in the resources folder where diirt config files can be found.
     */
    private static final String DIIRT_DIR = "/diirt/";
    
    /**
     * Tell PV manager to use our config files.
     */
    public static synchronized void setUp() {
        try {
            System.setProperty(DIIRT_HOME_ENV_VAR, 
                FileLocator.resolve(PVManagerSettings.class.getResource(DIIRT_DIR)).getPath());
            IsisLog.getLogger(PVManagerSettings.class).info("Setting diirt.home env var to %s" + FileLocator.resolve(PVManagerSettings.class.getResource(DIIRT_DIR)).getPath().toString());
        } catch (IOException | RuntimeException err) {
            IsisLog.getLogger(PVManagerSettings.class).error(err.getMessage(), err);
        }
    }
}
