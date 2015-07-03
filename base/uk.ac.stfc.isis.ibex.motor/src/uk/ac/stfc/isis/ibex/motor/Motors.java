package uk.ac.stfc.isis.ibex.motor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.motor.internal.MotorsTable;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Motors extends Plugin {
	private static Motors instance;
	private static BundleContext context;
	
	/** The number of motor tables to show. */
	private static final int NUMBER_MOTOR_TABLES = 3;
	/** The number of controllers (crates) in each table. */
	private static final int NUMBER_CONTROLLERS = 8;
	/** The number of motors for each controller. */
	private static final int NUMBER_MOTORS = 8;

    public static Motors getInstance() { 
    	return instance; 
    }
	
    private List<MotorsTable> motorsTableList = new ArrayList<MotorsTable>();
    
	public Motors() {
		super();
		instance = this;
		
		for (int i = 0; i < NUMBER_MOTOR_TABLES; i++) {
			int controllerStart = i * NUMBER_MOTORS + 1;
			motorsTableList.add(new MotorsTable(Instrument.getInstance(), NUMBER_CONTROLLERS, NUMBER_MOTORS, controllerStart));
		}
	}
    
	/**
	 * Getter for list of all the motor tables.
	 * @return List of motor tables
	 */
	public List<MotorsTable> getMotorsTablesList() {
		return motorsTableList;
	}
			
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Motors.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Motors.context = null;
		for (MotorsTable motorsTable : motorsTableList) {
			motorsTable.close();
		}
	}

}
